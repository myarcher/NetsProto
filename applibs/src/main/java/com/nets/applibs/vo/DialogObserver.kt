package com.nets.applibs.vo

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.nets.applibs.until.LoadingDialogUntil

class DialogObserver: Observer<Int> {
    private var loading: LoadingDialogUntil?=null
    private  var activity: FragmentActivity?=null
    constructor(activity: FragmentActivity){
        this.activity=activity
    }
    override fun onChanged(t: Int?) {
        if(loading==null){
            loading= LoadingDialogUntil[activity]
        }
        if(t==1){
            loading!!.showDialog()
        }else{
            loading!!.cancelDialog()
        }
    }


}