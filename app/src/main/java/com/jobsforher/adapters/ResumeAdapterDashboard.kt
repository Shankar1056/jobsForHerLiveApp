package com.jobsforher.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jobsforher.R
import com.jobsforher.models.MyResume
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.activities.ZActivityDashboard


class ResumeAdapterDashboard(private val mDataList: ArrayList<MyResume>) : RecyclerView.Adapter<ResumeAdapterDashboard.MyViewHolder>() {

    private var context: Context? = null
    var index: MyViewHolder? =null
    private var mSelectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.resumesadapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_title.text = mDataList[position].title
        var fileName = mDataList[position].path!!.substring(mDataList[position].path!!.lastIndexOf('/') + 1)
        fileName = fileName.substring(fileName.indexOf("_")+1)
        holder.row_name.text = fileName

        if(mDataList[position].is_default.equals("true")) {
            holder.row_default.text = "Default"
            holder.row_default.setTextColor(Color.parseColor("#DFDFDF"))
        }
        else
            holder.row_default.text = "Make as Default"

        val userDob: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mDataList[position].created_on.toString());
        val today : Date = Date();
        val diff =  today.getTime() - userDob.getTime()
        val numOfDays : Long = (diff / (1000 * 60 * 60 * 24));
        holder.row_date.text = getDate(mDataList[position].created_on!!)

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
                .load(mDataList[position].path)
                .placeholder(R.drawable.component)
                .into(holder.row_image)
        }else{
            Picasso.with(context)
                .load(R.drawable.component)
                .placeholder(R.drawable.component)
                .into(holder.row_image)
        }

        holder.row_delete.setOnClickListener {
            (context as ZActivityDashboard).deleteResume(mDataList[position].id!!,mDataList[position].user_id!!)
        }

        holder.row_default.setOnClickListener {
            (context as ZActivityDashboard).makeDefaultResume(mDataList[position].id!!,mDataList[position].user_id!!)
        }

        holder.row_title.setOnClickListener {
            val url: String=mDataList[position].path.toString()
            val browserIntent = Intent(Intent.ACTION_VIEW)
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                browserIntent.setDataAndType(Uri.parse(url), "text/html");
            } else if(url.toString().contains(".pdf")) {
                // PDF file
                browserIntent.setDataAndType(Uri.parse(url), "application/pdf");
            }
            (context as ZActivityDashboard).startActivity(browserIntent)


//            val intent = Intent(Intent.ACTION_VIEW)
//
//            intent.setDataAndType(Uri.parse(mDataList[position].path), "text/html")
//
//            (context as ZActivityDashboard).startActivity(intent)
        }


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
        internal var row_delete: ImageView
        internal lateinit var row_image: CircleImageView
        internal var sub_image:ImageView
        init {
            row_title = itemView.findViewById<View>(R.id.row_title) as TextView
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_date = itemView.findViewById<View>(R.id.row_date) as TextView
            row_default = itemView.findViewById<View>(R.id.row_default) as TextView
            row_image = itemView.findViewById<View>(R.id.row_circular_image) as CircleImageView
            row_layout = itemView.findViewById<View>(R.id.mainlayout) as LinearLayout
            row_delete = itemView.findViewById<View>(R.id.delete_resume) as ImageView
            sub_image = itemView.findViewById<View>(R.id.sub_image) as ImageView
        }
    }
}