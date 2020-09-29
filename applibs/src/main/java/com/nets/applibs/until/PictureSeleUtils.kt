package com.nets.applibs.until

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import java.lang.Exception

class PictureSeleUtils {
    private var listener:PicListener?=null
    constructor(listener:PicListener){
        this.listener=listener
    }

    fun toSelectImage(frag:Fragment,maxNum:Int,list: MutableList<LocalMedia>) {
        PictureSelector.create(frag)
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .maxSelectNum(maxNum)
            .imageSpanCount(3)
            .selectionMode(PictureConfig.MULTIPLE)
            .isPreviewImage(true)
            .isCamera(true)
            .isCompress(true)
            .selectionData(list)
            .minimumCompressSize(100)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

     fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
                var selectpaths = PictureSelector.obtainMultipleResult(data)
                if (selectpaths != null && selectpaths.size > 0) {
                    toDealUpPath(selectpaths)
                }else{
                    listener?.onPick(ArrayList(),ArrayList())
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun toDealUpPath(paths: MutableList<LocalMedia>) {
        var list=ArrayList<String>()
        for (loacal in paths) {
            var path = ""
            if (loacal.isCompressed) {
                path = loacal!!.compressPath
            } else if (loacal.isOriginal) {
                path = loacal!!.path
            } else {
                path = loacal!!.androidQToPath
            }
            list.add(path)
        }
        listener?.onPick(list,paths)
    }
    fun onDestroy(){
        listener=null
    }
}
interface PicListener{
    fun onPick(path:MutableList<String>,local:MutableList<LocalMedia>)
}