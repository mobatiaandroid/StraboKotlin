package com.vkc.strabo.activity.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mindorks.paracamera.Camera
import com.squareup.picasso.Picasso
import com.vkc.strabo.R
import com.vkc.strabo.activity.common.SignUpActivity
import com.vkc.strabo.activity.dealers.DealersActivity
import com.vkc.strabo.activity.my_customers.MyCustomersActivity
import com.vkc.strabo.constants.VKCUrlConstants
import com.vkc.strabo.manager.AppPrefenceManager
import com.vkc.strabo.manager.HeaderManager
import com.vkc.strabo.utils.AndroidMultiPartEntity
import com.vkc.strabo.utils.CustomToast
import com.vkc.strabo.utils.UtilityMethods.toFile
import com.vkc.strabo.volleymanager.VolleyWrapper
import com.vkc.strabo.volleymanager.VolleyWrapper.ResponseListener
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.Exception

/**
 * Created by Bibin Johnson on 9/8/17.
 */
class ProfileActivity : Activity(), View.OnClickListener,
    VKCUrlConstants {
    var camera: Camera? = null
    var adapter: ArrayAdapter<String>? = null
    lateinit var items: Array<String>
    private var imagepath = ""
    private val imagepath1 = ""
    var mContext: Activity? = null
    var dialogPic: AlertDialog? = null
    var finalFilePhoto: File? = null
    var builder: AlertDialog.Builder? = null
    var headermanager: HeaderManager? = null
    var relativeHeader: LinearLayout? = null
    var mImageBack: ImageView? = null
    var imageProfile: ImageView? = null
    var buttonUpdate: Button? = null
    var editMobile: EditText? = null
    var editOwner: EditText? = null
    var editShop: EditText? = null
    var editState: EditText? = null
    var editDist: EditText? = null
    var editPlace: EditText? = null
    var editPin: EditText? = null
    var editAddress: EditText? = null
    var editMobile2: EditText? = null
    var editEmail: EditText? = null
    var textCustId: TextView? = null
    var textMydealers: TextView? = null
    var textUpdate: TextView? = null
    var textMyCustomers: TextView? = null
    var filePath = ""
    var ACTIVITY_REQUEST_CODE = 700
    var ACTIVITY_FINISH_RESULT_CODE = 701
    private val mImageCaptureUri: Uri? = null
    var otpValue = ""
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        setContentView(R.layout.activity_profile)
        mContext = this
        initUI()
        profile
    }

    private fun initUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        headermanager = HeaderManager(this@ProfileActivity, resources.getString(R.string.profile))
        headermanager!!.getHeader(relativeHeader, 1)
        mImageBack = headermanager!!.leftButton
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        textCustId = findViewById<View>(R.id.textCustId) as TextView
        textUpdate = findViewById<View>(R.id.textUpdate) as TextView
        textMydealers = findViewById<View>(R.id.textMydealers) as TextView
        textMyCustomers = findViewById<View>(R.id.textMyCustomers) as TextView
        mImageBack?.setOnClickListener(this)
        imageProfile = findViewById<View>(R.id.imageProfile) as ImageView
        buttonUpdate = findViewById<View>(R.id.buttonUpdate) as Button
        editMobile = findViewById<View>(R.id.editMobile) as EditText
        editOwner = findViewById<View>(R.id.editOwner) as EditText
        editShop = findViewById<View>(R.id.editShop) as EditText
        editState = findViewById<View>(R.id.editState) as EditText
        editDist = findViewById<View>(R.id.editDistrict) as EditText
        editPlace = findViewById<View>(R.id.editPlace) as EditText
        editPin = findViewById<View>(R.id.editPin) as EditText
        editAddress = findViewById<View>(R.id.editAddress) as EditText
        editMobile2 = findViewById<View>(R.id.editMobile2) as EditText
        editEmail = findViewById<View>(R.id.editEmail) as EditText
        // editMobile.setEnabled(false);
        editOwner!!.isEnabled = true
        editShop!!.isEnabled = false
        editState!!.isEnabled = false
        editDist!!.isEnabled = false
        editPlace!!.isEnabled = true
        editPin!!.isEnabled = false
        editAddress!!.isEnabled = false
        if (AppPrefenceManager.getUserType(mContext) == "6") {
            textMydealers!!.visibility = View.GONE
            textMyCustomers!!.visibility = View.VISIBLE
        } else {
            textMyCustomers!!.visibility = View.GONE
            textMydealers!!.visibility = View.VISIBLE
        }
        buttonUpdate!!.setOnClickListener(this)
        textMydealers!!.setOnClickListener(this)
        imageProfile!!.setOnClickListener(this)
        textUpdate!!.setOnClickListener(this)
        textMyCustomers!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v === mImageBack) {
            finish()
        } else if (v === buttonUpdate) {
            val upload: UpdateProfile = UpdateProfile()
            upload.execute()
        } else if (v === imageProfile) {

            /* if ((int) Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.CAMERA},
                            100);

                    // Log.e("If permission is not granted", ", request for permission";


                } else {
                    createDialog();
                }
            }*/
            /*else {
                createDialog();
            }*/
            createDialog()
        } else if (v === textMydealers) {
            startActivity(Intent(this@ProfileActivity, DealersActivity::class.java))
        } else if (v === textMyCustomers) {
            startActivity(Intent(this@ProfileActivity, MyCustomersActivity::class.java))
        } else if (v === textUpdate) {
            if (editMobile!!.text.toString().trim { it <= ' ' }.length > 0) {
                if (editMobile!!.text.toString().trim { it <= ' ' }.length == 10) {

                    //Update mobile dialog
                    if (AppPrefenceManager.getMobile(mContext) == editMobile!!.text.toString()
                            .trim { it <= ' ' }
                    ) {
                    } else {
                        val dialog: DialogUpdateMobile = DialogUpdateMobile(mContext)
                        dialog.show()
                    }
                } else {
                    val toast = CustomToast(mContext!!)
                    toast.show(54)
                }
            } else {
                val toast = CustomToast(mContext!!)
                toast.show(54)
            }
        }
    }// CustomStatusDialog(RESPONSE_FAILURE);

    //Log.d("TAG", "Common error");
//CustomStatusDialog(RESPONSE_FAILURE);//.transform(new CircleTransform())
    //    System.out.println("Response---Login" + successResponse);
    val profile: Unit
        get() {
            try {
                val name = arrayOf("cust_id", "role")
                val values = arrayOf(
                    AppPrefenceManager.getCustomerId(mContext),
                    AppPrefenceManager.getUserType(mContext)
                )
                val manager = VolleyWrapper(VKCUrlConstants.GET_PROFILE)
                manager.getResponsePOST(mContext, 11, name, values,
                    object : ResponseListener {
                        override fun responseSuccess(successResponse: String) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {
                                try {
                                    val rootObject = JSONObject(successResponse)
                                    val objResponse = rootObject.optJSONObject("response")
                                    val status = objResponse.optString("status")
                                    if (status == "Success") {
                                        val objData = objResponse.optJSONObject("data")
                                        val name = objData.optString("name")
                                        val contact_person = objData.optString("contact_person")
                                        val district = objData.optString("district")
                                        val city = objData.optString("city")
                                        val state_name = objData.optString("state_name")
                                        val pincode = objData.optString("pincode")
                                        val phone = objData.optString("phone")
                                        val url = objData.optString("image")
                                        val mobile2 = objData.optString("phone2")
                                        val email = objData.optString("email")
                                        editMobile2!!.setText(mobile2)
                                        editEmail!!.setText(email)
                                        editShop!!.setText(name)
                                        editOwner!!.setText(contact_person)
                                        editDist!!.setText(district)
                                        editMobile!!.setText(phone)
                                        editPlace!!.setText(city)
                                        editState!!.setText(state_name)
                                        editPin!!.setText(pincode)
                                        editAddress!!.setText(objData.optString("address"))
                                        textCustId!!.text =
                                            "CUST_ID: - " + objData.optString("customer_id")
                                        Picasso.with(mContext).load(url)
                                            .placeholder(R.drawable.profile_image)
                                            .into(imageProfile) //.transform(new CircleTransform())
                                    }
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            } else {
                                val toast = CustomToast(mContext!!)
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

    private inner class UpdateProfile : AsyncTask<Void?, Int?, String?>() {
        val pDialog = ProgressDialog(mContext)
        private var obj: JSONObject? = null
        private var responseString = ""
        override fun onPreExecute() {
            super.onPreExecute()
            pDialog.show()
        }

        protected override fun onProgressUpdate(vararg values: Int?) {}
        protected override fun doInBackground(vararg p0: Void?): String? {
            return uploadFile()
        }

        private fun uploadFile(): String? {
            var responseString: String? = null
            try {
                val httpclient: HttpClient = DefaultHttpClient()
                val httppost = HttpPost(VKCUrlConstants.UPDATE_PROFILE)
                val file = File(filePath)
                val bin1 = FileBody(file.absoluteFile)
                val entity: AndroidMultiPartEntity
                entity = AndroidMultiPartEntity(
                    object : AndroidMultiPartEntity.ProgressListener {
                        override fun transferred(num: Long) {}
                    })
                entity.addPart("cust_id", StringBody(AppPrefenceManager.getCustomerId(mContext)))
                entity.addPart("role", StringBody(AppPrefenceManager.getUserType(mContext)))
                entity.addPart("phone", StringBody(editMobile!!.text.toString().trim { it <= ' ' }))
                entity.addPart(
                    "contact_person",
                    StringBody(editOwner!!.text.toString().trim { it <= ' ' })
                )
                entity.addPart("city", StringBody(editPlace!!.text.toString().trim { it <= ' ' }))
                entity.addPart(
                    "phone2",
                    StringBody(editMobile2!!.text.toString().trim { it <= ' ' })
                )
                entity.addPart("email", StringBody(editEmail!!.text.toString().trim { it <= ' ' }))
                if (filePath == "") {
                } else {
                    entity.addPart("image", bin1)
                }
                httppost.entity = entity
                val response = httpclient.execute(httppost)
                val r_entity = response.entity
                val statusCode = response.statusLine.statusCode
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity)
                } else {
                    responseString = EntityUtils.toString(r_entity)
                    responseString = ("Error occurred! Http Status Code: "
                            + statusCode)
                }
            } catch (e: ClientProtocolException) {
                responseString = e.toString()
                Log.e("UploadApp", "exception: $responseString")
            } catch (e: IOException) {
                responseString = e.toString()
                Log.e("UploadApp", "exception: $responseString")
            }
            return responseString
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (pDialog.isShowing) pDialog.dismiss()
            print("Result $result")
            try {
                obj = JSONObject(result)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val responseObj = obj!!.optJSONObject("response")
            responseString = responseObj.optString("status")
            if (responseString == "Success") {
                val toast = CustomToast(mContext!!)
                toast.show(26)
                profile
            } else {
                val toast = CustomToast(mContext!!)
                toast.show(27)
            }
        }
    }

    private fun dpToPx(dp: Float): Int {
        val density = mContext!!.resources.displayMetrics.density
        return Math.round(dp * density)
    }

    fun updateMobile() {
        try {
            val name = arrayOf("cust_id", "role", "phone")
            val values = arrayOf(
                AppPrefenceManager.getCustomerId(mContext),
                AppPrefenceManager.getUserType(mContext),
                editMobile!!.text.toString().trim { it <= ' ' })
            val manager = VolleyWrapper(VKCUrlConstants.UPDATE_MOBILE)
            manager.getResponsePOST(mContext, 11, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String) {
                        //    System.out.println("Response---Login" + successResponse);
                        if (successResponse != null) {
                            try {
                                val rootObject = JSONObject(successResponse)
                                val objResponse = rootObject.optJSONObject("response")
                                val status = objResponse.optString("status")
                                if (status == "Success") {
                                    val dialog = OTPDialog(mContext)
                                    dialog.show()
                                } else {
                                    val toast = CustomToast(mContext!!)
                                    toast.show(56)
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        } else {
                            val toast = CustomToast(mContext!!)
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
            //  Log.d("TAG", "Common error");
        }
    }

    inner class DialogUpdateMobile : Dialog, View.OnClickListener {
        var mActivity: Activity? = null
        var type: String? = null
        var message: String? = null

        constructor(a: Activity?) : super(a!!) {
            // TODO Auto-generated constructor stub
            mActivity = a
        }

        constructor(a: Activity?, type: String?, message: String?) : super(a!!) {
            this.type = type
            this.message = message
            // TODO Auto-generated constructor stub
        }

        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_update_mobile)
            init()
        }

        private fun init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            val textYes = findViewById<View>(R.id.textYes) as TextView
            val textNo = findViewById<View>(R.id.textNo) as TextView
            textYes.setOnClickListener {
                dismiss()
                // mActivity.finish();
                updateMobile()
            }
            textNo.setOnClickListener { dismiss() }
        }

        override fun onClick(v: View) {
            dismiss()
        }
    }

    inner class OTPDialog     // TODO Auto-generated constructor stub
        (var mActivity: Activity?) : Dialog(mActivity!!), View.OnClickListener {
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.dialog_otp_mobile)
            init()
        }

        private fun init() {
            val editOtp1 = findViewById<View>(R.id.editOtp1) as EditText
            val editOtp2 = findViewById<View>(R.id.editOtp2) as EditText
            val editOtp3 = findViewById<View>(R.id.editOtp3) as EditText
            val editOtp4 = findViewById<View>(R.id.editOtp4) as EditText
            val textOtp = findViewById<View>(R.id.textOtp) as TextView
            val textCancel = findViewById<View>(R.id.textCancel) as TextView
            val mob = AppPrefenceManager.getMobile(mContext).substring(6, 10)
            textOtp.text = "OTP has been sent to  XXXXXX$mob"
            editOtp1.isCursorVisible = false
            textCancel.setOnClickListener { dismiss() }
            editOtp1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    editOtp1.setBackgroundResource(R.drawable.rounded_rect_full_white)
                    if (s.length == 1) {
                        editOtp1.clearFocus()
                        editOtp2.requestFocus()
                    }
                    otpValue =
                        editOtp1.text.toString().trim { it <= ' ' } + editOtp2.text.toString()
                            .trim { it <= ' ' } + editOtp3.text.toString()
                            .trim { it <= ' ' } + editOtp4.text.toString().trim { it <= ' ' }
                }

                override fun afterTextChanged(s: Editable) {
                    if (s.length == 0) {
                        editOtp1.setBackgroundResource(R.drawable.rounded_rect_line)
                    }
                }
            })
            editOtp2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    editOtp2.setBackgroundResource(R.drawable.rounded_rect_full_white)
                    if (s.length == 1) {
                        editOtp2.clearFocus()
                        editOtp3.requestFocus()
                    }
                    otpValue =
                        editOtp1.text.toString().trim { it <= ' ' } + editOtp2.text.toString()
                            .trim { it <= ' ' } + editOtp3.text.toString()
                            .trim { it <= ' ' } + editOtp4.text.toString().trim { it <= ' ' }
                    if (otpValue.length == 1) {
                        editOtp2.clearFocus()
                        editOtp1.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    if (s.length == 0) {
                        editOtp2.setBackgroundResource(R.drawable.rounded_rect_line)
                    }
                }
            })
            editOtp3.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    editOtp3.setBackgroundResource(R.drawable.rounded_rect_full_white)
                    if (s.length == 1) {
                        editOtp3.clearFocus()
                        editOtp4.requestFocus()
                    }
                    otpValue =
                        editOtp1.text.toString().trim { it <= ' ' } + editOtp2.text.toString()
                            .trim { it <= ' ' } + editOtp3.text.toString()
                            .trim { it <= ' ' } + editOtp4.text.toString().trim { it <= ' ' }
                    if (otpValue.length == 2) {
                        editOtp3.clearFocus()
                        editOtp2.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    if (s.length == 0) {
                        editOtp3.setBackgroundResource(R.drawable.rounded_rect_line)
                    }
                }
            })
            editOtp4.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    editOtp4.setBackgroundResource(R.drawable.rounded_rect_full_white)
                    otpValue =
                        editOtp1.text.toString().trim { it <= ' ' } + editOtp2.text.toString()
                            .trim { it <= ' ' } + editOtp3.text.toString()
                            .trim { it <= ' ' } + editOtp4.text.toString().trim { it <= ' ' }
                    if (otpValue.length == 3) {
                        editOtp3.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    if (s.length == 0) {
                        editOtp4.setBackgroundResource(R.drawable.rounded_rect_line)
                    } else {
                        verifyOTP(otpValue, editMobile!!.text.toString().trim { it <= ' ' })
                    }
                }
            })
            val buttonCancel = findViewById<View>(R.id.buttonCancel) as Button
            buttonCancel.setOnClickListener { dismiss() }
        }

        override fun onClick(v: View) {
            dismiss()
        }
    }

    fun verifyOTP(otp: String, mobile: String) {
        try {
            val name = arrayOf("otp", "role", "cust_id", "phone", "isnewMobile")
            val values = arrayOf(
                otp,
                AppPrefenceManager.getUserType(mContext),
                AppPrefenceManager.getCustomerId(mContext),
                mobile,
                "1"
            )
            val manager = VolleyWrapper(VKCUrlConstants.OTP_VERIFY_URL)
            manager.getResponsePOST(mContext, 11, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String) {
                        if (successResponse != null) {
                            try {
                                val rootObject = JSONObject(successResponse)
                                val objResponse = rootObject.optJSONObject("response")
                                val status = objResponse.optString("status")
                                if (status.equals("Success", ignoreCase = true)) {


                                    //    AppPrefenceManager.setIsVerifiedOTP(mContext, "yes");
                                    val toast = CustomToast(mContext!!)
                                    toast.show(55)
                                    val intent =
                                        Intent(this@ProfileActivity, SignUpActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(intent)
                                } else {
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        } else {
                            val toast = CustomToast(mContext!!)
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

    var permissionCameralistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            onPickCamera()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(mContext, "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT)
                .show()
        }
    }
    var permissionListenerGallery: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            onPickCameraGallery()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(mContext, "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun createDialog() {
        builder = AlertDialog.Builder(this)

        // builder.setTitle(R.string.take_picture);
        items = arrayOf(
            mContext!!.resources.getString(R.string.take_picture),
            mContext!!.resources.getString(R.string.open_gallery)
        )
        adapter = ArrayAdapter(
            this,
            android.R.layout.select_dialog_item, items
        )
        builder!!.setAdapter(adapter) { dialog, item -> // pick from
            // camera
            if (item == 0) {
                if (Build.VERSION.SDK_INT >= 23) {
                    TedPermission.with(mContext)
                        .setPermissionListener(permissionCameralistener)
                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        .check()
                } else {
                    onPickCamera()
                }
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    TedPermission.with(mContext)
                        .setPermissionListener(permissionListenerGallery)
                        .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        .check()
                } else {
                    onPickCameraGallery()
                }
            }
        }
        dialogPic = builder!!.create()
        dialogPic?.show()
    }

    fun onPickCamera() {

// Build the camera
        camera = Camera.Builder()
            .resetToCorrectOrientation(true) // it will rotate the camera bitmap to the correct orientation from meta data
            .setTakePhotoRequestCode(1)
            .setDirectory("pics")
            .setName("Strabo_Pic" + mContext!!.resources.getString(R.string.app_name) + System.currentTimeMillis())
            .setImageFormat(Camera.IMAGE_JPEG)
            .setCompression(75)
            .build(this)

        // Call the camera takePicture method to open the existing camera
        try {
            camera?.takePicture()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onPickCameraGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_DOCUMENT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.d("Picture:", "Picture requestCode! $requestCode")
        Log.d("Picture:", "Picture resultCode! $resultCode")
        Log.d("Picture:", "Picture data! $data")
        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d("Picture:", "Picture kayarundu!")
            val bitmap = camera!!.cameraBitmap
            if (bitmap != null) {


                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                val tempUri = getImageUri(applicationContext, bitmap)
                finalFilePhoto = File(getRealPathFromURI(tempUri))
                filePath = getRealPathFromURI(tempUri)
                //                 String path = FileUtils.getPath(this, tempUri);
                imageProfile!!.setImageBitmap(bitmap)
                imagepath = toFile(bitmap, "straboimage1.jpg")
                //                if (dialogChooser != null)
//                    dialogChooser.dismiss();
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                Log.d("Picture:bitmap", bitmap.toString())
            } else {
//                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                Log.d("Picture:", "Picture not taken!")
            }
        }
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == RESULT_OK && data != null) {
            // this is the image selected by the user
            val imageUri = data.data
            var `is`: InputStream? = null
            var bitmap: Bitmap? = null
            try {
                `is` = contentResolver.openInputStream(imageUri!!)
                bitmap = BitmapFactory.decodeStream(`is`)
                `is`!!.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (bitmap != null) {
                val tempUri = getImageUri(applicationContext, bitmap)
                finalFilePhoto = File(getRealPathFromURI(tempUri))
                filePath = getRealPathFromURI(tempUri)
                imageProfile!!.setImageBitmap(bitmap)
                imagepath = toFile(bitmap, "straboimage2.jpg")
                //                    if (dialogChooser != null)
//                        dialogChooser.dismiss();
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri?): String {
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    companion object {
        private const val OPEN_DOCUMENT_CODE = 2
    }
}