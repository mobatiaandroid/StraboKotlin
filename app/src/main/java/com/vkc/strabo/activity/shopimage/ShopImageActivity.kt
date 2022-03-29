package com.vkc.strabo.activity.shopimage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
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
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mindorks.paracamera.Camera
import com.squareup.picasso.Picasso
import com.vkc.strabo.R
import com.vkc.strabo.activity.shopimage.model.ImageListModel
import com.vkc.strabo.constants.VKCUrlConstants
import com.vkc.strabo.manager.AppPrefenceManager
import com.vkc.strabo.manager.HeaderManager
import com.vkc.strabo.utils.AndroidMultiPartEntity
import com.vkc.strabo.utils.CustomToast
import com.vkc.strabo.utils.UtilityMethods
import com.vkc.strabo.utils.UtilityMethods.toFile
import com.vkc.strabo.volleymanager.VolleyWrapper
import com.vkc.strabo.volleymanager.VolleyWrapper.ResponseListener
import org.apache.http.HttpEntity
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by user2 on 11/8/17.
 */
class ShopImageActivity : Activity(), VKCUrlConstants, View.OnClickListener {
    var builder: AlertDialog.Builder? = null
    var camera: Camera? = null
    var adapter: ArrayAdapter<String>? = null
    lateinit var items: Array<String>
    private var imagepath = ""
    private val imagepath1 = ""
    var finalFilePhoto: File? = null
    var dialogPic: AlertDialog? = null
    var imageShop: ImageView? = null
    var mContext: Activity? = null
    var headermanager: HeaderManager? = null
    var relativeHeader: LinearLayout? = null
    var mImageBack: ImageView? = null
    private var mImageCaptureUri: Uri? = null
    var ACTIVITY_REQUEST_CODE = 700
    var ACTIVITY_FINISH_RESULT_CODE = 701
    var filePath = ""
    var btnCapture: Button? = null
    var btnUpload: Button? = null
    var imageList: ArrayList<ImageListModel>? = null
    var image1: ImageView? = null
    var image2: ImageView? = null
    var image1Delete: ImageView? = null
    var image2Delete: ImageView? = null
    var relative1: RelativeLayout? = null
    var relative2: RelativeLayout? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_image)
        mContext = this
        initUI()
    }

    private fun initUI() {
        imageList = ArrayList()
        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        headermanager =
            HeaderManager(this@ShopImageActivity, resources.getString(R.string.shop_image))
        headermanager!!.getHeader(relativeHeader, 1)
        mImageBack = headermanager!!.leftButton
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        imageShop = findViewById<View>(R.id.imageShop) as ImageView
        image1 = findViewById<View>(R.id.imageOne) as ImageView
        image2 = findViewById<View>(R.id.imageTwo) as ImageView
        image1Delete = findViewById<View>(R.id.deleteImage1) as ImageView
        image2Delete = findViewById<View>(R.id.deleteImage2) as ImageView
        image1Delete!!.visibility = View.GONE
        image2Delete!!.visibility = View.GONE
        relative1 = findViewById<View>(R.id.relative1) as RelativeLayout
        relative2 = findViewById<View>(R.id.relative2) as RelativeLayout
        btnCapture = findViewById<View>(R.id.buttonCapture) as Button
        btnUpload = findViewById<View>(R.id.buttonUpload) as Button
        image
        mImageBack!!.setOnClickListener(this)
        btnCapture!!.setOnClickListener(this)
        btnUpload!!.setOnClickListener(this)
        image1!!.setOnClickListener(this)
        image2!!.setOnClickListener(this)
        image1Delete!!.setOnClickListener(this)
        image2Delete!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v === mImageBack) {
            finish()
        } else if (v === btnCapture) {
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

            // showCamera();
            createDialog()
        } else if (v === btnUpload) {
            if (filePath == "") {
                val toast = CustomToast(
                    mContext!!
                )
                toast.show(21)
            } else {
                try {
                    val upload: UploadFileToServer = UploadFileToServer()
                    upload.execute()
                } catch (e: Exception) {
                }
            }
        } else if (v === image1Delete) {
            val dialog: DialogConfirm = DialogConfirm(mContext!!, imageList!![0].id)
            dialog.show()
            //  deleteImage(imageList.get(0).getId());
        } else if (v === image2Delete) {
            val dialog: DialogConfirm = DialogConfirm(mContext!!, imageList!![1].id)
            dialog.show()
        }
    }

    private inner class UploadFileToServer :
        AsyncTask<Void?, Int?, String?>() {
        val pDialog = ProgressDialog(mContext)
        private var obj: JSONObject? = null
        private var responseString = ""
        override fun onPreExecute() {
            super.onPreExecute()
            pDialog.setMessage("Uploading...")
            pDialog.setCanceledOnTouchOutside(false)
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
                val httppost = HttpPost(VKCUrlConstants.UPLOAD_IMAGE)
                val file = File(filePath)
                val bin1 = FileBody(file.absoluteFile)
                val entity: AndroidMultiPartEntity
                entity = AndroidMultiPartEntity(
                    object : AndroidMultiPartEntity.ProgressListener {
                        override fun transferred(num: Long) {}
                    })
                entity.addPart("cust_id", StringBody(AppPrefenceManager.getCustomerId(mContext)))
                entity.addPart("role", StringBody(AppPrefenceManager.getUserType(mContext)))
                entity.addPart("image", bin1)
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
            if (responseString.equals("Success", ignoreCase = true)) {
                val toast = CustomToast(
                    mContext!!
                )
                toast.show(19)
                imageHistory
            } else if (responseString.equals("Exceeded", ignoreCase = true)) {
                val toast = CustomToast(
                    mContext!!
                )
                toast.show(20)
                imageHistory
            } else {
                val toast = CustomToast(
                    mContext!!
                )
                toast.show(0)
            }
        }
    }

    fun showCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val root = Environment.getExternalStorageDirectory().absolutePath
        val myDir = File(root + "/" + resources.getString(R.string.app_name))
        myDir.mkdirs()
        val file = File(
            myDir, "tmp_avatar_"
                    + System.currentTimeMillis().toString() + ".JPEG"
        )
        mImageCaptureUri = Uri.fromFile(file)
        cameraIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            mImageCaptureUri
        )
        try {
            cameraIntent.putExtra("return-data", true)
            startActivityForResult(cameraIntent, 0)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    /*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_CODE) {
            if (resultCode == ACTIVITY_FINISH_RESULT_CODE) {
                finish();
            }
        } else {
            Bitmap bitmap = null;
            Uri imageUri = null;
            if (resultCode != Activity.RESULT_OK)
                return;
            switch (requestCode) {
                case 0:
                    imageUri = mImageCaptureUri;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
            if (bitmap != null) {
                try {
                    File tempFile = new File(imageUri.getPath());
                    long size = tempFile.length();
                    ByteArrayOutputStream byteArrayOutputStream;
                    Log.e("Size image:", "" + size);
                    int minSize = 600;
                    int widthOfImage = bitmap.getWidth();
                    int heightOfImage = bitmap.getHeight();
                    Log.e("Width", "" + widthOfImage);
                    Log.e("Height", "" + heightOfImage);
                    int newHeight = 600;
                    int newWidht = 600;
                    if (widthOfImage < minSize && heightOfImage < minSize) {
                        newWidht = widthOfImage;
                        newHeight = heightOfImage;
                    } else {
                        if (widthOfImage >= heightOfImage) {
                            //int newheght = heightOfImage/600;
                            float ratio = (float) minSize / widthOfImage;
                            Log.e("Multi width greater", "" + minSize + "/" + widthOfImage + "=" + ratio);
                            newHeight = (int) (heightOfImage * ratio);
                            newWidht = minSize;
                        } else if (heightOfImage > widthOfImage) {
                            float ratio = (float) minSize / heightOfImage;
                            newWidht = (int) (widthOfImage * ratio);
                            newHeight = minSize;
                        }

                    }
                    if (size > 1024 * 1024) {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap b;
                        b = Bitmap.createScaledBitmap(bitmap, newWidht, newHeight, true);
                        b.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

                    } else {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap b;
                        b = Bitmap.createScaledBitmap(bitmap, newWidht, newHeight, true);
                        b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    }


                    if (size > (4 * 1024 * 1024)) {
                        //CustomStatusDialog(RESPONSE_LARGE_IMAGE);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    } else {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int bounding = dpToPx(mContext.getResources().getDisplayMetrics().density);
                        float xScale = (100 * (float) bounding) / width;
                        float yScale = (100 * (float) bounding) / height;
                        float scale = (xScale <= yScale) ? xScale : yScale;
                        Matrix matrix = new Matrix();
                        matrix.postScale(scale, scale);
                        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);


                        BitmapDrawable result = new BitmapDrawable(scaledBitmap);

                        imageShop.setImageDrawable(result);
                        imageShop.setScaleType(ImageView.ScaleType.FIT_XY);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        filePath = "/sdcard/file" + "vkc" + ".jpg";
                        File f = new File(filePath);
                        try {
                            f.createNewFile();
                            FileOutputStream fos = null;

                            fos = new FileOutputStream(f);
                            fos.write(byteArray);
                            fos.close();
                            filePath = f.getAbsolutePath();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                    // CustomStatusDialog(RESPONSE_OUT_OF_MEMORY);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
            }
        }
    }*/
    private fun dpToPx(dp: Float): Int {
        val density = mContext!!.resources.displayMetrics.density
        return Math.round(dp * density)
    }// CustomStatusDialog(RESPONSE_FAILURE);//CustomStatusDialog(RESPONSE_FAILURE);//    } else {

    //  CustomToast toast = new CustomToast(mContext);
    //    toast.show(4);
    //   }
    //    System.out.println("Response---Login" + successResponse);
    val image: Unit
        get() {
            try {
                val name = arrayOf("cust_id", "role")
                val values = arrayOf(
                    AppPrefenceManager.getCustomerId(mContext),
                    AppPrefenceManager.getUserType(mContext)
                )
                val manager = VolleyWrapper(VKCUrlConstants.UPLOADED_IMAGE)
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
                                        val imageUrl = objData.optString("image")
                                        Picasso.with(mContext).load(imageUrl)
                                            .placeholder(R.drawable.shop_image).into(imageShop)
                                        imageHistory
                                    }

                                    //    } else {
                                    //  CustomToast toast = new CustomToast(mContext);
                                    //    toast.show(4);
                                    //   }
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
        }// CustomStatusDialog(RESPONSE_FAILURE);//CustomStatusDialog(RESPONSE_FAILURE);// initUI();
    /* String imageUrl = objData.optString("image");
Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.shop_image).into(imageShop);*/

    //    } else {
    //  CustomToast toast = new CustomToast(mContext);
    //    toast.show(4);
    //   }

    //    System.out.println("Response---Login" + successResponse);
    val imageHistory: Unit
        get() {
            imageList!!.clear()
            try {
                val name = arrayOf("cust_id", "role")
                val values = arrayOf(
                    AppPrefenceManager.getCustomerId(mContext),
                    AppPrefenceManager.getUserType(mContext)
                )
                val manager = VolleyWrapper(VKCUrlConstants.GET_IMAGE_HISTORY)
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
                                        val objData = objResponse.optJSONArray("data")
                                        if (objData.length() > 0) {
                                            for (i in 0 until objData.length()) {
                                                val obj = objData.optJSONObject(i)
                                                val model = ImageListModel()
                                                model.image = obj.getString("image")
                                                model.id = obj.getString("id")
                                                imageList!!.add(model)
                                            }
                                            if (imageList!!.size > 1) {
                                                if (imageList!![0].image != "") {
                                                    relative1!!.visibility = View.VISIBLE
                                                    image1Delete!!.visibility = View.VISIBLE
                                                    Picasso.with(mContext)
                                                        .load(imageList!![0].image).resize(200, 200)
                                                        .centerInside().into(image1)
                                                } else {
                                                    relative1!!.visibility = View.GONE
                                                    image1Delete!!.visibility = View.GONE
                                                }
                                                if (imageList!![1].image != "") {
                                                    relative2!!.visibility = View.VISIBLE
                                                    image2Delete!!.visibility = View.VISIBLE
                                                    Picasso.with(mContext)
                                                        .load(imageList!![1].image).resize(200, 200)
                                                        .centerInside().into(image2)
                                                } else {
                                                    relative2!!.visibility = View.GONE
                                                    image2Delete!!.visibility = View.GONE
                                                }
                                            } else {
                                                relative2!!.visibility = View.GONE
                                                image2Delete!!.visibility = View.GONE
                                                if (imageList!![0].image != "") {
                                                    relative1!!.visibility = View.VISIBLE
                                                    image1Delete!!.visibility = View.VISIBLE
                                                    Picasso.with(mContext)
                                                        .load(imageList!![0].image).resize(200, 200)
                                                        .centerInside().into(image1)
                                                } else {
                                                    relative1!!.visibility = View.GONE
                                                    image1Delete!!.visibility = View.GONE
                                                }
                                            }
                                        } else {
                                            relative1!!.visibility = View.GONE
                                            image1Delete!!.visibility = View.GONE
                                            relative2!!.visibility = View.GONE
                                            image2Delete!!.visibility = View.GONE
                                            // initUI();
                                            val toast = CustomToast(mContext!!)
                                            toast.show(51)
                                        }
                                        /* String imageUrl = objData.optString("image");
             Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.shop_image).into(imageShop);*/
                                    }

                                    //    } else {
                                    //  CustomToast toast = new CustomToast(mContext);
                                    //    toast.show(4);
                                    //   }
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

    fun deleteImage(id: String) {
        try {
            val name = arrayOf("id")
            val values = arrayOf(id)
            val manager = VolleyWrapper(VKCUrlConstants.DELETE_IMAGE)
            manager.getResponsePOST(mContext, 11, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String) {
                        //    System.out.println("Response---Login" + successResponse);
                        if (successResponse != null) {
                            try {
                                val rootObject = JSONObject(successResponse)
                                //JSONObject objResponse = rootObject.optJSONObject("response");
                                val status = rootObject.optString("status")
                                if (status == "Success") {
                                    val toast = CustomToast(mContext!!)
                                    toast.show(52)
                                    imageHistory
                                } else if (status == "Error") {
                                    imageHistory
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

    inner class DialogConfirm     // TODO Auto-generated constructor stub
        (var mActivity: Activity, var id: String) : Dialog(mActivity),
        View.OnClickListener {
        var type: String? = null
        var message: String? = null
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_delete_image)
            init()
        }

        private fun init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            val textYes = findViewById<View>(R.id.textYes) as TextView
            val textNo = findViewById<View>(R.id.textNo) as TextView
            textYes.setOnClickListener {
                deleteImage(id)
                dismiss()
            }
            textNo.setOnClickListener {
                dismiss()
                // mActivity.finish();
            }
        }

        override fun onClick(v: View) {
            dismiss()
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
        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d("Picture:", "Picture kayarundu!")
            val bitmap = camera!!.cameraBitmap
            if (bitmap != null) {


                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                val tempUri = getImageUri(applicationContext, bitmap)
                finalFilePhoto = File(getRealPathFromURI(tempUri))
                filePath = getRealPathFromURI(tempUri)
                //                 String path = FileUtils.getPath(this, tempUri);
                image1!!.setImageBitmap(bitmap)
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
                image1!!.setImageBitmap(bitmap)
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