package com.fenger.fragmentdemo.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * com.fenger.permission
 * Created by fenger
 * in 2019-12-12
 *
 * @author kissgakki
 */

@Target(ElementType.METHOD)//放在"方法"上面
@Retention(RetentionPolicy.RUNTIME)//编译时还是运行时监测？
public @interface PermissionFail {
    public int requestCode();
}