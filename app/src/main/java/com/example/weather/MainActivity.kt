package com.example.weather

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.weather.models.ViewPageModel
import com.example.weather.utils.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
               setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)

        adapter = ViewPagerAdapter(this, ViewPageModel.viewPagerList)
        viewPager = findViewById(R.id.viewPager)

        viewPager.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenu -> {
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}