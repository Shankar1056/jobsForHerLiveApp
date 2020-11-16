package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import com.jobsforher.R
import com.jobsforher.activities.ZActivityGroups
import com.jobsforher.models.CategoriesMainView

class GroupCategoryAdapter(private val mDataList: ArrayList<CategoriesMainView>) : RecyclerView.Adapter<GroupCategoryAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.groups_category_adapter_row, parent, false)

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
            (context as ZActivityGroups).loadFilteredData("","",mDataList[position].id.toString())
        }

    }


    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_text: TextView
        internal  var row_layout: LinearLayout
        internal lateinit var row_image: CircleImageView
        init {
            row_text = itemView.findViewById<View>(R.id.row_text) as TextView
            row_image = itemView.findViewById<View>(R.id.row_circular_image) as CircleImageView
            row_layout = itemView.findViewById<View>(R.id.mainlayout) as LinearLayout
        }
    }
}