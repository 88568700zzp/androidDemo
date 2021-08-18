package com.zzp.applicationkotlin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zzp.applicationkotlin.fragment.BitmapFragment

/**
 *
 * Created by samzhang on 2021/8/9.
 */
class ViewPager2FragmentAdapter(activity: FragmentActivity):FragmentStateAdapter(activity){


    override fun getItemCount(): Int {
        return 10
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = BitmapFragment()
        fragment.index = position
        return fragment
    }

}