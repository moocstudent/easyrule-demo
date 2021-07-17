package com.example.easyruledemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @Author: zhangQi
 * @Date: 2021-07-17 11:40
 */
@Target(ElementType.METHOD)
public @interface InUse {
    String value() default "使用中的方法";
}
