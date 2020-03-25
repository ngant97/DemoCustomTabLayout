package com.example.democustomtablayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class RkPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val mFragmentList: ArrayList<Fragment> = ArrayList()
    private val mFragmentTitleList: ArrayList<String> = ArrayList()

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    fun clearFragments() {
        mFragmentList.clear()
        mFragmentTitleList.clear()
        notifyDataSetChanged()
    }

    fun getFragments(): ArrayList<Fragment> {
        return mFragmentList
    }

}
