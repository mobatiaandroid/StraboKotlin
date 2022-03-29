package com.vkc.strabo.activity.inbox

import androidx.appcompat.app.AppCompatActivity
import com.vkc.strabo.constants.VKCUrlConstants
import android.app.Activity
import com.vkc.strabo.activity.inbox.model.InboxModel
import com.vkc.strabo.manager.HeaderManager
import android.widget.LinearLayout
import android.os.Bundle
import com.vkc.strabo.R
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.vkc.strabo.activity.inbox.InboxDetailsActivity
import com.vkc.strabo.appcontroller.AppController
import com.vkc.strabo.manager.AppPrefenceManager
import com.vkc.strabo.volleymanager.VolleyWrapper
import com.vkc.strabo.volleymanager.VolleyWrapper.ResponseListener
import org.json.JSONObject
import org.json.JSONArray
import com.vkc.strabo.activity.inbox.adapter.InboxAdapter
import com.vkc.strabo.utils.CustomToast
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by user2 on 4/12/17.
 */
class InboxActivity : AppCompatActivity(), View.OnClickListener, VKCUrlConstants {
    lateinit var mContext: Activity
    var listNotification: ArrayList<InboxModel>? = null
    var listViewInbox: ListView? = null
    var headermanager: HeaderManager? = null
    var relativeHeader: LinearLayout? = null
    var mImageBack: ImageView? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        setContentView(R.layout.activity_inbox)
        mContext = this
        initUI()
    }

    private fun initUI() {
        listNotification = ArrayList()
        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        headermanager = HeaderManager(this@InboxActivity, resources.getString(R.string.inbox))
        headermanager!!.getHeader(relativeHeader, 1)
        mImageBack = headermanager!!.leftButton
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        listViewInbox = findViewById<View>(R.id.listViewInbox) as ListView
        mImageBack!!.setOnClickListener(this)
        inbox
        listViewInbox!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@InboxActivity, InboxDetailsActivity::class.java)
            /* intent.putExtra("title",listNotification.get(position).getTitle());
                    intent.putExtra("message",listNotification.get(position).getMessage());
                    intent.putExtra("created_on",listNotification.get(position).getCreatedon());
                    intent.putExtra("image",listNotification.get(position).getImage());
                    intent.putExtra("date_from",listNotification.get(position).getFrom_date());
                    intent.putExtra("date_to",listNotification.get(position).getTo_date());*/intent.putExtra(
            "position",
            position
        )
            startActivity(intent)
        }
    }

    override fun onClick(v: View) {
        if (v === mImageBack) {
            finish()
        }
    }// CustomStatusDialog(RESPONSE_FAILURE);//CustomStatusDialog(RESPONSE_FAILURE);

    //    System.out.println("Response---Login" + successResponse);
    val inbox: Unit
        get() {
            AppController.listNotification.clear()
            try {
                val name = arrayOf("cust_id", "role")
                val values = arrayOf(
                    AppPrefenceManager.getCustomerId(mContext),
                    AppPrefenceManager.getUserType(mContext)
                )
                val manager = VolleyWrapper(VKCUrlConstants.GET_NOTIFICATIONS)
                manager.getResponsePOST(mContext, 11, name, values,
                    object : ResponseListener {
                        override fun responseSuccess(successResponse: String) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {
                                try {
                                    val jsonObject = JSONObject(successResponse)
                                    val objResponse = jsonObject.optJSONObject("response")
                                    val status = objResponse.optString("status")
                                    if (status == "Success") {
                                        val arrayData = objResponse.optJSONArray("data")
                                        if (arrayData.length() > 0) {
                                            for (i in 0 until arrayData.length()) {
                                                val obj = arrayData.getJSONObject(i)
                                                val model = InboxModel()
                                                model.title = obj.optString("title")
                                                model.message = obj.optString("message")
                                                model.image = obj.optString("image")
                                                model.createdon = obj.optString("createdon")
                                                model.from_date = obj.optString("from_date")
                                                model.to_date = obj.optString("to_date")
                                                AppController.listNotification.add(model)
                                            }
                                            val adapter = InboxAdapter(
                                                mContext, AppController.listNotification
                                            )
                                            listViewInbox!!.adapter = adapter
                                        } else {
                                            val toast = CustomToast(mContext)
                                            toast.show(50)
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

    override fun onRestart() {
        super.onRestart()
        inbox
    }
}