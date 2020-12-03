package com.jobsforher.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.R
import com.jobsforher.activities.ZActivityDashboard
import com.jobsforher.activities.ZActivityGroups
import com.jobsforher.activities.ZActivityGroupsDetails
import com.jobsforher.helpers.Logger
import com.jobsforher.models.GroupsView
import com.jobsforher.network.responsemodels.DeletePostResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface

class MyGroupsAdapter(private val mDataList: ArrayList<GroupsView>, isloggedin:Boolean, type:Int,
                      private val mDataListCompare: ArrayList<GroupsView>,filter:Int, isRec:Int,page:String) : RecyclerView.Adapter<MyGroupsAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var isLoggedIn: Boolean = isloggedin
    private var type: Int= type
    private var filter: Int= filter
    private var retrofitInterface: RetrofitInterface? = null
    private var isRecommended :Int = isRec
    private var string: String = ""
    private var page: String = page

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mygroups_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false);
        holder.row_name.text = mDataList[position].label
        if(Integer.parseInt(mDataList[position]!!.noOfMembers!!)<10)
            holder.row_noOfMembers.visibility = View.GONE
        else
            holder.row_noOfMembers.text = mDataList[position].noOfMembers + " Members"
        holder.row_description.text = mDataList[position].description
        holder.row_location.text = mDataList[position].location
        if (mDataList[position].groupType.equals("public"))
            holder.row_grouptype.text = "Open Group"
        else
            holder.row_grouptype.text = "Closed Group"

        var stringBuilder: String? = ""
        if (type==2 ||mDataList[position].featured ==true){
            holder.row_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_group_featured, 0);
        }
        else{
            holder.row_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        if (mDataList[position].categories!!.size>0){
            for (i in 0 until mDataList[position].categories!!.size){
                stringBuilder = stringBuilder+" "+mDataList[position].categories!!.get(i).category
                Log.d("TAGG", stringBuilder)
            }
            holder.row_label.text = stringBuilder
        }else{

            holder.row_label.text = ""
        }

        if (mDataList[position].icon_url!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].icon_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }

        if(page.equals("NewsFeed")){
            holder.row_description.visibility = View.GONE
        }

        if (filter==0) {
            Log.d("TAGG","INSIDE ELSE"+ isLoggedIn+ type+ mDataList[position].is_member)
            if (isLoggedIn && type == 1) {

                holder.btnJoinGroup.visibility = View.GONE
                if(mDataList[position].is_member==true)
                    holder.btnJoined.visibility = View.VISIBLE
                else{
                    Log.d("TAGG","INSIDE ELSE")
                    holder.btnJoined.visibility = View.VISIBLE
                    holder.btnJoined.setText("Pending")
                    holder.btnJoined.isEnabled = false
                }

            } else if (type > 1 && isLoggedIn && mDataListCompare.size > 0) {

                for (k in 0 until mDataListCompare.size) {
                    if (mDataListCompare[k].id == mDataList[position].id) {
                        holder.btnJoinGroup.visibility = View.GONE
                        if(mDataListCompare[k].is_member==true)
                            holder.btnJoined.visibility = View.VISIBLE
                        else{
                            Log.d("TAGG","INSIDE ELSE")
                            holder.btnJoined.visibility = View.VISIBLE
                            holder.btnJoined.setText("Pending")
                            holder.btnJoined.isEnabled = false
                        }
                        break
                    } else {
                        holder.btnJoinGroup.visibility = View.VISIBLE
                        holder.btnJoined.visibility = View.GONE
                    }
                }
            } else {
                holder.btnJoinGroup.visibility = View.VISIBLE
                holder.btnJoined.visibility = View.GONE
            }
        }
        else{
            holder.btnJoinGroup.visibility = View.VISIBLE
            holder.btnJoined.visibility = View.GONE
            for (k in 0 until mDataListCompare.size) {
                if (mDataListCompare[k].id == mDataList[position].id) {
                    holder.btnJoinGroup.visibility = View.GONE
                    if(mDataList[position].is_member==true)
                        holder.btnJoined.visibility = View.VISIBLE
                    else{
                        holder.btnJoined.visibility = View.VISIBLE
                        holder.btnJoined.setText("Pending")
                        holder.btnJoined.isEnabled = false
                    }
                    break
                }
            }
        }


        holder.llCard.setOnClickListener {

            val bundle = Bundle()
            if (isLoggedIn && type==1) {
                Log.d("TAGG","from1 "+mDataList[position].id)
                val intent = Intent(context, ZActivityGroupsDetails::class.java)
                bundle.putBoolean("isLoggedIn", true)
                bundle.putString("group_id", mDataList[position].id.toString())
                bundle.putString("group_type", mDataList[position].groupType)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                bundle.putString("page",page)
                if(holder.btnJoined.text.equals("Pending"))
                    bundle.putInt("isMygroup", -1)
                else
                    bundle.putInt("isMygroup", 1)
                intent.putExtras(bundle)
                context!!.startActivity(intent)
            }
            else if(type>1 && isLoggedIn){
                Log.d("TAGG","from2")
                if (holder.btnJoinGroup.visibility == View.VISIBLE){
                    val intent = Intent(context, ZActivityGroupsDetails::class.java)
                    bundle.putBoolean("isLoggedIn",false)
                    bundle.putString("group_type", mDataList[position].groupType)
                    bundle.putString("group_id", mDataList[position].id.toString())
                    bundle.putInt("isMygroup",0)
                    bundle.putString("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtras(bundle)
                    context!!.startActivity(intent)
                }
                else{

                    Log.d("TAGG","from3 ")
                    val intent = Intent(context, ZActivityGroupsDetails::class.java)
                    bundle.putBoolean("isLoggedIn", true)
                    bundle.putString("group_type", mDataList[position].groupType)
                    bundle.putString("group_id", mDataList[position].id.toString())
                    if(holder.btnJoined.text.equals("Pending"))
                        bundle.putInt("isMygroup", -1)
                    else
                        bundle.putInt("isMygroup", 1)
                    bundle.putString("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtras(bundle)
                    context!!.startActivity(intent)
                }
            }
            else{
                Log.d("TAGG","from4")
                val intent = Intent(context, ZActivityGroupsDetails::class.java)
                bundle.putBoolean("isLoggedIn",false)
                bundle.putString("page",page)
                bundle.putString("group_id", mDataList[position].id.toString())
                bundle.putString("group_type", mDataList[position].groupType)
                bundle.putInt("isMygroup",0)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtras(bundle)
                context!!.startActivity(intent)
            }


        }



        holder.btnJoined.setOnClickListener {

            retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

            val call = retrofitInterface!!.ReportCheck(mDataList[position].id, EndPoints.CLIENT_ID, "Bearer "+ EndPoints.ACCESS_TOKEN)

            call.enqueue(object : Callback<DeletePostResponse> {
                override fun onResponse(call: Call<DeletePostResponse>, response: Response<DeletePostResponse>) {

                    Logger.d("CODE", response.code().toString() + "")
                    Logger.d("MESSAGE", response.message() + "")
                    Logger.d("URL", "" + response)
                    Logger.d("TAGG","CODE IS "+response.body()!!.responseCode.toString())
                    Logger.d("RESPONSE join group", "" + Gson().toJson(response))

                    if (response.isSuccessful && response.body()!!.responseCode.toString().equals("10802") ) {      //11804
                        val popupMenu: PopupMenu = PopupMenu(context,holder.btnJoined)
                        popupMenu.menuInflater.inflate(R.menu.group_popup_menu1,popupMenu.menu)
                        val positionOfMenuItem = 1 // or whatever...
                        val item = popupMenu.menu.getItem(positionOfMenuItem)
                        val s = SpannableString("Reported")
                        s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, 0)
                        item.setTitle(s)
                        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                            when(item.itemId) {
                                R.id.action_leave -> {
                                    val builder = AlertDialog.Builder(context)
                                    builder.setTitle("Leave group")
                                    builder.setMessage("Are you sure you want to leave this group?")
                                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
                                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                        if (isRecommended==0) {
                                            (context as ZActivityGroups).leaveGroup(
                                                mDataList[position].id,
                                                holder.btnJoinGroup,
                                                holder.btnJoined
                                            )
                                        }
                                        else if (isRecommended==1 && page.equals("NewsFeed")) {
                                            val bundle = Bundle()
                                            val intent = Intent(context, ZActivityGroupsDetails::class.java)
                                            bundle.putBoolean("isLoggedIn", true)
                                            bundle.putString("group_type", mDataList[position].groupType)
                                            bundle.putString("group_id", mDataList[position].id.toString())
                                            bundle.putInt("isMygroup", 1)
                                            bundle.putString("page",page)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            intent.putExtras(bundle)
                                            context!!.startActivity(intent)
                                        }
                                        else{
                                            (context as ZActivityDashboard).leaveGroup(
                                                mDataList[position].id,
                                                holder.btnJoinGroup,
                                                holder.btnJoined
                                            )
                                        }
                                    }
                                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                                        dialog.cancel()
                                    }
                                    builder.show()
                                }
                                R.id.action_report ->
                                    if (isRecommended==0)
                                        (context as ZActivityGroups).openBottomSheetReports(mDataList[position].id,"group")
                                    else (context as ZActivityDashboard).openBottomSheetReports(mDataList[position].id,"group")
                                R.id.action_reported ->
                                    Toast.makeText(context,"Already Reported", Toast.LENGTH_LONG).show()
                            }
                            true
                        })
                        popupMenu.show()

                    } else {
                        val popupMenu: PopupMenu = PopupMenu(context, holder.btnJoined)
                        popupMenu.menuInflater.inflate(R.menu.group_popup_menu,popupMenu.menu)

                        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                            when(item.itemId) {
                                R.id.action_leave -> {
                                    val builder = AlertDialog.Builder(context)
                                    builder.setTitle("Leave group")
                                    builder.setMessage("Are you sure you want to leave this group?")
                                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
                                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                        if(isRecommended==0) {
                                            (context as ZActivityGroups).leaveGroup(
                                                mDataList[position].id,
                                                holder.btnJoinGroup,
                                                holder.btnJoined
                                            )
                                        }
                                        else{
                                            (context as ZActivityDashboard).leaveGroup(
                                                mDataList[position].id,
                                                holder.btnJoinGroup,
                                                holder.btnJoined
                                            )
                                        }

                                    }
                                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                                        dialog.cancel()
                                    }
                                    builder.show()
                                }
                                R.id.action_report ->
                                    if(isRecommended==0)
                                        (context as ZActivityGroups).openBottomSheetReports(mDataList[position].id,"group")
                                    else (context as ZActivityDashboard).openBottomSheetReports(mDataList[position].id,"group")
                                R.id.action_reported ->
                                    Toast.makeText(context,"Already Reported", Toast.LENGTH_LONG).show()
                            }
                            true
                        })
                        popupMenu.show()
                    }
                }

                override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {

                    Logger.d("TAGG", "FAILED : $t")
                }
            })
        }

        holder.btnJoinGroup.setOnClickListener{


            Log.d("CODE", mDataList[position].status)
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Join group")
            builder.setMessage("Are you sure you want to join this group?")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                if (isRecommended==0) {
                    (context as ZActivityGroups).joinGroup(
                        mDataList[position].id,
                        holder.btnJoinGroup,
                        mDataList[position].groupType.toString(), holder.btnJoined
                    )
                }
                else if (isRecommended==1 && page.equals("NewsFeed")) { //for newsFeed Screen
                    val bundle = Bundle()
                    val intent = Intent(context, ZActivityGroupsDetails::class.java)
                    bundle.putBoolean("isLoggedIn", true)
                    bundle.putString("group_type", mDataList[position].groupType)
                    bundle.putString("group_id", mDataList[position].id.toString())
                    bundle.putInt("isMygroup", 0)
                    bundle.putString("page",page)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtras(bundle)
                    context!!.startActivity(intent)
                }
                else{
                    (context as ZActivityDashboard).joinGroup(
                        mDataList[position].id,
                        holder.btnJoinGroup,
                        mDataList[position].groupType.toString(), holder.btnJoined
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
        internal var row_location: TextView
        internal var row_grouptype:TextView
        internal var row_icon:ImageView
        internal var btnJoined: Button
        internal var btnJoinGroup: Button

        internal var llCard: LinearLayout

        init {
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_label = itemView.findViewById<View>(R.id.row_label) as TextView
            row_noOfMembers = itemView.findViewById<View>(R.id.row_noOfMembers) as TextView
            row_description = itemView.findViewById<View>(R.id.row_description) as TextView
            row_location = itemView.findViewById<View>(R.id.row_location) as TextView
            btnJoined = itemView.findViewById<View>(R.id.btnJoined) as Button
            row_grouptype = itemView.findViewById<View>(R.id.tv_grouptype) as TextView
            btnJoinGroup = itemView.findViewById<View>(R.id.join_group) as Button
            row_icon = itemView.findViewById<View>(R.id.row_icon) as ImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

            if (isLoggedIn)

            else{
                itemView.setOnClickListener(null)
            }
        }

    }
}