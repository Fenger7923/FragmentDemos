package com.fenger.fragmentdemo.permission

/**
 * com.fenger.permission
 * Created by fenger
 * in 2019-12-12
 *
 * @author kissgakki
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER) //新建的注解要放在"方法"上面
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME) //编译时还是运行时监测？
annotation class PermissionSuccess(val requestCode: Int)
