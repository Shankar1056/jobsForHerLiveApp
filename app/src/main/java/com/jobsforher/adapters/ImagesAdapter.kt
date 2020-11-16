package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.jobsforher.R
import com.jobsforher.network.retrofithelpers.RetrofitInterface

class ImagesAdapter(private val mDataList: ArrayList<String>, isloggedin:Boolean) : RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var isLoggedIn: Boolean = isloggedin

    private var retrofitInterface: RetrofitInterface? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.images_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (mDataList[position]!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position])
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }


    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var row_icon:ImageView

        init {

            row_icon = itemView.findViewById<View>(R.id.row_icon) as ImageView


            if (isLoggedIn)

            else{
                itemView.setOnClickListener(null)
            }
        }

    }
}