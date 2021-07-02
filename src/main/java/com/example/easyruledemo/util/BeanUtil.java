package com.example.easyruledemo.util;

import org.springframework.beans.BeanUtils;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 11:29
 */
public class BeanUtil {


    public static <T> T copyInstance(Object source,T clazz){
        T o = null;
        try {
            o = (T) clazz.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(source,o);
        return o;
    }
}
