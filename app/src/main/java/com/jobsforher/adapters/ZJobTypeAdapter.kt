package com.android.jobsforher.activities

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.ZActivityJobs
import com.jobsforher.models.JobTypeView

class ZJobTypeAdapter(private val mDataList: ArrayList<JobTypeView>,val compare:ArrayList<String>,val data:ArrayList<String>) : RecyclerView.Adapter<ZJobTypeAdapter.MyViewHolder>() {

    private var context: Context? = null
    var index: ZJobTypeAdapter.MyViewHolder? =null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.zjob_jobtype_adapter_row, parent, false)
        this.context = parent.context;
        context = (context as ContextWrapper).baseContext
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        index = holder
        holder.name.text = mDataList[position].name.toString()
        holder.name.setTextColor(Color.parseColor("#BEBEBE"))
        for(i in 0 until compare.size){

            if (compare[i].toString().trim().equals(data?.get(position)!!.toString().trim())){
                Log.d("TAGG",compare[i].toString()+"....."+data?.get(position)!!.toString().trim())
                holder.name.setTextColor(Color.parseColor("#00FF00"))
                holder.bullet.setTextColor(Color.parseColor("#00FF00"))
                holder.image.setColorFilter(Color.parseColor("#00FF00"), android.graphics.PorterDuff.Mode.SRC_ATOP);
                holder.checkbox.isChecked = true
                holder.checkbox.setButtonTintList(AppCompatResources.getColorStateList(context!!, R.color.green));
            }
        }
        Log.d("TAGG",mDataList[position].image.toString())
        val imageResource = context!!.getResources().getIdentifier(mDataList[position].image.toString(), null, context!!.packageName)
        val res = context!!.getResources().getDrawable(imageResource)
        holder.image.setImageDrawable(res)

        holder.checkbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {

            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {

                index = holder
                val hexColor = String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor())
                Log.d("TAGG",hexColor)
                if (hexColor.equals("#000000")) {
                    (context as ZActivityJobs).getSelectedJobType( 0,data?.get(position)!!.toString())
                    holder.name.setTextColor(Color.parseColor("#BEBEBE"))
                    holder.image.setColorFilter(Color.parseColor("#BEBEBE"), android.graphics.PorterDuff.Mode.SRC_ATOP);
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
                    (context as ZActivityJobs).getSelectedJobType(1,data?.get(position)!!.toString())
                    holder.name.setTextColor(Color.parseColor("#000000"))
                    holder.image.setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_ATOP)
                    holder.bullet.setTextColor(Color.parseColor("#00FF00"))
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
//            val hexColor = String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor())
//            Log.d("TAGG",hexColor)
//            if (hexColor.equals("#00FF00")) {
//                    (context as ZActivityJobs).getSelectedJobType( 0,mDataList[position].name.toString())
//                holder.name.setTextColor(Color.parseColor("#BEBEBE"))
//                holder.image.setColorFilter(Color.parseColor("#BEBEBE"), android.graphics.PorterDuff.Mode.SRC_ATOP);
//                holder.bullet.setTextColor(Color.parseColor("#BEBEBE"))
//                Log.d("TAGG", "Inside 2nd block A" +String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor()))
//            }
//            else{
//                    (context as ZActivityJobs).getSelectedJobType(1,mDataList[position].name.toString())
//                holder.name.setTextColor(Color.parseColor("#00FF00"))
//                holder.image.setColorFilter(Color.parseColor("#00FF00"), android.graphics.PorterDuff.Mode.SRC_ATOP)
//                holder.bullet.setTextColor(Color.parseColor("#00FF00"))
//                Log.d("TAGG", "Inside 2nd block B" +String.format("#%06X", 0xFFFFFF and holder.name.getCurrentTextColor()))
//            }
//            Log.d("TAGG", "Inside 2nd block")
        }

//        if (index == holder){
////            //holder.name.setTextColor(Color.parseColor("#00FF00"))
////        }
////        else{
////            holder.name.setTextColor(Color.parseColor("#BEBEBE"))
////            holder.image.setColorFilter(Color.parseColor("#BEBEBE"), android.graphics.PorterDuff.Mode.SRC_ATOP);
////            holder.bullet.setTextColor(Color.parseColor("#BEBEBE"))
////        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal  var name: TextView
        internal  var bullet: TextView
        internal  var image: ImageView
        internal var checkbox: CheckBox


        init {
            name = itemView.findViewById<View>(R.id.jobtype_name) as TextView
            image = itemView.findViewById<View>(R.id.jobtype_image) as ImageView
            bullet = itemView.findViewById<View>(R.id.bullet) as TextView
            checkbox = itemView.findViewById<View>(R.id.check) as CheckBox
        }
    }
}