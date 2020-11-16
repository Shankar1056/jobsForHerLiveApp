package com.jobsforher.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import java.util.regex.Pattern

class ZGroupsVideosAdapter(private val mDataList: ArrayList<String>) : RecyclerView.Adapter<ZGroupsVideosAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.zgroups_videos_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

//        Picasso.with(context)
//            .load("http://cdn.journaldev.com/wp-content/uploads/2016/11/android-image-picker-project-structure.png")
//            .placeholder(R.drawable.ic_launcher_foreground)
//            .into(holder.row_image)

        holder.row_video.setWebViewClient(WebViewClient())
        holder.row_video.getSettings().setJavaScriptEnabled(true)
        holder.row_video.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        holder.row_video.getSettings().setPluginState(WebSettings.PluginState.ON)
        holder.row_video.getSettings().setMediaPlaybackRequiresUserGesture(false)
        holder.row_video.setWebChromeClient(WebChromeClient())

        var text = mDataList[position].toString().replace("watch?","embed/")
        var t = extractYTId(text)
        Log.d("TAGG","URL IS"+t)
        if (t!!.contains("v=")) {
            val splitText = t!!.split("v=")
            val v = splitText[1].split("&".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            Log.d("TAGG",mDataList[position].toString().replace("watch?v=","embed/"))
            holder.row_video.loadUrl("https://www.youtube.com/embed/"+v[0])
        }
        else {
            holder.row_video.loadUrl("https://www.youtube.com/embed/" + t)
        }


//        val frameVideo =
//            "<html><body>Youtube video .. <br> <iframe width=\"320\" height=\"315\" src=\"https://www.youtube.com/\" frameborder=\"0\" allowfullscreen></iframe></body></html>"
//        holder.row_video.loadData(frameVideo, "text/html", "utf-8")
//        holder.row_video.loadUrl("http://www.youtube.com/")


//        holder.row_video.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=lG5LZNhfyGM"))
        //"http://www.ebookfrenzy.com/android_book/movie.mp4")

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_video: WebView

        init {
            row_video = itemView.findViewById<View>(R.id.video_view) as WebView
        }
    }

    fun extractYTId(ytUrl: String): String? {
        var vId: String? = null
        val pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher = pattern.matcher(ytUrl)
        if (matcher.matches()) {
            vId = matcher.group(1)
        }
        return vId
    }
}

