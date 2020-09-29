package com.nets.applibs.until

import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

object AppUntil{
    fun <T : ViewModel> obtainViewModel(fragment: Fragment, claz: Class<T>): T {
        return ViewModelProviders.of(fragment).get(claz)
    }

    fun <T : ViewModel> obtainViewModel(activity: FragmentActivity, claz: Class<T>): T {
        return ViewModelProviders.of(activity).get(claz)
    }
    fun isStrNull(str: String?): Boolean {
        return TextUtils.isEmpty(str) || str.equals("null")
    }
     fun getRequestParamMap(URL: String): MutableMap<String, String>? {
        if (TextUtils.isEmpty(URL)) {
            return null
        }

        var strUrlParam = truncateUrlPage(URL);//得到参数
        if (TextUtils.isEmpty(strUrlParam)) {
            return null;
        }
        var mapRequest = HashMap<String, String>()
        //每个键值为一组
        if (strUrlParam != null) {
            var arrSplit = strUrlParam.split("&")
            for (strSplit in arrSplit) {
                var arrSplitEqual = strSplit.split("=")
                //解析出键值
                if (arrSplitEqual.size > 1) {
                    if (!TextUtils.isEmpty(arrSplitEqual[1])) {
                        mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);//正确解析
                    } else {
                        mapRequest.put(arrSplitEqual[0], "");//无value
                    }
                }
            }
        }
        return mapRequest;
    }
    private fun truncateUrlPage(strURL: String): String? {
        if (TextUtils.isEmpty(strURL)) {
            return null
        }

        //    var strURLs = strURL.trim().toLowerCase();
        var arrSplit = strURL.split("?")
        var strAllParam: String? = strURL
        //有参数
        if (arrSplit.size > 1) {
            if (arrSplit[1] != null) {
                strAllParam = arrSplit[1]
            }
        }

        return strAllParam;
    }


}