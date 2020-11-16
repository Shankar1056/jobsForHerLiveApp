package com.jobsforher.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.jobsforher.R
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.network.responsemodels.CreateProfileResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_profile_pic.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import women.jobs.jobsforher.activities.BaseActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

private val GALLERY_IMAGE = 1

public var profilePicturePath:String? = ""
public var profileImageEncoded = "";
private var retrofitInterface: RetrofitInterface? = null

class AddProfileActivity : BaseActivity() {

    private val TAG = javaClass.simpleName

    var type : String = ""

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile_pic)

        btnSubmitProfile.setOnClickListener {

            submitProfileDetails()


        }

        circleView.setOnClickListener {

            showPictureDialog()
        }

        tvProfileSkip.setOnClickListener {

            val intent = Intent(applicationContext, ProfileView::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    private fun showPictureDialog() {

        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Profile Icon")
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setCropMenuCropButtonTitle("Done")
            .setMultiTouchEnabled(true)
            .setMinCropWindowSize(538,320)
            .setRequestedSize(538, 320)
            .setFixAspectRatio(false)
            .setAspectRatio(538,320)
            .setCropMenuCropButtonIcon(R.drawable.ic_tick)
            .start(this)

    }

    fun choosePhotoFromGallary() {

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY_IMAGE)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_IMAGE)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val f = File(contentURI.toString())
                    profilePicturePath = f.getName()+".png"
                    Log.d("TAGG", picturepath.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    circleView!!.setImageBitmap(bitmap)
                    circleView.setPadding(0,0,0,0)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val b = baos.toByteArray()
                    profileImageEncoded = Base64.encodeToString(b, Base64.DEFAULT)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
//                (findViewById(R.id.circleView) as ImageView).setImageURI(result.uri)
                Toast.makeText(this, "Cropping successful", Toast.LENGTH_LONG).show()
                Picasso.with(applicationContext)
                    .load(result.uri)
                    .centerCrop().resize(150,150)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(circleView)
                val contentURI = result.uri
                val f = File(contentURI.toString())
                profilePicturePath = f.getName()//+".png"
                Log.d("TAGG", picturepath.toString())
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                circleView!!.setImageBitmap(bitmap)
                circleView.setPadding(0,0,0,0)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                profileImageEncoded = Base64.encodeToString(b, Base64.DEFAULT)

            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true

        ToastHelper.makeToast(applicationContext, "Please click BACK again to exit")

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }


    fun submitProfileDetails() {

        val params = HashMap<String, String>()

        params["about"] = etWorkSkills.text.toString()
        params["image_filename"] = profilePicturePath.toString()
        params["image_file"] = profileImageEncoded

        Logger.d("TAGG", params.toString())

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.AddProfileDetails(
            EndPoints.CLIENT_ID, "Bearer "+
                    EndPoints.ACCESS_TOKEN, params)

        call.enqueue(object : Callback<CreateProfileResponse> {
            override fun onResponse(call: Call<CreateProfileResponse>, response: Response<CreateProfileResponse>) {

                Logger.d("CODE life exp", response.code().toString() + "")
                Logger.d("MESSAGE life exp", response.message() + "")
                Logger.d("URL life exp", "" + response)
                Logger.d("RESPONSE life exp", "" + Gson().toJson(response))

                if (response.isSuccessful) {

                    EndPoints.ACCESS_TOKEN = response.body()!!.auth!!.accessToken.toString()

                    val intent = Intent(applicationContext, ProfileView::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<CreateProfileResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })
    }

}
