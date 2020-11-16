package com.jobsforher.activities

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.layout_create_post.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.helpers.Logger
import com.jobsforher.network.responsemodels.CreatePostResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ZCreatePostActivity : BaseActivity() {

    private val TAG = javaClass.simpleName
    private var retrofitInterface_post: RetrofitInterface? = null
    private val GALLERY_IMAGE = 1
    private val GALLERY_VIDEO = 2
    private val GALLERY_PDF = 3
    public var imageEncoded = "";
    public var post_type="text"
    public var picturepath:String? = ""
    public var groupId:String?="23"
    public var groupNAME:String?=""
    public var editText_Data:String=""
    public var is_Owner:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_create_post)

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

//        textdescription.setOnFocusChangeListener(object : View.OnFocusChangeListener {
//            override fun onFocusChange(v: View, hasFocus: Boolean) {
//                if (hasFocus) {
//                    horizontal_bottom_menu.visibility = View.VISIBLE
//                    bottom_menu.visibility = View.GONE
//                } else {
//                    horizontal_bottom_menu.visibility = View.GONE
//                    bottom_menu.visibility = View.VISIBLE
//                }
//            }
//        })

        create_post.setEnabled(true)
        groupId=intent.getStringExtra("groupID")
        groupNAME = intent.getStringExtra("groupName")
        // editText_Data = intent.getStringExtra("edittext_data")
        is_Owner  =intent.getStringExtra("onwner")
        createpost_username.setText(EndPoints.USERNAME)
        createpost_groupname.setText(groupNAME)
        //textdescription.setText(editText_Data)
        toptext.setText("Create Post")
        if (EndPoints.PROFILE_ICON.length>4) {
            Picasso.with(applicationContext)
                .load(EndPoints.PROFILE_ICON)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(group_icon_createpost)
        }
        if (is_Owner.equals("true")){
            pinned_id.visibility = View.VISIBLE
            layout_pickdocument.visibility  =View.VISIBLE
        }
        else{
            pinned_id.visibility = View.GONE
            layout_pickdocument.visibility  =View.GONE
        }


        layout_pickimage.setOnClickListener {

            videoURL.visibility = View.GONE
            showPictureDialog();

        }

        cancel.setOnClickListener{
            openBottomSheet()
        }

        layout_pickvideo.setOnClickListener {

            remove_media.visibility = View.VISIBLE
            pick_image.visibility= View.GONE
            //pick_video.visibility= View.GONE
            videoURL.visibility = View.VISIBLE
            //showPictureDialogVideo()
            post_type = "video"
        }

        layout_pickcolor.setOnClickListener {

        }

        layout_pickdocument.setOnClickListener(View.OnClickListener {
            if(isReadStoragePermissionGranted()) {
                val intent = Intent()
                intent.setType("*/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                post_type = "document"
                startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_PDF)
            }
        })

        if (post_type.equals("text")){
            remove_media.visibility = View.GONE
        }
        else if (post_type.equals("document")){
            remove_media.visibility = View.VISIBLE
        }
        else if (post_type.equals("video")){
            remove_media.visibility = View.VISIBLE
        }
        else {
            remove_media.visibility = View.VISIBLE
            pick_image.visibility = View.VISIBLE
        }

        remove_media.setOnClickListener {
            post_type = "text"
            pick_image.setImageDrawable(null)
            pick_image.visibility = View.GONE
            videoURL.setText("")
            videoURL.visibility = View.GONE
            remove_media.visibility = View.GONE
        }

        create_post.setOnClickListener {
            if(post_type.equals("text")){
                if(textdescription.text.length>0){
                    addPost(post_type)
                    create_post.setEnabled(false)
                }
                else Toast.makeText(applicationContext,"Please enter the post description!", Toast.LENGTH_LONG).show()
            }
            else if(post_type.equals("video")){
                if(videoURL.text.length>0){
                    addPost(post_type)
                    create_post.setEnabled(false)
                }
                else Toast.makeText(applicationContext,"Please add video Url", Toast.LENGTH_LONG).show()
            }
            else {
                if(picturepath!!.length>0){
                    addPost(post_type)
                    create_post.setEnabled(false)
                }
                else Toast.makeText(applicationContext,"Please select a file", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showPictureDialog() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Post Image")
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setCropMenuCropButtonTitle("Done")
            .setFixAspectRatio(false)
            .setMultiTouchEnabled(true)
            .setMinCropWindowSize(520, 300)
            .setAspectRatio(520, 300)
            .setRequestedSize(520, 300)
            .setCropMenuCropButtonIcon(R.drawable.ic_tick)
            .start(this)
//        val pictureDialog = AlertDialog.Builder(this)
//        pictureDialog.setTitle("Select Action")
//        val pictureDialogItems = arrayOf("Select photo from gallery")
//        pictureDialog.setItems(pictureDialogItems
//        ) { dialog, which ->
//            when (which) {
//                0 -> choosePhotoFromGallary()
//                //1 -> takePhotoFromCamera()
//            }
//        }
//        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY_IMAGE)
    }

    private fun showPictureDialogVideo() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select video from gallery")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> chooseVideoFromGallary()
                //1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun chooseVideoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY_VIDEO)

    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY_IMAGE)
        {
            if (data != null)
            {
                post_type = "image"
                remove_media.visibility = View.VISIBLE
                pick_image.visibility= View.VISIBLE

                val contentURI = data!!.data
                try
                {
                    val f = File(contentURI.toString())
                    picturepath = f.getName()+".png"
                    Log.d("TAGG", picturepath.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    pick_image!!.setImageBitmap(bitmap)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val b = baos.toByteArray()
                    imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
//        else if (requestCode == GALLERY_VIDEO){
//            if (data != null)
//            {
//                try
//                {
//                    pick_image.visibility = View.VISIBLE
//                    var contentURI :Uri ? = data.getData();
//                    var selectedVideoPath: String? = contentURI?.let { getPath(it) };
//                    Log.d("path",selectedVideoPath);
//                    selectedVideoPath?.let { saveVideoToInternalStorage(it) };
////                    pick_video.setVideoURI(selectedVideoPath);
////                    pick_video.requestFocus();
////                    pick_video.start();
//
//                    Glide.with(applicationContext)
//                        .load(selectedVideoPath)
//                        .into(pick_image)
//                }
//                catch (e: IOException) {
//                    e.printStackTrace()
//                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
        else if (requestCode == GALLERY_PDF){

            if (data != null)
            {
                val contentURI1 = data!!.data
                try
                {
                    val f = File(contentURI1.toString())
                    val file:File= File(contentURI1!!.getPath());

                    picturepath = file.getName();
                    val data1 = applicationContext.contentResolver.openInputStream(contentURI1!!)!!.readBytes()
                    val encodedBytes = Base64.encode(data1, Base64.DEFAULT)
                    imageEncoded =encodedBytes.toString()
                    Log.d("TAGG","PATH: "+picturepath+ imageEncoded)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Log.d("TAGG","STACK"+e.printStackTrace())
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
//                (findViewById(R.id.circleView) as ImageView).setImageURI(result.uri)
                post_type = "image"
                remove_media.visibility = View.VISIBLE
                pick_image.visibility= View.VISIBLE

                Toast.makeText(this, "Cropping successful" , Toast.LENGTH_LONG).show()
                Picasso.with(applicationContext)
                    .load(result.uri)
                    .centerCrop().resize(150,150)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(pick_image)
                val contentURI = result.uri
                val f = File(contentURI.toString())
                picturepath = f.getName()//+".png"
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                circleView!!.setImageBitmap(bitmap)
                pick_image.setPadding(0,0,0,0)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)

            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun isReadStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAGG","Permission is granted1");
                return true;
            } else {

                Log.v("TAGG","Permission is revoked1");
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAGG","Permission is granted1");
            return true;
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2 -> {
                Log.d("TAGG", "External storage2")
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("TAGG", "Permission: " + permissions[0] + "was " + grantResults[0])
                    //resume tasks needing this permission
                    //downloadPdfFile()
                    val intent = Intent()
                    intent.setType("*/*")
                    intent.setAction(Intent.ACTION_GET_CONTENT)
                    post_type = "document"
                    startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_PDF)
                } else {
                    //progress.dismiss()
                }
            }

            3 -> {
                Log.d("TAGG", "External storage1")
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("TAGG", "Permission: " + permissions[0] + "was " + grantResults[0])
                    //resume tasks needing this permission
                    //SharePdfFile()
                } else {
                    //progress.dismiss()
                }
            }
        }
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
    }

    private fun saveVideoToInternalStorage(filePath: String) {

        val newfile: File

        try {

//            val currentFile = File(filePath)
//            val wallpaperDirectory = File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY)
//            newfile = File(wallpaperDirectory, Calendar.getInstance().timeInMillis.toString() + ".mp4")
//
//            if (!wallpaperDirectory.exists()) {
//                wallpaperDirectory.mkdirs()
//            }
//
//            if (currentFile.exists()) {
//
//                val `in` = FileInputStream(currentFile)
//                val out = FileOutputStream(newfile)
//
//                // Copy the bits from instream to outstream
//                val buf = ByteArray(1024)
//                var len: Int
//
//                while ((len = `in`.read(buf)) > 0) {
//                    out.write(buf, 0, len)
//                }
//                `in`.close()
//                out.close()
//                Log.v("vii", "Video file saved successfully.")
//            } else {
//                Log.v("vii", "Video saving failed. Source file missing.")
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else
            return null
    }

    private fun addPost(post_type: String){

        val params = HashMap<String, String>()
        var msg: String=""
        val description = textdescription.text.toString()
        pinned_id?.setOnCheckedChangeListener { buttonView, isChecked ->
            msg = (if (isChecked) "true" else "false")
        }

        val pinned_post = msg
        val post_type = post_type
        val image_filename= picturepath
        val image_file= imageEncoded

        params["description"] = description
        if (msg.equals("true")) {
            params["pinned_post"] = pinned_post.toString()
        }

        params["post_type"] = post_type
        if (post_type.equals("text")) {
            params["image_filename"] = ""
            params["image_file"] = ""
            params["url"]= ""
        }
        else if(post_type.equals("video")){
            params["image_filename"] = ""
            params["image_file"] = ""
            params["url"] = videoURL.text.toString()
        }
        else {
            params["image_filename"] = image_filename.toString()
            params["image_file"] = image_file

        }

        Log.d("TAGG", "PARAMS "+params)
        retrofitInterface_post = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface_post!!.addPost(
            Integer.parseInt(groupId!!),
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer "+EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<CreatePostResponse> {

            override fun onResponse(call: Call<CreatePostResponse>, response: Response<CreatePostResponse>) {

                Log.d("TAG", "CODE Value" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE VAlue" + response.message() + "")
                Log.d("TAG", "RESPONSE Value" + "" + Gson().toJson(response))
                Log.d("TAGG", "URL Value" + "" + response)

                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject: JSONObject =
                    JSONObject(str_response.substring(str_response.indexOf("{"), str_response.lastIndexOf("}") + 1))
                val jsonObject1: JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                var jsonaobj: JSONObject = jsonObject1.getJSONObject("body")
                if (response.isSuccessful) {
                    if(responseCode==200) {
                        //mDataList[position].comment_list!! =
                        Log.d("TAGG", "DATA" + jsonaobj.getString("id"))
                        Toast.makeText(applicationContext,"Post Created!!", Toast.LENGTH_LONG).show()
                        val returnIntent = Intent()
                        setResult(1, returnIntent)
                        finish()
                    }
                    else{

                    }
                    Log.d("TAGG", "DATA" + jsonaobj.getString("id"))
                    Toast.makeText(applicationContext,"Post Created!!", Toast.LENGTH_LONG).show()
                    val returnIntent = Intent()
                    setResult(1, returnIntent)
                    finish()
                } else {

                }
            }

            override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

    public fun openBottomSheet() {

        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(true)
        dialog .setContentView(R.layout.bottom_sheet_discardpost)
        val continuepost = dialog .findViewById(R.id.continue_post) as LinearLayout
        val discardpost = dialog .findViewById(R.id.discard_post) as LinearLayout

        continuepost.setOnClickListener {
            dialog.cancel()
        }

        discardpost.setOnClickListener {
            val returnIntent = Intent()
            setResult(0, returnIntent)
            finish()
        }

        var window : Window = dialog.getWindow()!!;
        var wlp: WindowManager.LayoutParams = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog .show()
    }

    override fun onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
