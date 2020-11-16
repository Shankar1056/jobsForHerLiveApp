package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import kotlin.collections.ArrayList

class JobsAdsAdapter(private val mDataList: ArrayList<String>, isloggedin:Boolean, type:Int, filter:Int) : RecyclerView.Adapter<JobsAdsAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var isLoggedIn: Boolean = isloggedin
    private var type: Int= type
    private var filter: Int= filter
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.jobsads_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_name.text = mDataList[position].toString()

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

            if (isLoggedIn)

            else{
                itemView.setOnClickListener(null)
            }
        }

    }
}