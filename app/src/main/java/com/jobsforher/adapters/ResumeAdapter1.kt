package com.jobsforher.adapters

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import com.jobsforher.models.ResumeView
import java.text.SimpleDateFormat
import java.util.*
import com.jobsforher.R
import com.jobsforher.activities.ZActivityCompanyDetails
import com.jobsforher.activities.ZActivityJobDetails
import com.jobsforher.activities.ZActivityJobs
import java.text.ParseException
import kotlin.collections.ArrayList

class ResumeAdapter1(private val mDataList: ArrayList<ResumeView>,val selectChecks: ArrayList<Int>, val page:String) : RecyclerView.Adapter<ResumeAdapter1.MyViewHolder>() {

    private var context: Context? = null
    var index: MyViewHolder? =null
    private var lastCheckedPos  = -1
    var inflater: LayoutInflater? = null
    val selectCheck: ArrayList<Int>? = selectChecks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.resumeview_adapter_row, parent, false)
        this.context = parent.context;
        context = (context as ContextWrapper).baseContext
        inflater = LayoutInflater.from(context);

        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_title.text = mDataList[position].title
        var fileName = mDataList[position].path!!.substring(mDataList[position].path!!.lastIndexOf('/') + 1)
        fileName = fileName.substring(fileName.indexOf("_")+1)
        holder.row_name.text = fileName

        holder.row_default.text = mDataList[position].is_default
        val userDob: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mDataList[position].created_on.toString());
        val today : Date = Date();
        val diff =  today.getTime() - userDob.getTime()
        val numOfDays : Long = (diff / (1000 * 60 * 60 * 24));
        holder.row_date.text = getDate(mDataList[position].created_on!!)
        //holder.row_date.text = mDataList[position].created_on.toString()

        if(fileName.contains("pdf")){
            Picasso.with(context)
                .load(R.drawable.pdf)
                .placeholder(R.drawable.pdf)
                .into(holder.sub_image)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.doc)
                .placeholder(R.drawable.doc)
                .into(holder.sub_image)
        }

        if (mDataList[position].path!!.length>0) {
            Picasso.with(context)
                .load(R.drawable.component)
                .placeholder(R.drawable.component)
                .into(holder.row_image)
        }else{
            Picasso.with(context)
                .load(R.drawable.component)
                .placeholder(R.drawable.component)
                .into(holder.row_image)
        }


//        if (position==0){
//            holder.radio.isChecked = true
//            lastCheckedPos = 0;
//            index=holder
//        }
//        else
//            holder.radio.isChecked=false

        if (selectCheck?.get(position)!!.equals(1)) {
            holder.radio.isChecked = true
            Log.d("TAGG", "Checked")
            if(page.equals("Jobs")){
                (context as ZActivityJobs).getRadioSelected(mDataList[position].id)
            }
            else if(page.equals("Jobs Details")){
                (context as ZActivityJobDetails).getRadioSelected(mDataList[position].id)
            }
            else if(page.equals("Company Details")){
                (context as ZActivityCompanyDetails).getRadioSelected(mDataList[position].id)
            }
        } else {
            holder.radio.isChecked = false
            Log.d("TAGG", "unchecked")
        }

        holder.radio.setOnClickListener{
            holder.row_layout.callOnClick()

        }


        holder.row_layout.setOnClickListener{

            //           if(lastCheckedPos!=position && lastCheckedPos>-1){
//               Toast.makeText(context,"Please uncheck the previous selection", Toast.LENGTH_LONG).show()
//           }
//           else {
//               index = holder
//               if (!holder.radio.isChecked) {
//                   holder.radio.isChecked = true
//                   lastCheckedPos = position
//               }
//               else {
//                   holder.radio.isChecked = false
//                   lastCheckedPos = -1
//               }
//
//           }

            for (k in 0 until selectCheck.size) {
                if (k == position) {
                    selectCheck[k] = 1
                } else {
                    selectCheck[k] = 0
                }
            }
            notifyDataSetChanged()
        }

//        holder.radio.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
//
//                if (isChecked == true) {
//                    holder.radio.isChecked = true
//                    Log.d("TAGG", "Ccked")
//
//                }
//            }
//        })

//        if (index == holder){
//            //holder.name.setTextColor(Color.parseColor("#00FF00"))
//        }
//        else{
//            holder.radio.isChecked=false
//        }
    }

    fun getDate(times:String):String{
        val formatter1:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        val dateInString:String = times
        val formatterOut:SimpleDateFormat = SimpleDateFormat("dd MMM yyyy");
        var date : Date? = null
        var s: String=""
        try {
            date=formatter1.parse(dateInString)
            System.out.println(formatterOut.format(date));
            s = formatterOut.format(date)

        } catch ( e: ParseException) {
            e.printStackTrace()
        }
        return s
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_title: TextView
        internal var row_name: TextView
        internal var row_date: TextView
        internal var row_default : TextView
        internal  var row_layout: LinearLayout
        internal lateinit var row_image: CircleImageView
        internal var sub_image:ImageView
        internal lateinit var radio: RadioButton
        init {
            row_title = itemView.findViewById<View>(R.id.row_title) as TextView
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_date = itemView.findViewById<View>(R.id.row_date) as TextView
            row_default = itemView.findViewById<View>(R.id.row_default) as TextView
            row_image = itemView.findViewById<View>(R.id.row_circular_image) as CircleImageView
            row_layout = itemView.findViewById<View>(R.id.mainlayout) as LinearLayout
            radio = itemView.findViewById<View>(R.id.offer_select) as RadioButton
            sub_image = itemView.findViewById<View>(R.id.sub_image) as ImageView
        }


    }
}