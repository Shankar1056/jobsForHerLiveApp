package com.jobsforher.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.*
import com.jobsforher.models.NotificationModel
import com.squareup.picasso.Picasso

class NotificationAdapter(private val mDataList: ArrayList<NotificationModel>) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notifications_row, parent, false)
//        mygroups_row
        Log.d("TAGG", " SIZE is "+mDataList.size)
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_name.text = mDataList[position].entity_type?.capitalize()
        holder.row_noOfMembers.text = mDataList[position].notification_str

        holder.row_time.text = mDataList[position].created_on_str

        if (mDataList[position].icon!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .fit()
                .into(holder.job_icon)
            //holder.row_icon.setBackgroundResource(R.drawable.curved_grey_border_filled)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.ic_default_profile_icon)
                .placeholder(R.drawable.ic_default_profile_icon)
                .into(holder.job_icon)
        }

        var notification_type:String = ""
        var entity_type:String = ""


        holder.llCard.setOnClickListener {

            notification_type = mDataList[position].notification_type.toString()
            entity_type = mDataList[position].entity_type.toString()
            Log.d("TAGG","Entity"+entity_type)

//            if(notification_type.equals("group_details")) {
//                /*val bundle1 = Bundle()
//                val intent = Intent(context, ZActivityGroupsDetails::class.java)
//                bundle1.putBoolean("isLoggedIn",false)
//                bundle1.putString("group_type", "open")
//                bundle1.putString("group_id", mDataList[position].group_id!!.toString())
//                bundle1.putInt("isMygroup",1)
//                bundle1.putString("page","NewsFeed")
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                intent.putExtras(bundle1)
//                context!!.startActivity(intent)*/
//
//                val intent = Intent(context, DetailsNotificationActivity::class.java)
//                intent.putExtra("post_id", mDataList[position].entity_id.toString())
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                context!!.startActivity(intent)
//
//            }
//
//            else
            if(entity_type.equals("post")){
                val intent = Intent(context, DetailsNotificationActivity::class.java)
                intent.putExtra("post_id", mDataList[position].entity_id.toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context!!.startActivity(intent)
            }

            else if(entity_type.equals("comment")){
                val intent = Intent(context, DetailsNotificationActivity::class.java)
                //intent.putExtra("isLoggedIn",true)
                Log.d("TAGG","Entity id"+mDataList[position].entity_id)
                intent.putExtra("comment_id", mDataList[position].entity_id.toString())
                intent.putExtra("post_id",mDataList[position].post_id.toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context!!.startActivity(intent)
            }

            else if(entity_type.equals("reply")){
                val intent = Intent(context, DetailsNotificationActivity::class.java)
                //intent.putExtra("isLoggedIn",true)
                intent.putExtra("reply_id", mDataList[position].entity_id.toString())
                intent.putExtra("post_id", mDataList[position].post_id.toString())
                intent.putExtra("comment_id", mDataList[position].comment_id.toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context!!.startActivity(intent)
            }


            else if(notification_type.equals("job_details")){
                val intent = Intent(context, ZActivityJobDetails::class.java)
                intent.putExtra("isLoggedIn",true)
                intent.putExtra("group_Id", mDataList[position].entity_id.toString())
                intent.putExtra("title", "")
                intent.putExtra("isMygroup",1)
                intent.putExtra("page","NewsFeed")
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context!!.startActivity(intent)
            }

            else if(notification_type.equals("company_details")){
                val intent = Intent(context, ZActivityCompanyDetails::class.java)
                intent.putExtra("isLoggedIn",true)
                intent.putExtra("group_Id", mDataList[position].entity_id.toString())
                intent.putExtra("title", "")
                intent.putExtra("isMygroup",1)
                intent.putExtra("page","NewsFeed")
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context!!.startActivity(intent)
            }

            else if(notification_type.equals("events_details")){
                val intent = Intent(context, ZActivityEventDetails::class.java)
                intent.putExtra("isLoggedIn",true)
                intent.putExtra("group_Id", mDataList[position].entity_id.toString())
                intent.putExtra("title", "")
                intent.putExtra("isMygroup",1)
                intent.putExtra("page","NewsFeed")
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context!!.startActivity(intent)
            }
        }



    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_name: TextView
        internal var row_time: TextView
        internal var row_noOfMembers: TextView
        internal var job_icon:ImageView

        internal var llCard: LinearLayout

        init {
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_time = itemView.findViewById<View>(R.id.row_time) as TextView
            row_noOfMembers = itemView.findViewById<View>(R.id.row_noOfMembers) as TextView
            job_icon = itemView.findViewById<View>(R.id.row_icon) as ImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

//
        }

    }
}