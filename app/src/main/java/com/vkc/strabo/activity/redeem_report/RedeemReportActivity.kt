package com.vkc.strabo.activity.redeem_report

import android.app.Activity
import android.os.Build
import com.vkc.strabo.constants.VKCUrlConstants
import com.vkc.strabo.manager.HeaderManager
import android.widget.LinearLayout
import com.vkc.strabo.activity.redeem_report.adapter.RedeemReportsAdapter
import com.vkc.strabo.activity.redeem_report.model.RedeemReportModel
import android.widget.EditText
import android.os.Bundle
import com.vkc.strabo.R
import android.text.TextWatcher
import com.vkc.strabo.appcontroller.AppController
import android.text.Editable
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.vkc.strabo.manager.AppPrefenceManager
import com.vkc.strabo.volleymanager.VolleyWrapper
import com.vkc.strabo.volleymanager.VolleyWrapper.ResponseListener
import com.vkc.strabo.utils.CustomToast
import org.json.JSONArray
import org.json.JSONObject
import com.vkc.strabo.activity.redeem_report.model.ReportDetailModel
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by user2 on 9/8/17.
 */
class RedeemReportActivity : Activity(), View.OnClickListener, VKCUrlConstants {
    lateinit var mContext: Activity
    var headermanager: HeaderManager? = null
    var relativeHeader: LinearLayout? = null
    var mImageBack: ImageView? = null
    private var listViewRedeemReportList: ListView? = null
    var adapter: RedeemReportsAdapter? = null
    private var tempRedeemReportList: ArrayList<RedeemReportModel>? = null
    var editSearch: EditText? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        setContentView(R.layout.activity_redeem_report)
        mContext = this
        initUI()
        report
    }

    private fun initUI() {
        tempRedeemReportList = ArrayList()
        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        headermanager =
            HeaderManager(this@RedeemReportActivity, resources.getString(R.string.redeem_report))
        headermanager!!.getHeader(relativeHeader, 1)
        mImageBack = headermanager!!.leftButton
        listViewRedeemReportList = findViewById<View>(R.id.listViewRedeemReportList) as ListView
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        mImageBack!!.setOnClickListener(this)
        editSearch = findViewById<View>(R.id.editSearch) as EditText
        editSearch!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                // TODO Auto-generated method stub
                if (s.length > 0) {
                    tempRedeemReportList!!.clear()
                    for (i in AppController.listRedeemReport.indices) {
                        if (AppController.listRedeemReport[i].custId
                                .contains(s)
                            || AppController.listRedeemReport[i]
                                .custName.toLowerCase().contains(s)
                            || AppController.listRedeemReport[i]
                                .custMobile.contains(s)
                            || AppController.listRedeemReport[i]
                                .custPlace.toLowerCase().contains(s)
                        ) {
                            tempRedeemReportList!!
                                .add(AppController.listRedeemReport[i])
                        } else {
                            listViewRedeemReportList!!.adapter = null
                        }
                    }
                    adapter = RedeemReportsAdapter(
                        mContext,
                        tempRedeemReportList
                    )
                    adapter!!.notifyDataSetChanged()
                    listViewRedeemReportList!!.adapter = adapter
                } else {
                    adapter = RedeemReportsAdapter(
                        mContext,
                        AppController.listRedeemReport
                    )
                    adapter!!.notifyDataSetChanged()
                    listViewRedeemReportList!!.adapter = adapter
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })
    }

    override fun onClick(v: View) {
        if (v === mImageBack) {
            finish()
        }
    }// CustomStatusDialog(RESPONSE_FAILURE);

    //Log.d("TAG", "Common error");
//CustomStatusDialog(RESPONSE_FAILURE);
    //    System.out.println("Response---Login" + successResponse);
    val report: Unit
        get() {
            try {
                AppController.listRedeemReport.clear()
                val name = arrayOf("dealerId")
                val values = arrayOf(AppPrefenceManager.getCustomerId(mContext))
                val manager = VolleyWrapper(VKCUrlConstants.GET_REDEEM_REPORT_APP)
                manager.getResponsePOST(mContext, 11, name, values,
                    object : ResponseListener {
                        override fun responseSuccess(successResponse: String) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {
                                parseResponse(successResponse)
                            } else {
                                val toast = CustomToast(mContext)
                                toast.show(0)
                            }
                        }

                        override fun responseFailure(failureResponse: String) {
                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }
                    })
            } catch (e: Exception) {
                // CustomStatusDialog(RESPONSE_FAILURE);
                e.printStackTrace()
                //Log.d("TAG", "Common error");
            }
        }

    private fun parseResponse(result: String) {
        try {
            var arrayData: JSONArray? = null
            val jsonObjectresponse = JSONObject(result)
            val objResponse = jsonObjectresponse
                .optJSONObject("response")
            val status = objResponse.getString("status")
            if (status == "Success") {

                /* if (response.has("orders")) { */
                arrayData = objResponse.optJSONArray("data")
                // }

                // int len = arrayOrders.length();
                if (arrayData.length() > 0) {
                    for (i in 0 until arrayData.length()) {
                        val redeemModel = RedeemReportModel()
                        val obj = arrayData.optJSONObject(i)
                        redeemModel.custId = obj.getString("cust_id")
                        redeemModel.custName = obj.getString("name")
                        redeemModel.custPlace = obj.getString("place")
                        redeemModel.custMobile = obj.getString("phone")
                        val objArray = obj.optJSONArray("details")
                        val listDetail = ArrayList<ReportDetailModel>()
                        for (j in 0 until objArray.length()) {
                            val model = ReportDetailModel()
                            val objData = objArray.optJSONObject(j)
                            model.gift_name = objData.optString("gift_name")
                            model.gift_qty = objData.optString("gift_qty")
                            model.rwd_points = objData.optString("rwd_points")
                            model.tot_coupons = objData
                                .optString("tot_coupons")
                            listDetail.add(model)
                        }
                        redeemModel.setListReportDetail(listDetail)
                        AppController.listRedeemReport.add(redeemModel)
                    }
                    val adapter = RedeemReportsAdapter(
                        mContext,
                        AppController.listRedeemReport
                    )
                    // adapter.notifyDataSetChanged();
                    listViewRedeemReportList!!.adapter = adapter
                } else {
                    val toast = CustomToast(mContext)
                    toast.show(0)
                }
            } else if (status == "scheme_error") {
                val toast = CustomToast(mContext)
                toast.show(68)
            }
        } catch (e: Exception) {
            println("Error$e")
        }
    }
}