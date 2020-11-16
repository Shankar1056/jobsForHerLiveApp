package com.jobsforher.adapters

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.ZActivityEvents
import com.jobsforher.models.CategoryView
import com.jobsforher.models.CityView
import java.util.*


class ZEventsCitiesAdapter(private val mDataList: ArrayList<CityView>,val compare:ArrayList<String>) : RecyclerView.Adapter<ZEventsCitiesAdapter.MyViewHolder>() {

    private var context: Context? = null
    var index: MyViewHolder? =null
    var arraylist: ArrayList<CategoryView>?=null
    var inflater: LayoutInflater? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.zgroups_category_adapter_row, parent, false)
        this.context = parent.context;
        context = (context as ContextWrapper).baseContext
        inflater = LayoutInflater.from(context);
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.name.text = mDataList[position].name.toString()
        holder.name.setTextColor(Color.parseColor("#BEBEBE"))
        for(i in 0 until compare.size){
            if (compare[i].toString().trim().equals(mDataList[position].name.toString().trim())){
                Log.d("TAGG",compare[i].toString()+"....."+mDataList[position].name.toString().trim())
                holder.name.setTextColor(Color.parseColor("#000000"))
                holder.bullet.setTextColor(Color.parseColor("#000000"))
                holder.checkbox.isChecked = true
                holder.checkbox.setButtonTintList(AppCompatResources.getColorStateList(context!!, R.color.green));

            }
        }

        holder.checkbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {

            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {

                index = holder
                val hexColor = String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor())
                Log.d("TAGG",hexColor)
                if (hexColor.equals("#000000")) {
                    (context as ZActivityEvents).getSelectedCities(0, mDataList[position].name.toString())
                    holder.name.setTextColor(Color.parseColor("#BEBEBE"))
                    holder.bullet.setTextColor(Color.parseColor("#BEBEBE"))
                    holder.checkbox.isChecked = false
                    holder.checkbox.setButtonTintList(
                        AppCompatResources.getColorStateList(
                            context!!,
                            R.color.comments_gray
                        )
                    );
                    Log.d("TAGG", "Inside 2nd block A" +String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor()))
                }
                else{
                    (context as ZActivityEvents).getSelectedCities(mDataList[position].id, mDataList[position].name.toString())
                    holder.name.setTextColor(Color.parseColor("#000000"))
                    holder.bullet.setTextColor(Color.parseColor("#000000"))
                    holder.checkbox.isChecked = true
                    holder.checkbox.setButtonTintList(AppCompatResources.getColorStateList(context!!, R.color.green));

                    Log.d("TAGG", "Inside 2nd block B" +String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor()))
                }
                Log.d("TAGG", "Inside 2nd block")
            }
        }
        )

        holder.name.setOnClickListener {

            //            index = holder
//                val hexColor = String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor())
//                Log.d("TAGG",hexColor)
//                if (hexColor.equals("#00FF00")) {
//                    (context as ZActivityEvents).getSelectedCities(0, mDataList[position].name.toString())
//                    holder.name.setTextColor(Color.parseColor("#BEBEBE"))
//                    holder.bullet.setTextColor(Color.parseColor("#BEBEBE"))
//                    Log.d("TAGG", "Inside 2nd block A" +String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor()))
//                }
//                else{
//                    (context as ZActivityEvents).getSelectedCities(mDataList[position].id, mDataList[position].name.toString())
//                    holder.name.setTextColor(Color.parseColor("#00FF00"))
//                    holder.bullet.setTextColor(Color.parseColor("#00FF00"))
//                    Log.d("TAGG", "Inside 2nd block B" +String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor()))
//                }
//                Log.d("TAGG", "Inside 2nd block")
        }

//        if (index == holder){
//            //holder.name.setTextColor(Color.parseColor("#00FF00"))
//        }
//        else{
//            holder.name.setTextColor(Color.parseColor("#BEBEBE"))
//            holder.bullet.setTextColor(Color.parseColor("#BEBEBE"))
//        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal  var name: TextView
        internal  var bullet: TextView
        internal var checkbox: CheckBox

        init {
            name = itemView.findViewById<View>(R.id.categoryname) as TextView
            bullet = itemView.findViewById<View>(R.id.bullet) as TextView
            checkbox = itemView.findViewById<View>(R.id.check) as CheckBox
        }
    }


//    fun filter(charText: String) {
//        var charText = charText
//        charText = charText.toLowerCase(Locale.getDefault())
//        Log.d("TAGG", charText)
//
//        mDataList!!.clear()
//        if (charText.length == 0) {
//            mDataList.addAll(arraylist!!)
//        } else {
//            for (wp in arraylist!!) {
//                Log.d("TAGG", "ARRAYLIST DATA")
//                if (wp.name!!.toLowerCase(Locale.getDefault()).contains(charText)) {
//                    mDataList.add(wp)
//                    Log.d("TAGG", "ADDED"+wp.name)
//                }
//            }
//            Log.d("TAGG", "SIZE OF DATA: "+mDataList.size)
//        }
//        notifyDataSetChanged()
//    }
}