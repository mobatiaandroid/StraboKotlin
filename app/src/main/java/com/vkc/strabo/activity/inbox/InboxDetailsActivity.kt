package com.vkc.strabo.activity.inbox

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.vkc.strabo.R
import com.vkc.strabo.appcontroller.AppController
import com.vkc.strabo.constants.VKCUrlConstants
import com.vkc.strabo.manager.HeaderManager

/**
 * Created by user2 on 4/12/17.
 */
class InboxDetailsActivity : Activity(), View.OnClickListener, VKCUrlConstants {
    var mContext: Activity? = null
    var headermanager: HeaderManager? = null
    var relativeHeader: LinearLayout? = null
    var mImageBack: ImageView? = null
    var imageNotification: ImageView? = null
    var title: String? = null
    var message: String? = null
    var created_on: String? = null
    var image: String? = null
    var date_from: String? = null
    var date_to: String? = null
    var textTitle: TextView? = null
    var textPinch: TextView? = null
    var webViewMessage: WebView? = null
    var webViewImage: WebView? = null
    var position = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        setContentView(R.layout.activity_inbox_details)
        mContext = this
        val intent = intent
        position = intent.extras!!.getInt("position")
        initUI()
    }

    private fun initUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        headermanager =
            HeaderManager(this@InboxDetailsActivity, resources.getString(R.string.inbox))
        headermanager!!.getHeader(relativeHeader, 1)
        mImageBack = headermanager!!.leftButton
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        textTitle = findViewById<View>(R.id.textTitle) as TextView
        textPinch = findViewById<View>(R.id.textPinch) as TextView
        imageNotification = findViewById<View>(R.id.imageNotification) as ImageView
        webViewMessage = findViewById<View>(R.id.webMessage) as WebView
        webViewImage = findViewById<View>(R.id.webImage) as WebView
        message = AppController.listNotification[position].message
        image = AppController.listNotification[position].image
        webViewImage!!.settings.builtInZoomControls = true
        webViewImage!!.settings.displayZoomControls = false
        title = AppController.listNotification[position].title
        val settings = webViewMessage!!.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        webViewMessage!!.setBackgroundColor(0x00000000)
        if (image!!.trim { it <= ' ' }.length > 0) {
            webViewImage!!.visibility = View.VISIBLE
            textPinch!!.visibility = View.VISIBLE
            // imageNotification.setVisibility(View.VISIBLE);
            //image = image.replaceAll(" ", "%20");

            //  Picasso.with(mContext).load(image).into(imageNotification);
            val summary =
                "<html><body style=\"color:white;\"><center><img  src='$image'width='100%', height='auto'\"></center></body></html>"
            webViewImage!!.setBackgroundColor(Color.TRANSPARENT)
            webViewImage!!.loadData(summary, "text/html", null)
        } else {
            // imageNotification.setVisibility(View.GONE);
            webViewImage!!.visibility = View.GONE
            textPinch!!.visibility = View.GONE
        }
        mImageBack!!.setOnClickListener(this)
        textTitle!!.text = title
        val summary = "<html><body style=\"color:white;\">$message</body></html>"
        webViewMessage!!.setBackgroundColor(Color.TRANSPARENT)
        webViewMessage!!.loadData(summary, "text/html", null)
        webViewMessage!!.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                view.loadUrl(request.url.toString())
                return false

                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString()));
                startActivity(intent);*/
                //return false;
            }

            override fun onPageFinished(view: WebView, url: String) {}
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
            }
        }
    }

    override fun onClick(v: View) {
        if (v === mImageBack) {
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (webViewMessage!!.canGoBack()) {
                        webViewMessage!!.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}