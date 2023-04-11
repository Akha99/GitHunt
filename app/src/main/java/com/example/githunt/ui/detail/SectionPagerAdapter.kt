package com.example.githunt.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githunt.R

class SectionPagerAdapter(private val mCtx: Context, fm: FragmentManager, data : Bundle ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentBundle: Bundle
    init {
        fragmentBundle = data
    }

    @StringRes
    private val tabTitles = intArrayOf(R.string.tab_1, R.string.tab_2)

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = FollowersFragment()
                fragment.arguments = this.fragmentBundle
                fragment
            }
            1 -> {
                val fragment = FollowingFragment()
                fragment.arguments = this.fragmentBundle
                fragment
            }
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? = tabTitles.getOrNull(position)?.let { mCtx.resources.getString(it) }

}