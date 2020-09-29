package com.nets.applibs.vo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import com.nets.applibs.R
import com.nets.applibs.loadsir.callback.CallBackType
import com.nets.applibs.loadsir.callback.Callback
import com.nets.applibs.loadsir.core.LoadService
import com.nets.applibs.loadsir.core.LoadSir
import com.nets.applibs.net.BaseViewModel
import com.nets.applibs.net.ResponseData
import com.nets.applibs.widget.RefreshLoadLayout
import com.nets.applibs.widget.RefreshLoadListener
import com.scwang.smart.refresh.layout.api.RefreshHeader
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import com.nets.applibs.net.Result

abstract class CustomFragment : BaseFragment(), Observer<ResponseData>,
    EasyPermissions.PermissionCallbacks,
    RefreshLoadListener, Callback.OnViewListener {
    private var dialog: DialogObserver? = null
    var isResumeoad = false
    var isHashData = false
    var mLoadService: LoadService<*>? = null
    var refreshLoad: RefreshLoadLayout? = null

    companion object {
        const val PERM_C0DE = 100
    }

    override fun initData(bundle: Bundle?) {
        super.initData(bundle)
        if (!isResumeoad) {
            firstLoad()
        }
        setCustomset()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mView != null) {
            reMoveOld()
        } else {
            setMview(inflater, container)
        }
        initView()
        return mView
    }

    open fun setMview(inflater: LayoutInflater, container: ViewGroup?) {
        var showLayoutId = if (isNeedRefload()) {
            layoutId
        } else {
            getContentId()
        }
        if (showLayoutId > 0) {
            mView = inflater.inflate(showLayoutId, container, false)

            if (isNeedRefload()) {
                refreshLoad = mView!!.findViewById(R.id.base_refreshload)
                refreshLoad?.setLayoutListener(this)
            }
            if (isCanShowStutus()) {
                var statRootView = mView
                var rootId = getStatRootId()
                if (rootId > 0) {
                    statRootView = mView!!.findViewById(rootId)
                }
                mLoadService = LoadSir.default?.register(statRootView, this)
                if (rootId <= 0) {
                    mView = mLoadService!!.loadLayout
                }
            }
        }
    }

    override fun onReload(v: View?, type: CallBackType?, stat: Int, isBnt: Boolean) {
        firstLoad()
    }

    open fun setCustomHeader(header: RefreshHeader) {
        refreshLoad?.setRefreshHeader(header)
    }

    open fun setCustomset() {
        refreshLoad?.setEnableRefresh(false)
        refreshLoad?.setEnableLoadMore(false)
    }

    open fun setHeadRefresh(isCan: Boolean) {
        refreshLoad?.setEnableRefresh(isCan)
    }

    open fun setHeadRefresh(isCan: Boolean, type: Int) {
        setHeadRefresh(isCan)
    }

    open fun getStatRootId(): Int {
        return 0
    }


    override fun initView() {
    }

    fun resetRefrLoad() {
        refreshLoad?.resetRefreshLoad()
    }

    open fun isNeedRefload(): Boolean {
        return false
    }

    open fun isCanShowStutus(): Boolean {
        return true
    }

    open fun firstLoad() {
        refreshLoad?.pageIndex = 1
        isHashData = false
        showView(CallBackType.LOADING)
        var pageIndex = refreshLoad?.pageIndex ?: 1
        var pageCount = refreshLoad?.pagerCount ?: 10
        onRefreshLoad(pageIndex, pageCount)
    }

    fun isNotHasDataSize(): Boolean {
        return refreshLoad?.isNotHasDataSize() ?: false
    }

    override val layoutId: Int = R.layout.layout_fragmnet_base


    override fun onSupportVisible() {
        if (isResumeoad) {
            firstLoad()
        }
        super.onSupportVisible()
    }

    open fun showView(callType: CallBackType) {

    }


   open fun toast(mess: String?) {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
    }

    override fun onRefreshLoad(pageIndex: Int, pagerCount: Int) {

    }


    override fun onChanged(rdata: ResponseData) {
        if (rdata != null && rdata.isShowStat && !isHashData) {
            if (rdata.getCode() == -5 || rdata.getCode() == -6) {
                showView(CallBackType.NOT_NET)
            } else {
                showView(CallBackType.EMPTY)
            }
        } else {
            toast(rdata?.getMess())
        }
    }

    private fun getDialogObserver(): DialogObserver {
        if (dialog == null) {
            dialog = DialogObserver(activity!!)
        }
        return dialog!!
    }

    fun setBaseUpdata(viewModel: BaseViewModel?) {
        viewModel?.getErrData()?.observe(this, this)
        viewModel?.dialog()?.observe(this, getDialogObserver())
    }


    fun setData(data: Any?) {
        setDataIndexSize(10, 1, 20, data)
        isHashData = data != null
        if (data != null) {
            setPageInfo(data)
        }
        showIsEmpty()
    }

    open fun setDataIndexSize(pageSize: Int, pIndex: Int, dataSize: Int, data: Any?) {
        refreshLoad?.setDataIndexSize(pageSize, pIndex, dataSize)
    }

    open fun refreshComplete() {
        refreshLoad?.finishRefreshLoaded()
    }

    open fun showIsEmpty() {
        if (isHashData) {
            showView(CallBackType.SUCCESS)
        } else {
            showView(CallBackType.EMPTY)
        }
    }

    open fun setPageInfo(data: Any) {

    }

    fun setWhiteBarFont() {

    }

    open fun setStatuss(url: String, msg: String, rdata: Result<*>?) {

    }

    override fun onDestroyView() {
        dialog = null
        super.onDestroyView()
    }

    open fun hasPermission(): Boolean {
        return return EasyPermissions.hasPermissions(context!!, *getPersionArray())
    }

    open fun toSelectAction(type: Int, isAll: Boolean) {

    }

    open fun getPersionArray(): Array<String> {
        return arrayOf()
    }


    @AfterPermissionGranted(PERM_C0DE)
    fun getPerssionAction() {
        if (hasPermission()) {
            toSelectAction(1, true)
        } else {
            EasyPermissions.requestPermissions(
                this, "这个应用程序需要需要您同意一些权限，部分功能才能正常工作",
                PERM_C0DE,
                *getPersionArray()
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                .setTitle("提示")
                .setRequestCode(PERM_C0DE)
                .setRationale("没有获得权限，此应用程序可能无法正常工作，打开app 设置界面，修改app权限")
                .setNegativeButton("取消")
                .setPositiveButton("设置").build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == PERM_C0DE) {//是选择图片
                if (hasPermission()) {//有权限
                    toSelectAction(2, true)
                } else {
                    toSelectAction(2, false)
                }
            }
        } catch (e: Exception) {
        }
    }

}


