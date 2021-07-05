package com.example.easyruledemo.task;

import com.example.easyruledemo.container.SubscriptionContainer;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.rules.MailEventsThread;
import com.example.easyruledemo.service.IEwsEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 21:32
 */
@Component
@Slf4j
public class EmailEventTask {

//    int executeThisDay = 0;

    /**
     * 是否支持动态监听新加入数据库的邮箱
     */
    //    @Value() todo
    private boolean canDynamicInsertNewOneMail;

    @Autowired
    private IEwsEmailService ewsEmailService;

    /**
     * 每天凌晨1点执行一次
     */
//    @Scheduled(cron = "0 0 1 * * ?")
    @PostConstruct
    public void initialSubscriptionToday(){
        log.info("开始初始化今日邮箱订阅监听");
        Integer size = SubscriptionContainer.initialSubscriptionToday(ewsEmailService
                .getMailConfigList(EwsMailEntity.builder().build()));
        log.info("今日邮箱订阅监听完成,监听数量:{}个",size);
    }

    /**
     * 邮件监听
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void emailEventPoll() {
        if(!(SubscriptionContainer.getInitialCount()>0)){
            log.info("如果subscription监听未初始化,则不进行事件处理");
            return;
        }
        try {
            List<EwsMailEntity> mailConfigList = ewsEmailService.getMailConfigList(EwsMailEntity.builder().build());
            for (EwsMailEntity mailConfig : mailConfigList){
                MailEventsThread mailEventsThread = new MailEventsThread();
                mailEventsThread.setMailConfig(mailConfig);
                mailEventsThread.start();
            }
//            executeThisDay++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 这种方法不适用在这里
     */
//    @Scheduled(cron = "*/30 * * * * ?")
//    public void emailEventConn(){
//        try {
//            StreamingSubscriptionConnection subConn = SubscriptionContainer.initialAndGetStreamingSubscriptionConn();
//            subConn.open();
//            subConn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
