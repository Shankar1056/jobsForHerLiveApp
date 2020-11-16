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
import com.jobsforher.models.Years

class YearsAdapter(private val mDataList: ArrayList<Years>, type: String) : RecyclerView.Adapter<YearsAdapter.MyViewHolder>() {

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

        holder.row_text.text = mDataList[position].years

        holder.row_text.setOnClickListener {

            if (index!=holder){

                if (type.equals("Education")){
                    (context as EducationActivity).SelectedYear(mDataList[position].years!!)

                }else if (type.equals("LifeExp")){
                    (context as LifeExperienceActivity).SelectedYear(mDataList[position].years!!)

                }else if (type.equals("WorkExp")){
                    (context as WorkExperienceActivity).SelectedYear(mDataList[position].years!!)

                }else if (type.equals("OnBreak")){
                    (context as RestarterActivity).SelectedYear(mDataList[position].years!!)

                }
                else if(type.equals("Education_Edit")){
                    (context as EducationEdit).SelectedYear(mDataList[position].years!!)
                }
                else if(type.equals("Certification_Edit")){
                    (context as CertificationEdit).SelectedYear(mDataList[position].years!!)
                }
                else if(type.equals("Life_Edit")){
                    (context as LifeExperienceEdit).SelectedYear(mDataList[position].years!!)
                }
                else if(type.equals("Work_Edit")){
                    (context as WorkingEdit).SelectedYear(mDataList[position].years!!)
                }
                else if(type.equals("Awards_Edit")){
                    (context as AwardsEdit).SelectedYear(mDataList[position].years!!)
                }


            } else if(index==holder){

                if (type.equals("Education")){

                    (context as EducationActivity).SelectedYear("")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }else if (type.equals("LifeExp")){

                    (context as LifeExperienceActivity).SelectedYear("")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as LifeExperienceActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)


                }else if (type.equals("WorkExp")){

                    (context as WorkExperienceActivity).SelectedYear("")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as WorkExperienceActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)


                }else if (type.equals("OnBreak")){

                    (context as RestarterActivity).SelectedYear("")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as RestarterActivity, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if(type.equals("Education_Edit")){

                    (context as EducationEdit).SelectedYear("")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)
                }
                else if(type.equals("Certification_Edit")){
                    (context as CertificationEdit).SelectedYear("")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as CertificationEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)
                }
                else if(type.equals("Life_Edit")){
                    (context as LifeExperienceEdit).SelectedYear("")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as LifeExperienceEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)
                }
                else if(type.equals("Work_Edit")){
                    (context as WorkingEdit).SelectedYear("")

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as WorkingEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)
                }
                else if(type.equals("Awards_Edit")){
                    (context as AwardsEdit).SelectedYear("")

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
                else if (type.equals("Education_Edit")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as EducationEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if (type.equals("Certification_Edit")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as CertificationEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if (type.equals("Life_Edit")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as LifeExperienceEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if (type.equals("Work_Edit")){

                    val typeFace: Typeface? = ResourcesCompat.getFont(context as WorkingEdit, R.font.opensansregular)

                    index!!.row_text.setTypeface(typeFace)

                }
                else if (type.equals("Awards_Edit")){

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