package com.example.materialschool

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback

class MainActivity : ComponentActivity() {

    private lateinit var web: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        web = WebView(this).apply {

            setBackgroundColor(Color.WHITE)

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                loadsImagesAutomatically = true

                mixedContentMode =
                    WebSettings.MIXED_CONTENT_NEVER_ALLOW

                builtInZoomControls = false
                displayZoomControls = false
            }

            // Сохраняем авторизацию и cookies
            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance()
                .setAcceptThirdPartyCookies(this, true)

            webChromeClient = WebChromeClient()

            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {

                    val url = request.url.toString()

                    // Официальная «Моя школа» остаётся внутри приложения
                    if (
                        url.startsWith("https://myschool.edu.ru/") ||
                        url.startsWith("https://gosuslugi.ru/") ||
                        url.startsWith("https://www.gosuslugi.ru/")
                    ) {
                        return false
                    }

                    // Остальные сайты открываем обычным браузером
                    return try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(url)
                            )
                        )
                        true
                    } catch (_: Exception) {
                        false
                    }
                }
            }

            // Загружаем настоящую веб-версию
            loadUrl("https://myschool.edu.ru/")
        }

        setContentView(web)

        // Кнопка «Назад» работает как в браузере
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {

                    if (web.canGoBack()) {
                        web.goBack()
                    } else {
                        finish()
                    }
                }
            }
        )
    }

    override fun onDestroy() {
        web.stopLoading()
        web.destroy()
        super.onDestroy()
    }
}
