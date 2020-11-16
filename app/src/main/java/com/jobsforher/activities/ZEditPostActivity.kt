package com.jobsforher.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_create_post.*
import com.jobsforher.R
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import android.util.Base64
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.CreatePostResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import women.jobs.jobsforher.activities.BaseActivity

class ZEditPostActivity : BaseActivity() {

    var id:Int=0
    var data: String= "";
    var icon: String=""
    var url: String=""
    var post_type=""
    var grpName =""
    var owner=""
    private val GALLERY_IMAGE = 1
    private val GALLERY_VIDEO = 2
    private val GALLERY_PDF = 3
    private var retrofitInterface: RetrofitInterface? = null
    public var picturepath:String? = ""
    public var imageEncoded = "";

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_create_post)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        id=intent.getIntExtra("id",0)
        data = intent.getStringExtra("data")
        icon = intent.getStringExtra("icon")
        post_type = intent.getStringExtra("post_type")
        url = intent.getStringExtra("url")
        grpName = intent.getStringExtra("grpName")
        owner = intent.getStringExtra("owner")
        textdescription.text = Editable.Factory.getInstance().newEditable(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT))
        createpost_username.text = EndPoints.USERNAME
        createpost_groupname.text = grpName
        toptext.setText("Edit Post")

        Log.d("TAGG", url)
        if(icon.isNotEmpty()) {
            Picasso.with(applicationContext)
                .load(icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(group_icon_createpost)
        }else{
            Picasso.with(applicationContext)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(group_icon_createpost)
        }
        layout_pickimage.setOnClickListener {

            pick_image.visibility= View.VISIBLE
            //pick_video.visibility= View.GONE
            videoURL.visibility = View.GONE
            showPictureDialog();
            post_type = "image"
        }

        layout_pickvideo.setOnClickListener {

            remove_media.visibility = View.VISIBLE
            pick_image.visibility= View.GONE
            //pick_video.visibility= View.GONE
            videoURL.visibility = View.VISIBLE
            //showPictureDialogVideo()
            post_type = "video"
        }

//        if (owner.equals("true")) {
//            pinned_id.visibility = View.VISIBLE
//            layout_pickdocument.visibility= View.VISIBLE
//        }
//        else {
        pinned_id.visibility = View.GONE
        layout_pickdocument.visibility= View.GONE
//        }

        if (post_type.equals("text")){
            remove_media.visibility = View.GONE
        }
        else if (post_type.equals("document")){
            remove_media.visibility = View.VISIBLE
        }
        else if (post_type.equals("video")){
            remove_media.visibility = View.VISIBLE
            videoURL.visibility = View.VISIBLE
            videoURL.setText(url)
        }
        else {
            remove_media.visibility = View.VISIBLE
            pick_image.visibility = View.VISIBLE
            Picasso.with(applicationContext)
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(pick_image)
        }

        remove_media.setOnClickListener {
            post_type = "text"
            pick_image.setImageDrawable(null)
            pick_image.visibility = View.GONE
            videoURL.setText("")
            videoURL.visibility = View.GONE
            remove_media.visibility = View.GONE
        }


        cancel.setOnClickListener {
            finish()
        }
//        //groupId=intent.getStringExtra("groupID")
        create_post.setOnClickListener {
            if(post_type.equals("text")){
                if(textdescription.text.length>0){
                    editPostData(id)
                    create_post.setEnabled(false)
                }
                else Toast.makeText(applicationContext,"Please enter the post description!", Toast.LENGTH_LONG).show()
            }
            else if(post_type.equals("video")){
                if(videoURL.text.length>0){
                    editPostData(id)
                    create_post.setEnabled(false)
                }
                else Toast.makeText(applicationContext,"Please add video Url", Toast.LENGTH_LONG).show()
            }
            else {
                if(picturepath!!.length>0 || pick_image.visibility == View.VISIBLE){
                    editPostData(id)
                    create_post.setEnabled(false)
                }
                else Toast.makeText(applicationContext,"Please select a file", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showPictureDialog() {

        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Edit Image")
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
                    imageEncoded = encodedBytes.toString()
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

    fun editPostData(id: Int){

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

        if (post_type.equals("text")) {
            params["post_type"] = post_type
            params["image_filename"] = ""
            params["image_file"] = ""
            params["url"]= ""
        }
        else if(post_type.equals("video")){
            params["post_type"] = post_type
            params["image_filename"] = ""
            params["image_file"] = ""
            params["url"] = videoURL.text.toString()
        }
        else {
            if(image_filename.toString().equals("")){

            }
            else {
                params["post_type"] = post_type
                params["image_filename"] = image_filename.toString()
                params["image_file"] = image_file
            }
        }

        Log.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.updatePost(id, "application/json",EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN, params)
        call.enqueue(object : Callback<CreatePostResponse> {

            override fun onResponse(call: Call<CreatePostResponse>, response: Response<CreatePostResponse>) {

                Log.d("TAG","CODE"+ response.code().toString() + "")
                Log.d("TAG","MESSAGE"+ response.message() + "")
                Log.d("TAG","RESPONSE"+ "" + Gson().toJson(response))
                Log.d("TAG","URL"+ "" + response)
                //var str_response = Gson().toJson(response)
                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
//                var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if (response.isSuccessful && responseCode==10201) {
//                    for (i in 0 until response.body()!!.body!!.size) {
//                        var json_objectdetail: JSONObject = jsonarray.getJSONObject(i)
//                        listOfPhotos.add(json_objectdetail.getString("url"))
//
//                    }
                    Toast.makeText(applicationContext,"Post Updated!!", Toast.LENGTH_LONG).show()
                    val returnIntent = Intent()
                    setResult(1, returnIntent)
                    finish()
                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid")
                }

            }

            override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {

                Logger.d("TAG", "FAILED : $t")
            }
        })


    }


}
