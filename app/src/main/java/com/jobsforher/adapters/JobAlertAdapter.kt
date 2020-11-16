package com.jobsforher.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.ZActivityDashboard
import com.jobsforher.models.PreferenceModel

class JobAlertAdapter(private val mDataList: ArrayList<PreferenceModel>) : RecyclerView.Adapter<JobAlertAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.jobalert_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.title.setText(mDataList[position].title)
        holder.pref_location_multiAutoCompleteTextView.setText(mDataList[position].city_name)
        if(mDataList[position].job_type.equals("null")||mDataList[position].job_type.equals(""))
            holder.jobtype_multiAutoCompleteTextView.setText("Not specified")
        else
            holder.jobtype_multiAutoCompleteTextView.setText(mDataList[position].job_type)
        if(mDataList[position].functional_area.equals("null")||mDataList[position].functional_area.equals(""))
            holder.farea_multiAutoCompleteTextView.setText("Not specified")
        else
            holder.farea_multiAutoCompleteTextView.setText(mDataList[position].functional_area)
        if(mDataList[position].industry.equals("null")||mDataList[position].industry.equals(""))
            holder.industry_multiAutoCompleteTextView.setText("Not specified")
        else
            holder.industry_multiAutoCompleteTextView.setText(mDataList[position].industry.toString())

        if(mDataList[position].experience_max_year==0 || mDataList[position].experience_min_year==0)
            holder.experience_multiAutoCompleteTextView.setText("Not specified")
        else
            holder.experience_multiAutoCompleteTextView.setText(mDataList[position].experience_min_year.toString()+"-"+mDataList[position].experience_max_year)

        if(mDataList[position].skills.equals("null")||mDataList[position].skills.equals(""))
            holder.pref_keyword.setText("Not specified")
        else
            holder.pref_keyword.setText(mDataList[position].skills)

        holder.edit_pref.setOnClickListener {
            (context as ZActivityDashboard).openBottomSheetJobAlerts(mDataList[position].id!!)
        }

        holder.delete_pref.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Job Alert")
            builder.setMessage("Are you sure you want to delete this job alert?")

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                (context as ZActivityDashboard).deleteJobAlerts(mDataList[position].id!!)
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                dialog.cancel()
            }
            builder.show()

        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title: TextView
        internal var pref_location_multiAutoCompleteTextView: MultiAutoCompleteTextView
        internal var jobtype_multiAutoCompleteTextView: MultiAutoCompleteTextView
        internal var farea_multiAutoCompleteTextView: MultiAutoCompleteTextView
        internal var industry_multiAutoCompleteTextView:MultiAutoCompleteTextView
        internal var experience_multiAutoCompleteTextView: MultiAutoCompleteTextView
        internal var pref_keyword: EditText
        internal var edit_pref: ImageView
        internal var delete_pref:ImageView


        init {
            title = itemView.findViewById<View>(R.id.title) as TextView
            pref_location_multiAutoCompleteTextView = itemView.findViewById<View>(R.id.pref_location_multiAutoCompleteTextView) as MultiAutoCompleteTextView
            jobtype_multiAutoCompleteTextView = itemView.findViewById<View>(R.id.jobtype_multiAutoCompleteTextView) as MultiAutoCompleteTextView
            farea_multiAutoCompleteTextView = itemView.findViewById<View>(R.id.farea_multiAutoCompleteTextView) as MultiAutoCompleteTextView
            industry_multiAutoCompleteTextView = itemView.findViewById<View>(R.id.industry_multiAutoCompleteTextView) as MultiAutoCompleteTextView
            experience_multiAutoCompleteTextView = itemView.findViewById<View>(R.id.experience_multiAutoCompleteTextView) as MultiAutoCompleteTextView
            pref_keyword = itemView.findViewById<View>(R.id.pref_keyword) as EditText
            edit_pref = itemView.findViewById<View>(R.id.edit_pref) as ImageView
            delete_pref = itemView.findViewById<View>(R.id.delete_pref) as ImageView

        }

    }



}