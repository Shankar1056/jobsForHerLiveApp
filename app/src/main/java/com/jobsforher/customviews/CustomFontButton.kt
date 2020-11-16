package com.jobsforher.customviews

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.jobsforher.R


class CustomFontButton : AppCompatButton {

    constructor(context: Context) : super(context) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    override fun setTypeface(tf: Typeface?, style: Int) {

        super.setTypeface(tf, style)

    }


    fun init() {
        val typeface: Typeface?

        if (Build.VERSION.SDK_INT >= 26) {
            typeface = resources.getFont(R.font.robotoregular)

        } else {
            typeface = ResourcesCompat.getFont(context, R.font.robotoregular)
        }

        setTypeface(typeface)
    }
}
