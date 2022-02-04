package org.techtown.feelim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import org.techtown.feelim.R

class moreInfo : AppCompatActivity() {
    lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_info)

        webview = findViewById(R.id.webview)


        //가져온 이름으로 웹뷰에서 해당 페이지를 열도록 한다.
        if(intent.hasExtra("intent_name")){
            val mvName = intent.getStringExtra("intent_name")
            val url = "https://search.daum.net/search?q=$mvName&w=tot&DA=S43"

            //웹뷰 처리
            println("영화 이름 : " + mvName)
            val webSettings = webview.settings
            webSettings.javaScriptEnabled = true
            webview.loadUrl(url)
            webview.webViewClient = WebViewClient()
        }
    }

    //메뉴 추가
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.item1 -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.item2 -> {
                val intent = Intent(this, myFeelim::class.java)
                startActivity(intent)
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }




}