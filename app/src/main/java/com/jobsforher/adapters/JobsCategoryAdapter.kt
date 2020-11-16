package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.jobsforher.R
import com.jobsforher.activities.ZActivityJobs
import com.jobsforher.models.CategoriesMainView

class JobsCategoryAdapter(private val mDataList: ArrayList<CategoriesMainView>) : RecyclerView.Adapter<JobsCategoryAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.jobs_category_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_text.text = mDataList[position].name

        if (mDataList[position].image_url!!.length>0) {

            Picasso.with(context)
                .load(mDataList[position].image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_image)

        }else{

            Picasso.with(context)
                .load(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_image)
        }

        holder.row_layout.setOnClickListener {
            (context as ZActivityJobs).loadFilteredData("",mDataList[position].name.toString(),"","",""
                ,"","","","","")
        }

    }


    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_text: TextView
        internal  var row_layout: FrameLayout
        internal lateinit var row_image: ImageView
        init {
            row_text = itemView.findViewById<View>(R.id.row_text) as TextView
            row_image = itemView.findViewById<View>(R.id.imageView1) as ImageView
            row_layout = itemView.findViewById<View>(R.id.mainlayout) as FrameLayout
        }
    }
}