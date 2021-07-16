package com.example.easyruledemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.container.EwsExContainer;
import com.example.easyruledemo.container.SubscriptionContainer;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsSubscriptionEntity;
import com.example.easyruledemo.enums.ItemActionType;
import com.example.easyruledemo.mapper.EwsSubscriptionMapper;
import com.example.easyruledemo.service.IEwsEmailService;
import com.example.easyruledemo.service.ISubscriptionService;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author: zhangQi
 * @Date: 2021-07-06 19:26
 */
@Service
@Slf4j
public class SubscriptionServiceImpl extends ServiceImpl<EwsSubscriptionMapper, EwsSubscriptionEntity> implements ISubscriptionService {

    @Autowired
    private IEwsEmailService ewsEmailService;

    @Override
    public Integer unSubscriptionById(String subscriptionId, EwsMailEntity ewsMail) {
        ExchangeService exchangeService = EwsExContainer.getExchangeService(ewsMail.getEmail(), ewsMail.getPassword());
        try {
            exchangeService.unsubscribe(subscriptionId);
            log.info("取消订阅,订阅id每日更新一次,如果没有进行主动替换,则需要进行手动替换.");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("取消订阅异常");
            return -1;
        }
        return 1;
    }

    @Override
    public Integer unSubscriptionByMailCache(EwsMailEntity ewsMail) {
        ExchangeService exchangeService = EwsExContainer.getExchangeService(ewsMail.getEmail(), ewsMail.getPassword());
        String key = ewsMail.getEmail() + ewsMail.getPassword();
        String subscriptionId = "从缓存中根据key拿取订阅id";
        try {
            exchangeService.unsubscribe(subscriptionId);
            log.info("取消订阅,相应清除redis中订阅id.订阅id每日更新一次,如果没有进行主动替换,则需要进行手动替换.");
            //todo 清除id从缓存中
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过缓存取消订阅异常");
            return -1;
        }
        return 1;
    }

    @Override
    public Boolean saveOrUpdateSubcription(EwsSubscriptionEntity ewsSubscription) {
        return super.saveOrUpdate(ewsSubscription);
    }

    @Override
    public Boolean saveOrUpdateSubcriptionByKey(EwsSubscriptionEntity ewsSubscription) {
        LambdaQueryWrapper<EwsSubscriptionEntity> queryWrapper =
                new LambdaQueryWrapper<EwsSubscriptionEntity>()
                        .eq(EwsSubscriptionEntity::getSubscriptionKey, ewsSubscription.getSubscriptionKey())
                        .eq(EwsSubscriptionEntity::getDeleteFlag, 0);
        List list = Optional.ofNullable(baseMapper.selectList(queryWrapper)).orElse(Collections.EMPTY_LIST);
        if (list.size()==0){
            //没有这个key,则进行新增,不传id
            return this.saveOrUpdateSubcription(ewsSubscription);
        }
        //有的话则更新
        LambdaUpdateWrapper<EwsSubscriptionEntity> updateWrapper =
                new LambdaUpdateWrapper<EwsSubscriptionEntity>()
                        .eq(EwsSubscriptionEntity::getSubscriptionKey, ewsSubscription.getSubscriptionKey())
                        .eq(EwsSubscriptionEntity::getDeleteFlag, 0);
        return update(ewsSubscription, updateWrapper);
    }

    @Override
    public String getSubscriptionIdByKey(String key) {
        log.info("getSubscriptionIdByKey invoke, the key:{}", key);
        LambdaQueryWrapper<EwsSubscriptionEntity> queryWrapper = new LambdaQueryWrapper<EwsSubscriptionEntity>()
                .eq(EwsSubscriptionEntity::getSubscriptionKey, key)
                .eq(EwsSubscriptionEntity::getDeleteFlag, 0)
                .orderByDesc(EwsSubscriptionEntity::getSubscriptionDate);
        List<EwsSubscriptionEntity> subscriptionList
                = Optional.ofNullable(baseMapper.selectList(queryWrapper)).orElse(Collections.EMPTY_LIST);
        if (subscriptionList.size() > 0) {
            EwsSubscriptionEntity ewsSubscription = subscriptionList.stream().findFirst().get();
            return ewsSubscription.getSubscriptionId();
        } else {
            return null;
        }
    }

    @Override
    public List<EwsSubscriptionEntity> listSelective(EwsSubscriptionEntity ewsSubscription) {
        return new LambdaQueryChainWrapper<EwsSubscriptionEntity>(baseMapper)
                .eq(EwsSubscriptionEntity::getDeleteFlag, 0)
                .like(StringUtils.isEmpty(ewsSubscription.getSubscriptionKey()), EwsSubscriptionEntity::getSubscriptionKey, ewsSubscription.getSubscriptionKey())
                .eq(StringUtils.isEmpty(ewsSubscription.getSubscriptionMinutes()), EwsSubscriptionEntity::getSubscriptionMinutes, ewsSubscription.getSubscriptionMinutes())
                .eq(StringUtils.isEmpty(ewsSubscription.getSubscriptionDate()), EwsSubscriptionEntity::getSubscriptionDate, ewsSubscription.getSubscriptionDate())
                .orderByDesc(EwsSubscriptionEntity::getEwsSubscriptionId)
                .list();
    }

    @Override
    public Integer updateByKey(EwsSubscriptionEntity ewsSubscriptionEntity) {
        LambdaQueryWrapper updateWrapper = new LambdaQueryWrapper<EwsSubscriptionEntity>()
                .eq(EwsSubscriptionEntity::getSubscriptionKey, ewsSubscriptionEntity.getSubscriptionKey())
                .eq(EwsSubscriptionEntity::getDeleteFlag, 0);
        return baseMapper.update(ewsSubscriptionEntity, updateWrapper);
    }


    @Override
    public Integer initSubscription(List<String> itemActionTypeList) {
        List<EwsMailEntity> mailConfigList
                = ewsEmailService.getMailConfigList(EwsMailEntity.builder().build(), ItemActionType.getEnumList(itemActionTypeList));
        log.info("initSubscription:");
        mailConfigList.forEach(System.out::println);
        return SubscriptionContainer.initialSubscriptionToday(mailConfigList);
    }

    @Override
    public Integer initSubscription(Long mailId) {
        EwsMailEntity mailConfig = ewsEmailService.findOne(mailId);
        return SubscriptionContainer.getSubscriptionToday(mailConfig)!=null?1:0;
    }


    @Override
    public Integer unSubscription(Long mailId) {
        EwsMailEntity mailConfig = ewsEmailService.findOne(mailId);
        return SubscriptionContainer.unsubscriptionByMail(mailConfig);
    }

    @Override
    public PullSubscription unAndInitOne(EwsMailEntity mailEntity, String key) {
        Integer unsubscription = SubscriptionContainer.unsubscriptionByMail(mailEntity);
        PullSubscription subscription = null;
        if(unsubscription>0){
            subscription = SubscriptionContainer.initialSubscriptionToday(mailEntity);
            Boolean saveSubscription = this
                    .saveOrUpdateSubcriptionByKey(
                            EwsSubscriptionEntity
                                    .builder()
                                    .subscriptionId(subscription.getId())
                                    .subscriptionKey(key)
                                    .subscriptionDate(LocalDateTime.now())
                                    .subscriptionMinutes(1440)
                                    .build()
                    );
            return subscription;
        }else{
            log.info("取消订阅异常 unAndInitOne");
        }
        return subscription;
    }

}
