/**
 *
 */
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
import android.os.Build
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.text.InputFilter
import android.text.Spanned
import android.view.*

/**
 * @author mobatia-user
 */
class CustomToast(var mActivity: Activity) {
    var mTextView: TextView? = null
    var mToast: Toast? = null
    fun init() {
        val inflater = mActivity.layoutInflater
        val layouttoast = inflater.inflate(R.layout.custom_toast, null)
        mTextView = layouttoast.findViewById<View>(R.id.texttoast) as TextView
        mToast = Toast(mActivity)
        mToast!!.view = layouttoast
    }

    fun show(errorCode: Int) {
        if (errorCode == 0) {
            mTextView!!.text = mActivity.resources.getString(
                R.string.common_error
            )
        } else if (errorCode == 1) {
            mTextView!!.text = "Successfully logged in."
        } else if (errorCode == 2) {
            mTextView!!.text = "Login failed.Please try again later"
        } else if (errorCode == 3) {
            mTextView!!.text = "Successfully submitted login request"
        } else if (errorCode == 4) {
            mTextView!!.text = "Invalid user type"
        } else if (errorCode == 5) {
            mTextView!!.text = "No results found"
        } else if (errorCode == 6) {
            mTextView!!.text = "Successfully added to cart"
        } else if (errorCode == 7) {
            mTextView!!.text = "Already redeemed this gift"
        } else if (errorCode == 8) {
            mTextView!!.text = "OTP Verification Successful"
        } else if (errorCode == 9) {
            mTextView!!.text = "Incorrect OTP"
        } else if (errorCode == 10) {
            mTextView!!.text = "Please select dealers"
        } else if (errorCode == 11) {
            mTextView!!.text = "Cannot assign more than 10 Dealers"
        } else if (errorCode == 12) {
            mTextView!!.text = "Dealers added successfully"
        } else if (errorCode == 13) {
            mTextView!!.text = "No record found"
        } else if (errorCode == 14) {
            mTextView!!.text = "Please select a retailer"
        } else if (errorCode == 15) {
            mTextView!!.text = "Please enter coupon value"
        } else if (errorCode == 16) {
            mTextView!!.text = "Coupon value should not be greater than credit value"
        } else if (errorCode == 17) {
            mTextView!!.text = "Please enter coupon value to issue"
        } else if (errorCode == 18) {
            mTextView!!.text = "Coupon issued successfully"
        } else if (errorCode == 19) {
            mTextView!!.text = "Image uploaded successfully"
        } else if (errorCode == 20) {
            mTextView!!.text = "Cannot upload more than 2 images in a week"
        } else if (errorCode == 21) {
            mTextView!!.text = "Please capture an image to upload"
        } else if (errorCode == 22) {
            mTextView!!.text = "Insufficient coupon balance to redeem the gift"
        } else if (errorCode == 23) {
            mTextView!!.text = "This feature is only available for retailers"
        } else if (errorCode == 24) {
            mTextView!!.text = "Failed.Try again later"
        } else if (errorCode == 25) {
            mTextView!!.text = "Please select a distributor"
        } else if (errorCode == 26) {
            mTextView!!.text = "Profile updated successfully"
        } else if (errorCode == 27) {
            mTextView!!.text = "Profile updation failed"
        } else if (errorCode == 28) {
            mTextView!!.text =
                "Your registration with Strabo is on hold.Please login after verification"
        } else if (errorCode == 29) {
            mTextView!!.text = "Cannot login using multiple devices. Please contact VKC"
        } else if (errorCode == 30) {
            mTextView!!.text =
                "Mobile number updated successfully. Please login using new mobile number"
        } else if (errorCode == 31) {
            mTextView!!.text = "This feature is currently not available."
        } else if (errorCode == 32) {
            mTextView!!.text = "Please select state."
        } else if (errorCode == 33) {
            mTextView!!.text = "Please select district."
        } else if (errorCode == 34) {
            mTextView!!.text = "OTP resend successfully."
        } else if (errorCode == 35) {
            mTextView!!.text = "Already registered with scheme"
        } else if (errorCode == 36) {
            mTextView!!.text = "Please enter search key"
        } else if (errorCode == 37) {
            mTextView!!.text = "Please agree terms and conditions to continue."
        } else if (errorCode == 38) {
            mTextView!!.text = "Do not have enough coupons to add to cart"
        } else if (errorCode == 39) {
            mTextView!!.text = "Add to cart failed"
        } else if (errorCode == 40) {
            mTextView!!.text = "Please enter quantity value"
        } else if (errorCode == 41) {
            mTextView!!.text = "Please select a voucher"
        } else if (errorCode == 42) {
            mTextView!!.text = "Please enter the quantity value"
        } else if (errorCode == 43) {
            mTextView!!.text = "No items in cart"
        } else if (errorCode == 44) {
            mTextView!!.text = "No dealers found"
        } else if (errorCode == 45) {
            mTextView!!.text = "Please select dealer"
        } else if (errorCode == 46) {
            mTextView!!.text = "Not enough data to place order"
        } else if (errorCode == 47) {
            mTextView!!.text = "Order Placed Successfully"
        } else if (errorCode == 48) {
            mTextView!!.text = "Unable to place order. Try again"
        } else if (errorCode == 49) {
            mTextView!!.text = "Unable to delete data. Try again"
        } else if (errorCode == 50) {
            mTextView!!.text = "No messages found"
        } else if (errorCode == 51) {
            mTextView!!.text = "No images uploaded this week"
        } else if (errorCode == 52) {
            mTextView!!.text = "Image Deleted Successfully"
        } else if (errorCode == 53) {
            mTextView!!.text = "Please enter mobile number"
        } else if (errorCode == 54) {
            mTextView!!.text = "Invalid mobile number"
        } else if (errorCode == 55) {
            mTextView!!.text = "Mobile number updated successfully. Please login again."
        } else if (errorCode == 56) {
            mTextView!!.text = "Updation Failed."
        } else if (errorCode == 57) {
            mTextView!!.text = "Please select user type"
        } else if (errorCode == 58) {
            mTextView!!.text = "No internet connectivity"
        } else if (errorCode == 59) {
            mTextView!!.text = "Please enter a valid quantity"
        } else if (errorCode == 60) {
            mTextView!!.text = "Quantity cannot be 0"
        } else if (errorCode == 61) {
            mTextView!!.text =
                "Unable to issue coupons,since there is no scheme running in your state"
        } else if (errorCode == 62) {
            mTextView!!.text =
                "Unable to fetch cart count,since there is no scheme running in your state"
        } else if (errorCode == 63) {
            mTextView!!.text =
                "Unable to update cart,since there is no scheme running in your state"
        } else if (errorCode == 64) {
            mTextView!!.text =
                "Unable to add to cart,since there is no scheme running in your state"
        } else if (errorCode == 65) {
            mTextView!!.text = "Please select retailer"
        } else if (errorCode == 66) {
            mTextView!!.text = "No retailer redeem data found"
        } else if (errorCode == 67) {
            mTextView!!.text = "Submission Failed"
        } else if (errorCode == 68) {
            mTextView!!.text =
                "Unable to load report, since there is no scheme running in your state"
        }
        mToast!!.duration = Toast.LENGTH_SHORT
        mToast!!.show()
    } /*
     * CustomToast toast = new CustomToast(mActivity); toast.show(18);
	 */

    init {
        init()
    }
}