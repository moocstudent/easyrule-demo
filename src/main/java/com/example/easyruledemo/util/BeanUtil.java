package com.example.easyruledemo.util;

import com.example.easyruledemo.entity.EwsMailEntity;
import org.jooq.lambda.Unchecked;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 11:29
 */
public class BeanUtil {

    //拷贝元素
    public static void copyProperties(Object src, Object target) {
        Optional.ofNullable(src).ifPresent(item -> BeanUtils.copyProperties(item, target, getNullPropertyNames(item)));
    }

    //获取对象里的空值属性
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors()).filter(pd -> src.getPropertyValue(pd.getName()) == null).map(PropertyDescriptor::getName).distinct().toArray(String[]::new);
    }

    //转换object,字段对等
    public static <T,E>Optional<T> getClass(Class<T> tClass,E e){
        return Optional.ofNullable(e).map(Unchecked.function(item->{
            T temp = tClass.newInstance();
            copyProperties(item,temp);
            return temp;
        }));
    }

    //转换object,字段中string类型的进行trim去除空格
    public static <E>Optional<E> getTrimClass(E e){
        return Optional.ofNullable(e).map(Unchecked.function(item->{
            Class<?> tClass = e.getClass();
            Field[] tDeclaredFields = tClass.getDeclaredFields();
            for(Field tFiled:tDeclaredFields){
                tFiled.setAccessible(true);
                if(tFiled.getType().getTypeName().indexOf("String")>-1){
                    System.out.println("string indexof");
                    String trim = tFiled.get(e).toString().trim();
                    System.out.println("trim:"+trim);
                    tFiled.set(e,trim);
                }
            }
            return e;
        }));
    }

    public static void main(String[] args) {
        EwsMailEntity build = EwsMailEntity.builder().email("hello@163.com ").password("123").build();

        Optional<EwsMailEntity> trimClass = getTrimClass(build);
        EwsMailEntity ewsMailEntity = trimClass.get();
        System.out.println(ewsMailEntity.getEmail());
    }
}
