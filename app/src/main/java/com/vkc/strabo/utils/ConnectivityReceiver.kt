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
import android.content.Context
import android.os.Build
import android.view.Display
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.text.InputFilter
import android.text.Spanned

/**
 * Created by user2 on 3/1/18.
 */
class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, arg1: Intent) {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = (activeNetwork != null
                && activeNetwork.isConnectedOrConnecting)
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnected)
        }
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        @JvmField
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
        val isConnected: Boolean
            get() {
                val cm = AppController.getInstance().applicationContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = cm.activeNetworkInfo
                return (activeNetwork != null
                        && activeNetwork.isConnectedOrConnecting)
            }
    }
}