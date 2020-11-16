package com.jobsforher.adapters

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.CallbackManager
import com.squareup.picasso.Picasso
import com.jobsforher.R
import com.jobsforher.models.GroupsEditedHistoryModel
import com.jobsforher.network.retrofithelpers.RetrofitInterface

class EditedHistoryPostsAdapter(private val mDataList: ArrayList<GroupsEditedHistoryModel>) : RecyclerView.Adapter<EditedHistoryPostsAdapter.MyViewHolder>() {


    public var context: Context? = null
    private var retrofitInterface: RetrofitInterface? = null
    private var callbackManager: CallbackManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.editedpost_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.comment_name.text = mDataList[position].username
        holder.comment_datetime.text = mDataList[position].modified_on_str
        holder.comment_data.text= Html.fromHtml(mDataList[position].description)

        Log.d("TAGG", mDataList[position].post_type.toString())
        if(mDataList[position].profile_icon!!.isNotEmpty()) {
            Picasso.with(context)
                .load(mDataList[position].profile_icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into( holder.commenticon)
        }else{
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into( holder.commenticon)
        }

        if (mDataList[position].post_type.toString().equals("text")){

            holder.row_image.visibility = View.GONE
            holder.row_document.visibility = View.GONE
            holder.progressBar.setVisibility(View.GONE)
        }
        else if(mDataList[position].post_type.toString().equals("document")){
            holder.row_image.visibility = View.GONE
            holder.row_document.visibility = View.VISIBLE
            holder.row_document.getSettings().setJavaScriptEnabled(true);
//            holder.row_document!!.webViewClient = object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                    view?.loadUrl(url)
//                    return true
//                }
//            }
            holder.row_document.setWebViewClient(object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    // do your stuff here
                    holder.progressBar.setVisibility(View.GONE)
                }
            })
//            "http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf"
            holder.row_document.getSettings().setBuiltInZoomControls(true);
            holder.row_document.setHorizontalScrollBarEnabled(true);
            holder.row_document.setVerticalScrollBarEnabled(true);
            holder.row_document.loadUrl("http://docs.google.com/gview?embedded=true&url="+mDataList[position].url);
        }
        else if (mDataList[position].post_type.toString().equals("video")){
            holder.row_image.visibility = View.GONE
            holder.progressBar.setVisibility(View.GONE)
            holder.row_document.setWebViewClient(WebViewClient())
            holder.row_document.getSettings().setJavaScriptEnabled(true)
            holder.row_document.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
            holder.row_document.getSettings().setPluginState(WebSettings.PluginState.ON)
            holder.row_document.getSettings().setMediaPlaybackRequiresUserGesture(false)
            holder.row_document.setWebChromeClient(WebChromeClient())
            var text = mDataList[position].url.toString().replace("watch?","embed/")
            val splitText = text.split("v=")
            val v = splitText[1].split("&".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            Log.d("TAGG",mDataList[position].url.toString().replace("watch?v=","embed/"))
            holder.row_document.loadUrl("https://www.youtube.com/embed/"+v[0])
        }
        else {
            Log.d("TAGG",mDataList[position].url.toString())
            holder.row_document.visibility = View.GONE
            holder.progressBar.setVisibility(View.GONE)
            if(mDataList[position].url!!.isNotEmpty()) {
                Picasso.with(context)
                    .load("http://jfh-testing.s3.amazonaws.com/"+mDataList[position].url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.row_image)
            }else{
                Picasso.with(context)
                    .load(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.row_image)
            }
        }

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var comment_name: TextView
        internal var comment_datetime: TextView
        internal var comment_data: TextView
        internal var commenticon:ImageView
        internal var row_image: ImageView
        internal var row_document:WebView
        internal var progressBar:ProgressBar

        init {
            comment_name= itemView.findViewById<View>(R.id.commentusername) as TextView
            comment_datetime= itemView.findViewById<View>(R.id.commentdatetime) as TextView
            comment_data= itemView.findViewById<View>(R.id.commentdata) as TextView
            commenticon = itemView.findViewById<View>(R.id.commenticon) as ImageView

            row_image = itemView.findViewById<View>(R.id.pick_image) as ImageView
            row_document = itemView.findViewById<View>(R.id.postdocument) as WebView
            progressBar = itemView.findViewById<View>(R.id.progressbar) as ProgressBar
        }
    }

}