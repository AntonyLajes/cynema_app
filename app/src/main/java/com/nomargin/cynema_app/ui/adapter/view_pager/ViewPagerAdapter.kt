package com.nomargin.cynema_app.ui.adapter.view_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private var fragments: ArrayList<Fragment> = arrayListOf()
    private var title: ArrayList<String> = arrayListOf()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun setFragment(fragment: Fragment, title: String){
        fragments.add(fragment)
        this.title.add(title)
    }

    fun getTitle(position: Int): String = title[position]

}
