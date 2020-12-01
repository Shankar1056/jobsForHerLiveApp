package com.jobsforher.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.ZActivityCompanyDetails
import com.jobsforher.activities.ZActivityDashboard
import com.jobsforher.activities.ZActivityJobDetails
import com.jobsforher.activities.ZActivityJobs
import com.jobsforher.models.JobsView
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso

class JobsAdapter(private val mDataList: ArrayList<JobsView>, isloggedin:Boolean, type:Int, filter:Int,
                  val compareList: ArrayList<Int>, isRec:Int,page:String) : RecyclerView.Adapter<JobsAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var isLoggedIn: Boolean = isloggedin
    private var type: Int= type
    private var filter: Int= filter
    private var retrofitInterface: RetrofitInterface? = null
    private var isRecommended: Int = isRec
    private var page: String = page

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.jobs_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_name.text = mDataList[position].title
        holder.row_noOfMembers.text = mDataList[position].company_name
        Log.d("TAGG","TYPE"+mDataList[position].job_types!!.size)
        var s:String = ""
        for (i in 0 until mDataList[position].job_types!!.size) {
            if (mDataList[position].job_types!!.get(i).equals("full_time")) {
                s = s+"Full Time"
            } else if (mDataList[position].job_types!!.get(i).equals("part_time")) {
                if(s.length<5)
                    s = s+"Part Time"
                else
                    s = s+","+"Part Time"
            } else if (mDataList[position].job_types!!.get(i).equals("work_from_home")){
                if(s.length<5)
                    s=s+"Work From Home"
                else
                    s = s+","+"Work From Home"
            }
            else if (mDataList[position].job_types!!.get(i).equals("returnee_program")){
                if(s.length<5)
                    s=s+"Returnee Program"
                else
                    s = s+","+"Returnee Program"
            }
            else if (mDataList[position].job_types!!.get(i).equals("volunteer")){
                if(s.length<5)
                    s=s+"Volunteer"
                else
                    s = s+","+"Volunteer"
            }
            else if (mDataList[position].job_types!!.get(i).equals("freelance")){
                if(s.length<5)
                   s= s+"Freelance"
                else
                    s = s+","+"Freelance"
            }
        }
        val msg: String = "  "+s
        val mImageSpan: ImageSpan = ImageSpan(context!!, R.drawable.ic_clockk);
        val text: SpannableString = SpannableString(msg);
        text.setSpan(mImageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.row_description.text = text

        holder.row_location.text = mDataList[position].location_name
        holder.row_label.text  =mDataList[position].min_year.toString()+" - "+mDataList[position].max_year.toString()+"Yrs"


        if (type==1){
            if(mDataList[position].boosted!!) {
                val msg: String = mDataList[position].title + "   "
                val mImageSpan: ImageSpan = ImageSpan(context!!, R.drawable.ic_hot_job);
                val text: SpannableString = SpannableString(msg);
                text.setSpan(mImageSpan, msg.length - 1, msg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.row_name.setText(text);
            }
//            holder.job_icon.visibility = View.VISIBLE
        }
        else{
            //holder.row_name.setCompoundDrawables(null, null, null, null);
//            holder.job_icon.visibility = View.GONE
        }

        holder.row_name.setOnClickListener {
            holder.llCard.callOnClick()
        }

        if(mDataList[position].boosted==true){
            val  msg: String=mDataList[position].title+"   "
            val mImageSpan: ImageSpan = ImageSpan(context!!, R.drawable.ic_hot_job);
            val text: SpannableString = SpannableString(msg);
            text.setSpan(mImageSpan, msg.length - 1, msg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.row_name.setText(text);
        }

        if(mDataList[position].status.equals("active")){
            holder.row_status.visibility = View.GONE
        }
        else{
            holder.row_status.visibility = View.VISIBLE
            holder.btnApply.isEnabled = false
            holder.btnApply.setBackgroundResource(R.drawable.curved_grey_without_border)
            holder.btnApply.setTextColor(Color.BLACK)
            holder.btnApply.setText("Closed")
            holder.btnApplied.isEnabled = false
        }

        if(page.equals("NewsFeed")){
            holder.row_location.visibility = View.GONE
            holder.row_label.visibility = View.GONE
            holder.row_description.visibility = View.GONE
        }

        holder.row_noOfMembers.setOnClickListener {


            if(page.equals("Company Details")){
                val intent = Intent(context, ZActivityCompanyDetails::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("isLoggedIn", true)
                intent.putExtra("page",page)
                intent.putExtra("group_Id", mDataList[position].company_id)
                intent.putExtra("title", mDataList[position].title)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context!!.startActivity(intent)
            }
            else if(isRecommended ==0)
                (context as ZActivityJobs).hideSuccesLayout()

            val intent = Intent(context, ZActivityCompanyDetails::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isLoggedIn", true)
            intent.putExtra("page",page)
            intent.putExtra("group_Id", mDataList[position].company_id)
            intent.putExtra("title", mDataList[position].title)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context!!.startActivity(intent)
        }

//        if (mDataList[position].categories!!.size>0){
//            for (i in 0 until mDataList[position].categories!!.size){
//                stringBuilder = stringBuilder+" "+mDataList[position].categories!!.get(i).category
//                Log.d("TAGG", stringBuilder)
//            }
//            holder.row_label.text = stringBuilder
//        }else{
//
//            holder.row_label.text = ""
//        }

        if (mDataList[position].company_logo!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].company_logo)
                .placeholder(R.drawable.ic_launcher_foreground)
                .fit()
                .into(holder.row_icon)

            //holder.row_icon.setBackgroundResource(R.drawable.curved_grey_border_filled)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.jobs_default)
                .placeholder(R.drawable.jobs_default)
                .into(holder.row_icon)
        }

        if (filter==0) {
//            if (isLoggedIn && type == 1) {
//                holder.btnJoinGroup.visibility = View.GONE
//                holder.btnJoined.visibility = View.VISIBLE
//
//            } else if (type > 1 && isLoggedIn && mDataListCompare.size > 0) {
//
            if(compareList.size>0) {
                for (k in 0 until compareList.size) {
                    Log.d("TAGG", "COMPARE" + compareList[k] + "  " + mDataList[position].id)
                    if (compareList[k] == mDataList[position].id) {
                        holder.btnApply.visibility = View.GONE
                        holder.btnApplied.visibility = View.VISIBLE
                        break
                    } else {
                        holder.btnApply.visibility = View.VISIBLE
                        holder.btnApplied.visibility = View.GONE
                    }
                }
            }
            else{
                holder.btnApply.visibility = View.VISIBLE
                holder.btnApplied.visibility = View.GONE
            }
//            } else {
//                holder.btnApply.visibility = View.VISIBLE
//                holder.btnApplied.visibility = View.GONE
//            }
        }
        else{
            holder.btnApply.visibility = View.VISIBLE
            holder.btnApplied.visibility = View.GONE
//            for (k in 0 until mDataListCompare.size) {
//                if (mDataListCompare[k].id == mDataList[position].id) {
//                    holder.btnJoinGroup.visibility = View.GONE
//                    holder.btnJoined.visibility = View.VISIBLE
//                    break
//                }
//            }
        }


        holder.llCard.setOnClickListener {

            if(page.equals("Company Details")){
                val intent = Intent(context, ZActivityJobDetails::class.java)
                intent.putExtra("isLoggedIn", true)
                intent.putExtra("group_Id", mDataList[position].id)
                intent.putExtra("title", mDataList[position].title)
                intent.putExtra("isRequired",mDataList[position].resume_required)
                intent.putExtra("isboosted",mDataList[position].boosted)
                intent.putExtra("type", type)
                intent.putExtra("page",page)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                Log.d("TAGG","INSIDE ONCLICK")
                if (holder.btnApply.visibility == View.VISIBLE)
                    intent.putExtra("isMygroup", 0)
                else
                    intent.putExtra("isMygroup", 1)
                (context as ZActivityCompanyDetails).startActivityForResult(intent, 2)
            }
            else if(isRecommended ==0)
                (context as ZActivityJobs).hideSuccesLayout()

            if (isLoggedIn && type==1) {
                val intent = Intent(context, ZActivityJobDetails::class.java)
                intent.putExtra("isLoggedIn", true)
                intent.putExtra("group_Id", mDataList[position].id)
                intent.putExtra("title", mDataList[position].title)
                intent.putExtra("type", type)
                intent.putExtra("page",page)
                intent.putExtra("isRequired",mDataList[position].resume_required)
                intent.putExtra("isboosted",mDataList[position].boosted)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                Log.d("TAGG","INSIDE ONCLICK")
                if (holder.btnApply.visibility == View.VISIBLE)
                    intent.putExtra("isMygroup", 0)
                else
                    intent.putExtra("isMygroup", 1)
                (context as Activity).startActivityForResult(intent, 2)
            }
            else if(type>1 && isLoggedIn){
                if (holder.btnApply.visibility == View.VISIBLE){
                    val intent = Intent(context, ZActivityJobDetails::class.java)
                    intent.putExtra("isLoggedIn",false)
                    intent.putExtra("group_Id", mDataList[position].id)
                    intent.putExtra("isRequired",mDataList[position].resume_required)
                    intent.putExtra("isboosted",mDataList[position].boosted)
                    intent.putExtra("title", mDataList[position].title)
                    if (holder.btnApply.visibility == View.VISIBLE)
                        intent.putExtra("isMygroup", 0)
                    else
                        intent.putExtra("isMygroup", 1)
                    intent.putExtra("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    (context as Activity).startActivityForResult(intent, 2)
                }
                else{
                    val intent = Intent(context, ZActivityJobDetails::class.java)
                    intent.putExtra("isLoggedIn", true)
                    intent.putExtra("group_Id", mDataList[position].id)
                    intent.putExtra("title", mDataList[position].title)
                    intent.putExtra("isRequired",mDataList[position].resume_required)
                    intent.putExtra("isboosted",mDataList[position].boosted)
                    if (holder.btnApply.visibility == View.VISIBLE)
                        intent.putExtra("isMygroup", 0)
                    else
                        intent.putExtra("isMygroup", 1)
                    intent.putExtra("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    (context as Activity).startActivityForResult(intent, 2)
                }
            }
            else{
                val intent = Intent(context, ZActivityJobDetails::class.java)
                intent.putExtra("isLoggedIn",true)
                intent.putExtra("group_Id", mDataList[position].id)
                intent.putExtra("title", mDataList[position].title)
                intent.putExtra("isRequired",mDataList[position].resume_required)
                intent.putExtra("isboosted",mDataList[position].boosted)
                if (holder.btnApply.visibility == View.VISIBLE)
                    intent.putExtra("isMygroup", 0)
                else
                    intent.putExtra("isMygroup", 1)
                intent.putExtra("page",page)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                (context as Activity).startActivityForResult(intent, 2)
            }


        }

        holder.btnApply.setOnClickListener{

            if(isRecommended == 0 && page.equals("Jobs")) //0 -> not recommended
                (context as ZActivityJobs).hideSuccesLayout()

            Log.d("CODE", mDataList[position].status)
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Apply Job")
            builder.setMessage("Are you sure you want to apply for this job?")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                if(isRecommended == 0 && page.equals("Jobs")) {
                    (context as ZActivityJobs).applyJob(
                        mDataList[position].id,
                        holder.btnApply,
                        mDataList[position]!!.title.toString(),
                        holder.btnApplied,
                        mDataList[position]!!.resume_required!!,mDataList[position]!!.boosted!!
                    )
                }
                else if(isRecommended == 0 && page.equals("Company Details")){
                    (context as ZActivityCompanyDetails).applyJob(
                        mDataList[position].id,
                        //holder.btnApply,
                        mDataList[position]!!.title.toString(),
                        //holder.btnApplied,
                        mDataList[position]!!.resume_required!!
                    )
                }
                else if(isRecommended == 1 && page.equals("NewsFeed")){ //It should not call another class, implement apply feature here
                    val intent = Intent(context, ZActivityJobDetails::class.java)
                    intent.putExtra("isLoggedIn",false)
                    intent.putExtra("group_Id", mDataList[position].id)
                    intent.putExtra("isRequired",mDataList[position].resume_required)
                    intent.putExtra("isboosted",mDataList[position].boosted)
                    intent.putExtra("title", mDataList[position].title)
                    if (holder.btnApply.visibility == View.VISIBLE)
                        intent.putExtra("isMygroup", 0) // to check whether i have applied the job or not
                    else
                        intent.putExtra("isMygroup", 1)
                    intent.putExtra("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    (context as Activity).startActivityForResult(intent, 2)
                }
                else{
                    (context as ZActivityDashboard).applyJob(
                        mDataList[position].id,
                        holder.btnApply,
                        mDataList[position]!!.title.toString(),
                        holder.btnApplied,
                        mDataList[position]!!.resume_required!!
                    )
                }
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
        internal var row_name: TextView
        internal var row_label: TextView
        internal var row_noOfMembers: TextView
        internal var row_description:TextView
        internal var row_status:TextView
        internal var row_location: TextView
        internal var row_icon:ImageView
        internal var btnApply: Button
        internal var btnApplied: Button
//        internal var job_icon:ImageView

        internal var llCard: LinearLayout

        init {
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_label = itemView.findViewById<View>(R.id.row_label) as TextView
            row_noOfMembers = itemView.findViewById<View>(R.id.row_noOfMembers) as TextView
            row_description = itemView.findViewById<View>(R.id.row_description) as TextView
            row_status = itemView.findViewById<View>(R.id.row_status) as TextView
            row_location = itemView.findViewById<View>(R.id.row_location) as TextView
            btnApplied = itemView.findViewById<View>(R.id.btnApplied) as Button
            btnApply = itemView.findViewById<View>(R.id.apply_job) as Button
            row_icon = itemView.findViewById<View>(R.id.row_icon) as ImageView
//            job_icon = itemView.findViewById<View>(R.id.hotjobs_icon) as ImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

            if (isLoggedIn)

            else{
                itemView.setOnClickListener(null)
            }
        }

    }
}