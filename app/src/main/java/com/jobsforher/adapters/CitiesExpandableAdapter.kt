package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.jobsforher.R
import com.jobsforher.models.Cities
import java.util.ArrayList

class CitiesExpandableAdapter(
    internal var ctx: Context,
    var childList: ArrayList<ArrayList<Cities>>,
    private val parents: Array<String>) : BaseExpandableListAdapter() {

    init {
        this.childList = childList

    }

    override fun getGroupCount(): Int {
        return childList.size
    }

    override fun getChildrenCount(parent: Int): Int {
        return parents.size
    }

    override fun getGroup(parent: Int): Any {

        return parents[parent]
    }

    override fun getChild(parent: Int, child: Int): Cities {
        return childList[parent][child]
    }

    override fun getGroupId(parent: Int): Long {
        return parent.toLong()
    }

    override fun getChildId(parent: Int, child: Int): Long {
        return child.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(parent: Int, isExpanded: Boolean, convertView: View?, parentview: ViewGroup): View {
        var convertView1 = convertView

        if (convertView1 == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView1 = inflater.inflate(R.layout.parent_layout, parentview, false)
        }

        val parent_textvew = convertView1!!.findViewById(R.id.parent_txt) as TextView
        parent_textvew.text = parents[parent]
        return convertView1
    }

    override fun getChildView(
        parent: Int,
        child: Int,
        isLastChild: Boolean,
        convertView: View?,
        parentview: ViewGroup
    ): View {
        var convertView = convertView

        if (convertView == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.child_layout, parentview, false)
        }

        val child_textvew = convertView!!.findViewById(R.id.child_txt) as TextView
        child_textvew.text = getChild(parent, child).name
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    companion object {
        lateinit var childList: ArrayList<ArrayList<Cities>>
    }
}
