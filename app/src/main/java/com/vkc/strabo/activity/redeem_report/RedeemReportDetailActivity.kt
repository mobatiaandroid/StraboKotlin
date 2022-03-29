package com.vkc.strabo.activity.redeem_report

import androidx.appcompat.app.AppCompatActivity
import com.vkc.strabo.constants.VKCUrlConstants
import android.app.Activity
import com.vkc.strabo.manager.HeaderManager
import android.widget.LinearLayout
import android.os.Bundle
import com.vkc.strabo.R
import android.content.Intent
import com.vkc.strabo.appcontroller.AppController
import com.vkc.strabo.activity.redeem_report.adapter.ReportDetailAdapter
import android.graphics.drawable.ColorDrawable
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ListView

class RedeemReportDetailActivity : AppCompatActivity(), View.OnClickListener, VKCUrlConstants {
    var listViewRedeemReportDetail: ListView? = null
    var position = 0
    var cust_id: String? = null
    var mContext: Activity? = null
    var headermanager: HeaderManager? = null
    var relativeHeader: LinearLayout? = null
    var mImageBack: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redeem_report_detail)
        mContext = this
        initialiseUI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialiseUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        headermanager = HeaderManager(
            this@RedeemReportDetailActivity,
            resources.getString(R.string.redeem_report_detail)
        )
        headermanager!!.getHeader(relativeHeader, 1)
        mImageBack = headermanager!!.leftButton
        listViewRedeemReportDetail = findViewById<View>(R.id.listViewRedeemReportDetail) as ListView
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        mImageBack!!.setOnClickListener(this)
        val intent = intent
        //position = Integer.parseInt(intent.getExtras().getString("position"));
        cust_id = intent.extras!!.getString("cust_id")
        for (i in AppController.listRedeemReport.indices) {
            if (AppController.listRedeemReport[i].custId
                == cust_id
            ) {
                position = i
                break
            }
        }
        val adapter = ReportDetailAdapter(
            mContext,
            AppController.listRedeemReport[position]
                .getListReportDetail()
        )
        listViewRedeemReportDetail!!.adapter = adapter
    }

    fun setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        val actionBar = supportActionBar
        actionBar!!.subtitle = ""
        actionBar.title = ""
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun onRestart() {
        // TODO Auto-generated method stub
        super.onRestart()
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
    }

    override fun onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed()
        finish()
    }

    override fun onClick(v: View) {
        if (v === mImageBack) {
            finish()
        }
    }
}