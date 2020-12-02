package com.jobsforher.ui.preference.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jobsforher.R
import com.jobsforher.data.model.PreferenceFunctionalAreaBody
import com.jobsforher.helpers.Constants
import com.jobsforher.ui.newsfeed.NewsFeedViewModel
import com.jobsforher.ui.preference.PrefereceViewModel
import com.jobsforher.ui.preference.adapter.PreferenceAreaAdapter
import com.jobsforher.util.Utility
import kotlinx.android.synthetic.main.fragment_city.*

class FunctionalAreaFragment(val name: String) : Fragment() {
    val viewModel: PrefereceViewModel by activityViewModels()
    val newsFeedViewModel: NewsFeedViewModel by activityViewModels()
    private lateinit var functionalAeaAdapter: PreferenceAreaAdapter
    private var allFuctioalAreaList = ArrayList<PreferenceFunctionalAreaBody>()
    private var fuctioalAreaList = ArrayList<PreferenceFunctionalAreaBody>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWidgit()

        getviewModelValue()

        viewModel.getFunctionalArea(name)
    }

    private fun getviewModelValue() {

        activity?.let {
            viewModel.functionalAreaList.observe(it, Observer {
                allFuctioalAreaList.addAll(it)
                if (allFuctioalAreaList.size > 8) {
                    for (i in 0 until 5)
                        fuctioalAreaList.add(it.get(i))
                } else {
                    fuctioalAreaList.addAll(it)
                }
                functionalAeaAdapter.notifyDataSetChanged()
                setValueInAutoCompleteTextView()
            })
        }

        activity?.let {
            viewModel.preferenceSuccessMessage.observe(it, Observer {
                activity?.let { context ->
                    Utility.showToast(context, it)
                }
                newsFeedViewModel.loadPreference()
            })
        }

    }

    private fun setValueInAutoCompleteTextView() {
        val list = ArrayList<String>()
        for (item in allFuctioalAreaList) {
            item.name?.let { list.add(it) }
        }
        val locationArray =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, list) }

        pref_city.setAdapter(locationArray)


    }

    private fun initWidgit() {


        cityRv.layoutManager = LinearLayoutManager(activity)

        if (name == Constants.TYPE_AREAS) {
            pref_city.hint = resources.getString(R.string.type_functional_area)
        } else if (name == Constants.TYPE_INDUSTRIES) {
            pref_city.hint = resources.getString(R.string.type_functional_industries)
        } else if (name == Constants.TYPE_JOB) {
            cityRv.layoutManager = GridLayoutManager(activity, 2)
            pref_city.hint = resources.getString(R.string.type_functional_job)
        }

        pref_city.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(
                activity,
                parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT
            ).show()
            for (item in allFuctioalAreaList) {
                if (parent.getItemAtPosition(position).toString() == item.name) {
                    item.isClicked = true
                    checkAndAddItem(item)
                    functionalAeaAdapter?.notifyDataSetChanged()
                    pref_city.setText("")
                    return@setOnItemClickListener
                }
            }
        }



        functionalAeaAdapter =
            PreferenceAreaAdapter(fuctioalAreaList, object : PreferenceAreaAdapter.CityItemClick {
                override fun onItemClicked(position: Int) {
                    fuctioalAreaList[position].isClicked =
                        fuctioalAreaList[position].isClicked != true
                    functionalAeaAdapter?.notifyDataSetChanged()
                }

            })
        cityRv.adapter = functionalAeaAdapter


        cityupdate.setOnClickListener {
            viewModel.addUpdateFunctionalArea(allFuctioalAreaList, name)
        }

    }

    private fun checkAndAddItem(item: PreferenceFunctionalAreaBody) {
        for (data in fuctioalAreaList) {
            if (item.name == data.name) {
                return
            }
        }
        fuctioalAreaList.add(item)
    }


}