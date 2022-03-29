package com.vkc.strabo.activity.pointhistory

import android.app.Activity
import android.os.Build
import com.vkc.strabo.constants.VKCUrlConstants
import com.vkc.strabo.activity.pointhistory.model.TransactionModel
import com.vkc.strabo.manager.HeaderManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.vkc.strabo.R
import android.widget.ExpandableListView.OnGroupExpandListener
import androidx.annotation.RequiresApi
import com.vkc.strabo.manager.AppPrefenceManager
import com.vkc.strabo.activity.pointhistory.adapter.TransactionHistoryAdapter
import com.vkc.strabo.volleymanager.VolleyWrapper
import com.vkc.strabo.volleymanager.VolleyWrapper.ResponseListener
import org.json.JSONObject
import org.json.JSONArray
import com.vkc.strabo.activity.pointhistory.model.HistoryModel
import com.vkc.strabo.utils.CustomToast
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by user2 on 9/8/17.
 */
class PointHistoryActivity : Activity(), View.OnClickListener, VKCUrlConstants {
    var btnLogin: Button? = null
    var textEarned: TextView? = null
    var textDealerCount: TextView? = null
    var textCredit: TextView? = null
    var textDebit: TextView? = null
    var textEarnedPoint: TextView? = null
    var textTransferred: TextView? = null
    var textBalance: TextView? = null
    lateinit var mContext: Activity
    var mEditUserName: EditText? = null
    var mEditPassword: EditText? = null
    var imei_no: String? = null
    var historyType = ""
    lateinit var listHistory: ArrayList<TransactionModel>
    var listViewHistory: ExpandableListView? = null
    var headermanager: HeaderManager? = null
    var relativeHeader: LinearLayout? = null
    var llTransaction: LinearLayout? = null
    var llRetailer: LinearLayout? = null
    var llSubDealer: LinearLayout? = null
    var mImageBack: ImageView? = null
    private var lastExpandedPosition = -1
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        setContentView(R.layout.activity_transaction_history)
        mContext = this
        initUI()
    }

    private fun initUI() {
        listHistory = ArrayList()
        listViewHistory = findViewById<View>(R.id.listViewHistory) as ExpandableListView
        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        llTransaction = findViewById<View>(R.id.llTransactionType) as LinearLayout
        llRetailer = findViewById<View>(R.id.llRetailer) as LinearLayout
        llSubDealer = findViewById<View>(R.id.llSubDealer) as LinearLayout
        textEarned = findViewById<View>(R.id.textEarned) as TextView
        textEarnedPoint = findViewById<View>(R.id.textEarnedPoints) as TextView
        textTransferred = findViewById<View>(R.id.textTransferred) as TextView
        textBalance = findViewById<View>(R.id.textBalance) as TextView
        textDealerCount = findViewById<View>(R.id.textDealerCount) as TextView
        textCredit = findViewById<View>(R.id.textCredit) as TextView
        textDebit = findViewById<View>(R.id.textDebit) as TextView
        headermanager =
            HeaderManager(this@PointHistoryActivity, resources.getString(R.string.point_history))
        headermanager!!.getHeader(relativeHeader, 1)
        mImageBack = headermanager!!.leftButton
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        mImageBack!!.setOnClickListener(this)
        textDebit!!.setOnClickListener(this)
        textCredit!!.setOnClickListener(this)
        listViewHistory!!.setOnGroupExpandListener { groupPosition ->
            if (lastExpandedPosition != -1
                && groupPosition != lastExpandedPosition
            ) {
                listViewHistory!!.collapseGroup(lastExpandedPosition)
            }
            lastExpandedPosition = groupPosition
        }
        if (AppPrefenceManager.getUserType(mContext) == "7" || AppPrefenceManager.getUserType(
                mContext
            ) == "6"
        ) {
            llTransaction!!.visibility = View.VISIBLE
            llRetailer!!.visibility = View.GONE
            llSubDealer!!.visibility = View.VISIBLE
            historyType = "CREDIT"
            history
        } else {
            llTransaction!!.visibility = View.GONE
            llRetailer!!.visibility = View.VISIBLE
            llSubDealer!!.visibility = View.GONE
            historyType = ""
            history
        }
    }

    override fun onClick(v: View) {
        if (v === mImageBack) {
            finish()
        } else if (v === textCredit) {
            historyType = "CREDIT"
            textCredit!!.setBackgroundResource(R.drawable.rounded_rect_pantone)
            textDebit!!.setBackgroundResource(R.drawable.rounded_rect_redline)
            val adapter = TransactionHistoryAdapter(
                mContext, listHistory
            )
            listViewHistory!!.setAdapter(adapter)
            history
        } else if (v === textDebit) {
            historyType = "DEBIT"
            val adapter = TransactionHistoryAdapter(
                mContext, listHistory
            )
            listViewHistory!!.setAdapter(adapter)
            textCredit!!.setBackgroundResource(R.drawable.rounded_rect_redline)
            textDebit!!.setBackgroundResource(R.drawable.rounded_rect_pantone)
            history
        }
    }// CustomStatusDialog(RESPONSE_FAILURE);//CustomStatusDialog(RESPONSE_FAILURE);//  System.out.println("Date Value " + model1.getDateValue());

    //	System.out.println("Parsed " + listHist.get(i).getDateValue());
    //     System.out.println("List History " + listHistory.get(0).getListHistory().get(1).getDateValue());
    /*
* HistoryAdapter adapter = new
* HistoryAdapter(getActivity(), listHistory);
* listViewHistory.setAdapter(adapter);
*/
/*JSONArray arrayDetail = new JSONArray(
                                obj.getString("details"));*/
    //  System.out.println("Detail Array " + arrayDetail);
    //    System.out.println("Response---Login" + successResponse);
    val history: Unit
        get() {
            listHistory!!.clear()
            try {
                val name = arrayOf("userid", "role", "type")
                val values = arrayOf(
                    AppPrefenceManager.getCustomerId(mContext),
                    AppPrefenceManager.getUserType(mContext), historyType
                )
                val manager = VolleyWrapper(VKCUrlConstants.GET_POINTS_HISTORY)
                manager.getResponsePOST(mContext, 11, name, values,
                    object : ResponseListener {
                        override fun responseSuccess(successResponse: String) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {
                                try {
                                    val jsonObject = JSONObject(successResponse)
                                    val objResponse = jsonObject.optJSONObject("response")
                                    val status = objResponse.optString("status")
                                    val credits = objResponse.optString("total_credits")
                                    textEarned!!.text = credits
                                    textEarnedPoint!!.text = objResponse.optString("total_credits")
                                    textTransferred!!.text = objResponse.optString("total_debits")
                                    var balance: String?
                                    if (objResponse.optString("balance_point") == "") {
                                        balance = "0"
                                    }
                                    run { balance = objResponse.optString("balance_point") }
                                    textBalance!!.text = balance
                                    if (status == "Success") {
                                        val arrayData = objResponse.optJSONArray("data")
                                        if (arrayData.length() > 0) {
                                            for (i in 0 until arrayData.length()) {
                                                val model = TransactionModel()
                                                val obj = arrayData.optJSONObject(i)
                                                val arrayDetail = obj.optJSONArray("details")
                                                /*JSONArray arrayDetail = new JSONArray(
                 obj.getString("details"));*/
                                                //  System.out.println("Detail Array " + arrayDetail);
                                                model.userName = obj.getString("to_name")
                                                model.totPoints = obj.getString("tot_points")
                                                val listHist = ArrayList<HistoryModel>()
                                                for (j in 0 until arrayDetail.length()) {
                                                    val obj1 = arrayDetail.optJSONObject(j)
                                                    val model1 = HistoryModel()
                                                    model1.points = obj1.getString("points")
                                                    model1.type = obj1.getString("type")
                                                    model1.to_name = obj1.getString("to_name")
                                                    model1.to_role = obj1.getString("to_role")
                                                    model1.dateValue = obj1.getString("date")

                                                    //  System.out.println("Date Value " + model1.getDateValue());
                                                    listHist.add(model1)
                                                }

                                                //	System.out.println("Parsed " + listHist.get(i).getDateValue());
                                                model.listHistory = listHist
                                                listHistory!!.add(model)
                                                //     System.out.println("List History " + listHistory.get(0).getListHistory().get(1).getDateValue());
                                            }
                                            /*
          * HistoryAdapter adapter = new
          * HistoryAdapter(getActivity(), listHistory);
          * listViewHistory.setAdapter(adapter);
          */textDealerCount!!.text = listHistory!!.size.toString()
                                            val adapter = TransactionHistoryAdapter(
                                                mContext, listHistory
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
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
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
                Log.d("TAG", "Common error")
            }
        }
}