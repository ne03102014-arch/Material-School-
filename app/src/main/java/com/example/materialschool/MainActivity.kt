package com.example.materialschool
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
class MainActivity: AppCompatActivity(){ @SuppressLint("SetJavaScriptEnabled") override fun onCreate(b:Bundle?){super.onCreate(b); val w=WebView(this); w.settings.javaScriptEnabled=true; w.settings.domStorageEnabled=true; CookieManager.getInstance().setAcceptCookie(true); w.webChromeClient=WebChromeClient(); w.webViewClient=object:WebViewClient(){override fun shouldOverrideUrlLoading(v:WebView,r:WebResourceRequest):Boolean{val u=r.url.toString(); if(u.startsWith("https://myschool.edu.ru/")||u.startsWith("https://gosuslugi.ru/")||u.startsWith("https://www.gosuslugi.ru/"))return false; startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(u))); return true}}; w.loadUrl("https://myschool.edu.ru/"); setContentView(w); onBackPressedDispatcher.addCallback(this,object:OnBackPressedCallback(true){override fun handleOnBackPressed(){if(w.canGoBack())w.goBack()else finish()}})}}
