package com.jobsforher.ui.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.jobsforher.R
import com.jobsforher.data.model.PreferenceCityBody
import com.jobsforher.ui.preference.adapter.PreferenceCityAdapter
import kotlinx.android.synthetic.main.fragment_city.*

class CityFragment : Fragment() {
    val viewModel: PrefereceViewModel by activityViewModels()
    private var cityAdapter: PreferenceCityAdapter? = null
    private var cityList = ArrayList<PreferenceCityBody>()
    private var allCityList = ArrayList<PreferenceCityBody>()

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

        viewModel.getCity()
    }

    private fun initWidgit() {

        pref_city.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(activity, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
            for (item in allCityList){
                if (parent.getItemAtPosition(position).toString() == item.value){
                    item.isClicked = true
                    cityList.add(item)
                    cityAdapter?.notifyDataSetChanged()
                    pref_city.setText("")
                    return@setOnItemClickListener
                }
            }
        }



        cityAdapter = PreferenceCityAdapter(cityList, object : PreferenceCityAdapter.CityItemClick {
            override fun onItemClicked(position: Int) {
                allCityList[position].isClicked = allCityList[position].isClicked != true
                cityAdapter?.notifyDataSetChanged()
            }

        })
        cityRv.adapter = cityAdapter


        cityupdate.setOnClickListener {
            viewModel.addUpdateCity(allCityList)
        }
    }

    private fun getviewModelValue() {
        activity?.let { it ->
            viewModel.cityList.observe(it, Observer {
                for (item in it){
                    if (item.ordinal == 0){
                        cityList.add(item)
                    }
                }
                allCityList.addAll(it)
                cityAdapter?.notifyDataSetChanged()
                setValueInAutoCompleteTextView()
            })
        }

    }

    private fun setValueInAutoCompleteTextView() {
        val list = ArrayList<String>()
        for (item in allCityList){
            item.value?.let { list.add(it) }
        }
        val locationArray =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, list) }

        pref_city.setAdapter(locationArray)


    }
}