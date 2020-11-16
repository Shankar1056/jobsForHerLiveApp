package com.jobsforher.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import com.jobsforher.R
import com.jobsforher.activities.WebActivity
import com.jobsforher.models.EventLocation
import java.text.ParseException
import kotlin.collections.ArrayList

class EventLocation1Adapter(private val mDataList: ArrayList<EventLocation>) : RecyclerView.Adapter<EventLocation1Adapter.MyViewHolder>() {

    private var context: Context? = null
    var index: MyViewHolder? =null
    private var mSelectedPosition = -1



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.eventlocation_adapter_row1, parent, false)

        this.context = parent.context
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_title.text = mDataList[position].city
        holder.row_date.text = getDate(mDataList[position].event_start_date_time!!)+" to "+getDate(mDataList[position].event_end_date_time!!)
        holder.row_time.text = getTime(mDataList[position].event_start_date_time!!)+" to "+
                getTime(mDataList[position].event_end_date_time!!)

        var add:String = ""
        var city:String = ""
        var ststa:String =""
        if(mDataList[position].address!!.length>0) {
            add = mDataList[position].address!!
            if(mDataList[position].city!!.length>0 || mDataList[position].state!!.length>0){
                add = add+","
            }
        }
        if(mDataList[position].city!!.length>0) {
            city = mDataList[position].city!!
            if(mDataList[position].state!!.length>0){
                city = city+","
            }
        }
        if(mDataList[position].state!!.length>0)
            ststa = mDataList[position].state!!


//        holder.row_location.text = add+city+ststa
        val  msg: String="  "+add+city+ststa
        val mImageSpan: ImageSpan = ImageSpan(context!!, R.drawable.ic_location);
        val text: SpannableString = SpannableString(msg);
        text.setSpan(mImageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.row_location.setText(text);
        if(mDataList[position].amount==0) {
            holder.row_amount.text = "FREE"
            holder.row_amount1.visibility = View.GONE
        }
        else{
            if(mDataList[position].discounted_price==0) {
                holder.row_amount1.visibility = View.GONE
                holder.row_amount.text = "\u20B9" + mDataList[position].amount.toString()
            }
            else{
                holder.row_amount.setText("\u20B9"+mDataList[position].discounted_price.toString()+ " Onwards")
                holder.row_amount1.text = "\u20B9"+mDataList[position].amount.toString()
                holder.row_amount1.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            }
        }

//        if(mDataList[position].)
//        holder.row_text.text = ""
        if(mDataList[position].google_map_url!!.length>0){
            val  msg: String="  "+" "+ Html.fromHtml("<font color='#3FA9E6'><u>"+mDataList[position].google_map_url!!.trim()+"</u></font>")
            val mImageSpan: ImageSpan = ImageSpan(context!!, R.drawable.ic_location);
            val text: SpannableString = SpannableString(msg);
            text.setSpan(mImageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.row_google.setText(text);
            holder.row_google.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
        }
        else
            holder.row_google.visibility = View.GONE

        holder.row_google.setOnClickListener {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("value",mDataList[position].google_map_url)
            context!!.startActivity(intent)
        }


    }

    fun getTime(times:String):String{
        val formatter1:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        val dateInString:String = times
        val formatterOut:SimpleDateFormat = SimpleDateFormat("hh:mm a");
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
        internal var row_date: TextView
        internal var row_time : TextView
        internal var row_amount:TextView
        internal var row_amount1:TextView
        internal var row_text:TextView
        internal var row_location: TextView
        internal var row_google: TextView
        internal var llCard: LinearLayout

        init {
            row_title = itemView.findViewById<View>(R.id.row_title) as TextView
            row_date = itemView.findViewById<View>(R.id.row_date) as TextView
            row_time = itemView.findViewById<View>(R.id.row_time) as TextView
            row_location = itemView.findViewById<View>(R.id.row_location) as TextView
            row_amount = itemView.findViewById<View>(R.id.row_amount) as TextView
            row_amount1 = itemView.findViewById<View>(R.id.row_amount1) as TextView
            row_text = itemView.findViewById<View>(R.id.row_text) as TextView
            row_google = itemView.findViewById<View>(R.id.row_google) as TextView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

        }
    }
}