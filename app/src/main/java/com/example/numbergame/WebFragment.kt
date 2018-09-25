package com.example.numbergame

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_webview.*


/**
 * @author caizeming
 * @email  caizeming@cvte.com
 * @date   2018/9/14
 * @description:
 */
class WebFragment : Fragment() {

    var isLogin = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webview, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init() {

        wv.settings.javaScriptEnabled = true
        wv.addJavascriptInterface(InJavaScriptLocalObj(), "java_obj")
//        wv.settings.setSupportZoom(true)
//        wv.settings.builtInZoomControls = true
//        wv.settings.domStorageEnabled = true
//        wv.requestFocus()
//        wv.settings.useWideViewPort = true
//        wv.settings.loadWithOverviewMode = true

        wv.loadUrl("https://286.com/login")

        val sp = activity!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val userName = sp.getString("userName", "")
        val pwd = sp.getString("pwd", "")

        val js = "javascript:document.getElementsByTagName('input')[0].value = '$userName';document.getElementsByTagName('input')[1].value = '$pwd';"
        Log.d("WebViewActivity", "js=$js")

        wv.webViewClient = object : WebViewClient() {


            override fun onPageFinished(view: WebView, url: String) {

                // 获取解析<meta name="share-description" content="获取到的值">
//                view.loadUrl("javascript:window.java_obj.showSource("
//                        + "document.getElementsByTagName('html')[0].innerHTML);")
                // 获取页面内容
//                view.loadUrl("javascript:window.java_obj.showDescription(" + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')" + ");")
//                Log.d("WebViewActivity", "onPageFinished this page url:$url")
//                view.loadUrl(js)
                super.onPageFinished(view, url)
                if( Build.VERSION.SDK_INT >= 19 ){
                    view.evaluateJavascript(js, ValueCallback {
                        fun onReceiveValue(value: String){
                        }
                    })
                }else{
                    view.loadUrl(js)
                }

            }

        }
    }

    class InJavaScriptLocalObj {

        @JavascriptInterface
        fun showSource(html: String) {
            Log.d("WebViewActivity", "=====>html=$html")
        }

        @JavascriptInterface
        fun showDescription(string: String) {
            Log.d("WebViewActivity", "=====>Description=$string")
        }
    }

}