package com.jobsforher.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_group_info.*
import com.jobsforher.R
import android.content.Intent
import android.os.Build
import android.app.Activity
import com.jobsforher.helpers.Logger
import women.jobs.jobsforher.activities.BaseActivity
import java.io.File


class GroupInfoActivity : BaseActivity() {

    var fileType: String = ""

    private val CHOOSE_FILE_REQUESTCODE = 8777

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_group_info)

    }

    // multiple Radio click method
    fun onRadioButtonClicked(view: View) {
        var checked = view as RadioButton
        if (radio_text == checked) {

            radio_video.setChecked(false)
            radio_image.setChecked(false)
            radio_document.setChecked(false)

            fileType = "Text"
        }
        if (radio_video == checked) {

            radio_text.setChecked(false)
            radio_image.setChecked(false)
            radio_document.setChecked(false)

            fileType = "Video"
        }

        if (radio_image == checked) {

            radio_video.setChecked(false)
            radio_text.setChecked(false)
            radio_document.setChecked(false)

            fileType = "Image"
        }
        if (radio_document == checked) {

            radio_video.setChecked(false)
            radio_image.setChecked(false)
            radio_text.setChecked(false)

            fileType = "Document"
        }

        uploadText.setOnClickListener {

            if (fileType.equals("")){

//                message("Select File Type")

                Toast.makeText(this, "Select File Type", Toast.LENGTH_SHORT).show()

            }else if (fileType.equals("Text")){

                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                intent.type = "file/*"
                intent.type = "text/plain"
                startActivityForResult(intent, CHOOSE_FILE_REQUESTCODE)

            }else if (fileType.equals("Video")){

                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                intent.type = "file/*"
                intent.type = "video/*"
                startActivityForResult(intent, CHOOSE_FILE_REQUESTCODE)

            }else if (fileType.equals("Image")){

                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                intent.type = "file/*"
                intent.type = "image/jpeg"
                startActivityForResult(intent, CHOOSE_FILE_REQUESTCODE)

            }else if (fileType.equals("Document")){

                browseDocuments()
            }
        }

    }

    @SuppressLint("ObsoleteSdkInt")
    private fun browseDocuments() {

        val mimeTypes = arrayOf(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
            "text/plain",
            "application/pdf",
            "application/zip"
        )

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
            if (mimeTypes.size > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        } else {
            var mimeTypesStr = ""
            for (mimeType in mimeTypes) {
                mimeTypesStr += "$mimeType|"
            }
            intent.type = mimeTypesStr.substring(0, mimeTypesStr.length - 1)
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), CHOOSE_FILE_REQUESTCODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CHOOSE_FILE_REQUESTCODE -> if (resultCode === Activity.RESULT_OK) {


                if (null != data) {

                    val uri = data.data

                    val fileName = File(uri!!.path).name

                    Logger.e("CODE", fileName + "")

//                        val selectedFile = data?.data //The uri with the location of the file

                    fileNameText.text = fileName


                }
            }
        }
    }

}
