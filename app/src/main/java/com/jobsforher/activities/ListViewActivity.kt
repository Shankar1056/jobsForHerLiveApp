package com.jobsforher.activities

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.arindicatorview.ARIndicatorView
import kotlinx.android.synthetic.main.activity_list_view.*
import com.jobsforher.R
import com.jobsforher.adapters.ListViewAdapter

class ListViewActivity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var arIndicatorView: ARIndicatorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        listView = list_view
        arIndicatorView = ar_indicator_list_view

        listView.adapter = ListViewAdapter(this@ListViewActivity)
    }
}

