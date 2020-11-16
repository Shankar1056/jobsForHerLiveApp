package com.jobsforher.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.facebook.CallbackManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.plumillonforge.android.chipview.Chip
import com.plumillonforge.android.chipview.ChipView
import com.plumillonforge.android.chipview.ChipViewAdapter
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.activities.*
import com.jobsforher.helpers.Logger
import com.jobsforher.models.*
import com.jobsforher.network.responsemodels.DeletePostResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import java.text.SimpleDateFormat


class MilestonesAdapter(private val mDataList: ArrayList<TimeListModel>) : RecyclerView.Adapter<MilestonesAdapter.MyViewHolder>() {

    public var context: Context? = null
    private var callbackManager: CallbackManager? = null
    var myDialog: Dialog? = null
    var retrofitInterface_delete: RetrofitInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.milestones_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var adapter:ChipViewAdapter = com.jobsforher.adapters.MainChipsViewAdapter(context);
//        chipView.setAdapter(adapter)

        if (mDataList[position].type.equals("working")) {

            holder.miletype.text = mDataList[position].type
            holder.milename.text = mDataList[position].organization
            holder.milecollege.text = mDataList[position].designation
            holder.miledate.text = dateConversion(mDataList[position].start_date.toString())+"-"+dateConversion(mDataList[position].end_date.toString())
            holder.miledesc.text = mDataList[position].description
            if (mDataList[position].location.equals("null") || mDataList[position].location.equals(""))
                holder.mileslocation.visibility  =View.GONE
            else holder.mileslocation.text = mDataList[position].location
            holder.groupicon_milestone.setImageResource(R.drawable.ic_briefcase)
            holder.groupicon_milestone.setBackgroundResource(R.drawable.bg_circle_briefcase)

            if (mDataList[position].skills!!.size > 0) {
                var arrayList: String? = ""
                val list = ArrayList<Chip>()
                for (k in 0 until mDataList[position].skills!!.size) {
                    if(!isWhitespace(mDataList[position].skills?.get(k)!!.trim()))
                    {
                        arrayList = arrayList + mDataList[position].skills?.get(k)!!.toString()!!.trim()
                        list.add(com.jobsforher.models.Tag(mDataList[position].skills?.get(k)!!.trim().toString()))
                    }
                }
                holder.mileskills1.setChipList(list)
//                holder.mileskills.text = arrayList
            } else {
                holder.mileskills1.visibility = View.GONE
                holder.mileskills.visibility = View.GONE
            }
            if (mDataList[position].image_url!!.length <= 0) {
                holder.mileimage.visibility = View.GONE
            } else {
                Picasso.with(context)
                    .load(mDataList[position].image_url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.mileimage)
            }

        }
        else  if (mDataList[position].type.equals("recognition")) {

            holder.miletype.text = mDataList[position].type
            holder.milename.text = mDataList[position].title
            holder.milecollege.text = mDataList[position].organization
            holder.miledate.text = dateConversion(mDataList[position].start_date.toString())
            holder.miledesc.text = mDataList[position].description
            holder.groupicon_milestone.setImageResource(R.drawable.ic_trophy)
            holder.groupicon_milestone.setBackgroundResource(R.drawable.bg_circle_trophy)
            if (mDataList[position].image_url!!.length <= 0) {
                holder.mileimage.visibility = View.GONE
            } else {
                Picasso.with(context)
                    .load(mDataList[position].image_url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.mileimage)
            }
            holder.mileslocation.visibility = View.GONE
            holder.mileskills1.visibility = View.GONE
            holder.mileskills.visibility = View.GONE
        }
        else if (mDataList[position].type.equals("certification")) {

            holder.miletype.text = mDataList[position].type
            holder.milename.text = mDataList[position].name
            holder.milecollege.text = mDataList[position].organization
            holder.miledate.text = dateConversion(mDataList[position].start_date.toString())+"-"+dateConversion(mDataList[position].expires_on.toString())
            if (mDataList[position].description.equals("null")||mDataList[position].description.equals(""))
                holder.miledesc.visibility  =View.GONE
            else holder.miledesc.text = mDataList[position].description

            if (mDataList[position].location.equals("null") || mDataList[position].location.equals(""))
                holder.mileslocation.visibility  =View.GONE
            else holder.mileslocation.text = mDataList[position].location

            holder.groupicon_milestone.setImageResource(R.drawable.ic_certificates)
            holder.groupicon_milestone.setBackgroundResource(R.drawable.bg_circle_file)
            if (mDataList[position].image_url!!.length <= 0) {
                holder.mileimage.visibility = View.GONE
            } else {
                Picasso.with(context)
                    .load(mDataList[position].image_url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.mileimage)
            }
            if (mDataList[position].skills!!.size > 0) {
                var arrayList: String? = ""
                val list = ArrayList<Chip>()
                for (k in 0 until mDataList[position].skills!!.size) {
                    if (mDataList[position].skills?.get(k)!!.toString().equals("null")){}
                    else {
                        if(!isWhitespace(mDataList[position].skills?.get(k)!!.trim())) {
                            arrayList = arrayList + mDataList[position].skills?.get(k)!!.toString() + ","
                            list.add(com.jobsforher.models.Tag(mDataList[position].skills?.get(k)!!.toString().trim()))
                        }
                    }
                }
                holder.mileskills1.setChipList(list)
            } else {
                holder.mileskills1.visibility = View.GONE
                holder.mileskills.visibility = View.GONE
            }
        }
        else if (mDataList[position].type.equals("life_experience")) {

            holder.miletype.text = mDataList[position].type
            holder.milecollege.text = mDataList[position].duration
            holder.miledate.text = dateConversion(mDataList[position].start_date.toString())+"-"+dateConversion(mDataList[position].end_date.toString())
            if (mDataList[position].description.equals("null")||mDataList[position].description.equals(""))
                holder.miledesc.visibility  =View.GONE
            else holder.miledesc.text = mDataList[position].description

            if (mDataList[position].location.equals("null") || mDataList[position].location.equals(""))
                holder.mileslocation.visibility  =View.GONE
            else holder.mileslocation.text = mDataList[position].location

            holder.groupicon_milestone.setImageResource(R.drawable.ic_favorite_heart_button)
            holder.groupicon_milestone.setBackgroundResource(R.drawable.bg_circle_heart)
            if (mDataList[position].image_url!!.length <= 0) {
                holder.mileimage.visibility = View.GONE
            } else {
                Picasso.with(context)
                    .load(mDataList[position].image_url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.mileimage)
            }
            if (mDataList[position].lifeexperience!!.size > 0) {
                var arrayList: String? = ""
                for (k in 0 until mDataList[position].lifeexperience!!.size) {
                    arrayList = arrayList + mDataList[position].lifeexperience?.get(k)!!.toString()
                }
                holder.milename.text = arrayList
            } else {
                holder.milename.visibility = View.GONE
            }

            if (mDataList[position].skills!!.size > 0) {
                var arrayList: String? = ""
                val list = ArrayList<Chip>()
                for (k in 0 until mDataList[position].skills!!.size) {
                    if (mDataList[position].skills?.get(k)!!.toString().equals("null")){}
                    else {
                        if(!isWhitespace(mDataList[position].skills?.get(k)!!.trim())) {
                            arrayList = arrayList + mDataList[position].skills?.get(k)!!.toString() + ","
                            list.add(com.jobsforher.models.Tag(mDataList[position].skills?.get(k)!!.toString().trim()))
                        }
                    }
                }
                holder.mileskills1.setChipList(list)
            } else {
                holder.mileskills1.visibility = View.GONE
                holder.mileskills.visibility = View.GONE
            }

        }
        else if (mDataList[position].type.equals("education")) {

            holder.miletype.text = mDataList[position].type
            holder.milename.text = mDataList[position].degree
            holder.milecollege.text = mDataList[position].college
            holder.miledate.text = dateConversion(mDataList[position].start_date.toString())+"-"+dateConversion(mDataList[position].end_date.toString())
            if (mDataList[position].description.equals("null")||mDataList[position].description.equals(""))
                holder.miledesc.visibility  =View.GONE
            else holder.miledesc.text = mDataList[position].description

            if (mDataList[position].location.equals("null") || mDataList[position].location.equals(""))
                holder.mileslocation.visibility  =View.GONE
            else holder.mileslocation.text = mDataList[position].location
            holder.groupicon_milestone.setImageResource(R.drawable.ic_college_graduation)
            holder.groupicon_milestone.setBackgroundResource(R.drawable.bg_circle_graduation)
            if (mDataList[position].image_url!!.length <= 0) {
                holder.mileimage.visibility = View.GONE
            } else {
                Picasso.with(context)
                    .load(mDataList[position].image_url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.mileimage)
            }
            if (mDataList[position].skills!!.size > 0) {
                var arrayList: String? = ""
                val list = ArrayList<Chip>()
                for (k in 0 until mDataList[position].skills!!.size) {
                    if (mDataList[position].skills?.get(k)!!.toString().equals("null")){}
                    else {
                        if(!isWhitespace(mDataList[position].skills?.get(k)!!.trim())) {
                            arrayList = arrayList + mDataList[position].skills?.get(k)!!.toString() + ","
                            list.add(com.jobsforher.models.Tag(mDataList[position].skills?.get(k)!!.toString().trim()))
                        }
                    }
                }
                holder.mileskills1.setChipList(list)
            } else {
                holder.mileskills1.visibility = View.GONE
                holder.mileskills.visibility = View.GONE
            }

        }

        holder.mileedit.setOnClickListener{

            val dialog :  Dialog =  Dialog(context!!);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.custompopup);

            val addmile: LinearLayout = dialog.findViewById(R.id.add_miletsone);
            addmile.setOnClickListener {
                if (mDataList[position].type.equals("working")) {
                    val intent = Intent(context, WorkingEdit::class.java)
                  //  Toast.makeText(context,mDataList[position].id.toString()+"",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                    (context as Activity).startActivityForResult(intent, 1)
                }
                else  if (mDataList[position].type.equals("recognition")) {
                    val intent = Intent(context, AwardsEdit::class.java)
                   // Toast.makeText(context,mDataList[position].id.toString()+"",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                    (context as Activity).startActivityForResult(intent, 1)
                }
                else if (mDataList[position].type.equals("certification")) {
                    val intent = Intent(context, CertificationEdit::class.java)
                   // Toast.makeText(context,mDataList[position].id.toString()+"",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                    (context as Activity).startActivityForResult(intent, 1)
                }
                else if (mDataList[position].type.equals("life_experience")) {
                    val intent = Intent(context, LifeExperienceEdit::class.java)
                   // Toast.makeText(context,mDataList[position].id.toString()+"",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                    (context as Activity).startActivityForResult(intent, 1)
                }
                else if (mDataList[position].type.equals("education")) {
                    val intent = Intent(context, EducationEdit::class.java)
                   // Toast.makeText(context,mDataList[position].id.toString()+"",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                    (context as Activity).startActivityForResult(intent, 1)
                }
            }

            val editmile: LinearLayout = dialog.findViewById(R.id.Edit_miletsone);
            editmile.setOnClickListener {
                if (mDataList[position].type.equals("working")) {
                    val intent = Intent(context, WorkingEdit::class.java)
                  //  Toast.makeText(context,mDataList[position].id.toString()+"",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                    intent.putExtra("ID",mDataList[position].id)
                    (context as Activity).startActivityForResult(intent, 1)
                }
                else  if (mDataList[position].type.equals("recognition")) {
                    val intent = Intent(context, AwardsEdit::class.java)
                    //Toast.makeText(context,mDataList[position].id.toString()+"",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                    intent.putExtra("Id",mDataList[position].id)
                    (context as Activity).startActivityForResult(intent, 1)
                }
                else if (mDataList[position].type.equals("certification")) {
                    val intent = Intent(context, CertificationEdit::class.java)
                    //Toast.makeText(context,mDataList[position].id.toString()+"",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                    intent.putExtra("ID", mDataList[position].id )
                    (context as Activity).startActivityForResult(intent, 1)
                }
                else if (mDataList[position].type.equals("life_experience")) {
                    val intent = Intent(context, LifeExperienceEdit::class.java)
                   // Toast.makeText(context,mDataList[position].id.toString()+"",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                    intent.putExtra("ID", mDataList[position].id )
                    (context as Activity).startActivityForResult(intent, 1)
                }
                else if (mDataList[position].type.equals("education")) {
                    val intent = Intent(context, EducationEdit::class.java)
                    //Toast.makeText(context,mDataList[position].id.toString()+"",Toast.LENGTH_LONG).show()
                    dialog.cancel()
                    intent.putExtra("ID", mDataList[position].id )
                    (context as Activity).startActivityForResult(intent, 1)
                }
            }

            val deletemile: LinearLayout = dialog.findViewById(R.id.Delete_miletsone);
            deletemile.setOnClickListener {
                if (mDataList[position].type.equals("working")) {
                    dialog.cancel()
                    deleteWorkingData(mDataList[position].id)
                }
                else  if (mDataList[position].type.equals("recognition")) {
                    dialog.cancel()
                    deleteRecognitionData(mDataList[position].id)
                }
                else if (mDataList[position].type.equals("certification")) {
                    dialog.cancel()
                    deleteCertificateData(mDataList[position].id)
                }
                else if (mDataList[position].type.equals("life_experience")) {
                    dialog.cancel()
                    deleteLifeData(mDataList[position].id)
                }
                else if (mDataList[position].type.equals("education")) {
                    dialog.cancel()
                    deleteEducationData(mDataList[position].id)
                }
            }
            dialog.show();
        }

    }




    fun isWhitespace(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val sz = str.length
        for (i in 0 until sz) {
            if (Character.isWhitespace(str[i]) == false) {
                return false
            }
        }
        return true
    }
    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var miletype:TextView
        internal var milename: TextView
        internal var milecollege: TextView
        internal var miledate: TextView
        internal var mileimage: ImageView
        internal var mileedit: RelativeLayout
        internal var miledesc: TextView
        internal var mileskills: TextView
        internal var mileskills1: ChipView
        internal var groupicon_milestone : ImageView
        internal var mileslocation: TextView
        internal var viewline: View



        init {
            miletype = itemView.findViewById<View>(R.id.mile_type) as TextView
            milename = itemView.findViewById<View>(R.id.mile_name) as TextView
            milecollege = itemView.findViewById<View>(R.id.mile_college) as TextView
            miledate = itemView.findViewById<View>(R.id.mile_date) as TextView
            mileimage= itemView.findViewById<View>(R.id.mile_image) as ImageView
            mileedit= itemView.findViewById<View>(R.id.mile_edit) as RelativeLayout
            miledesc = itemView.findViewById<View>(R.id.mile_desc) as TextView
            mileskills = itemView.findViewById<View>(R.id.mile_skills) as TextView
            mileskills1 = itemView.findViewById<View>(R.id.mile_skils1) as ChipView
            mileslocation = itemView.findViewById<View>(R.id.mile_location) as TextView
            groupicon_milestone  =itemView.findViewById<View>(R.id.group_icon_milestone) as ImageView
            viewline = itemView.findViewById<View>(R.id.view_line) as View


        }

//        var center_x = mileimage.getX() + mileimage.getWidth() / 2 - viewline.getWidth() / 2
//        val param = btnClickMe.layoutParams as LinearLayout.LayoutParams
//        param.setMargins(10,10,10,10)
//        btnClickMe.layoutParams = param

    }


    fun dateConversion(date:String): String? {
        Log.d("TAGG", date)
        if (date.equals("null")){
            return "Present"
        }
        else {
            val formatter7 = SimpleDateFormat("yyyy-MM-dd")
            val date7 = formatter7.parse(date)
            val pattern = "MMM yyyy"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val dateModified = simpleDateFormat.format(date7)
            return dateModified
        }

    }


    fun deleteWorkingData(workId:Int){

        var model: WorkingModel = WorkingModel()
        retrofitInterface_delete = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface_delete!!.DeleteWorkExperienceData(workId,
            EndPoints.CLIENT_ID, "Bearer "+ EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if(response.isSuccessful){
                    Toast.makeText(context,"Deleted Successfully!!", Toast.LENGTH_LONG).show()
                    (context as ProfileView).loadProfileData()
                }
                else {
                    Toast.makeText(context,"Delete not possible!!", Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(context,"Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun deleteRecognitionData(recogId:Int){

        retrofitInterface_delete = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface_delete!!.DeleteRecognitionData(recogId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if(response.isSuccessful){
                    Toast.makeText(context,"Deleted Successfully!!", Toast.LENGTH_LONG).show()
                    (context as ProfileView).loadProfileData()
                }
                else {
                    Toast.makeText(context,"Delete Failed!!", Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(context,"Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun deleteCertificateData(certId:Int){

        var model: CertificateModel = CertificateModel()
        retrofitInterface_delete = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface_delete!!.DeleteCertificationData(certId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if(response.isSuccessful){
                    Toast.makeText(context,"Deleted Successfully!!", Toast.LENGTH_LONG).show()
                    (context as ProfileView).loadProfileData()
                }
                else {
                    Toast.makeText(context,"Delete Failed!!", Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(context,"Delete Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun deleteLifeData(lifeId:Int){

        var model: LifeModel = LifeModel();
        retrofitInterface_delete = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface_delete!!.DeleteLifeExperienceData(lifeId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)
        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {
                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))
                var str_response = Gson().toJson(response)
                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if(response.isSuccessful){
                    Toast.makeText(context,"Deleted Successfully!!", Toast.LENGTH_LONG).show()
                    (context as ProfileView).loadProfileData()
                }
                else {
                    Toast.makeText(context,"Delete Failed!!", Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(context," Failed!!", Toast.LENGTH_LONG).show()
                Toast.makeText(context,"Delete Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }

    fun deleteEducationData(editId:Int){

        retrofitInterface_delete = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface_delete!!.DeleteEducationData(
            editId,EndPoints.CLIENT_ID, "Bearer "+EndPoints.ACCESS_TOKEN)

        call.enqueue(object : Callback<DeletePostResponse> {
            override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {

                Logger.d("URL", "" + response+ EndPoints.CLIENT_ID)
                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("RESPONSE", "" + Gson().toJson(response))

                val gson = GsonBuilder().serializeNulls().create()
                var str_response = gson.toJson(response)

                val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),
                    str_response.lastIndexOf("}")+1))
                val jsonObject1 : JSONObject = jsonObject.getJSONObject("body")
                val responseCode: Int = jsonObject1.getInt("response_code")
                val message: String = jsonObject1.getString("message")
                // var jsonarray: JSONArray = jsonObject1.getJSONArray("body")

                if(response.isSuccessful){
                    Toast.makeText(context,"Deleted Successfully!!", Toast.LENGTH_LONG).show()
                    (context as ProfileView).loadProfileData()
                }
                else {
                    Toast.makeText(context,"Delete Failed!!", Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(context,"Delete Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }
}