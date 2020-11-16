package com.jobsforher.activities

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.jobsforher.R

class WebActivity : AppCompatActivity() {
    var mywebview: WebView? = null
    var weburl: String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_web)
        weburl=intent.getStringExtra("value")
        mywebview = findViewById<WebView>(R.id.webview)
        mywebview!!.getSettings().setJavaScriptEnabled(true);
        mywebview!!.getSettings().setDomStorageEnabled(true);
        mywebview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        mywebview!!.loadUrl(weburl)
    }
}