package com.example.interviewhub.model

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.interviewhub.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var WebView1: WebView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //intialize the variable
        webView = findViewById(R.id.youtube_view)
        val video : String="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/wexzvClUcUk?si=ft_p2u6elItoV5qs\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html","utf-8")
        webView.getSettings().javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()


        //second youtube video regarding behavioral changes
        WebView1 = findViewById(R.id.youtube_view1)
        val video1 : String="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/jhH2whyQr3Q?si=bvMTUaXREHGBQwol\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView.loadData(video1,"text/html","utf-8")
        webView.getSettings().javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()

        // Initialize bottom navigation
        bottomNavigation = findViewById(R.id.bottomBar)

        // Set the bottom navigation view to be selected
        bottomNavigation.selectedItemId = R.id.home

        // force the bottom navigation bar to always show icon and title
        bottomNavigation.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_LABELED

        // Set bottom navigation to selected item
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Handle Saved Observations menu item click
                    true
                }
                R.id.add -> {
                    startActivity(Intent(this, InterviewObservation::class.java))
                    true
                }
                R.id.view -> {
                    startActivity(Intent(this, InterviewViewObservation::class.java))
                    true
                }

                R.id.notes -> {
                    // Handle settings menu item click
                    startActivity(Intent(this, Tips::class.java))
                    true
                }

                R.id.settings -> {
                    // Handle settings menu item click
                    startActivity(Intent(this, Settings::class.java))
                    true
                }

                else -> false
            }
        }
    }
}
