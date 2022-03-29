package com.vkc.strabo.activity.common

import android.app.Activity
import android.content.Context
import android.widget.VideoView
import android.os.Bundle
import com.vkc.strabo.R
import com.vkc.strabo.manager.AppPrefenceManager
import android.content.Intent
import com.vkc.strabo.activity.HomeActivity
import com.vkc.strabo.activity.dealers.DealersActivity
import com.vkc.strabo.activity.common.SignUpActivity
import android.os.CountDownTimer
import com.vkc.strabo.activity.common.TermsandConditionActivity

class SplashActivity : Activity() {
    var videoView: VideoView? = null
    var mContext: Context? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext = this
       loadSplash()
    }

    private fun startNextActivity() {
        if (isFinishing) return
        if (AppPrefenceManager.getLoginStatusFlag(mContext) == "yes") {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else if (AppPrefenceManager.getIsVerifiedOTP(mContext) == "yes") {
            if (AppPrefenceManager.getUserType(mContext) == "6") {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                if (AppPrefenceManager.getDealerCount(mContext) > 0) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, DealersActivity::class.java))
                    finish()
                }
            }
        } else {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    private fun loadSplash() {
        val countDownTimer: CountDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (AppPrefenceManager.getAgreeTerms(mContext)) {
                    startNextActivity()
                } else {
                    startActivity(
                        Intent(
                            this@SplashActivity,
                            TermsandConditionActivity::class.java
                        )
                    )
                    finish()
                }

                //
            }
        }
        countDownTimer.start()
    }
}