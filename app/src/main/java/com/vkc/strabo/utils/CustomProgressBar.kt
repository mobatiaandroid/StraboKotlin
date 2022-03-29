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
import android.app.Activity
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
import android.text.TextWatcher
import android.text.Editable
import android.view.View.OnTouchListener
import android.view.MotionEvent
import com.vkc.strabo.utils.UtilityMethods
import android.annotation.TargetApi
import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.Display
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.text.InputFilter
import android.text.Spanned
import android.view.animation.Animation
import android.widget.*

class CustomProgressBar(context: Context?, resourceIdOfImage: Int) : Dialog(
    context!!, R.style.TransparentProgressDialog
) {
    private val iv: ImageView
    override fun show() {
        super.show()
        val anim = RotateAnimation(
            0.0f, 360.0f,
            Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
            .5f
        )
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = Animation.INFINITE
        anim.duration = 1000
        iv.animation = anim
        iv.startAnimation(anim)
    }

    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT
        )
        iv = ImageView(context)
        iv.setImageResource(resourceIdOfImage)
        layout.addView(iv, params)
        addContentView(layout, params)
    }
}