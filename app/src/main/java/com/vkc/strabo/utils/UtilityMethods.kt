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
import android.widget.LinearLayout
import android.app.Activity
import android.widget.TextView
import android.widget.Toast
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
import com.vkc.strabo.utils.UtilityMethods
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.text.InputFilter
import android.text.Spanned
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import java.io.*
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object UtilityMethods {
    private val getTokenIntrface: GetAccessTokenInterface? = null
    @JvmStatic
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return if (ni == null) {
            false
        } else true
    }

    fun setErrorForEditTextNull(edt: EditText) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
                edt.error = null
            }
        })
    }

    fun setErrorForEditText(edt: EditText, msg: String?) {
        edt.error = msg
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    fun validateLong(value: String): Boolean {
        return try {
            value.toLong()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun isValidPhoneNumber(context: Context?, target: CharSequence?): Boolean {
        return Patterns.PHONE.matcher(target).matches()
    }

    fun htmlparsing(text: String): String {
        var encodedString: String
        encodedString = text.replace("&lt;".toRegex(), "<")
        encodedString = encodedString.replace("&gt;".toRegex(), ">")
        encodedString = encodedString.replace("&amp;".toRegex(), "")
        encodedString = encodedString.replace("amp;".toRegex(), "")
        return encodedString
    }

    fun htmlFile(context: Context, content: String): String {
        val APP_DIR = "." + context.getString(R.string.app_name) + "/"
        return "file://APP_DIR/$content"
    }

    fun hideKeyboardOnTouchOutside(
        view: View,
        context: Context, edtText: EditText?
    ) {
        if (view !is EditText) view.setOnTouchListener { v, event ->
            hideKeyBoard(context)
            false
        }
    }

    fun convertDpIntoPixels(context: Context, padding_in_dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (padding_in_dp * scale + 0.5f).toInt()
    }

    fun getButtonDrawableByScreenCathegory(
        con: Context,
        normalStateResID: Int, pressedStateResID: Int
    ): Drawable {
        val state_normal = con.resources
            .getDrawable(normalStateResID).mutate()
        val state_pressed = con.resources
            .getDrawable(pressedStateResID).mutate()
        val drawable = StateListDrawable()
        drawable.addState(
            intArrayOf(android.R.attr.state_pressed),
            state_pressed
        )
        drawable.addState(
            intArrayOf(android.R.attr.state_enabled),
            state_normal
        )
        return drawable
    }

    fun editTextValidationAlert(
        edtText: EditText,
        message: String?, context: Context?
    ) {
        edtText.error = message
    }

    fun setEdtTextTextChangelistener(
        edtTxt: EditText,
        context: Context?
    ) {
        edtTxt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                edtTxt.error = null
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                edtTxt.error = null
            }
        })
    }

    fun txtViewValidationAlert(
        edtText: TextView, message: String?,
        context: Context?
    ) {
        edtText.error = message
    }

    fun setTxtViewChangelistener(
        edtTxt: TextView,
        context: Context?
    ) {
        edtTxt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                edtTxt.error = null
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                edtTxt.error = null
            }
        })
    }

    fun toFile(bm: Bitmap?, fileName: String): String {
        var bm = bm
        var filePath: String
        filePath = "/sdcard/$fileName"
        val f = File(filePath)
        try {
            f.createNewFile()
            val bos = ByteArrayOutputStream()
            bm!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapdata = bos.toByteArray()
            var fos: FileOutputStream? = null
            fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.close()
            filePath = f.absolutePath
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        bm = null
        return filePath
    }

    fun setErrorForTextView(edt: TextView, msg: String?) {
        edt.error = msg
    }

    fun textWatcherForEditText(
        edt: EditText,
        msg: String?
    ) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                if (s.length == 0 || s == "") {
                    setErrorForEditText(edt, msg)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                if (s == "") {
                    setErrorForEditText(edt, msg)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (s != null && s.length > 0 && edt.error != null) {
                    edt.error = null
                } else if (s.length == 0 || s.equals("")) {
                    setErrorForEditText(edt, msg)
                }
            }
        })
    }

    fun textWatcherForTextView(
        edt: TextView,
        msg: String?
    ) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s != null && s.length > 0 && edt.error != null) {
                    edt.error = null
                } else if (s.length == 0) {
                    setErrorForTextView(edt, msg)
                }
            }
        })
    }

    fun hideKeyBoard(context: Context) {
        val imm = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(
                (context as Activity).currentFocus
                    ?.getWindowToken(), 0
            )
        }
    }

    fun dateComprison(
        inputFirstDate: Date,
        inputSecondDate: Date?
    ): Boolean {
        return if (inputFirstDate.before(inputSecondDate)) {
            true
        } else {
            false
        }
    }

    fun dateComprisonToday(inputDate: Date): Boolean {
        return if (inputDate.after(currentDate)) {
            true
        } else {
            false
        }
    }

    val currentDate: Date
        get() {
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]
            return Date(mYear, mMonth, mDay)
        }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    fun getWindowDimens(context: Context): IntArray {
        val dimen = IntArray(2)
        val size = Point()
        val w = (context as Activity).windowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.defaultDisplay.getSize(size)
            dimen[0] = size.x
            dimen[1] = size.y
        } else {
            val d = w.defaultDisplay
            dimen[0] = d.width
            dimen[1] = d.height
        }
        return dimen
    }

    fun isAppAvailable(ctx: Context, intent: Intent?): Boolean {
        val mgr = ctx.packageManager
        val list = mgr.queryIntentActivities(intent!!, PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }

    fun showToast(msg: String?, context: Context?) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun getTimeStampMM_DD_YYYY(inputDate: String?): Date {
        var newDate = Date()
        val formatter = SimpleDateFormat("MM-dd-yyyy")
        try {
            newDate = formatter.parse(inputDate)
        } catch (e: ParseException) {
        }
        return newDate
    }

    fun convertMillisecondsToDate(milliseconds: Long): String {

        // creating Date from millisecond
        val currentDate = Date(milliseconds)
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        // Converting milliseconds to Date using Calendar
        val cal = Calendar.getInstance()
        cal.timeInMillis = milliseconds
        return df.format(cal.time)
    }

    fun checkDate(today: String, dateselected: String): Boolean {
        val dfDate = SimpleDateFormat("dd-MMM-yyyy")
        var b = false
        try {
            b = if (dfDate.parse(today).before(dfDate.parse(dateselected))) {
                false // If start date is before end date
            } else if (dfDate.parse(today) == dfDate.parse(dateselected)) {
                false // If two dates are equal
            } else {
                true // If start date is after the end date
            }
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return b
    }

    fun checkDateEqual(today: String, dateselected: String): Boolean {
        val dfDate = SimpleDateFormat("dd-MMM-yyyy")
        var b = false
        try {
            if (dfDate.parse(today) == dfDate.parse(dateselected)) {
                b = true // If two dates are equal
            }
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return b
    }

    fun getTimeStamp(inputDate: String?): Date {
        var newDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        try {
            newDate = formatter.parse(inputDate)
        } catch (e: ParseException) {
        }
        return newDate
    }

    val currentTimes: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd_HH:mm")
            val currentDateandTime = sdf.format(Date())
            println("Time--$currentDateandTime")
            return currentDateandTime
        }

    interface GetAccessTokenInterface {
        val accessToken: Unit
    }

    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {
        var mPattern: Pattern
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence {
            val matcher = mPattern.matcher(dest)
            return if (!matcher.matches()) "" else null.toString()
        }

        init {
            mPattern =
                Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")
        }
    }
}