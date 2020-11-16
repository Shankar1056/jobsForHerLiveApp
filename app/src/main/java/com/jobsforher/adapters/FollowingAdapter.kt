package com.jobsforher.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.ZActivityDashboard
import com.jobsforher.models.Following
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class FollowingAdapter(private val mDataList: ArrayList<Following>, isloggedin:Boolean) : RecyclerView.Adapter<FollowingAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var isLoggedIn: Boolean = isloggedin
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.following_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_name.text = mDataList[position].stage_type
        if(mDataList[position].stage_type!!.compareTo("riser")==0)
            holder.row_name.setTextColor(Color.parseColor("#4CAAC6"))
        else if(mDataList[position].stage_type!!.compareTo("restarter")==0)
            holder.row_name.setTextColor(Color.parseColor("#37D2A7"))
        else if(mDataList[position].stage_type!!.compareTo("starter")==0)
            holder.row_name.setTextColor(Color.parseColor("#FAA83F"))
        holder.row_noOfMembers.text = mDataList[position].username
        holder.row_label.text  =mDataList[position].company_name


        if (mDataList[position].profile_icon!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].profile_icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }
        holder.btnApply.visibility = View.VISIBLE
        holder.btnApplied.visibility = View.VISIBLE

        holder.btnApply.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Unfollow")
            builder.setMessage("Do you really want to unfollow "+mDataList[position].username+"?")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                (context as ZActivityDashboard).unfollowProfile(
                    mDataList[position].user_id, mDataList[position].username!!
                )

            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                dialog.cancel()
            }
            builder.show()
        }

        holder.btnApplied.setOnClickListener {
            (context as ZActivityDashboard).shareProfile()

        }

        holder.llCard.setOnClickListener {

            //            if (isLoggedIn && type==1) {
//
//                val intent = Intent(context, ZActivityJobDetails::class.java)
//                intent.putExtra("isLoggedIn", true)
//                intent.putExtra("group_Id", mDataList[position].id)
//                intent.putExtra("title", mDataList[position].title)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                if (holder.btnApply.visibility == View.VISIBLE)
//                    intent.putExtra("isMygroup", 0)
//                else
//                    intent.putExtra("isMygroup", 1)
//                context!!.startActivity(intent)
//            }
//            else if(type>1 && isLoggedIn){
//               if (holder.btnApply.visibility == View.VISIBLE){
//                   val intent = Intent(context, ZActivityJobDetails::class.java)
//                   intent.putExtra("isLoggedIn",false)
//                   intent.putExtra("group_Id", mDataList[position].id)
//                   intent.putExtra("title", mDataList[position].title)
//                   intent.putExtra("isMygroup",0)
//                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                   context!!.startActivity(intent)
//               }
//                else{
//                   val intent = Intent(context, ZActivityJobDetails::class.java)
//                   intent.putExtra("isLoggedIn", true)
//                   intent.putExtra("group_Id", mDataList[position].id)
//                   intent.putExtra("title", mDataList[position].title)
//                   intent.putExtra("isMygroup", 1)
//                   intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                   context!!.startActivity(intent)
//               }
//            }
//            else{
//                val intent = Intent(context, ZActivityJobDetails::class.java)
//                intent.putExtra("isLoggedIn",false)
//                intent.putExtra("group_Id", mDataList[position].id)
//                intent.putExtra("title", mDataList[position].title)
//                intent.putExtra("isMygroup",0)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                context!!.startActivity(intent)
//            }


        }



//        holder.btnJoined.setOnClickListener {
//
//            retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)
//
//            val call = retrofitInterface!!.ReportCheck(mDataList[position].id, EndPoints.CLIENT_ID, "Bearer "+ EndPoints.ACCESS_TOKEN)
//
//            call.enqueue(object : Callback<DeletePostResponse> {
//                override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {
//
//                    Logger.d("CODE", response.code().toString() + "")
//                    Logger.d("MESSAGE", response.message() + "")
//                    Logger.d("URL", "" + response)
//                    Logger.d("TAGG","CODE IS "+response.body()!!.responseCode.toString())
//                    Logger.d("RESPONSE join group", "" + Gson().toJson(response))
//
//                    if (response.isSuccessful && response.body()!!.responseCode.toString().equals("10802") ) {      //11804
//                        val popupMenu: PopupMenu = PopupMenu(context,holder.btnJoined)
//                        popupMenu.menuInflater.inflate(R.menu.group_popup_menu1,popupMenu.menu)
//                        val positionOfMenuItem = 1 // or whatever...
//                        val item = popupMenu.menu.getItem(positionOfMenuItem)
//                        val s = SpannableString("Reported")
//                        s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, 0)
//                        item.setTitle(s)
//                        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
//                            when(item.itemId) {
//                                R.id.action_leave -> {
//                                    val builder = AlertDialog.Builder(context)
//                                    builder.setTitle("Leave group")
//                                    builder.setMessage("Are you sure you want to leave this group?")
//                                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
//                                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//                                        (context as ZActivityGroups).leaveGroup(
//                                            mDataList[position].id,
//                                            holder.btnJoinGroup,
//                                            holder.btnJoined
//                                        )
//                                    }
//                                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                                        dialog.cancel()
//                                    }
//                                    builder.show()
//                                }
//                                R.id.action_report ->
//                                    (context as ZActivityGroups).openBottomSheetReports(mDataList[position].id,"group")
//                                R.id.action_reported ->
//                                    Toast.makeText(context,"Already Reported", Toast.LENGTH_LONG).show()
//                            }
//                            true
//                        })
//                        popupMenu.show()
//
//                    } else {
//                        val popupMenu: PopupMenu = PopupMenu(context, holder.btnJoined)
//                        popupMenu.menuInflater.inflate(R.menu.group_popup_menu,popupMenu.menu)
//
//                        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
//                            when(item.itemId) {
//                                R.id.action_leave -> {
//                                    val builder = AlertDialog.Builder(context)
//                                    builder.setTitle("Leave group")
//                                    builder.setMessage("Are you sure you want to leave this group?")
//                                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
//                                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//                                        (context as ZActivityGroups).leaveGroup(
//                                            mDataList[position].id,
//                                            holder.btnJoinGroup,
//                                            holder.btnJoined
//                                        )
//
//                                    }
//                                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                                        dialog.cancel()
//                                    }
//                                    builder.show()
//                                }
//                                R.id.action_report ->
//                                    (context as ZActivityGroups).openBottomSheetReports(mDataList[position].id,"group")
//                                R.id.action_reported ->
//                                    Toast.makeText(context,"Already Reported", Toast.LENGTH_LONG).show()
//                            }
//                            true
//                        })
//                        popupMenu.show()
//                    }
//                }
//
//                override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
//
//                    Logger.d("TAGG", "FAILED : $t")
//                }
//            })
//        }
//

//        holder.btnApply.setOnClickListener{

//            Log.d("CODE", mDataList[position].status)
//                val builder = AlertDialog.Builder(context)
//                builder.setTitle("Apply Job")
//                builder.setMessage("Are you sure you want to apply for this job?")
//                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
//                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//                    (context as ZActivityJobs).applyJob(mDataList[position].id!!,
//                        holder.btnApply,
//                        mDataList[position]!!.title.toString(),holder.btnApplied)
//                }
//
//                builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                    dialog.cancel()
//                }
//                builder.show()
//             }

    }


    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_name: TextView
        internal var row_label: TextView
        internal var row_noOfMembers: TextView
        internal var row_icon:ImageView
        internal var btnApply: Button
        internal var btnApplied: TextView

        internal var llCard: LinearLayout

        init {
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_label = itemView.findViewById<View>(R.id.row_label) as TextView
            row_noOfMembers = itemView.findViewById<View>(R.id.row_noOfMembers) as TextView
            btnApplied = itemView.findViewById<View>(R.id.btnApplied) as TextView
            btnApply = itemView.findViewById<View>(R.id.apply_job) as Button
            row_icon = itemView.findViewById<View>(R.id.row_icon) as ImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

            if (isLoggedIn)

            else{
                itemView.setOnClickListener(null)
            }
        }

    }
}