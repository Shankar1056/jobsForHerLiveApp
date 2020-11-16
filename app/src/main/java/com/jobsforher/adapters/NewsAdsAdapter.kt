package com.jobsforher.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.activities.*
import com.jobsforher.network.retrofithelpers.RetrofitInterface


class NewsAdsAdapter(private val mDataList: ArrayList<String>, val mDataList1: ArrayList<String>,val mDataList2: ArrayList<String>, isloggedin:Boolean, type:Int, filter:Int) : RecyclerView.Adapter<NewsAdsAdapter.MyViewHolder>() {

    private var context: Context? = null
    private var isLoggedIn: Boolean = isloggedin
    private var type: Int= type
    private var filter: Int= filter
    private var retrofitInterface: RetrofitInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //  val view = LayoutInflater.from(parent.context).inflate(R.layout.newsads_row, parent, false)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newsads_row1, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

//        holder.row_name.text = mDataList[position].toString()
//        holder.row_name1.text = mDataList1[position].toString()
//        holder.row_name2.text = mDataList2[position].toString()
//        holder.row_name2.setTag(position)
        if (position==0){
            holder.row_icon.setImageResource(R.drawable.news_1)
        }
        else if(position==1)
            holder.row_icon.setImageResource(R.drawable.news_2)
        else if(position==2)
            holder.row_icon.setImageResource(R.drawable.news_3)
        else if(position==3)
            holder.row_icon.setImageResource(R.drawable.news_4)
        else if(position==4)
            holder.row_icon.setImageResource(R.drawable.news_5)


        holder.row_icon.setOnClickListener {
            if(position==0){
                val intent = Intent(context, ZActivityGroups::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("isLoggedIn", true)
                (context as Activity).startActivity(intent)
            }
            else if(position==1){
                val intent = Intent(context, ZActivityJobs::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("isLoggedIn", true)
                (context as Activity).startActivity(intent)
            }
            else if(position==2){
                val intent = Intent(context, SignUpWelcomeActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("isLoggedIn", true)
                (context as Activity).startActivity(intent)
            }
            else if(position==3){
                val intent = Intent(context, ZActivityCompanies::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("isLoggedIn", true)
                (context as Activity).startActivity(intent)
            }
            else if(position==4){
                val intent = Intent(context, ZActivityEvents::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("isLoggedIn", true)
                (context as Activity).startActivity(intent)
            }
        }

//        holder.row_name2.setOnClickListener {
//
//            if(holder.row_name2.text.equals("Create Profile +")){
//                val intent = Intent(context, SignUpWelcomeActivity::class.java)
//                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                        intent.putExtra("isLoggedIn", true)
//                        (context as Activity).startActivity(intent)
//            }
//            else if(holder.row_name2.text.equals("Join Group")) {
//                val intent = Intent(context, ZActivityGroups::class.java)
//                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                        intent.putExtra("isLoggedIn", true)
//                        (context as Activity).startActivity(intent)
//            }
//            else if(holder.row_name2.text.equals("Register for free")) {
//                val intent = Intent(context, ZActivityEvents::class.java)
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                intent.putExtra("isLoggedIn", true)
//                (context as Activity).startActivity(intent)
//            }
//        }

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        internal var row_name: TextView
//        internal var row_name1: TextView
//        internal var row_name2: TextView
        internal var row_icon:ImageView
        internal var llCard: LinearLayout

        init {
//            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
//            row_name1 = itemView.findViewById<View>(R.id.row_name1) as TextView
//            row_name2 = itemView.findViewById<View>(R.id.row_data) as TextView

            row_icon = itemView.findViewById<View>(R.id.row_icon) as ImageView

            llCard = itemView.findViewById<View>(R.id.llCard) as LinearLayout

            if (isLoggedIn)

            else{
                itemView.setOnClickListener {
                    //                    Toast.makeText(context, adapterPosition.toString(), Toast.LENGTH_SHORT).show()
////                    if(adapterPosition==0){
////                        val intent = Intent(context, ProfileView::class.java)
////                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
////                        intent.putExtra("isLoggedIn", true)
////                        (context as Activity).startActivity(intent)
////                    }
////                    else if(adapterPosition==1){
////                        val intent = Intent(context, ZActivityGroups::class.java)
////                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
////                        intent.putExtra("isLoggedIn", true)
////                        (context as Activity).startActivity(intent)
////                    }
////                    else if(adapterPosition==2){
////                        val intent = Intent(context, ZActivityEvents::class.java)
////                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
////                        intent.putExtra("isLoggedIn", true)
////                        (context as Activity).startActivity(intent)
////                    }
                }
            }

//            fun onClick(v: View) {
//                Toast.makeText(context, adapterPosition.toString(), Toast.LENGTH_SHORT).show()
//            }
        }



    }
}