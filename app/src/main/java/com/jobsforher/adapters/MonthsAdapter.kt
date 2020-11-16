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
import com.jobsforher.activities.*
import com.jobsforher.models.Months

class MonthsAdapter(private val mDataList: ArrayList<Months>, type: String) : RecyclerView.Adapter<MonthsAdapter.MyViewHolder>() {

    private var context: Context? = null

    private var type: String = type

    var index: MyViewHolder? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.month_adapter_row, parent, false)

        this.context = parent.context;

        context = (context as ContextWrapper).baseContext

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // index = holder

        holder.row_text.text = mDataList[position].months

        holder.row_text.setOnClickListener {

            if (index!=holder){

                if (type.equals("Education")){

                    (context as EducationActivity).SelectedMonth(mDataList[position].monthsId, mDataList[position].months!!)

                }else if (type.equals("LifeExp")){

                    (context as LifeExperienceActivity).SelectedMonth(mDataList[position].monthsId, mDataList[position].months!!)

                }else if (type.equals("WorkExp")){

                    (context as WorkExperienceActivity).SelectedMonth(mDataList[position].monthsId, mDataList[position].months!!)

                }else if (type.equals("OnBreak")){

                    (context as RestarterActivity).SelectedMonth(mDataList[position].monthsId, mDataList[position].months!!)

                }else if(type.equals("Education_Edit")){

                    (context as EducationEdit).SelectedMonth(mDataList[position].monthsId, mDataList[position].months!!)
                }
                else if(type.equals("Certification_Edit")){

                    (context as CertificationEdit).SelectedMonth(mDataList[position].monthsId, mDataList[position].months!!)
                }
                else if(type.equals("Life_Edit")){

                    (context as LifeExperienceEdit).SelectedMonth(mDataList[position].monthsId, mDataList[position].months!!)
                }
                else if(type.equals("Work_Edit")){

                    (context as WorkingEdit).SelectedMonth(mDataList[position].monthsId, mDataList[position].months!!)
                }
                else if(type.equals("Awards_Edit")){

                    (context as AwardsEdit).SelectedMonth(mDataList[position].monthsId, mDataList[position].months!!)
                }


            } else if(index==holder){

                if (type.equals("Education")){

                    (context as EducationActivity).SelectedMonth(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("LifeExp")){

                    (context as LifeExperienceActivity).SelectedMonth(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as LifeExperienceActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("WorkExp")){

                    (context as WorkExperienceActivity).SelectedMonth(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as WorkExperienceActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("OnBreak")){

                    (context as RestarterActivity).SelectedMonth(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as RestarterActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if(type.equals("Education_Edit")){

                    (context as EducationEdit).SelectedMonth(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)
                }
                else if(type.equals("Certification_Edit")){

                    (context as CertificationEdit).SelectedMonth(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as CertificationEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)
                }
                else if(type.equals("Life_Edit")){

                    (context as LifeExperienceEdit).SelectedMonth(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as LifeExperienceEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)
                }
                else if(type.equals("Work_Edit")){

                    (context as WorkingEdit).SelectedMonth(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as WorkingEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)
                }
                else if(type.equals("Awards_Edit")){

                    (context as AwardsEdit).SelectedMonth(0, "")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as AwardsEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)
                }


            }

            holder.row_text.setTypeface(Typeface.DEFAULT_BOLD)

            if(index != null)
            {
                if (type.equals("Education")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("LifeExp")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as LifeExperienceActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("WorkExp")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as WorkExperienceActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("OnBreak")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as RestarterActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if(type.equals("Education_Edit")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if(type.equals("Certification_Edit")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as CertificationEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if(type.equals("Life_Edit")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as LifeExperienceEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if(type.equals("Work_Edit")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as WorkingEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if(type.equals("Awards_Edit")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as AwardsEdit, R.font.opensansregular)

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
            row_text = itemView.findViewById<View>(R.id.tvMonth) as TextView
        }
    }
}