package com.jobsforher.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_categories.*
import com.jobsforher.R
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jobsforher.adapters.CateoryAdapter
import com.jobsforher.helpers.Logger
import com.jobsforher.helpers.ToastHelper
import com.jobsforher.models.CategoriesMainView
import com.jobsforher.network.responsemodels.GroupCategoriesResponse
import com.jobsforher.network.retrofithelpers.EndPoints
import com.jobsforher.network.retrofithelpers.RetrofitClient
import com.jobsforher.network.retrofithelpers.RetrofitInterface
import women.jobs.jobsforher.activities.BaseActivity
import java.util.HashMap


class GroupsCategoryPage: BaseActivity(){

    var flag: Int = 0
    var quotient:Int=0
    var remainder:Int=0
    var countValue:Int=0
    var mRecyclerView: RecyclerView? = null
    var mAdapter: RecyclerView.Adapter<*>? = null

    var listOfCategories: ArrayList<CategoriesMainView> = ArrayList()

    var listOfCategoriesLoad: ArrayList<CategoriesMainView> = ArrayList()

    private var retrofitInterface: RetrofitInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        getAllCategories()

        back_button.setOnClickListener {

            //            val intent = Intent()
//            intent.putExtra("VAL", "")
            setResult(10, intent)
            finish()
        }

        more_button.visibility = View.GONE

    }


    fun getAllCategories(){

        listOfCategories.clear()
        listOfCategoriesLoad.clear()

        val params = HashMap<String, String>()

        params["page_no"] = 1.toString()
        params["size"] = 5.toString()

        retrofitInterface = RetrofitClient.client!!.create(RetrofitInterface::class.java)

        val call = retrofitInterface!!.getMainCategories(EndPoints.CLIENT_ID,params)

        call.enqueue(object : Callback<GroupCategoriesResponse> {
            override fun onResponse(call: Call<GroupCategoriesResponse>, response: Response<GroupCategoriesResponse>) {

//                ToastHelper.makeToast(applicationContext, "Categories")

                Logger.d("CODE", response.code().toString() + "")
                Logger.d("MESSAGE", response.message() + "")
                Logger.d("URL", "" + response)
                Logger.d("RESPONSE Categories", "" + Gson().toJson(response))

                if (response.isSuccessful) {
//                    ToastHelper.makeToast(applicationContext, response.body()!!.message.toString())

                    val str_response = Gson().toJson(response)
                    val jsonObject : JSONObject = JSONObject(str_response.substring(str_response.indexOf("{"),
                        str_response.lastIndexOf("}")+1))
                    val jsonObject1 : JSONObject = jsonObject.optJSONObject("body")
                    val jsonarray_info: JSONArray = jsonObject1.getJSONArray("body")


                    for (i in 0 until response.body()!!.body!!.size){

                        val json_objectdetail: JSONObject =jsonarray_info.optJSONObject(i)

                        val model: CategoriesMainView = CategoriesMainView();

                        model.id = json_objectdetail.getInt("id")
                        model.name = json_objectdetail.getString("name")
                        model.image_url = json_objectdetail.getString("image_url")
                        model.status = json_objectdetail.getString("status")
                        model.created_on = json_objectdetail.getString("created_on")
                        model.modified_on = json_objectdetail.getString("modified_on")

                        listOfCategories.add(
                            CategoriesMainView(model.id, model.name!!, model.image_url!!,
                                model.status!!, model.created_on!!, model.modified_on!!)
                        )

                    }


                    mRecyclerView = findViewById(R.id.my_recycler_view)
                    val mLayoutManager = GridLayoutManager(applicationContext, 2)
                    mRecyclerView!!.layoutManager = mLayoutManager
                    mAdapter = CateoryAdapter(listOfCategories,1)
                    mRecyclerView!!.adapter = mAdapter

                } else {
                    ToastHelper.makeToast(applicationContext, "Invalid Request")
                }
            }

            override fun onFailure(call: Call<GroupCategoriesResponse>, t: Throwable) {

                Logger.d("TAGG", "FAILED : $t")
            }
        })

    }


    fun sendData(value:String){
        val intent = Intent()
        intent.putExtra("VAL", value)
        setResult(20, intent)
        finish()
    }


    override fun onBackPressed() {

    }
}

