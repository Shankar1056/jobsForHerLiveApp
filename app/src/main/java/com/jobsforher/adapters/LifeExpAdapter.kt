package com.jobsforher.adapters

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.LifeExperienceActivity
import com.jobsforher.activities.RestarterActivity
import com.jobsforher.models.LifeExperiences

class LifeExpAdapter(private val mDataList: ArrayList<LifeExperiences>, type: String) : RecyclerView.Adapter<LifeExpAdapter.MyViewHolder>() {

    private var context: Context? = null

    private var type: String = type

    var index: MyViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.degree_adapter_row, parent, false)

        this.context = parent.context;

        context = (context as ContextWrapper).baseContext

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        index = holder

        holder.row_text.text = mDataList[position].name

        holder.row_text.setOnClickListener {

            if (index!=holder){

                if (type.equals("OnBreak")){

                    (context as RestarterActivity).SelectedLifeExp(mDataList[position].id, mDataList[position].name!!)

                }else if (type.equals("LifeExp")){

                    (context as LifeExperienceActivity).SelectedLifeExp(mDataList[position].id, mDataList[position].name!!)

                }

            } else if(index==holder){

                if (type.equals("OnBreak")){

                    (context as RestarterActivity).SelectedLifeExp(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as RestarterActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("LifeExp")){

                    (context as LifeExperienceActivity).SelectedLifeExp(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as LifeExperienceActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }

            }
            holder.row_text.setTypeface(Typeface.DEFAULT_BOLD)

            if(index != null)
            {
                if (type.equals("OnBreak")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as RestarterActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("LifeExp")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as LifeExperienceActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
            }
            index = holder

        }

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_text: TextView

        init {
            row_text = itemView.findViewById<View>(R.id.tvDegrees) as TextView
        }
    }
}