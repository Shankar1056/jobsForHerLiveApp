package com.jobsforher.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.adapters.ZGroupsAllCategoryAdapter
import com.jobsforher.models.Groups


class ZActivityGroupsAllCategories : AppCompatActivity(){


    var listOfusers: ArrayList<Groups> = ArrayList()
    var mRecyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zgroups_allcategories_toolbr)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        listOfusers.add(Groups(0,"JobsForHer Ambassador Tribe","null"))
        listOfusers.add(Groups(0,"Restars","null"))
        listOfusers.add(Groups(0,"Women in Tech","null"))
        listOfusers.add(Groups(0,"Working women","null"))
        listOfusers.add(Groups(0, "Working Professionals","null"))
        listOfusers.add(Groups(0,"Career Development","null"))
        listOfusers.add(Groups(0, "Back to work", "null"))
        listOfusers.add(Groups(0, "EventsForHer", "null"))
        listOfusers.add(Groups(0, "Career Guidance", "null"))
        listOfusers.add(Groups(0, "JobsForHer Foundation", "null"))
        listOfusers.add(Groups(0, "Pledge for women", "null"))
        listOfusers.add(Groups(0, "Women Entrepreneurs", "null"))
        listOfusers.add(Groups(0,"Women Alumini Network","null"))
        listOfusers.add(Groups(0,"Steminists","null"))
        listOfusers.add(Groups(0,"Job Board","null"))
        listOfusers.add(Groups(0,"Personal Development","null"))
        listOfusers.add(Groups(0,"Women WriteHers","null"))

        mRecyclerView = findViewById(R.id.my_recycler_view)
        var mLayoutManager = GridLayoutManager(this, 2)
        mRecyclerView!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
//        mRecyclerView!!.addItemDecoration(CirclePagerIndicatorDecoration())
        mAdapter = ZGroupsAllCategoryAdapter(listOfusers)
        mRecyclerView!!.adapter = mAdapter
    }
}
