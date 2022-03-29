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
import android.telephony.gsm.SmsMessage
import android.text.InputFilter
import android.text.Spanned
import java.lang.Exception

/**
 * Created by user2 on 14/8/17.
 */
class IncomingSms : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        try {
            if (bundle != null) {
                val pdusObj = bundle["pdus"] as Array<Any>?
                for (i in pdusObj!!.indices) {
                    val currentMessage = SmsMessage.createFromPdu(
                        pdusObj[i] as ByteArray
                    )
                    val phoneNumber = currentMessage.displayOriginatingAddress
                    val message = currentMessage.displayMessageBody
                    try {
                        if (phoneNumber == "TA-VKC") {
                            val Sms = SignUpActivity()
                            Sms.recivedSms(message)
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        } catch (e: Exception) {
        }
    }
}