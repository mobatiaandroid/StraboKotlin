package com.vkc.strabo.activity.my_customers

import android.app.Activity
import android.os.Build
import com.vkc.strabo.constants.VKCUrlConstants
import android.widget.TextView
import com.vkc.strabo.manager.HeaderManager
import android.widget.LinearLayout
import com.vkc.strabo.activity.my_customers.model.CustomerModel
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.vkc.strabo.R
import com.vkc.strabo.activity.dealers.model.DealerModel
import com.vkc.strabo.manager.AppPrefenceManager
import com.vkc.strabo.volleymanager.VolleyWrapper
import com.vkc.strabo.volleymanager.VolleyWrapper.ResponseListener
import org.json.JSONObject
import org.json.JSONArray
import com.vkc.strabo.activity.my_customers.adapter.CustomerAdapter
import com.vkc.strabo.utils.CustomToast
import org.json.JSONException
import java.lang.Exception
import java.util.ArrayList
import java.util.Comparator

/**
 * Created by user2 on 7/8/17.
 */
class MyCustomersActivity : Activity(), VKCUrlConstants, View.OnClickListener {
    lateinit var mContext: Activity
    var listDealers: ArrayList<DealerModel>? = null
    var listViewDealer: ListView? = null
    var textSubmit: TextView? = null

    // ArrayList<String> listIds;
    var headermanager: HeaderManager? = null
    var relativeHeader: LinearLayout? = null
    var mImageBack: ImageView? = null
    lateinit var listCustomers: ArrayList<CustomerModel>
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        setContentView(R.layout.activity_my_customers)
        mContext = this
        ininUI()
        customers
    }

    private fun ininUI() {
        listCustomers = ArrayList()
        listViewDealer = findViewById<View>(R.id.listMyCustomer) as ListView
        // mImageSearch = (ImageView) findViewById(R.id.imageSearch);
        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        headermanager =
            HeaderManager(this@MyCustomersActivity, resources.getString(R.string.my_customers))
        headermanager!!.getHeader(relativeHeader, 1)
        mImageBack = headermanager!!.leftButton
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        mImageBack!!.setOnClickListener(this)
    }// CustomStatusDialog(RESPONSE_FAILURE);//CustomStatusDialog(RESPONSE_FAILURE);// TODO Auto-generated catch block

    //    System.out.println("Response---Login" + successResponse);
    val customers: Unit
        get() {
            try {
                val name = arrayOf("cust_id")
                val values = arrayOf(AppPrefenceManager.getCustomerId(mContext))
                val manager = VolleyWrapper(VKCUrlConstants.GET_CUSTOMERS)
                manager.getResponsePOST(mContext, 11, name, values,
                    object : ResponseListener {
                        override fun responseSuccess(successResponse: String) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {
                                try {
                                    val obj = JSONObject(successResponse)
                                    val objResponse = obj
                                        .optJSONObject("response")
                                    val status = objResponse.optString("status")
                                    if (status == "Success") {
                                        val arrayData = objResponse
                                            .optJSONArray("data")
                                        if (arrayData.length() > 0) {
                                            for (i in 0 until arrayData.length()) {
                                                val objItem = arrayData
                                                    .optJSONObject(i)
                                                val model = CustomerModel()
                                                model.name = objItem.optString("name")
                                                model.phone = objItem
                                                    .optString("phone")
                                                model.role = objItem.optString("role")
                                                listCustomers!!.add(model)
                                            }
                                            val adapter = CustomerAdapter(
                                                mContext, listCustomers
                                            )
                                            listViewDealer!!.adapter = adapter
                                        } else {
                                            val toast = CustomToast(mContext)
                                            toast.show(13)
                                        }
                                    } else {
                                        val toast = CustomToast(mContext)
                                        toast.show(24)
                                    }
                                } catch (e: JSONException) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace()
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

    override fun onClick(v: View) {
        if (v === mImageBack) {
            finish()
        }
        when (v.id) {
        }
    }

    inner class CustomComparator : Comparator<DealerModel> {
        override fun compare(o1: DealerModel, o2: DealerModel): Int {
            return o1.name.compareTo(o2.name)
        }
    }
}