package com.vkc.strabo.activity.redeem_subdealer

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import com.vkc.strabo.constants.VKCUrlConstants
import android.os.Bundle
import com.vkc.strabo.R
import android.widget.AdapterView.OnItemClickListener
import com.vkc.strabo.manager.AppPrefenceManager
import com.vkc.strabo.volleymanager.VolleyWrapper
import com.vkc.strabo.volleymanager.VolleyWrapper.ResponseListener
import org.json.JSONObject
import org.json.JSONArray
import com.vkc.strabo.activity.redeem_subdealer.adapter.SubdealerRedeemAdapter
import com.vkc.strabo.utils.CustomToast
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import com.vkc.strabo.activity.redeem.model.RedeemModel
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by user2 on 4/4/18.
 */
class RedeemHistorySubdealerActivity : Activity(), VKCUrlConstants {
    lateinit var mContext: Activity
    var listGifts: ArrayList<RedeemModel>? = null
    var listViewRedeem: ListView? = null
    var btn_left: ImageView? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        setContentView(R.layout.activity_subdealer_redeem_history)
        mContext = this
        initUI()
    }

    private fun initUI() {
        // get the reference of RecyclerView
        listGifts = ArrayList()
        listViewRedeem = findViewById<View>(R.id.listViewRedeem) as ListView
        btn_left = findViewById<View>(R.id.btn_left) as ImageView
        btn_left!!.setOnClickListener { finish() }
        listViewRedeem!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val dialog: DialogData = DialogData(mContext!!, position)
            dialog.show()
        }
        history
    }// CustomStatusDialog(RESPONSE_FAILURE);//CustomStatusDialog(RESPONSE_FAILURE);// Toast.makeText(mContext, getResources().getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();

    //    System.out.println("Response---Login" + successResponse);
    val history: Unit
        get() {
            listGifts!!.clear()
            try {
                val name = arrayOf("cust_id")
                val values = arrayOf(AppPrefenceManager.getCustomerId(mContext))
                val manager = VolleyWrapper(VKCUrlConstants.REDEEM_HISTORY_SUBDEALER)
                manager.getResponsePOST(mContext, 11, name, values,
                    object : ResponseListener {
                        override fun responseSuccess(successResponse: String) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {
                                try {
                                    val rootObject = JSONObject(successResponse)
                                    val objResponse = rootObject.optJSONObject("response")
                                    val status = objResponse.optString("status")
                                    if (status.equals("Success", ignoreCase = true)) {
                                        val dataArray = objResponse.optJSONArray("data")
                                        if (dataArray.length() > 0) {
                                            for (i in 0 until dataArray.length()) {
                                                val obj = dataArray.optJSONObject(i)
                                                val model = RedeemModel()
                                                model.image = obj.optString("image")
                                                model.point = obj.optString("point")
                                                model.title = obj.optString("title")
                                                model.date = obj.optString("date")
                                                model.quantity = obj.optString("quantity")
                                                model.status = obj.optString("status")
                                                model.dealer = obj.optString("dealer_name")
                                                model.gift_type = obj.optString("gift_type")
                                                model.retailer_name = obj.optString("retailer_name")
                                                listGifts!!.add(model)
                                            }
                                            val adapter =
                                                SubdealerRedeemAdapter(mContext!!, listGifts!!)
                                            listViewRedeem!!.adapter = adapter
                                        } else {
                                            val toast = CustomToast(mContext)
                                            toast.show(5)
                                        }
                                    } else {
                                        val toast = CustomToast(mContext)
                                        toast.show(4)
                                        // Toast.makeText(mContext, getResources().getString(R.string.invalid_user), Toast.LENGTH_SHORT).show();
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

    inner class DialogData     // TODO Auto-generated constructor stub
        (var mActivity: Activity, var position: Int) : Dialog(mActivity) {
        var type: String? = null
        var message: String? = null
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_subdealer_history)
            init()
        }

        private fun init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            val txt_date = findViewById<View>(R.id.txt_date) as TextView
            val txt_type = findViewById<View>(R.id.txt_type) as TextView
            val txt_name = findViewById<View>(R.id.txt_name) as TextView
            val text_point = findViewById<View>(R.id.text_point) as TextView
            val text_quantity = findViewById<View>(R.id.text_quantity) as TextView
            val text_dealer = findViewById<View>(R.id.text_dealer) as TextView
            val buttonCancel = findViewById<View>(R.id.btn_ok) as Button
            buttonCancel.setOnClickListener { dismiss() }
            txt_date.text = ": " + listGifts!![position].date
            txt_type.text = ": " + listGifts!![position].gift_type
            txt_name.text = ": " + listGifts!![position].title
            text_point.text = ": " + listGifts!![position].point
            text_quantity.text = ": " + listGifts!![position].quantity
            //  text_status.setText(": " + listGifts.get(position).getStatus());
            text_dealer.text = ": " + listGifts!![position].dealer
        }
    }
}