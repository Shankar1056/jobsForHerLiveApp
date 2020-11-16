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
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.activities.AwardsEdit
import com.jobsforher.activities.ProfileView
import com.jobsforher.helpers.Logger
import com.jobsforher.models.RecognitionModel
import com.jobsforher.network.responsemodels.DeletePostResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import java.text.SimpleDateFormat

class RecognitiosAdapter(private val mDataList: ArrayList<RecognitionModel>) : RecyclerView.Adapter<RecognitiosAdapter.MyViewHolder>() {

    public var context: Context? = null
    private var retrofitInterface: RetrofitInterface? = null
    private var callbackManager: CallbackManager? = null
    var retrofitInterface_delete: RetrofitInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.recogntion_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.recog_name.text = mDataList[position].name
        holder.recog_college.text = mDataList[position].organization
        holder.recog_date.text= dateConversion(mDataList[position].start_date.toString())
        holder.recog_desc.text= mDataList[position].description
        holder.groupicon_rec.setImageResource(R.drawable.ic_trophy)
        holder.groupicon_rec.setBackgroundResource(R.drawable.bg_circle_trophy)

        if (mDataList[position].image_url!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.recog_image)
        }
        else{
            holder.recog_image.visibility = View.GONE
//            Picasso.with(context)
//                .load(R.drawable.ic_launcher_foreground)
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into(holder.recog_image)
        }
        holder.recog_edit.setOnClickListener {
            val intent = Intent(context, AwardsEdit::class.java)
            intent.putExtra("Id",mDataList[position].id)
            (context as Activity).startActivityForResult(intent, 1)
        }

        holder.recog_edit.setOnClickListener{

            val dialog : Dialog =  Dialog(context!!);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.custompopup);

            val addmile: LinearLayout = dialog.findViewById(R.id.add_miletsone);
            addmile.setOnClickListener {
                val intent = Intent(context, AwardsEdit::class.java)
                Toast.makeText(context,mDataList[position].id.toString()+"", Toast.LENGTH_LONG).show()
                dialog.cancel()
                (context as Activity).startActivityForResult(intent, 1)
            }

            val editmile: LinearLayout = dialog.findViewById(R.id.Edit_miletsone);
            editmile.setOnClickListener {
                val intent = Intent(context, AwardsEdit::class.java)
                Toast.makeText(context,mDataList[position].id.toString()+"", Toast.LENGTH_LONG).show()
                dialog.cancel()
                intent.putExtra("Id",mDataList[position].id)
                (context as Activity).startActivityForResult(intent, 1)
            }

            val deletemile: LinearLayout = dialog.findViewById(R.id.Delete_miletsone);
            deletemile.setOnClickListener {
                dialog.cancel()
                deleteRecognitionData(mDataList[position].id)
            }
            dialog.show();
        }


    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var recog_edit:TextView
        internal var recog_name: TextView
        internal var recog_college: TextView
        internal var recog_date: TextView
        internal var recog_image: ImageView
        internal var recog_desc: TextView
        internal var groupicon_rec : ImageView



        init {
            recog_name = itemView.findViewById<View>(R.id.recog_name) as TextView
            recog_college = itemView.findViewById<View>(R.id.recog_college) as TextView
            recog_date = itemView.findViewById<View>(R.id.recog_date) as TextView
            recog_desc = itemView.findViewById<View>(R.id.recog_desc) as TextView
            recog_edit = itemView.findViewById<View>(R.id.recog_edit) as TextView
            recog_image = itemView.findViewById<View>(R.id.recog_image) as ImageView
            groupicon_rec  =itemView.findViewById<View>(R.id.group_icon) as ImageView
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


    fun deleteRecognitionData(recogId:Int){

        retrofitInterface_delete = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface_delete!!.DeleteRecognitionData(recogId, EndPoints.CLIENT_ID, "Bearer "+ EndPoints.ACCESS_TOKEN)

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
                    (context as ProfileView).loadRecognitionData()
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
}