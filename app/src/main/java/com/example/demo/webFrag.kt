package com.example.demo


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment


class webFrag : Fragment() {

    private var webpage: WebView? = null
    private var weblink: String? = ""

    //引入布局
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.web_frag, container, false)

        webpage = view.findViewById(R.id.webpage)
        weblink = arguments?.getString("link")
        if (weblink!!.contains("jianshu", ignoreCase = true)) {
            weblink!!.replace("https", "http")

        }

        //设置网页参数
        webpage?.apply {
            settings.javaScriptEnabled = true
            settings.blockNetworkImage = true
            //设置 缓存模式
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_IMPORTANT, false)
            settings.blockNetworkImage = false
            // 开启 DOM storage API 功能
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            loadUrl(weblink!!)
        }


        return view
    }


}