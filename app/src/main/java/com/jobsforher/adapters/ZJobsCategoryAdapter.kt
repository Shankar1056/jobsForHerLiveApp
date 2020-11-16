package com.jobsforher.adapters

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.ZActivityJobs
import com.jobsforher.models.CategoryView
import java.util.*


class ZJobsCategoryAdapter(private val mDataList: ArrayList<CategoryView>) : RecyclerView.Adapter<ZJobsCategoryAdapter.MyViewHolder>(), Filterable {

    internal var mfilter: NewFilter
    override fun getFilter(): Filter {
        return mfilter
    }

    init {
        mfilter = NewFilter(this@ZJobsCategoryAdapter)
    }

    inner class NewFilter(var mAdapter: ZJobsCategoryAdapter) : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            arraylist!!.clear()
            val results = FilterResults()
            if (charSequence.length == 0) {
                arraylist!!.addAll(arraylist!!)
            } else {
                val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                for (i in 0 until  arraylist!!.size) {
                    val userInfo: CategoryView = arraylist!!.get(i)
                    if (userInfo.name!!.toLowerCase().startsWith(filterPattern)) {
                        arraylist!!.add(userInfo)
                    }
                }
            }
            results.values = arraylist

            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            this.mAdapter.notifyDataSetChanged()
        }

    }

    private var context: Context? = null
    var index: MyViewHolder? =null
    var arraylist: ArrayList<CategoryView>?=null
    var inflater: LayoutInflater? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.zgroups_category_adapter_row, parent, false)
        this.context = parent.context;
        context = (context as ContextWrapper).baseContext
        arraylist= mDataList
        inflater = LayoutInflater.from(context);
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.name.text = mDataList[position].name.toString()
        holder.name.setTextColor(Color.parseColor("#BEBEBE"))

        holder.name.setOnClickListener {

            index = holder
            val hexColor = String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor())
            Log.d("TAGG",hexColor)
            if (hexColor.equals("#00FF00")) {
                (context as ZActivityJobs).getSelectedCategory(0, "")
                holder.name.setTextColor(Color.parseColor("#BEBEBE"))
                holder.bullet.setTextColor(Color.parseColor("#BEBEBE"))
                Log.d("TAGG", "Inside 2nd block A" +String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor()))
            }
            else{
                (context as ZActivityJobs).getSelectedCategory(mDataList[position].id, mDataList[position].name.toString())
                holder.name.setTextColor(Color.parseColor("#00FF00"))
                holder.bullet.setTextColor(Color.parseColor("#00FF00"))
                Log.d("TAGG", "Inside 2nd block B" +String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor()))
            }
            Log.d("TAGG", "Inside 2nd block")
        }

        if (index == holder){
            //holder.name.setTextColor(Color.parseColor("#00FF00"))
        }
        else{
            holder.name.setTextColor(Color.parseColor("#BEBEBE"))
            holder.bullet.setTextColor(Color.parseColor("#BEBEBE"))
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setFilter(newList: List<CategoryView>) {
        arraylist = ArrayList<CategoryView>()
        arraylist!!.addAll(newList)
        notifyDataSetChanged()
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal  var name: TextView
        internal  var bullet: TextView

        init {
            name = itemView.findViewById<View>(R.id.categoryname) as TextView
            bullet = itemView.findViewById<View>(R.id.bullet) as TextView
        }


    }



}