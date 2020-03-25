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
        pagerAdapter.addFragment(BlankFragment(), getString(R.string.for_you))
        pagerAdapter.addFragment(BlankFragment(), getString(R.string.follow))
        pagerAdapter.addFragment(BlankFragment(), getString(R.string.beauty))
        pagerAdapter.addFragment(BlankFragment(), getString(R.string.love))
        pagerAdapter.addFragment(BlankFragment(), getString(R.string.design))

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}
