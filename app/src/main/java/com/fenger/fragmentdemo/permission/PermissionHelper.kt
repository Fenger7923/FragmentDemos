package com.fenger.fragmentdemo.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * com.fenger.permission
 * Created by fenger
 * in 2019-12-12
 *
 * @author kissgakki
 */

//这个类用于权限处理
//功能包括:1.检查权限。2.申请权限。3.版本低于23时适配。4.重复请求权限。
// 传递的参数定义
class PermissionHelper private constructor(private val mObject: Context) {
    private var mRequestCode = 0 //请求码 = 0
    private var mRequestPermission: Array<String> = arrayOf() //所请求或者检查的权限

    //请求码
    fun requestCode(requestCode: Int): PermissionHelper {
        mRequestCode = requestCode
        return this
    }

    //权限的数组
    //String…的写法主要是不限制后面一个String参数的个数，
    // 可以理解为传递了一个String的List 过来是一样的 ，
    // 这么写的优点就是在调用的时候不用重新构造一个List。
    //在List<String> 中如果String值只有两三个的时候，且调用比较频繁的时候可以用String…代替List<String>
    fun requestPermission(permissions: Array<String>): PermissionHelper {
        mRequestPermission = permissions
        return this
    }

    fun request() {
        //判断是否是6.0以上
        if (!PermissionUtils.isOverMarshmallow) {
            // 如果不是6.0以上  那么进行判断
            for (permission in mRequestPermission) {
                //未授予的权限加入集合
                if (ContextCompat.checkSelfPermission(mObject, permission) == PackageManager.PERMISSION_DENIED) {
                    //如果存在未打开的权限就弹窗
                    Toast.makeText(PermissionUtils.getActivity(mObject), "有权限未打开哦", Toast.LENGTH_LONG).show()
                    return
                }
            }
            //全部权限都打开了就直接运行了
            PermissionUtils.executeSucceedMethod(mObject, mRequestCode)
            return
        }

        // 如果是6.0以上
        // 获取需要申请的权限中 获取还没有获得的权限
        val deniedPermissions = PermissionUtils.getDeniedPermissions(mObject, mRequestPermission)
        if (deniedPermissions.isEmpty()) {
            //所有权限都已获得
            PermissionUtils.executeSucceedMethod(mObject, mRequestCode)
        } else {
            // 存在权限未获得
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject)
                    , deniedPermissions.toTypedArray(), mRequestCode)
        }
    }

    companion object {
        //直接传入参数//只用于activity和fragment//直接调用的方式
        fun requestPermission(activity: Activity, requestCode: Int, permissions: Array<String>) {
            with(activity).requestCode(requestCode).requestPermission(permissions).request()
        }

        fun requestPermission(fragment: Fragment, requestCode: Int, permissions: Array<String>) {
            with(fragment).requestCode(requestCode).requestPermission(permissions).request()
        }

        //链式传递
        fun with(activity: Activity): PermissionHelper {
            return PermissionHelper(activity)
        }

        fun with(fragment: Fragment): PermissionHelper {
            return PermissionHelper(fragment.requireContext())
        }

        //处理权限请求之后的回调
        fun onRequestPermissionResult(mContext: Context, requestCode: Int, permissions: Array<String>) {
            val deniedPermissions = PermissionUtils.getDeniedPermissions(mContext, permissions)
            if (deniedPermissions.isEmpty()) {
                //所有权限都已获得
                PermissionUtils.executeSucceedMethod(mContext, requestCode)
            } else {
                // 存在权限未获得
                PermissionUtils.executeFailedMethod(mContext, requestCode)
            }
        }
    }
}
