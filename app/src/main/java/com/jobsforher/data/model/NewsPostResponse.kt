package com.jobsforher.data.model

import android.text.Html
import android.webkit.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jobsforher.data.model.common_response.JobsForHerAuth
import com.jobsforher.data.model.common_response.JobsForHerPagination


data class NewsPostResponse(
    var response_code: Int? = null,
    var message: String? = null,
    var auth: JobsForHerAuth? = null,
    var pagination: JobsForHerPagination? = null,
    var body: ArrayList<NewsPostBody>? = null

)

data class NewsPostBody(
    var group_id: Int? = null,
    var description: String? = null,
    var pinned_post: Boolean? = null,
    var created_by: Long? = null,
    var hash_tags: String? = null,
    var id: Int? = null,
    var downvote_count: Int? = null,
    var url: String? = null,
    var post_type: String? = null,
    var edited: Boolean? = null,
    var modified_on: String? = null,
    var status: String? = null,
    var upvote_count: Int? = null,
    var created_on: String? = null,
    var time_ago: String? = null,
    var username: String? = null,
    var profile_icon: String? = null,
    var aggregate_count: Int? = null,
    var comments_count: Int? = null,
    var group_name: String? = null
) {
    fun getWebViewClient(): WebViewClient? {
        return Client()
    }
}

@BindingAdapter("setWebViewClient")
fun setWebViewClient(view: WebView, client: WebViewClient?) {
    view.setWebViewClient(client)
    view.settings.javaScriptEnabled = true
    view.settings.javaScriptCanOpenWindowsAutomatically = true
    view.getSettings().setPluginState(WebSettings.PluginState.ON)
    view.getSettings().mediaPlaybackRequiresUserGesture = false

    view.settings.builtInZoomControls = true
    view.isHorizontalScrollBarEnabled = true
    view.isVerticalScrollBarEnabled = true
}

@BindingAdapter("loadDocumentVideoUrl")
fun loadDocumentVideoUrl(view: WebView, url: String?) {
    view.loadUrl(url)
}

class Client : WebViewClient() {
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
//        setHideProgress(true);
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
//        setHideProgress(true);
    }
}

@BindingAdapter("newsPostImageUrl")
fun newsPostImageUrl(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}

@BindingAdapter("newsPostProfileIcon")
fun newsPostProfileIcon(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl).apply(RequestOptions().circleCrop())
        .into(view)
}

@BindingAdapter("loadHtml")
fun loadHtml(textView: TextView, content: String?) {
    if (!content.isNullOrEmpty()) {
        textView.text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(content)
        }
    }
}