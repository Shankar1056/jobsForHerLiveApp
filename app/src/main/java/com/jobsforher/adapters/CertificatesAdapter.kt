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
import com.jobsforher.activities.CertificationEdit
import com.jobsforher.activities.ProfileView
import com.jobsforher.helpers.Logger
import com.jobsforher.models.CertificateModel
import com.jobsforher.network.responsemodels.DeletePostResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import java.text.SimpleDateFormat

class CertificatesAdapter(private val mDataList: ArrayList<CertificateModel>) : RecyclerView.Adapter<CertificatesAdapter.MyViewHolder>() {

    public var context: Context? = null
    private var retrofitInterface: RetrofitInterface? = null
    private var callbackManager: CallbackManager? = null
    var retrofitInterface_delete: RetrofitInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.certificated_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.groupicon_cert.setImageResource(R.drawable.ic_certificates)
        holder.groupicon_cert.setBackgroundResource(R.drawable.bg_circle_file)
        holder.cert_name.text = mDataList[position].name
        holder.cert_college.text = mDataList[position].organization
        if (mDataList[position].expires!!.toBoolean()==false)
            holder.cert_date.text= dateConversion(mDataList[position].start_date.toString())+"-"+dateConversion(mDataList[position].expires_on.toString())
        else
            holder.cert_date.text= dateConversion(mDataList[position].start_date.toString())+"-Present"

        if (mDataList[position].description.equals("null")||mDataList[position].description.equals(""))
            holder.cert_desc.visibility  =View.GONE
        else holder.cert_desc.text = mDataList[position].description

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
//            holder.cert_skills.text = arrayList
            holder.certkills1.setChipList(list)
        } else {
            holder.certkills1.visibility = View.GONE
            holder.cert_skills.visibility = View.GONE
        }

        if(mDataList[position].image_url!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.cert_image)
        }
        else
            holder.cert_image.visibility=View.GONE


        holder.cert_edit.setOnClickListener {
            val dialog : Dialog =  Dialog(context!!);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.custompopup);

            val addmile: LinearLayout = dialog.findViewById(R.id.add_miletsone);
            addmile.setOnClickListener {
                val intent = Intent(context, CertificationEdit::class.java)
                Toast.makeText(context,mDataList[position].id.toString()+"", Toast.LENGTH_LONG).show()
                dialog.cancel()
                (context as Activity).startActivityForResult(intent, 1)
            }

            val editmile: LinearLayout = dialog.findViewById(R.id.Edit_miletsone);
            editmile.setOnClickListener {
                val intent = Intent(context, CertificationEdit::class.java)
                Toast.makeText(context,mDataList[position].id.toString()+"", Toast.LENGTH_LONG).show()
                dialog.cancel()
                intent.putExtra("ID", mDataList[position].id )
                (context as Activity).startActivityForResult(intent, 1)
            }

            val deletemile: LinearLayout = dialog.findViewById(R.id.Delete_miletsone);
            deletemile.setOnClickListener {
                dialog.cancel()
                deleteCertificateData(mDataList[position].id)
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
        internal var cert_edit:TextView
        internal var cert_name: TextView
        internal var cert_college: TextView
        internal var cert_date: TextView
        internal var cert_image: ImageView
        internal var cert_desc: TextView
        internal var cert_skills:TextView
        internal var certkills1: ChipView
        internal var groupicon_cert : ImageView
//        internal var mileskills1: TagContainerLayout


        init {
            cert_name = itemView.findViewById<View>(R.id.cert_name) as TextView
            cert_college = itemView.findViewById<View>(R.id.cert_college) as TextView
            cert_date = itemView.findViewById<View>(R.id.cert_date) as TextView
            cert_desc = itemView.findViewById<View>(R.id.cert_desc) as TextView
            cert_edit = itemView.findViewById<View>(R.id.cert_edit) as TextView
            cert_image = itemView.findViewById<View>(R.id.cert_image) as ImageView
            cert_skills = itemView.findViewById<View>(R.id.cert_skills) as TextView
//            mileskills1 = itemView.findViewById<View>(R.id.mile_skills1) as TagContainerLayout
            certkills1 = itemView.findViewById<View>(R.id.cert_skils1) as ChipView
            groupicon_cert  =itemView.findViewById<View>(R.id.group_icon) as ImageView
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


    fun deleteCertificateData(certId:Int){

        var model: CertificateModel = CertificateModel()
        retrofitInterface_delete = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface_delete!!.DeleteCertificationData(certId, EndPoints.CLIENT_ID, "Bearer "+ EndPoints.ACCESS_TOKEN)

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
                    (context as ProfileView).loadCertificateData()
                }
                else {
                    Toast.makeText(context,"Update Failed!!", Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                Logger.d("TAGG", "FAILED : $t")
                Toast.makeText(context,"Update Failed!!", Toast.LENGTH_LONG).show()

            }
        })
    }
}