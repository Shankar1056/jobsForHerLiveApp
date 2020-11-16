package com.android.jobsforher.activities

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.ZActivityGroups
import com.jobsforher.models.CityView

class ZGroupsCitiesAdapter(private val mDataList: ArrayList<CityView>) : RecyclerView.Adapter<ZGroupsCitiesAdapter.MyViewHolder>() {

    private var context: Context? = null
    var index: ZGroupsCitiesAdapter.MyViewHolder? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.zgroups_category_adapter_row, parent, false)
        this.context = parent.context;
        context = (context as ContextWrapper).baseContext
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        index = holder
        holder.name.text = mDataList[position].name.toString()
        holder.name.setOnClickListener {
            if (index!=holder)
                (context as ZActivityGroups).getSelectedCities(mDataList[position].id, mDataList[position].name!!)
            else if(index==holder){
                (context as ZActivityGroups).getSelectedCities(0, "")
                index!!.name.setTextColor(Color.GRAY)
            }
            holder.name.setTextColor(Color.GREEN)
            if(index != null)
            {
                index!!.name.setTextColor(Color.GRAY)

            }
            index = holder
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal  var name: TextView

        init {
            name = itemView.findViewById<View>(R.id.categoryname) as TextView
        }
    }
}