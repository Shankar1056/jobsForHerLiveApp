package com.jobsforher.adapters

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.models.ResumeView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class ResumeAdapter(private val mDataList: ArrayList<ResumeView>) :
    RecyclerView.Adapter<ResumeAdapter.MyViewHolder>() {

    private var context: Context? = null
    var index: MyViewHolder? = null
    private var lastCheckedPos = -1
    var inflater: LayoutInflater? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.resumeview_adapter_row, parent, false)
        this.context = parent.context;
        context = (context as ContextWrapper).baseContext
        inflater = LayoutInflater.from(context);

        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_title.text = mDataList[position].title
        var fileName =
            mDataList[position].path!!.substring(mDataList[position].path!!.lastIndexOf('/') + 1)
        fileName = fileName.substring(fileName.indexOf("_") + 1)
        holder.row_name.text = fileName

        holder.row_default.text = mDataList[position].is_default
        val userDob: Date =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mDataList[position].created_on.toString());
        val today: Date = Date();
        val diff = today.getTime() - userDob.getTime()
        val numOfDays: Long = (diff / (1000 * 60 * 60 * 24));
        holder.row_date.text = userDob.getDate()
            .toString() + "-" + userDob.month.toString() + "-" + userDob.year.toString()
        holder.row_date.text = mDataList[position].created_on.toString()

        if (mDataList[position].path!!.length > 0) {
            Picasso.with(context)
                .load(mDataList[position].path)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_image)
        } else {
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_image)
        }


        if (position == 0) {
            holder.radio.isChecked = true
            lastCheckedPos = 0;
            index = holder
        } else
            holder.radio.isChecked = false




        holder.row_layout.setOnClickListener {

            if (lastCheckedPos != position && lastCheckedPos > -1) {
                Toast.makeText(context, "Please uncheck the previous selection", Toast.LENGTH_LONG)
                    .show()
            } else {
                index = holder
                if (!holder.radio.isChecked) {
                    holder.radio.isChecked = true
                    lastCheckedPos = position
                } else {
                    holder.radio.isChecked = false
                    lastCheckedPos = -1
                }

            }


        }

        if (index == holder) {
            //holder.name.setTextColor(Color.parseColor("#00FF00"))
        } else {
            holder.radio.isChecked = false
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_title: TextView
        internal var row_name: TextView
        internal var row_date: TextView
        internal var row_default: TextView
        internal var row_layout: LinearLayout
        internal lateinit var row_image: CircleImageView
        internal lateinit var radio: RadioButton

        init {
            row_title = itemView.findViewById<View>(R.id.row_title) as TextView
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_date = itemView.findViewById<View>(R.id.row_date) as TextView
            row_default = itemView.findViewById<View>(R.id.row_default) as TextView
            row_image = itemView.findViewById<View>(R.id.row_circular_image) as CircleImageView
            row_layout = itemView.findViewById<View>(R.id.mainlayout) as LinearLayout
            radio = itemView.findViewById<View>(R.id.offer_select) as RadioButton
        }


    }
}