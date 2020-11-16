package com.jobsforher.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.jobsforher.R
import com.jobsforher.activities.*
import com.jobsforher.models.CompaniesView
import com.jobsforher.network.retrofithelpers.RetrofitInterface

class CompaniesAdapter(private val mDataList: ArrayList<CompaniesView>, isloggedin:Boolean, type:Int,
                       filter:Int, val compareList: ArrayList<Int>, isRec:Int,page:String) : RecyclerView.Adapter<CompaniesAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var isLoggedIn: Boolean = isloggedin
    private var type: Int= type
    private var filter: Int= filter
    private var retrofitInterface: RetrofitInterface? = null
    private var isRecommended: Int = isRec
    private var page: String = page

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.companies_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_name.text = mDataList[position].name
        holder.row_noOfMembers.text = mDataList[position].industry.toString().substring(1,mDataList[position].industry.toString().length-1)
        holder.row_location.text = "Mentors"
        if(mDataList[position].active_jobs_count.toString().compareTo("0")==0)
            holder.row_label.visibility = View.GONE
        else
            holder.row_label.text  = mDataList[position].active_jobs_count.toString()+" Active Jobs"

        if (type==3){
            holder.job_icon.visibility = View.GONE
            val  msg: String=mDataList[position].name+"   "
            val mImageSpan: ImageSpan = ImageSpan(context!!, R.drawable.ic_group_featured);
            val text: SpannableString = SpannableString(msg);
            text.setSpan(mImageSpan, msg.length - 1, msg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.row_name.setText(text);
        }
        else{
            holder.job_icon.visibility = View.GONE
        }
//
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

        if (mDataList[position].logo!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].logo)
                .placeholder(R.drawable.ic_launcher_foreground)
                .fit()
                .into(holder.row_icon)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.comp_default)
                .placeholder(R.drawable.comp_default)
                .into(holder.row_icon)
        }

        if (filter==0) {
//            if (isLoggedIn && type == 1) {
//                holder.btnJoinGroup.visibility = View.GONE
//                holder.btnJoined.visibility = View.VISIBLE
//
//            } else if (type > 1 && isLoggedIn && mDataListCompare.size > 0) {
//
            if (compareList.size==0){
                holder.btnApply.visibility = View.VISIBLE
                holder.btnApplied.visibility = View.GONE
            }

            else{
                for (k in 0 until compareList.size) {
                    Log.d("TAGG","COMPARE"+ compareList[k]+"  "+mDataList[position].id)
                    if (compareList[k]== mDataList[position].id) {
                        holder.btnApply.visibility = View.GONE
                        holder.btnApplied.visibility = View.VISIBLE
                        break
                    } else {
                        holder.btnApply.visibility = View.VISIBLE
                        holder.btnApplied.visibility = View.GONE
                    }
                }
            }

//            if(isRecommended==1){
//                holder.btnApply.visibility = View.VISIBLE
//                holder.btnApplied.visibility = View.GONE
//
//            }

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

            if (isLoggedIn && type==1) {

                val intent = Intent(context, ZActivityCompanyDetails::class.java)
                intent.putExtra("isLoggedIn", true)
                intent.putExtra("group_Id", mDataList[position].id)
                intent.putExtra("title", mDataList[position].name)
                intent.putExtra("page",page)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                if (holder.btnApply.visibility == View.VISIBLE)
                    intent.putExtra("isMygroup", 0)
                else
                    intent.putExtra("isMygroup", 1)
                context!!.startActivity(intent)
            }
            else if(type>1 && isLoggedIn){
                if (holder.btnApply.visibility == View.VISIBLE){
                    val intent = Intent(context, ZActivityCompanyDetails::class.java)
                    intent.putExtra("isLoggedIn",false)
                    intent.putExtra("group_Id", mDataList[position].id)
                    intent.putExtra("title", mDataList[position].name)
                    intent.putExtra("isMygroup",0)
                    intent.putExtra("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context!!.startActivity(intent)
                }
                else{
                    val intent = Intent(context, ZActivityCompanyDetails::class.java)
                    intent.putExtra("isLoggedIn", true)
                    intent.putExtra("group_Id", mDataList[position].id)
                    intent.putExtra("title", mDataList[position].name)
                    intent.putExtra("isMygroup", 1)
                    intent.putExtra("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context!!.startActivity(intent)
                }
            }
            else{
                val intent = Intent(context, ZActivityCompanyDetails::class.java)
                intent.putExtra("isLoggedIn",false)
                intent.putExtra("group_Id", mDataList[position].id)
                intent.putExtra("title", mDataList[position].name)
                intent.putExtra("isMygroup",0)
                intent.putExtra("page",page)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context!!.startActivity(intent)
            }


        }


        holder.btnApplied.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Unfollow Company")
            builder.setMessage("Are you sure you want to unfollow this company?")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                if(isRecommended==0) {
                    (context as ZActivityCompanies).followCompany(
                        mDataList[position].id,
                        holder.btnApply, holder.btnApplied, "unfollow", mDataList[position].name.toString()
                    )
                    holder.btnApply.visibility = View.VISIBLE
                    holder.btnApplied.visibility = View.GONE
                }
                else{
                    (context as ZActivityDashboard).followCompany(
                        mDataList[position].id,
                        holder.btnApply, holder.btnApplied, "unfollow", mDataList[position].name.toString()
                    )
                    holder.btnApply.visibility = View.VISIBLE
                    holder.btnApplied.visibility = View.GONE
                }
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                dialog.cancel()
            }
            builder.show()
        }

        holder.btnApply.setOnClickListener{

            Log.d("CODE", mDataList[position].status)
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Follow Company")
            builder.setMessage("Are you sure you want to follow this company?")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                if(isRecommended==0) {
                    (context as ZActivityCompanies).followCompany(
                        mDataList[position].id,
                        holder.btnApply, holder.btnApplied, "follow", mDataList[position].name.toString()
                    )
                    holder.btnApply.visibility = View.GONE
                    holder.btnApplied.visibility = View.VISIBLE
                }
                else if(isRecommended==1 && page.equals("NewsFeed")){
                    val intent = Intent(context, ZActivityCompanyDetails::class.java)
                    intent.putExtra("isLoggedIn", true)
                    intent.putExtra("group_Id", mDataList[position].id)
                    intent.putExtra("title", mDataList[position].name)
                    intent.putExtra("isMygroup", 0)
                    intent.putExtra("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context!!.startActivity(intent)
                }
                else{
                    (context as ZActivityDashboard).followCompany(
                        mDataList[position].id,
                        holder.btnApply, holder.btnApplied, "follow", mDataList[position].name.toString()
                    )
                    holder.btnApply.visibility = View.GONE
                    holder.btnApplied.visibility = View.VISIBLE
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
        internal var row_location: TextView
        internal var row_icon:ImageView
        internal var btnApply: Button
        internal var btnApplied: Button
        internal var job_icon:ImageView

        internal var llCard: LinearLayout

        init {
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_label = itemView.findViewById<View>(R.id.row_label) as TextView
            row_noOfMembers = itemView.findViewById<View>(R.id.row_noOfMembers) as TextView
            row_location = itemView.findViewById<View>(R.id.row_location) as TextView
            btnApplied = itemView.findViewById<View>(R.id.btnApplied) as Button
            btnApply = itemView.findViewById<View>(R.id.apply_job) as Button
            row_icon = itemView.findViewById<View>(R.id.row_icon) as ImageView
            job_icon = itemView.findViewById<View>(R.id.hotjobs_icon) as ImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

            if (isLoggedIn)

            else{
                itemView.setOnClickListener(null)
            }
        }

    }
}