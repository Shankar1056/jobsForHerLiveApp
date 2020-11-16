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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.facebook.CallbackManager
import com.google.gson.Gson
import com.plumillonforge.android.chipview.Chip
import com.plumillonforge.android.chipview.ChipView
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.activities.ProfileView
import com.jobsforher.activities.WorkingEdit
import com.jobsforher.helpers.Logger
import com.jobsforher.models.WorkingModel
import com.jobsforher.network.responsemodels.DeletePostResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import java.text.SimpleDateFormat

class WorkingAdapter(private val mDataList: ArrayList<WorkingModel>) : RecyclerView.Adapter<WorkingAdapter.MyViewHolder>() {

    public var context: Context? = null
    private var retrofitInterface: RetrofitInterface? = null
    private var callbackManager: CallbackManager? = null
    var retrofitInterface_delete: RetrofitInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.working_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.groupicon_work.setImageResource(R.drawable.ic_briefcase)
        holder.groupicon_work.setBackgroundResource(R.drawable.bg_circle_briefcase)
        holder.work_name.text = mDataList[position].company_name
        holder.work_college.text = mDataList[position].designation
        holder.work_date.text= dateConversion(mDataList[position].start_date.toString())+"-"+dateConversion(mDataList[position].end_date.toString())
        holder.work_desc.text= mDataList[position].description
        if (mDataList[position].location.equals("null") || mDataList[position].location.equals(""))
            holder.work_location.visibility  =View.GONE
        else holder.work_location.text = mDataList[position].location
        if(mDataList[position].image_url!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.work_image)
        }
        else
            holder.work_image.visibility=View.GONE
        if (mDataList[position].skills!!.size > 0) {
            var arrayList: String? = ""
            val list = ArrayList<Chip>()
            for (k in 0 until mDataList[position].skills!!.size) {
                if(!isWhitespace(mDataList[position].skills?.get(k)!!.trim())) {
                    arrayList = arrayList + mDataList[position].skills?.get(k)!!.toString()
                    list.add(com.jobsforher.models.Tag(mDataList[position].skills?.get(k)!!.toString().trim()))
                }
            }
//            holder.work_skills.text = arrayList
//            holder.work_skills1.setTagList(list)
            holder.work_skills1.setChipList(list);
        } else {
            holder.work_skills.visibility = View.GONE
            holder.work_skills1.visibility = View.GONE
        }

        holder.work_edit.setOnClickListener {
            val dialog : Dialog =  Dialog(context!!);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.custompopup);

            val addmile: LinearLayout = dialog.findViewById(R.id.add_miletsone);
            addmile.setOnClickListener {
                val intent = Intent(context, WorkingEdit::class.java)
                Toast.makeText(context,mDataList[position].id.toString()+"", Toast.LENGTH_LONG).show()
                dialog.cancel()
                (context as Activity).startActivityForResult(intent, 1)
            }

            val editmile: LinearLayout = dialog.findViewById(R.id.Edit_miletsone);
            editmile.setOnClickListener {
                val intent = Intent(context, WorkingEdit::class.java)
                Toast.makeText(context,mDataList[position].id.toString()+"", Toast.LENGTH_LONG).show()
                dialog.cancel()
                intent.putExtra("ID",mDataList[position].id)
                (context as Activity).startActivityForResult(intent, 1)
            }

            val deletemile: LinearLayout = dialog.findViewById(R.id.Delete_miletsone);
            deletemile.setOnClickListener {
                dialog.cancel()
                deleteWorkingData(mDataList[position].id)
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
        internal var work_edit:TextView
        internal var work_name: TextView
        internal var work_college: TextView
        internal var work_date: TextView
        internal var work_image: ImageView
        internal var work_desc: TextView
        internal var work_skills: TextView
        internal var work_location: TextView
        internal var work_skills1: ChipView
        internal var groupicon_work : ImageView



        init {
            work_name = itemView.findViewById<View>(R.id.work_name) as TextView
            work_college = itemView.findViewById<View>(R.id.work_college) as TextView
            work_date = itemView.findViewById<View>(R.id.work_date) as TextView
            work_desc = itemView.findViewById<View>(R.id.work_desc) as TextView
            work_edit = itemView.findViewById<View>(R.id.work_edit) as TextView
            work_image = itemView.findViewById<View>(R.id.work_image) as ImageView
            work_skills = itemView.findViewById<View>(R.id.work_skils) as TextView
            work_skills1 = itemView.findViewById<View>(R.id.work_skils1) as ChipView
            work_location = itemView.findViewById<View>(R.id.work_location) as TextView
            groupicon_work  =itemView.findViewById<View>(R.id.group_icon) as ImageView
        }
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
                    Toast.makeText(context,"Delete Successfully!!", Toast.LENGTH_LONG).show()
                    (context as ProfileView).loadWorkExperienceData()
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

}