package org.appjam.comman.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by RyuDongIl on 2018-01-03.
 */
class MainTabAdapter(fm : FragmentManager?) : FragmentStatePagerAdapter(fm) {
    var tabCount : Int = 0
    var firstTab : MyCourseFragment? = null
    var firstTab2 : SearchNewCourseFragment? = null
    var secondTab : SearchFragment? = null

    constructor(fm : FragmentManager?, tabCount : Int) : this(fm) {
        this.tabCount = tabCount
        this.firstTab2 = SearchNewCourseFragment()
        this.secondTab = SearchFragment()
    }

    override fun getItem(position: Int): Fragment? {
        when(position) {
            0 -> return firstTab2
            1 -> return secondTab
        }
        return null
    }

    override fun getCount(): Int = tabCount
}