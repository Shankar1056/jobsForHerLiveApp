package com.jobsforher.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.WebActivity
import com.jobsforher.models.BlogsView
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BlogsAdapter(private val mDataList: ArrayList<BlogsView>, isloggedin:Boolean, type:Int, filter:Int) : RecyclerView.Adapter<BlogsAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var isLoggedIn: Boolean = isloggedin
    private var type: Int= type
    private var filter: Int= filter
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blogs_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_name.text = mDataList[position].title
        holder.row_description.text = mDataList[position].description

        if (mDataList[position].image_url!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }

        holder.row_readmore.setOnClickListener{
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("value","https://www.jobsforher.com/blogs")
            (context as Activity).startActivity(intent)
        }

        holder.llCard.setOnClickListener {

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


    }

    fun getTime(times:String):String{
        val tk = StringTokenizer(times)
        val date = tk.nextToken()
        val time = tk.nextToken()
        val sdf = SimpleDateFormat("hh:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        val dt: Date
        dt = sdf.parse(time)
        return sdfs.format(dt)
    }

    fun getDate(times:String):String{
        val tk = StringTokenizer(times)
        val date = tk.nextToken()
        val time = tk.nextToken()
        return date
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_name: TextView
        internal var row_description:TextView
        internal var row_icon:ImageView
        internal var llCard: LinearLayout
        internal var row_readmore: Button

        init {
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_description = itemView.findViewById<View>(R.id.row_description) as TextView

            row_icon = itemView.findViewById<View>(R.id.row_icon) as ImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

            row_readmore = itemView.findViewById<View>(R.id.read_more) as Button

            if (isLoggedIn)

            else{
                itemView.setOnClickListener(null)
            }
        }

    }
}