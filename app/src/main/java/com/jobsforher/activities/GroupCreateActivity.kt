package com.jobsforher.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_group_create.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.helpers.Logger
import com.jobsforher.network.responsemodels.DeletePostResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class GroupCreateActivity : BaseActivity() {

    var groupType: String = ""
    private val GALLERY_IMAGE = 1
    public var imageEncoded = "";
    public var post_type="text"
    public var picturepath:String? = ""
    public var visibility:String? = "public"
    private var retrofitInterface_post: RetrofitInterface? = null
    private var awesomeValidation: AwesomeValidation? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_group_create)

        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation!!.addValidation(group_name,RegexTemplate.NOT_EMPTY,"Name is missing")
        awesomeValidation!!.addValidation(group_desc, RegexTemplate.NOT_EMPTY,"Description is missing");
        awesomeValidation!!.addValidation(group_obj, RegexTemplate.NOT_EMPTY,"Objective is missing");
        awesomeValidation!!.addValidation(group_aud, RegexTemplate.NOT_EMPTY,"Group Audience is missing");
        awesomeValidation!!.addValidation( group_ph, "^[+]?[0-9]{10,13}$", "Enter valid phone number");


        layout_icon.setOnClickListener {
            showPictureDialog();
        }

        btnPost_group.setOnClickListener{
            if (awesomeValidation!!.validate())
                addGroup()
        }

        back_creategroup.setOnClickListener{
            finish()
        }

        btnCancel.setOnClickListener{
            finish()
        }

        rg.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                when (checkedId) {
                    R.id.radio_open_group -> {
                        visibility = "public"
                    }
                    R.id.radio_closed_group -> {
                        visibility = "private"
                    }

                }
            }
        })

    }



    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select icon from gallery")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                //1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
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
                    picturepath = f.getName()+".png"
                    Log.d("TAGG", picturepath.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    Toast.makeText(applicationContext,"Image Upload success", Toast.LENGTH_LONG).show()
                    Log.d("TAGG","Image upload success")
//                    pick_image!!.setImageBitmap(bitmap)
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
    }

    private fun addGroup(){

        val params = HashMap<String, String>()

        val description = group_desc.text
        val name = group_name.text
        val phone_number= group_ph.text
        val visiblity_type =visibility
        val icon_file= imageEncoded
        val icon_filename = picturepath
        val excerpt=group_obj.text
        val opened_to=group_aud.text

        params["description"] = description.toString()
        params["name"] = name.toString()
        params["phone_number"] = phone_number.toString()
        params["visiblity_type"] = visiblity_type.toString()
        params["icon_file"] = icon_file.toString()
        params["icon_filename"] = icon_filename.toString()
        params["excerpt"] = excerpt.toString()
        params["opened_to"] = opened_to.toString()
        params["group_creation"] = "external"
        params["status"] = "requested"

        Log.d("TAGG",params.toString())

        retrofitInterface_post = RetrofitClient.client!!.create(RetrofitInterface::class.java)
        val call = retrofitInterface_post!!.addGroup(
            "application/json",
            EndPoints.CLIENT_ID,
            "Bearer "+ EndPoints.ACCESS_TOKEN,
            params
        )
        call.enqueue(object : Callback<DeletePostResponse> {

            override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {

                Log.d("TAG", "CODE Value" + response.code().toString() + "")
                Log.d("TAG", "MESSAGE VAlue" + response.message() + "")
                Log.d("TAG", "RESPONSE Value" + "" + Gson().toJson(response))
                Log.d("TAGG", "URL Value" + "" + response)


                if (response.isSuccessful && response.body()!!.responseCode == 10109) {
                    //mDataList[position].comment_list!! =
//                    Log.d("TAGG", "DATA" + jsonaobj.getString("id"))
                    Toast.makeText(applicationContext,"Group creation request under review", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext,"Error " +response.body()!!.message.toString(), Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }
}


