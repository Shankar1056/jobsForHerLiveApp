package com.jobsforher.helpers

import android.content.Context
import android.widget.Toast



object ToastHelper
{
    fun makeToast(context: Context, msg: String)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

}