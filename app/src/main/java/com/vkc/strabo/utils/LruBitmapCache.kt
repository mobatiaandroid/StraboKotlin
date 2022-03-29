package com.vkc.strabo.utils

import org.apache.http.entity.mime.HttpMultipartMode
import kotlin.Throws
import com.vkc.strabo.utils.AndroidMultiPartEntity.CountingOutputStream
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.content.BroadcastReceiver
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.vkc.strabo.utils.ConnectivityReceiver
import com.vkc.strabo.utils.ConnectivityReceiver.ConnectivityReceiverListener
import com.vkc.strabo.appcontroller.AppController
import com.vkc.strabo.R
import android.view.animation.RotateAnimation
import android.view.animation.LinearInterpolator
import android.view.WindowManager
import android.view.Gravity
import android.widget.LinearLayout
import android.app.Activity
import android.widget.TextView
import android.widget.Toast
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.res.TypedArray
import android.os.Bundle
import com.vkc.strabo.activity.common.SignUpActivity
import kotlin.jvm.JvmOverloads
import com.vkc.strabo.utils.LruBitmapCache
import com.vkc.strabo.utils.UtilityMethods.GetAccessTokenInterface
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import android.view.View.OnTouchListener
import android.view.MotionEvent
import com.vkc.strabo.utils.UtilityMethods
import android.annotation.TargetApi
import android.os.Build
import android.view.Display
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.text.InputFilter
import android.text.Spanned
import androidx.collection.LruCache
import com.android.volley.toolbox.ImageLoader

class LruBitmapCache @JvmOverloads constructor(sizeInKiloBytes: Int = defaultLruCacheSize) :
    LruCache<String?, Bitmap>(sizeInKiloBytes), ImageLoader.ImageCache {
     override fun sizeOf(key: String, value: Bitmap): Int {
        return value.rowBytes * value.height / 1024
    }

    override fun getBitmap(url: String): Bitmap {
        return get(url)!!
    }

    override fun putBitmap(url: String, bitmap: Bitmap) {
        put(url, bitmap)
    }

    companion object {
        val defaultLruCacheSize: Int
            get() {
                val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
                return maxMemory / 8
            }
    }
}