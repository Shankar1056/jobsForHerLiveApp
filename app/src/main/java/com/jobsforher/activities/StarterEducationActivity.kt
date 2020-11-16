package com.jobsforher.activities

import android.os.Bundle
import android.widget.EditText
import com.plumillonforge.android.chipview.Chip
import com.plumillonforge.android.chipview.ChipView
import com.plumillonforge.android.chipview.OnChipClickListener
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes
import com.jobsforher.R
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.ArrayList

class StarterEducationActivity : BaseActivity(), OnChipClickListener {

    override fun onChipClick(chip: Chip?) {

        mTextChipLayout!!.remove(chip)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val TAG = javaClass.simpleName
    private var retrofitInterface: RetrofitInterface? = null

    private var mTagList1: MutableList<Chip>? = null

    private var mTextChipLayout: ChipView? = null

    private var textFieldBoxes: TextFieldBoxes? = null

    private var etSkill: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_starter_education)


        mTagList1 = ArrayList()
        (mTagList1 as ArrayList<Chip>).add(com.jobsforher.models.Tag("Lorem"))
        (mTagList1 as ArrayList<Chip>).add(com.jobsforher.models.Tag("Ipsum dolor"))
        (mTagList1 as ArrayList<Chip>).add(com.jobsforher.models.Tag("Sit amet"))
        (mTagList1 as ArrayList<Chip>).add(com.jobsforher.models.Tag("Consectetur"))
        (mTagList1 as ArrayList<Chip>).add(com.jobsforher.models.Tag("adipiscing elit"))


//        mTextChipLayout.setAdapter(adapterLayout)
//        mTextChipLayout.setAdapter(adapterLayout)
//        mTextChipLayout.setChipLayoutRes(R.layout.chip_close)
//        mTextChipLayout.setChipBackgroundColor(resources.getColor(R.color.green))
//        mTextChipLayout.setChipBackgroundColorSelected(resources.getColor(R.color.green))
//        mTextChipLayout.setChipList(mTagList1)
//        mTextChipLayout.setOnChipClickListener(this)


    }

}
