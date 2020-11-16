package com.jobsforher.adapters

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import com.jobsforher.R
import com.jobsforher.activities.ZActivityEventDetails
import com.jobsforher.activities.ZActivityEvents
import com.jobsforher.models.EventLocation
import java.text.ParseException
import kotlin.collections.ArrayList

class EventLocationAdapter(private val diag:Dialog,private val mDataList: ArrayList<EventLocation>, id:Int, btnJoinGroup: Button,
                           title:String, btnJoined:Button, resumeRequired:Boolean, prefRequired:Boolean,page:String) : RecyclerView.Adapter<EventLocationAdapter.MyViewHolder>() {

    private var context: Context? = null
    var index: MyViewHolder? =null
    private var mSelectedPosition = -1
    var id = id
    var btnJoinGroup = btnJoinGroup
    var btnJoined = btnJoined
    var title = title
    var resumeRequired = resumeRequired
    var prefRequired = prefRequired
    var page = page
    var dialog = diag


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.eventlocation_adapter_row, parent, false)

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

        var msg:String ="  "+add+city+ststa
        var mImageSpan: ImageSpan= ImageSpan(context!!, R.drawable.ic_location);
        var text : SpannableString = SpannableString(msg);
        text.setSpan(mImageSpan, 0, 1, 0);
        holder.row_location.text = text

        holder.arrow.setOnClickListener {
            holder.llCard.callOnClick()
        }

        holder.llCard.setOnClickListener {
            // if(resumeRequired) {
            diag.cancel()
            callme(position)
            //}
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


    fun callme(position: Int){
        context = (context as ContextWrapper).getBaseContext()
        if(page.equals("Events")) {
            (context as ZActivityEvents).applyEvents(
                mDataList[position].city!!, mDataList[position].event_id!!,
                mDataList[position].id!!, btnJoinGroup, title, btnJoined, resumeRequired, prefRequired,mDataList[position].external_url!!
            )
        }
        else{
            (context as ZActivityEventDetails).applyEvents(
                mDataList[position].city!!, mDataList[position].event_id!!,
                mDataList[position].id!!, btnJoinGroup, title, btnJoined, resumeRequired, prefRequired,mDataList[position].external_url!!
            )
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_title: TextView
        internal var row_date: TextView
        internal var row_time : TextView
        internal var row_location: TextView
        internal var arrow:ImageView
        internal var llCard: LinearLayout

        init {
            row_title = itemView.findViewById<View>(R.id.row_title) as TextView
            row_date = itemView.findViewById<View>(R.id.row_date) as TextView
            row_time = itemView.findViewById<View>(R.id.row_time) as TextView
            row_location = itemView.findViewById<View>(R.id.row_location) as TextView
            arrow = itemView.findViewById<View>(R.id.offer_select) as ImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

        }
    }
}