package com.jobsforher.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.ZActivityEventDetails
import com.jobsforher.activities.ZActivityEvents
import com.jobsforher.models.EventsView
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter(private val mDataList: ArrayList<EventsView>, isloggedin:Boolean, type:Int, filter:Int,
                    val compareList: ArrayList<Int>, isRec:Int,page:String) : RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var isLoggedIn: Boolean = isloggedin
    private var type: Int= type
    private var filter: Int= filter
    private var retrofitInterface: RetrofitInterface? = null
    private var page = page
    private var isRecommended: Int = isRec

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.events_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Log.d("TAGG","COMAPRE LIST"+compareList.size)
        holder.row_name.text = mDataList[position].title
        if(mDataList[position].event_category!!.length>0)
            holder.row_noOfMembers.text = mDataList[position].event_category
        else
            holder.row_noOfMembers.visibility  =View.GONE


        if(mDataList[position].link_companies_name!!.size>0) {
            var s: String = ""
            for (i in 0 until mDataList[position].link_companies_name!!.size) {
                s = s + mDataList[position].link_companies_name?.get(i)!!.company_name!!+","
            }
            Log.d("TAGG","DATA COMP ADAP "+s)
            s = s.substring(0,s.length-1)
            holder.row_comp.visibility = View.VISIBLE
            holder.row_comp.text = s
        }
        else
            holder.row_comp.visibility = View.GONE


//        holder.row_description.text = getTime(mDataList[position].event_start_date_time!!)+" to "+
//                getTime(mDataList[position].event_end_date_time!!)
        val drawable: Drawable
        if(mDataList[position].event_start_date_time!!.length>0){
            drawable = context!!.resources.getDrawable(R.drawable.ic_clockk);
            val  msg: String="  "+ getTime(mDataList[position].event_start_date_time!!)+" to "+
                    getTime(mDataList[position].event_end_date_time!!)
            drawable.setBounds(0, 0, 40, 40);
            val mImageSpan = ImageSpan(drawable)
            //val mImageSpan: ImageSpan = ImageSpan(context, R.drawable.ic_clockk);
            val text: SpannableString = SpannableString(msg);
            text.setSpan(mImageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.row_description.setText(text);
        }
        else
            holder.row_description.visibility = View.GONE

        if(mDataList[position].event_locations!!.size>0)
            holder.row_location.text = mDataList[position].event_locations!![0].city!!
        else
            holder.row_location.text = "Bangalore"

        holder.row_label.text  =getDate(mDataList[position].event_start_date_time!!)+" to "+
                getDate(mDataList[position].event_end_date_time!!)

        Log.d("TAGG", "REG"+ mDataList[position].registered)

        if(mDataList[position].ticket_type.equals("free")) {
            holder.price.text = "FREE"
            holder.price_disc.visibility = View.GONE
        }
        else {
//            if (compare(mDataList[position].discount_start_date_time.toString()) == false &&
//                compare(mDataList[position].discount_end_date_time.toString()) == true
//            ) {
            if(mDataList[position].price_before_discount==0) {
                holder.price.text = "OTHER"
                holder.price_disc.visibility = View.GONE
            }
            else {
                if(mDataList[position].price_after_discount ==0)
                    holder.price.text = "\u20B9" + mDataList[position].price_before_discount.toString()
                else
                    holder.price.text = "\u20B9" + mDataList[position].price_after_discount.toString()

                holder.price_disc.text = "\u20B9" + mDataList[position].price_before_discount.toString()
                holder.price_disc.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
                if(mDataList[position].price_after_discount ==0)
                    holder.price_disc.visibility = View.GONE

            }
//            } else
//                holder.price.text = mDataList[position].price_before_discount.toString()
        }


        if (mDataList[position].featured_event!!){
            val  msg: String=mDataList[position].title+"   "
            val mImageSpan: ImageSpan = ImageSpan(context!!, R.drawable.ic_group_featured);
            val text: SpannableString = SpannableString(msg);
            text.setSpan(mImageSpan, msg.length - 1, msg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.row_name.setText(text);
            //holder.job_icon.visibility = View.GONE
        }
        else{
            //holder.job_icon.visibility = View.GONE
        }



//        if (mDataList[position].categories!!.size>0){
//            for (i in 0 until mDataList[position].categories!!.size){
//                stringBuilder = stringBuilder+" "+mDataList[position].categories!!.get(i).category
//                Log.d("TAGG", stringBuilder)
//            }
//            holder.row_label.text = stringBuilder
//        }else{
//
//            holder.row_label.text = ""
//        }

        if (mDataList[position].thumbnail_url!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].thumbnail_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.event_default)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }

        Log.d("TAGG","ITEREST"+mDataList[position].interested)
        if(mDataList[position].interested==true){
            holder.interested.setTextColor(Color.parseColor("#B2B2B2"))
            holder.interested.isEnabled = false
        }
        else {
            holder.interested.setTextColor(Color.parseColor("#99CA3B"))
            holder.interested.isEnabled = true
        }

        if (filter==0) {
            if (compareList.size==0){
                holder.btnApply.visibility = View.VISIBLE
                holder.btnApplied.visibility = View.GONE
            }
            else{
                for (k in 0 until compareList.size) {
                    Log.d("TAGG","COMPARE"+ compareList[k]+"  "+mDataList[position].id)
                    if (compareList[k]== mDataList[position].id) {
                        holder.btnApply.visibility = View.GONE
                        holder.btnApplied.visibility = View.VISIBLE
                        holder.interested.setTextColor(Color.parseColor("#B2B2B2"))
                        holder.interested.isEnabled = false
                        break
                    } else {
                        holder.btnApply.visibility = View.VISIBLE
                        holder.btnApplied.visibility = View.GONE
                    }
                }
            }
//            if (isLoggedIn && type == 1) {
//                holder.btnJoinGroup.visibility = View.GONE
//                holder.btnJoined.visibility = View.VISIBLE
//
//            } else if (type > 1 && isLoggedIn && mDataListCompare.size > 0) {
//

//                for (k in 0 until compareList.size) {
//                    Log.d("TAGG","COMPARE"+ compareList[k]+"  "+mDataList[position].id)
//                    if (compareList[k]== mDataList[position].id) {
//                        holder.btnApply.visibility = View.GONE
//                        holder.btnApplied.visibility = View.VISIBLE
//                        break
//                    } else {
//                        holder.btnApply.visibility = View.VISIBLE
//                        holder.btnApplied.visibility = View.GONE
//                    }
//                }
//            } else {
//                holder.btnApply.visibility = View.VISIBLE
//                holder.btnApplied.visibility = View.GONE
//            }
        }
        else{
            holder.btnApply.visibility = View.VISIBLE
            holder.btnApplied.visibility = View.GONE
//            for (k in 0 until mDataListCompare.size) {
//                if (mDataListCompare[k].id == mDataList[position].id) {
//                    holder.btnJoinGroup.visibility = View.GONE
//                    holder.btnJoined.visibility = View.VISIBLE
//                    break
//                }
//            }
        }


        if(mDataList[position].event_register_end_date_time!!.length>0) {
            if (compare(mDataList[position].event_register_end_date_time.toString())) {
                holder.btnApply.visibility = View.GONE
                holder.btnApplied.visibility = View.GONE
                holder.registration_closed.visibility = View.VISIBLE
                if(type==2)
                    holder.interested.visibility = View.GONE
            }
        }

        holder.interested.setOnClickListener {
            holder.interested.isEnabled = false
            if(isRecommended == 1 && page.equals("Dashboard")){
                val intent = Intent(context, ZActivityEventDetails::class.java)
                intent.putExtra("isLoggedIn", true)
                intent.putExtra("group_Id", mDataList[position].id)
                intent.putExtra("title", mDataList[position].title)
                intent.putExtra("isMygroup", 0)
                intent.putExtra("type",type)
                intent.putExtra("page",page)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context!!.startActivity(intent)
            }
            else {
                (context as ZActivityEvents).addInterest(mDataList[position].id!!, holder.interested)
            }
        }


        holder.llCard.setOnClickListener {

            if (isLoggedIn && type==1) {

                val intent = Intent(context, ZActivityEventDetails::class.java)
                intent.putExtra("isLoggedIn", true)
                intent.putExtra("group_Id", mDataList[position].id)
                intent.putExtra("title", mDataList[position].title)
                intent.putExtra("type",type)
                intent.putExtra("page",page)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                if (holder.btnApply.visibility == View.VISIBLE)
                    intent.putExtra("isMygroup", 0)
                else
                    intent.putExtra("isMygroup", 1)
                context!!.startActivity(intent)
            }
            else if(type>1 && isLoggedIn){
                if (holder.btnApply.visibility == View.VISIBLE){
                    val intent = Intent(context, ZActivityEventDetails::class.java)
                    intent.putExtra("isLoggedIn",false)
                    intent.putExtra("type",type)
                    intent.putExtra("group_Id", mDataList[position].id)
                    intent.putExtra("title", mDataList[position].title)
                    intent.putExtra("isMygroup",0)
                    intent.putExtra("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context!!.startActivity(intent)
                }
                else{
                    val intent = Intent(context, ZActivityEventDetails::class.java)
                    intent.putExtra("isLoggedIn", true)
                    intent.putExtra("group_Id", mDataList[position].id)
                    intent.putExtra("title", mDataList[position].title)
                    intent.putExtra("isMygroup", 1)
                    intent.putExtra("type",type)
                    intent.putExtra("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context!!.startActivity(intent)
                }
            }
            else{
                val intent = Intent(context, ZActivityEventDetails::class.java)
                intent.putExtra("isLoggedIn",false)
                intent.putExtra("group_Id", mDataList[position].id)
                intent.putExtra("title", mDataList[position].title)
                intent.putExtra("isMygroup",0)
                intent.putExtra("type",type)
                intent.putExtra("page",page)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context!!.startActivity(intent)
            }
        }



//        holder.btnJoined.setOnClickListener {
//
//            retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
//
//            val call = retrofitInterface!!.ReportCheck(mDataList[position].id, EndPoints.CLIENT_ID, "Bearer "+ EndPoints.ACCESS_TOKEN)
//
//            call.enqueue(object : Callback<DeletePostResponse> {
//                override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {
//
//                    Logger.d("CODE", response.code().toString() + "")
//                    Logger.d("MESSAGE", response.message() + "")
//                    Logger.d("URL", "" + response)
//                    Logger.d("TAGG","CODE IS "+response.body()!!.responseCode.toString())
//                    Logger.d("RESPONSE join group", "" + Gson().toJson(response))
//
//                    if (response.isSuccessful && response.body()!!.responseCode.toString().equals("10802") ) {      //11804
//                        val popupMenu: PopupMenu = PopupMenu(context,holder.btnJoined)
//                        popupMenu.menuInflater.inflate(R.menu.group_popup_menu1,popupMenu.menu)
//                        val positionOfMenuItem = 1 // or whatever...
//                        val item = popupMenu.menu.getItem(positionOfMenuItem)
//                        val s = SpannableString("Reported")
//                        s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, 0)
//                        item.setTitle(s)
//                        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
//                            when(item.itemId) {
//                                R.id.action_leave -> {
//                                    val builder = AlertDialog.Builder(context)
//                                    builder.setTitle("Leave group")
//                                    builder.setMessage("Are you sure you want to leave this group?")
//                                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
//                                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//                                        (context as ZActivityGroups).leaveGroup(
//                                            mDataList[position].id,
//                                            holder.btnJoinGroup,
//                                            holder.btnJoined
//                                        )
//                                    }
//                                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                                        dialog.cancel()
//                                    }
//                                    builder.show()
//                                }
//                                R.id.action_report ->
//                                    (context as ZActivityGroups).openBottomSheetReports(mDataList[position].id,"group")
//                                R.id.action_reported ->
//                                    Toast.makeText(context,"Already Reported", Toast.LENGTH_LONG).show()
//                            }
//                            true
//                        })
//                        popupMenu.show()
//
//                    } else {
//                        val popupMenu: PopupMenu = PopupMenu(context, holder.btnJoined)
//                        popupMenu.menuInflater.inflate(R.menu.group_popup_menu,popupMenu.menu)
//
//                        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
//                            when(item.itemId) {
//                                R.id.action_leave -> {
//                                    val builder = AlertDialog.Builder(context)
//                                    builder.setTitle("Leave group")
//                                    builder.setMessage("Are you sure you want to leave this group?")
//                                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
//                                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//                                        (context as ZActivityGroups).leaveGroup(
//                                            mDataList[position].id,
//                                            holder.btnJoinGroup,
//                                            holder.btnJoined
//                                        )
//
//                                    }
//                                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                                        dialog.cancel()
//                                    }
//                                    builder.show()
//                                }
//                                R.id.action_report ->
//                                    (context as ZActivityGroups).openBottomSheetReports(mDataList[position].id,"group")
//                                R.id.action_reported ->
//                                    Toast.makeText(context,"Already Reported", Toast.LENGTH_LONG).show()
//                            }
//                            true
//                        })
//                        popupMenu.show()
//                    }
//                }
//
//                override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
//
//                    Logger.d("TAGG", "FAILED : $t")
//                }
//            })
//        }
//
        holder.btnApply.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Event Register")
            builder.setMessage("Are you sure you want to register for this event?")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                if(isRecommended == 1 && page.equals("Dashboard")){
                    val intent = Intent(context, ZActivityEventDetails::class.java)
                    intent.putExtra("isLoggedIn", true)
                    intent.putExtra("group_Id", mDataList[position].id)
                    intent.putExtra("title", mDataList[position].title)
                    intent.putExtra("isMygroup", 0)
                    intent.putExtra("type",type)
                    intent.putExtra("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context!!.startActivity(intent)
                }
                else if(page.equals("Company Details")){
                    val intent = Intent(context, ZActivityEventDetails::class.java)
                    intent.putExtra("isLoggedIn", true)
                    intent.putExtra("group_Id", mDataList[position].id)
                    intent.putExtra("title", mDataList[position].title)
                    intent.putExtra("isMygroup", 0)
                    intent.putExtra("type",type)
                    intent.putExtra("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context!!.startActivity(intent)
                }
                else{
                    (context as ZActivityEvents).getListOfLocations(mDataList[position].id!!,
                        holder.btnApply,
                        mDataList[position]!!.title.toString(),holder.btnApplied, mDataList[position]!!.resume_required!!,
                        mDataList[position]!!.preference_required!!,mDataList[position].ticket_type!!)
                }


                //(context as ZActivityEvents).callpay();
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                dialog.cancel()
            }
            builder.show()
        }

        holder.hotjobs_share.setOnClickListener {

            val s:String = mDataList[position].title!!.replace(" ","-")
            val s1:String = s.replace("_","-")

            val intent = Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.putExtra(Intent.EXTRA_SUBJECT, mDataList[position].title.toString()+"| JobsForHer");
            intent.putExtra(Intent.EXTRA_TEXT, "Click on the link \n http://www.workingnaari.in/events/"+s1.toLowerCase()+"/"+mDataList[position].id+"\n\n"+
                    "Application Link : https://play.google.com/store/apps/details?id=${context!!.getPackageName()}");
            //intent.putExtra(Intent.EXTRA_TEXT, "Application Link : https://play.google.com/store/apps/details?id=${context.getPackageName()}")
            context!!.startActivity(Intent.createChooser(intent, "Share Job link!"));
        }
    }


    fun compare(date:String):Boolean{
        val sdformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateobj = Date()
        val d1 = sdformat.parse(date)                           //event date
        val d2 = sdformat.parse(sdformat.format(dateobj))       //current date

        if (d1.compareTo(d2) > 0) {
            Log.d("TAGG","Date 1 occurs after Date 2")
            return false
        } else if (d1.compareTo(d2) < 0) {
            Log.d("TAGG","Date 1 occurs before Date 2")
            return true
        } else {
            Log.d("TAGG","Both dates are equal")
            return false
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

        } catch ( e:ParseException) {
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

        } catch ( e:ParseException) {
            e.printStackTrace()
        }
        return s
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_name: TextView
        internal var row_label: TextView
        internal var row_noOfMembers: TextView
        internal var row_description:TextView
        internal var row_location: TextView
        internal var row_icon:ImageView
        internal var btnApply: Button
        internal var btnApplied: Button
        internal var job_icon:ImageView
        internal var price:TextView
        internal var price_disc:TextView
        internal var interested:TextView
        internal var registration_closed:TextView
        internal var hotjobs_share: ImageView
        internal var row_comp:TextView


        internal var llCard: LinearLayout

        init {
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_label = itemView.findViewById<View>(R.id.row_label) as TextView
            row_noOfMembers = itemView.findViewById<View>(R.id.row_noOfMembers) as TextView
            row_comp = itemView.findViewById<View>(R.id.row_comp) as TextView
            row_description = itemView.findViewById<View>(R.id.row_description) as TextView
            row_location = itemView.findViewById<View>(R.id.row_location) as TextView
            btnApplied = itemView.findViewById<View>(R.id.btnApplied) as Button
            btnApply = itemView.findViewById<View>(R.id.apply_job) as Button
            row_icon = itemView.findViewById<View>(R.id.row_icon) as ImageView
            job_icon = itemView.findViewById<View>(R.id.hotjobs_icon) as ImageView
            price = itemView.findViewById<View>(R.id.row_price) as TextView
            price_disc = itemView.findViewById<View>(R.id.row_price_disc) as TextView
            interested = itemView.findViewById<View>(R.id.row_interested) as TextView
            registration_closed = itemView.findViewById<View>(R.id.row_registration_closed) as TextView
            hotjobs_share = itemView.findViewById<View>(R.id.hotjobs_share) as ImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

            if (isLoggedIn)

            else{
                itemView.setOnClickListener(null)
            }
        }

    }
}