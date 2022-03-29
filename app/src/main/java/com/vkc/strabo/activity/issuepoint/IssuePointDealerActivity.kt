package com.vkc.strabo.activity.issuepoint

import androidx.appcompat.app.AppCompatActivity
import com.vkc.strabo.constants.VKCUrlConstants
import com.vkc.strabo.activity.issuepoint.model.UserModel
import com.vkc.strabo.manager.HeaderManager
import android.app.Activity
import android.os.Bundle
import com.vkc.strabo.R
import android.widget.AdapterView.OnItemClickListener
import android.text.TextWatcher
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.vkc.strabo.manager.AppPrefenceManager
import com.vkc.strabo.volleymanager.VolleyWrapper
import com.vkc.strabo.volleymanager.VolleyWrapper.ResponseListener
import org.json.JSONObject
import com.vkc.strabo.utils.CustomToast
import org.json.JSONArray
import org.json.JSONException
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by user2 on 10/8/17.
 */
class IssuePointDealerActivity : AppCompatActivity(), View.OnClickListener, VKCUrlConstants {
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    private var mTxtPoint: TextView? = null
    var categories: MutableList<String> = ArrayList()
    private val listArticleNumbers: ArrayList<String>? = null
    var spinnerUserType: Spinner? = null
    var btnSubmit: Button? = null
    var btnReset: Button? = null
    var mEditPoint: EditText? = null
    var myPoint = 0
    var userType: String? = null
    var selectedId: String? = null
    private var edtSearch: AutoCompleteTextView? = null
    var listUsers: ArrayList<UserModel>? = null
    var textId: TextView? = null
    var textName: TextView? = null
    var textAddress: TextView? = null
    var textPhone: TextView? = null
    var textType: TextView? = null
    var llData: LinearLayout? = null
    var headermanager: HeaderManager? = null
    lateinit var mContext: Activity
    private var relativeHeader: LinearLayout? = null
    private var mImageBack: ImageView? = null
    private val llUserType: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer_issue_point)
        mContext = this
        init()
    }

    private fun init() {
        listUsers = ArrayList()
        spinnerUserType = findViewById<View>(R.id.spinnerUserType) as Spinner
        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        selectedId = ""
        headermanager =
            HeaderManager(this@IssuePointDealerActivity, resources.getString(R.string.issue_point))
        headermanager!!.getHeader(relativeHeader, 1)
        mImageBack = headermanager!!.leftButton
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        mImageBack!!.setOnClickListener(this)
        mTxtPoint = findViewById<View>(R.id.textPoints) as TextView
        btnSubmit = findViewById<View>(R.id.btnSubmit) as Button
        btnReset = findViewById<View>(R.id.btnReset) as Button
        mEditPoint = findViewById<View>(R.id.editPoints) as EditText
        llData = findViewById<View>(R.id.llData) as LinearLayout
        llData!!.visibility = View.GONE
        // llUserType = (LinearLayout) findViewById(R.id.llUserType);
        textId = findViewById<View>(R.id.textViewId) as TextView
        textName = findViewById<View>(R.id.textViewName) as TextView
        textAddress = findViewById<View>(R.id.textViewAddress) as TextView
        textPhone = findViewById<View>(R.id.textViewPhone) as TextView
        textType = findViewById<View>(R.id.textViewType) as TextView
        edtSearch = findViewById<View>(R.id.autoSearch) as AutoCompleteTextView
        btnSubmit!!.setOnClickListener(this)
        btnReset!!.setOnClickListener(this)
        categories.clear()
        categories.add("Select User Type")
        categories.add("Retailer")
        categories.add("Sub Dealer")
        edtSearch!!.setOnClickListener { // TODO Auto-generated method stub
            edtSearch!!.showDropDown()
        }
        edtSearch!!.onItemClickListener =
            OnItemClickListener { arg0, arg1, arg2, arg3 -> // TODO Auto-generated method stub
                val selectedData = edtSearch!!.text.toString()
                for (i in listUsers!!.indices) {
                    if (listUsers!![i].userName == selectedData) {
                        selectedId = listUsers!![i].userId
                        println("Selected Id : $selectedId")
                        userData
                        break
                    } else {
                        selectedId = ""
                    }
                }
            }
        edtSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0) {
                } else {
                    selectedId = ""
                    llData!!.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        val dataAdapter = ArrayAdapter(
            mContext!!, android.R.layout.simple_spinner_item, categories
        )

        // Drop down layout style - list view with radio button
        dataAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinnerUserType!!.adapter = dataAdapter
        spinnerUserType!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View, pos: Int,
                arg3: Long
            ) {
                // TODO Auto-generated method stub
                if (pos > 0) {
                    if (pos == 1) {
                        userType = "5"
                        selectedId = ""
                        edtSearch!!.setText("")
                        // mEditPoint.setText("");
                        getUsers(userType!!)
                    } else {
                        userType = "7"
                        selectedId = ""
                        edtSearch!!.setText("")
                        getUsers(userType!!)
                    }
                } else {
                    userType = ""
                }
                println("User Type : $userType")
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
        myPoints
    }

    /**
     * Method FeedbackSubmitApi Return Type:void parameters:null Date:Feb 18,
     * 2015 Author:Archana.S
     */
    val myPoints: Unit
        get() {
            val name = arrayOf("cust_id", "role")
            val values = arrayOf(
                AppPrefenceManager.getCustomerId(mContext),
                AppPrefenceManager.getUserType(mContext)
            )
            val manager = VolleyWrapper(VKCUrlConstants.GET_DEALER_POINT)
            manager.getResponsePOST(mContext, 11, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String) {
                        parseResponse(successResponse)
                    }

                    override fun responseFailure(failureResponse: String) {}
                })
        }

    /**
     * Method Name:parseResponse Return Type:void parameters:response Date:Feb
     * 18, 2015 Author:Archana.S
     */
    fun parseResponse(response: String?) {
        try {
            val jsonObject = JSONObject(response)
            val objResponse = jsonObject.optJSONObject("response")
            val status = objResponse.optString("status")
            if (status == "Success") {
                val points = objResponse.optString("loyality_point")
                myPoint = points.toInt()
                mTxtPoint!!.text = points
            } else {

                // Bibin  VKCUtils.showtoast(mContext, 13);
            }
        } catch (e: Exception) {
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        if (v === btnSubmit) {
            if (userType == "") {
                val toast = CustomToast(mContext)
                toast.show(49)
            } else if (edtSearch!!.text.toString().trim { it <= ' ' } == "") {
                val toast = CustomToast(mContext)
                toast.show(51)
            } else if (mEditPoint!!.text.toString().trim { it <= ' ' } == "") {
                // VKCUtils.textWatcherForEditText(mEditPoint,
                // "Mandatory field");
                val toast = CustomToast(mContext)
                toast.show(52)
            } else if (mEditPoint!!.text.toString().trim { it <= ' ' }.toInt() > myPoint) {
                // FeedbackSubmitApi();
                val toast = CustomToast(mContext)
                toast.show(48)
            } else {
                submitPoints()
            }
        } else if (v === btnReset) {
            spinnerUserType!!.setSelection(0)
            userType = ""
            selectedId = ""
            edtSearch!!.setText("")
            mEditPoint!!.setText("")
            llData!!.visibility = View.GONE
        } else if (v === mImageBack) {
            finish()
        }
    }

    public override fun onResume() {
        // TODO Auto-generated method stub
        // edtSearch.setText(AppController.articleNumber);
        super.onResume()
    }

    private fun getUsers(type: String) {
        listUsers!!.clear()
        val name = arrayOf("cust_id", "user_type")
        val values = arrayOf(
            AppPrefenceManager.getCustomerId(mContext),
            type
        )
        val manager = VolleyWrapper(VKCUrlConstants.GET_USERS)
        manager.getResponsePOST(mContext, 11, name, values,
            object : ResponseListener {
                override fun responseSuccess(successResponse: String) {
                    try {
                        val responseObj = JSONObject(
                            successResponse
                        )
                        val response = responseObj
                            .getJSONObject("response")
                        val status = response.getString("status")
                        if (status == "Success") {
                            val dataArray = response
                                .optJSONArray("data")
                            if (dataArray.length() > 0) {
                                for (i in 0 until dataArray.length()) {
                                    // listArticle[i]=articleArray.getString(i);
                                    val obj = dataArray
                                        .getJSONObject(i)
                                    val model = UserModel()
                                    model.userId = obj.getString("id")
                                    model.userName = obj.getString("name")
                                    // model.setCity(obj.getString("city"));
                                    listUsers!!.add(model)
                                }
                                val listUser = ArrayList<String>()
                                for (i in listUsers!!.indices) {
                                    listUser.add(
                                        listUsers!![i]
                                            .userName
                                    )
                                }
                                val adapter = ArrayAdapter(
                                    mContext!!,
                                    android.R.layout.simple_list_item_1,
                                    listUser
                                )
                                edtSearch!!.threshold = 1
                                edtSearch!!.setAdapter(adapter)
                            } else {
                                val toast = CustomToast(
                                    mContext
                                )
                                toast.show(17)
                            }
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }

                override fun responseFailure(failureResponse: String) { // TODO
                    // Auto-generated method stub
                }
            })
    }

    fun submitPoints() {
        val name = arrayOf("userid", "to_user_id", "to_role", "points", "role")
        val values = arrayOf(
            AppPrefenceManager.getCustomerId(mContext),
            selectedId, userType, mEditPoint!!.text.toString(),
            AppPrefenceManager.getUserType(mContext)
        )
        val manager = VolleyWrapper(VKCUrlConstants.SUBMIT_POINTS)
        manager.getResponsePOST(mContext, 11, name, values,
            object : ResponseListener {
                override fun responseSuccess(successResponse: String) {
                    // TODO Auto-generated method stub
                    // Log.v("LOG", "18022015 success" + successResponse);
                    try {
                        val objResponse = JSONObject(
                            successResponse
                        )
                        val status = objResponse.optString("response")
                        if (status == "1") {
                            val toast = CustomToast(
                                mContext
                            )
                            toast.show(18)
                            edtSearch!!.setText("")
                            mEditPoint!!.setText("")
                            val dataAdapter = ArrayAdapter(
                                mContext!!,
                                android.R.layout.simple_spinner_item,
                                categories
                            )
                            dataAdapter
                                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerUserType!!.adapter = dataAdapter
                            myPoints
                        } else {
                            val toast = CustomToast(
                                mContext
                            )
                            toast.show(67)
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }

                    // parseResponse(successResponse);
                }

                override fun responseFailure(failureResponse: String) {
                    // TODO Auto-generated method stub
                    Log.v("LOG", "18022015 Errror$failureResponse")
                }
            })
    }// TODO

    // Auto-generated method stub
// TODO Auto-generated catch block
    //listUsers.clear();
    private val userData: Unit
        private get() {
            //listUsers.clear();
            val name = arrayOf("cust_id", "role")
            val values = arrayOf(selectedId, userType)
            val manager = VolleyWrapper(VKCUrlConstants.GET_USER_DATA)
            manager.getResponsePOST(mContext, 11, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String) {
                        try {
                            val responseObj = JSONObject(
                                successResponse
                            )
                            val response = responseObj
                                .getJSONObject("response")
                            val status = response.getString("status")
                            if (status == "Success") {
                                val objData = response
                                    .optJSONObject("data")
                                val cust_id = objData
                                    .optString("customer_id")
                                val address = objData.optString("address")
                                val name = objData.optString("name")
                                val phone = objData.optString("phone")
                                if (userType == "5") {
                                    textType!!.text = ": " + "Retailer"
                                } else if (userType == "7") {
                                    textType!!.text = ": " + "Sub Dealer"
                                }
                                textId!!.text = ": $cust_id"
                                textName!!.text = ": $name"
                                textAddress!!.text = ": $address"
                                textPhone!!.text = ": $phone"
                                llData!!.visibility = View.VISIBLE
                            }
                        } catch (e: JSONException) {
                            // TODO Auto-generated catch block
                            e.printStackTrace()
                        }
                    }

                    override fun responseFailure(failureResponse: String) { // TODO
                        // Auto-generated method stub
                    }
                })
        }
}