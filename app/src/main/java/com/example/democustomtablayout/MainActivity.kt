package com.example.democustomtablayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var pagerAdapter: RkPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagerAdapter = RkPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(BlankFragment(), "Dành cho bạn")
        pagerAdapter.addFragment(BlankFragment(), "Đang theo dõi")
        pagerAdapter.addFragment(BlankFragment(), "Làm đẹp")
        pagerAdapter.addFragment(BlankFragment(), "Đang yêu")
        pagerAdapter.addFragment(BlankFragment(), "Thiết kế")

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}
