package com.jobsforher.expert_chat

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bigappcompany.healthtunnel.data.network.ApiCallback
import com.jobsforher.data.model.ExpertChatBody
import com.jobsforher.data.model.ExpertChatReq
import com.jobsforher.data.model.ExpertChatResponse
import com.jobsforher.helpers.HelperMethods
import com.jobsforher.network.retrofithelpers.EndPoints
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class ExpertChatViewModel(val app: Application) : AndroidViewModel(app) {
    var selectedMonth = MutableLiveData<String>("October")
    var selectedYear = MutableLiveData<String>("2020")
    var selected1Week = MutableLiveData<String>()
    var selected2Week = MutableLiveData<String>()
    var selected3Week = MutableLiveData<String>()
    var selected4Week = MutableLiveData<String>()
    var selected5Week = MutableLiveData<String>()
    var expertChatList = MutableLiveData<ArrayList<ExpertChatBody>>()
    var expertChatForFilter = ArrayList<ExpertChatBody>()
    var weekClicked = MutableLiveData<Int>(1)
    lateinit var context: Activity
    private var currentMonth by Delegates.notNull<Int>()
    private var currentYear by Delegates.notNull<Int>()

    fun getCurrentDate(context: Activity) {
        this.context = context
        getCurrentDate()
        // monthClicked("Months", HelperMethods.getMonths())
    }

    private fun getCurrentDate() {
        val now = Calendar.getInstance()
        currentYear = now.get(Calendar.YEAR)
        currentMonth = now.get(Calendar.MONTH)
        selectedYear.value = currentYear.toString()
        selectedMonth.value = HelperMethods.getMonthsInString(currentMonth+1)

        /*val format = SimpleDateFormat("MM/dd/yyyy")

        val days = arrayOfNulls<String>(7)
        val delta = -now[GregorianCalendar.DAY_OF_WEEK] + 2 //add 2 if your week start on monday

        now.add(Calendar.DAY_OF_MONTH, delta)
        for (i in 0..6) {
            days[i] = format.format(now.time)
            now.add(Calendar.DAY_OF_MONTH, 1)
        }
        System.out.println(Arrays.toString(days))*/


    }

    fun getExpertChat() {
        val model = ExpertChatReq()
        model.month = HelperMethods.getMonthsInInt(selectedMonth.value)
        try {
            model.year = selectedYear.value?.toInt()
        } catch (e : Exception){
            e.printStackTrace()
        }

        model.getExpertChat(
            "Bearer ${EndPoints.ACCESS_TOKEN}", model,
            object : ApiCallback() {
                override fun onSuccess(obj: Any?) {
                    val response = obj as ExpertChatResponse

                    //expertChatList.value = response.body
                    if (response.body != null) {
                        expertChatForFilter = response.body!!
                    }
                }

                override fun onHandledError() {
                }
            })
    }

    fun getExpertChatWeekly(startDateOfTheWeek: Int, endDateOfTheWeek: Int) { //23 29
        if (expertChatForFilter == null) {
            return
        }
        for (data in (ArrayList<ExpertChatBody>(expertChatForFilter))) {
            val last2Char = data.date?.substring(Math.max(data.date?.length!! - 2, 0)) //06
            if (last2Char != null) {
                if (last2Char.toInt() < startDateOfTheWeek || last2Char.toInt() > endDateOfTheWeek) {
                    expertChatForFilter.remove(data)
                }

            }
        }

        expertChatList.value = expertChatForFilter
    }


    fun onMonthClicked(view: View) {
        monthClicked("Months", HelperMethods.getMonths())
    }


    fun onYearClicked(view: View) {
        monthClicked("Years", HelperMethods.getYears())

    }

    private fun monthClicked(clickedName: String, monthsYearList: ArrayList<String>) {
        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(context)
        builderSingle.setTitle("Select $clickedName")

        val arrayAdapter =
            ArrayAdapter<String>(context, R.layout.select_dialog_singlechoice)
        arrayAdapter.addAll(monthsYearList)


        builderSingle.setNegativeButton("cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

        builderSingle.setAdapter(arrayAdapter,
            DialogInterface.OnClickListener { dialog, which ->
                val strName = arrayAdapter.getItem(which)
                when (clickedName) {
                    "Years" -> selectedYear.value = strName
                    "Months" -> selectedMonth.value = strName
                }

                dialog.dismiss()
            })
        builderSingle.show()
    }

    fun week1Clicked(view: View) {
        weekClicked.value = 1
    }

    fun week2Clicked(view: View) {
        weekClicked.value = 2
    }

    fun week3Clicked(view: View) {
        weekClicked.value = 3
    }

    fun week4Clicked(view: View) {
        weekClicked.value = 4
    }

    fun week5Clicked(view: View) {
        weekClicked.value = 5
    }

    fun onGoClicked(view: View) {
        getExpertChat()
    }

}