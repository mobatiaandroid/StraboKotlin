package com.vkc.strabo.activity.redeem_list_dealer

import android.app.Activity
import com.vkc.strabo.constants.VKCUrlConstants
import com.vkc.strabo.manager.HeaderManager
import android.widget.LinearLayout
import android.widget.ExpandableListView
import com.vkc.strabo.activity.redeem_list_dealer.model.GiftListModel
import android.os.Bundle
import com.vkc.strabo.R
import android.widget.ExpandableListView.OnGroupExpandListener
import android.content.Intent
import com.vkc.strabo.activity.redeem_report.RedeemReportActivity
import com.vkc.strabo.manager.AppPrefenceManager
import com.vkc.strabo.volleymanager.VolleyWrapper
import com.vkc.strabo.volleymanager.VolleyWrapper.ResponseListener
import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.vkc.strabo.activity.redeem.model.RedeemModel
import org.json.JSONObject
import org.json.JSONArray
import com.vkc.strabo.activity.redeem_list_dealer.model.GiftUserModel
import com.vkc.strabo.activity.redeem_list_dealer.adapter.RedeemListAdapter
import com.vkc.strabo.utils.CustomToast
import java.lang.Exception
import java.util.ArrayList

class RedeemListDealerActivity : Activity(), VKCUrlConstants {
    lateinit var mContext: Activity
    lateinit var listGifts: ArrayList<RedeemModel>
    var listViewRedeem: ListView? = null
    var btn_left: ImageView? = null
    var headermanager: HeaderManager? = null
    var relativeHeader: LinearLayout? = null
    var mImageBack: ImageView? = null
    var mImageConsolidated: ImageView? = null
    var listViewHistory: ExpandableListView? = null

    // ArrayList<HistoryModel> ;
    var listGift: ArrayList<GiftListModel>? = null
    private var lastExpandedPosition = -1
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        setContentView(R.layout.activity_dealer_redeem_list)
        mContext = this
        initUI()
    }

    private fun initUI() {
        // get the reference of RecyclerView
        listGift = ArrayList()
        mImageBack = findViewById<View>(R.id.btn_left) as ImageView
        mImageConsolidated = findViewById<View>(R.id.btn_right) as ImageView
        listViewHistory = findViewById<View>(R.id.listViewRedeem) as ExpandableListView
        listViewHistory
            ?.setOnGroupExpandListener(OnGroupExpandListener { groupPosition ->
                if (lastExpandedPosition != -1
                    && groupPosition != lastExpandedPosition
                ) {
                    listViewHistory!!.collapseGroup(lastExpandedPosition)
                }
                lastExpandedPosition = groupPosition
            })
        mImageConsolidated!!.setOnClickListener {
            startActivity(
                Intent(
                    this@RedeemListDealerActivity,
                    RedeemReportActivity::class.java
                )
            )
        }
        mImageBack!!.setOnClickListener { finish() }
        redeemList
    }// TODO Auto-generated method stub

    // Log.v("LOG", "18022015 Errror" + failureResponse);
    // TODO Auto-generated method stub
    //   Log.v("LOG", "18022015 success" + successResponse);
    val redeemList: Unit
        get() {
            listGift!!.clear()
            val name = arrayOf("cust_id")
            val values = arrayOf(
                AppPrefenceManager.getCustomerId(mContext)
            )
            val manager = VolleyWrapper(VKCUrlConstants.GET_REDEEM_LIST)
            manager.getResponsePOST(mContext, 11, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String) {
                        // TODO Auto-generated method stub
                        //   Log.v("LOG", "18022015 success" + successResponse);
                        parseResponse(successResponse)
                    }

                    override fun responseFailure(failureResponse: String) {
                        // TODO Auto-generated method stub
                        // Log.v("LOG", "18022015 Errror" + failureResponse);
                    }
                })
        }

    @SuppressLint("NewApi")
    fun parseResponse(response: String?) {
        try {
            val jsonObject = JSONObject(response)
            val objResponse = jsonObject.optJSONObject("response")
            val status = objResponse.optString("status")
            if (status == "Success") {
                val arrayData = objResponse.optJSONArray("data")
                if (arrayData.length() > 0) {
                    for (i in 0 until arrayData.length()) {
                        val model = GiftListModel()
                        val obj = arrayData.optJSONObject(i)
                        val arrayDetail = obj.optJSONArray("details")
                        /*
                         * JSONArray arrayDetail = new JSONArray(
						 * obj.getString("details"));
						 */println("Detail Array $arrayDetail")
                        model.name = obj.getString("name")
                        model.phone = obj.getString("phone")
                        val listHist = ArrayList<GiftUserModel>()
                        for (j in 0 until arrayDetail.length()) {
                            val obj1 = arrayDetail.optJSONObject(j)
                            val model1 = GiftUserModel()
                            model1.gift_title = obj1.getString("gift_title")
                            model1.gift_image = obj1.getString("gift_image")
                            model1.gift_type = obj1.getString("gift_type")
                            model1.quantity = obj1.getString("quantity")
                            // System.out.println("Date Value "+model1.getDateValue());
                            listHist.add(model1)
                        }

                        // System.out.println("Parsed " +
                        // listHist.get(i).getDateValue());
                        model.listGiftUser = listHist
                        listGift!!.add(model)
                        // System.out.println("List History "+listHistory.get(0).getListHistory().get(1).getDateValue());
                      }
                    /*
                     * HistoryAdapter adapter = new
					 * HistoryAdapter(getActivity(), listHistory);
					 * listViewHistory.setAdapter(adapter);
					 */
                    val adapter = RedeemListAdapter(
                        mContext, listGift!!
                    )
                    listViewHistory!!.setAdapter(adapter)
                } else {
                    val toast = CustomToast(mContext)
                    toast.show(13)
                }
            } else {
                val toast = CustomToast(mContext)
                toast.show(24)
            }
        } catch (e: Exception) {
        }
    }
}