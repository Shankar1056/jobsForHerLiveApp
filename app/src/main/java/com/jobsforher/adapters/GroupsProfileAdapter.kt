package com.jobsforher.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.jobsforher.R
import com.jobsforher.activities.ZActivityGroupsDetails
import com.jobsforher.models.GroupsView

class GroupsProfileAdapter(private val mDataList: ArrayList<GroupsView>) : RecyclerView.Adapter<GroupsProfileAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mygroupsprofile_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_name.text = mDataList[position].label

        if (mDataList[position].icon_url!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].icon_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }



        holder.llCard.setOnClickListener {

//            val intent = Intent(context, ZActivityGroupsDetails::class.java)
//            intent.putExtra("isLoggedIn", true)
//            intent.putExtra("group_id", mDataList[position].id)
//            intent.putExtra("group_type", mDataList[position].groupType)
//            intent.putExtra("page","Profile")
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            intent.putExtra("isMygroup", 1)
//            context!!.startActivity(intent)
            val bundle = Bundle()
            val intent = Intent(context, ZActivityGroupsDetails::class.java)
            bundle.putBoolean("isLoggedIn", true)
            bundle.putString("group_id", mDataList[position].id.toString())
            bundle.putString("group_type", mDataList[position].groupType)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            bundle.putString("page","Profile")
            bundle.putInt("isMygroup", 1)
            intent.putExtras(bundle)
            context!!.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_name: TextView

        internal var row_icon:ImageView


        internal var llCard: LinearLayout

        init {
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_icon = itemView.findViewById<View>(R.id.row_icon) as ImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout


        }

    }
}