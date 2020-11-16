package com.jobsforher.adapters

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.LifeExperienceActivity
import com.jobsforher.activities.RestarterActivity
import com.jobsforher.activities.WorkExperienceActivity
import com.jobsforher.models.Skills


class SkillsAdapter(private val mDataList: ArrayList<Skills>, type:String, private val mDataListSelected: ArrayList<Skills>) : RecyclerView.Adapter<SkillsAdapter.MyViewHolder>(){

    private var context: Context? = null

    private var type: String = type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.skill_adapter_row, parent, false)

        this.context = parent.context;

        context = (context as ContextWrapper).baseContext


        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.setIsRecyclable(false);
        holder.row_text.text = mDataList[position].name

        Log.d("TAGG", mDataListSelected.size.toString() + "  " + mDataList[position].id)
        for (k in 0 until mDataListSelected.size) {
            Log.d("TAGG", mDataListSelected[k].id.toString())
            if (mDataListSelected[k].name.equals(mDataList[position].name)) {
                holder.row_text.setBackgroundResource(R.drawable.curved_green_border_new)
                Log.d("TAGG", "Inside if" + " " + mDataListSelected[k].id)
                break
            }
        }

        holder.row_text.setOnClickListener {

            holder.row_text.setTypeface(Typeface.DEFAULT_BOLD)
            if (holder.row_text.background.getConstantState() == context!!.getResources().getDrawable(R.drawable.curved_green_border_new).getConstantState()) {
                holder.row_text.setBackgroundResource(R.drawable.curved_grey_border_up)
            } else {
                holder.row_text.setBackgroundResource(R.drawable.curved_green_border_new)
            }

//            holder.row_text.setTextColor(Color.parseColor("#71EF51"))

            if (type.equals("lifeExp")) {

                (context as LifeExperienceActivity).SelectedSkill(
                    mDataList[position].approve, mDataList[position].id,
                    mDataList[position].category_type!!, mDataList[position].name!!
                )

            } else if (type.equals("WorkExp")) {

                (context as WorkExperienceActivity).SelectedSkill(
                    mDataList[position].approve, mDataList[position].id,
                    mDataList[position].category_type!!, mDataList[position].name!!
                )

            } else if (type.equals("OnBreak")) {

                (context as RestarterActivity).SelectedSkill(
                    mDataList[position].approve, mDataList[position].id,
                    mDataList[position].category_type!!, mDataList[position].name!!
                )

            }
        }

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_text: TextView

        init {
            row_text = itemView.findViewById<View>(R.id.tvSkills) as TextView
        }
    }

}