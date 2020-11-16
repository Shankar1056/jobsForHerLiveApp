package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.jobsforher.R
import com.squareup.picasso.Picasso


internal class GridViewAdapter internal constructor(context: Context, private val resource: Int, private val itemList: ArrayList<String>?) : ArrayAdapter<GridViewAdapter.ItemHolder>(context, resource) {

    override fun getCount(): Int {
        return if (this.itemList != null) this.itemList.size else 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ItemHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null)
            holder = ItemHolder()
            holder.icon = convertView.findViewById(R.id.icon)
            convertView.tag = holder
            Picasso.with(context)
                .load(this.itemList!![position])
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.icon)
        } else {
            holder = convertView.tag as ItemHolder
        }

//        holder.name!!.text = this.itemList!![position]
//        holder.icon!!.setImageResource(R.mipmap.ic_launcher)

        return convertView!!
    }

    internal class ItemHolder {
        //        var name: TextView? = null
        var icon: ImageView? = null
    }
}
