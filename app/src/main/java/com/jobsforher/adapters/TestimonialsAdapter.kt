package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.blogc.android.views.ExpandableTextView
import com.jobsforher.R
import com.jobsforher.models.TestimonialsView
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TestimonialsAdapter(private val mDataList: ArrayList<TestimonialsView>, isloggedin:Boolean, type:Int, filter:Int) : RecyclerView.Adapter<TestimonialsAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var isLoggedIn: Boolean = isloggedin
    private var type: Int= type
    private var filter: Int= filter
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.testimonials_row, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.setIsRecyclable(false);
        holder.row_name.text = mDataList[position].name
        holder.row_description.text = mDataList[position].testimonial
        holder.row_designation.text = mDataList[position].designation

        if (mDataList[position].image_icon!!.length>0) {
            Picasso.with(context)
                .load(mDataList[position].image_icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }
        else{
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_icon)
        }

        if (holder.row_description.text.length>40)
            holder.row_seemore.visibility = View.VISIBLE
//        else
//            holder.row_seemore.visibility = View.


        holder.row_seemore.setOnClickListener {

            holder.row_description.toggle()
            if (holder.row_seemore.text.equals("See More"))
                holder.row_seemore.setText("See Less")
            else
                holder.row_seemore.setText("See More")
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
        internal var row_description:ExpandableTextView
        internal var row_designation:TextView
        internal var row_icon:ImageView
        internal var llCard: LinearLayout
        internal var row_seemore:TextView

        init {
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_description = itemView.findViewById<View>(R.id.row_description) as ExpandableTextView
            row_seemore = itemView.findViewById<View>(R.id.seemore_test) as TextView
            row_designation = itemView.findViewById<View>(R.id.row_designation) as TextView

            row_icon = itemView.findViewById<View>(R.id.row_icon) as CircleImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

            if (isLoggedIn)

            else{
                itemView.setOnClickListener(null)
            }
        }

    }
}