package com.project.farmingappss.view.plantml.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class PagerAdapter(
    private val fragList: List<ViewPagerFragInfo>,
    private val fragmentManager: FragmentManager
): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = fragList.size

    override fun getItem(position: Int) =
        fragList[position].fragment

    override fun getPageTitle(position: Int): CharSequence? =
        fragList[position].pageTitle


}