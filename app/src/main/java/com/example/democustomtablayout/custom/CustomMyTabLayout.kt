package com.example.democustomtablayout.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import androidx.viewpager.widget.ViewPager
import java.util.ArrayList

class CustomMyTabLayout(context: Context, attrs: AttributeSet?) :
    HorizontalScrollView(context, attrs) {

    var view: CustomViewTabLayout = CustomViewTabLayout(context, attrs)

    init {
        view.horizontalScrollView = this
        this.isVerticalScrollBarEnabled = false
        this.isHorizontalScrollBarEnabled = false

    }

    fun setupWithViewPager(viewPager : ViewPager){
        var adapter = viewPager.adapter
        adapter?.let {
            val titles = ArrayList<String>()
            val adapterCount = adapter.count
            for (i in 0 until adapterCount) {
                titles.add(adapter.getPageTitle(i).toString())
            }
            view.setTitle(titles)
            addView(view)
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                view.updatePosition(position + positionOffset) // Update lại vị trí focus của tab
            }

            override fun onPageSelected(position: Int) {

            }
        })

        view.setListenter(object : CustomViewTabLayout.OnChangeTabIO{
            override fun tabSelected(pos: Int) {
                viewPager.currentItem = pos //đồng bộ khi click ở trên tab thì viewpager cũng chuyển qua vị trí
            }
        })
    }

}