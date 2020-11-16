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
import com.jobsforher.activities.EducationEdit
import com.jobsforher.activities.SelectingLocationActivity
import com.jobsforher.models.Cities

class CitiesAdapter(private val mDataList: ArrayList<Cities>, type: String) : RecyclerView.Adapter<CitiesAdapter.MyViewHolder>() {

    private var context: Context? = null

    var index: MyViewHolder? = null

    private var type: String = type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.cities_adapter_row, parent, false)

        this.context = parent.context;

        context = (context as ContextWrapper).baseContext

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        index = holder

        holder.row_text.text = mDataList[position].name

        holder.row_text.setOnClickListener {

            if (index!=holder){

                if (type.equals("LocationAct")){

                    (context as SelectingLocationActivity).SelectedCity(mDataList[position].id, mDataList[position].name!!)

                }else if (type.equals("EducationEdit")){

                    //(context as EducationEdit).SelectedCity(mDataList[position].id, mDataList[position].name!!)
                }


            } else if(index==holder){

                if (type.equals("LocationAct")){

                    (context as SelectingLocationActivity).SelectedCity(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as SelectingLocationActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("EducationEdit")){

//                    (context as EducationEdit).SelectedCity(0, "")
//
//                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationEdit, R.font.opensansregular)
//
//                    index!!.row_text.setTypeface(typeFace)

                }

            }

            holder.row_text.setTypeface(Typeface.DEFAULT_BOLD)

            if(index != null)
            {

                if (type.equals("LocationAct")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as SelectingLocationActivity, R.font.opensansregular)

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
            row_text = itemView.findViewById<View>(R.id.tvCities) as TextView
        }
    }
}