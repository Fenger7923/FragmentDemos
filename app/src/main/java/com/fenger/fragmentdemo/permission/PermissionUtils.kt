package com.fenger.fragmentdemo.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.Executors

/**
 * com.fenger.permission
 * Created by fenger
 * in 2019-12-12
 */
class PermissionUtils private constructor() {
    companion object {
        private const val TAG = "PermissionUtils"

        //判断安卓版本大于6.0
        val isOverMarshmallow: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

        //获取到权限
        fun executeSucceedMethod(reflectObject: Any, requestCode: Int) {
            val methods = reflectObject.javaClass.declaredMethods // 获取class中所有的方法

            // 遍历找到给了标记的方法
            for (method in methods) {
                Log.d(TAG, "打印所有方法: $method")
                // 获取该方法上面有没有打这个标记
                val succeedMethod = method.getAnnotation(PermissionSuccess::class.java)
                Log.d(TAG, "executeSucceedMethod: $succeedMethod")
                if (succeedMethod != null) {
                    // 代表该方法打了标记
                    // 并且我们的请求码必须 requestCode 一样
                    val methodCode = succeedMethod.requestCode
                    if (methodCode == requestCode) {
                        // 这个就是我们要找的成功方法
                        // 反射执行该方法
                        Log.d(TAG, "找到了该方法：$method")
                        executeMethod(reflectObject, method)
                    }
                }
            }
        }

        //获取权限失败
        fun executeFailedMethod(reflectObject: Any, requestCode: Int) {
            val methods = reflectObject.javaClass.declaredMethods // 获取class中所有的方法

            // 遍历找到给了标记的方法
            for (method in methods) {
                Log.d(TAG, "打印所有方法: $method")
                // 获取该方法上面有没有打这个标记
                val failMethod = method.getAnnotation(PermissionFail::class.java)
                //            Log.d(TAG, "executeFailMethod: "+failMethod);
                if (failMethod != null) {
                    // 代表该方法打了标记
                    // 并且我们的请求码必须 requestCode 一样
                    val methodCode = failMethod.requestCode
                    if (methodCode == requestCode) {
                        // 这个就是我们要找的成功方法
                        // 反射执行该方法
                        Log.d(TAG, "找到了该方法：$method")
                        executeMethod(reflectObject, method)
                    }
                }
            }
        }

        //反射执行找到的那个方法。传入：找到的类里面的方法
        fun executeMethod(reflectObject: Any?, method: Method) {
            try {
                method.isAccessible = true //允许执行私有方法//避免需要被执行的方法是private的
                method.invoke(reflectObject, *arrayOf()) //执行指定的方法
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getDeniedPermissions(mContext: Context, mPermissions: Array<String>): List<String> {
            val deniedPermissions = arrayListOf<String>()
            for (permission in mPermissions) {
                //未授予的权限加入集合
                if (ContextCompat.checkSelfPermission(getActivity(mContext), permission)
                        == PackageManager.PERMISSION_DENIED) {
                    deniedPermissions.add(permission)
                }
            }
            return deniedPermissions
        }

        //获取类的context
        fun getActivity(mContext: Context): Activity {
            return mContext as Activity
        }
    }

    //这个类里面都是静态方法，不允许被实例化
    init {
        throw UnsupportedOperationException("Can't be intensified")
    }
}
