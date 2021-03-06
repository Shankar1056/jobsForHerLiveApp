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
import com.jobsforher.activities.EducationActivity
import com.jobsforher.activities.EducationEdit
import com.jobsforher.models.Degrees

class DegreesAdapter(private val mDataList: ArrayList<Degrees>, type: String) : RecyclerView.Adapter<DegreesAdapter.MyViewHolder>() {

    private var context: Context? = null

    var index: MyViewHolder? = null

    private var type: String = type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.degree_adapter_row, parent, false)

        this.context = parent.context;

        context = (context as ContextWrapper).baseContext

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        index = holder

        holder.row_text.text = mDataList[position].sDegree

        holder.row_text.setOnClickListener {

            if (index!=holder){

                if (type.equals("Education")){

                    (context as EducationActivity).SelectedDegree(mDataList[position].id, mDataList[position].sDegree!!)

                }else if (type.equals("EducationEdit")){

//                    (context as EducationEdit).SelectedDegree(mDataList[position].id, mDataList[position].sDegree!!)
                }

            } else if(index==holder){

                if (type.equals("Education")){

                    (context as EducationActivity).SelectedDegree(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("EducationEdit")){

//                    (context as EducationEdit).SelectedDegree(0, "")
//
//                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationEdit, R.font.opensansregular)
//
//                    index!!.row_text.setTypeface(typeFace)

                }

            }

            holder.row_text.setTypeface(Typeface.DEFAULT_BOLD)

            if(index != null)
            {

                if (type.equals("Education")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("EducationEdit")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationEdit, R.font.opensansregular)

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