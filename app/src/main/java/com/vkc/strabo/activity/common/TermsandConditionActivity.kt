package com.vkc.strabo.activity.common

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.vkc.strabo.R
import com.vkc.strabo.manager.AppPrefenceManager
import android.content.Intent
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.CheckBox
import com.vkc.strabo.activity.HomeActivity
import com.vkc.strabo.activity.dealers.DealersActivity
import com.vkc.strabo.activity.common.SignUpActivity
import com.vkc.strabo.utils.CustomToast
import android.widget.CompoundButton

/**
 * Created by user2 on 12/10/17.
 */
class TermsandConditionActivity : Activity() {
    var webView: WebView? = null
    var mContext: Context? = null
    var checkTerms: CheckBox? = null
    var buttonNext: Button? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        mContext = this
        webView = findViewById<View>(R.id.webTerms) as WebView
        buttonNext = findViewById<View>(R.id.buttonNext) as Button
        buttonNext!!.setOnClickListener {
            if (AppPrefenceManager.getAgreeTerms(this@TermsandConditionActivity)) {
                if (AppPrefenceManager.getLoginStatusFlag(mContext) == "yes") {
                    startActivity(Intent(this@TermsandConditionActivity, HomeActivity::class.java))
                    finish()
                } else if (AppPrefenceManager.getIsVerifiedOTP(mContext) == "yes") {
                    if (AppPrefenceManager.getDealerCount(mContext) > 0) {
                        startActivity(
                            Intent(
                                this@TermsandConditionActivity,
                                HomeActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        startActivity(
                            Intent(
                                this@TermsandConditionActivity,
                                DealersActivity::class.java
                            )
                        )
                        finish()
                    }
                } else {
                    startActivity(
                        Intent(
                            this@TermsandConditionActivity,
                            SignUpActivity::class.java
                        )
                    )
                    finish()
                }
            } else {
                val toast = CustomToast(this@TermsandConditionActivity)
                toast.show(37)
            }
        }
        checkTerms = findViewById<View>(R.id.checkboxTerms) as CheckBox
        checkTerms!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //  Log.i("Checked ", " Yes");
                AppPrefenceManager.saveAgreeTerms(mContext, true)
            } else {
                AppPrefenceManager.saveAgreeTerms(mContext, false)
                //   Log.i("Checked ", " No");
            }
        }
        webView!!.loadUrl("file:///android_asset/terms.html")
        /* videoView = (VideoView) findViewById(R.id.videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }
        });

        videoView.start();*/
    }
}