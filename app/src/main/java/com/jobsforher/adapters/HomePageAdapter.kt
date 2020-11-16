package com.jobsforher.adapters

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.FacebookSdk.getApplicationContext
import com.jobsforher.R
import com.jobsforher.activities.HomePage


class HomePageAdapter(private val mDataList: ArrayList<String>,private val mDataImages: ArrayList<Int>,
                      private val mDataChoice: ArrayList<String>,private val mDataChoiceDesc: ArrayList<String>  ) : RecyclerView.Adapter<HomePageAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_homepage_grid, parent, false)
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Log.d("TAGG","Choice"+mDataChoice.toString())
        holder.row_text.text = mDataList[position].toString()
        holder.row_desc.text = mDataChoiceDesc[position].toString()

        holder.row_image.setImageResource(mDataImages[position])
        holder.row_image.setColorFilter(ContextCompat.getColor(
            getApplicationContext(),
            R.color.menu_grey));

        Log.d("TAGG", "contains" + mDataList[position] + "")
        for(l in 0 until mDataChoice.size) {

            if (mDataChoice[l].trim().equals(mDataList[position].toString())) {
                holder.row_layout.setBackgroundResource(R.drawable.ic_selection_border)
                holder.row_text.setTextColor(Color.parseColor("#99CA3B"))
                holder.row_image.setColorFilter(
                    ContextCompat.getColor(
                        getApplicationContext(),
                        R.color.green
                    )
                );
            }
//            else {
//                Log.d("TAGG", "NOt contais" + mDataList[position] + "")
//                holder.row_layout.setBackgroundResource(R.drawable.curved_grey_border_nofill)
//                holder.row_text.setTextColor(Color.parseColor("#000000"))
//                holder.row_image.setColorFilter(
//                    ContextCompat.getColor(
//                        getApplicationContext(),
//                        R.color.menu_grey
//                    )
//                );
//            }
        }

        holder.row_layout.setOnClickListener {

            val hexColor = String.format("#%06X", 0xFFFFFF and holder.row_text.getCurrentTextColor())
            Log.d("TAGG",hexColor)
            if (hexColor.equals("#99CA3B")) {
                holder.row_layout.setBackgroundResource(R.drawable.curved_grey_border_nofill)
                holder.row_text.setTextColor(Color.parseColor("#000000"))
                holder.row_image.setColorFilter(ContextCompat.getColor(
                    getApplicationContext(),
                    R.color.menu_grey));
                (context as HomePage).deleteInterest(holder.row_text.text.toString())
            }
            else{
                holder.row_layout.setBackgroundResource(R.drawable.ic_selection_border)
                holder.row_text.setTextColor(Color.parseColor("#99CA3B"))
                holder.row_image.setColorFilter(ContextCompat.getColor(
                    getApplicationContext(),
                    R.color.green));
                (context as HomePage).createInterest(holder.row_text.text.toString().trim())
            }

        }



    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_text: TextView
        internal var row_desc: TextView
        internal lateinit var row_image: ImageView
        internal  var row_layout: LinearLayout

        init {
            row_text = itemView.findViewById<View>(R.id.row_text) as TextView
            row_image = itemView.findViewById<View>(R.id.row_image) as ImageView
            row_layout = itemView.findViewById<View>(R.id.llCard) as LinearLayout
            row_desc = itemView.findViewById<View>(R.id.row_description) as TextView
        }
    }
}