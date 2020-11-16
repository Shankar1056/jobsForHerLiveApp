package com.jobsforher.activities

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.adapters.ZProfileAdapter
import com.jobsforher.models.GroupsView
import women.jobs.jobsforher.activities.BaseActivity

class ZActivityProfile : BaseActivity() {

    var listOfMyGroupdata: ArrayList<GroupsView> = ArrayList()
    var listOfMyGroupdataLoad: ArrayList<GroupsView> = ArrayList()
    var mgroupsRecyclerView: RecyclerView? = null
    var mgroupsAdapter: RecyclerView.Adapter<*>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zactivity_profile)

//        listOfMyGroupdata.add(
//            GroupsView(0,"Women Freelancers","Working Women, Women Professionals","","Open Group","9652",
//                "Wish to work as per your time of convenience? " +
//                        "Come aboard this group of freelancers to stay in the know of all things that matter in your world.")
//        )
//        listOfMyGroupdata.add(
//            GroupsView(0,"Women in AI/ML","Restars, Career Development, Working Women, Women in Tech, Steminists, Women Professionals","Any","Open Group","468",
//                "If you are looking for career opportunities in AI/ML, this group is for you. Find relevant jobs and benefit " +
//                        "from the knowledge and experience of other women here.")
//        )
//        listOfMyGroupdata.add(
//            GroupsView(0,"Women Freelancers","Working Women, Women Professionals","","Open Group","9652",
//                "Wish to work as per your time of convenience? " +
//                        "Come aboard this group of freelancers to stay in the know of all things that matter in your world.")
//        )

        mgroupsRecyclerView = findViewById(R.id.recycler_view_groups)
        var mgroupsLayoutManager = GridLayoutManager(this,1)
        mgroupsRecyclerView!!.layoutManager = mgroupsLayoutManager
        mgroupsAdapter = ZProfileAdapter(listOfMyGroupdata)
        mgroupsRecyclerView!!.adapter = mgroupsAdapter
    }
}