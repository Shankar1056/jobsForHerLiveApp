package com.jobsforher.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.CallbackManager
import com.jobsforher.R
import com.jobsforher.models.GroupsEditedCommentsHistoryModel
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class EditedHistoryCommentsAdapter(private val mDataList: ArrayList<GroupsEditedCommentsHistoryModel>) : RecyclerView.Adapter<EditedHistoryCommentsAdapter.MyViewHolder>() {


    public var context: Context? = null
    private var retrofitInterface: RetrofitInterface? = null
    private var callbackManager: CallbackManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.editedcomment_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.comment_name.text = mDataList[position].username
        holder.comment_datetime.text = mDataList[position].modified_on_str
        holder.comment_data.text= Html.fromHtml(mDataList[position].entity_value)

        if(mDataList[position].profile_icon!!.isNotEmpty()) {
            Picasso.with(context)
                .load(mDataList[position].profile_icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.commenticon)
        }else{
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.commenticon)
        }

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var comment_name: TextView
        internal var comment_datetime: TextView
        internal var comment_data: TextView
        internal var commenticon: ImageView


        init {
            comment_name= itemView.findViewById<View>(R.id.commentusername) as TextView
            comment_datetime= itemView.findViewById<View>(R.id.commentdatetime) as TextView
            comment_data= itemView.findViewById<View>(R.id.commentdata) as TextView
            commenticon=itemView.findViewById<View>(R.id.commenticon) as ImageView

        }
    }

}