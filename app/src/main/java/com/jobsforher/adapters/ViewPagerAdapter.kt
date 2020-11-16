package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.items_view_pager.view.*
import com.jobsforher.R
import com.jobsforher.models.ImageModel

class ViewPagerAdapter(val context: Context, private val mDataList: ArrayList<ImageModel>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater = LayoutInflater.from(context)
        val viewGroup = layoutInflater.inflate(R.layout.items_view_pager, container, false)

        val image = viewGroup.view_pager_image

        image.setImageResource(mDataList.get(position).image!!)

        container.addView(viewGroup)
        return viewGroup
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {

        return p0 == p1
    }

    override fun getCount(): Int {
        return mDataList.size
    }
}
