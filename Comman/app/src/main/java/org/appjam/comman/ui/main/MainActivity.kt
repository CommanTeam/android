package org.appjam.comman.ui.main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.appjam.comman.R
import org.appjam.comman.util.SetColorUtils

class MainActivity : AppCompatActivity() {
    private val TAG : String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view1 = layoutInflater.inflate(R.layout.custom_my_course_text_view, null)
        main_tab_layout.addTab(main_tab_layout.newTab().setCustomView(view1))
        val view2 = layoutInflater.inflate(R.layout.custom_search_text_view, null)
        main_tab_layout.addTab(main_tab_layout.newTab().setCustomView((view2)))

        val profile_img_url = intent.getStringExtra("profile_img_url")

        var mainTabAdapter = MainTabAdapter(supportFragmentManager, main_tab_layout.tabCount)

        main_content_view_pager.adapter = mainTabAdapter
        main_content_view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tab_layout))
        main_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.v(TAG, tab!!.position.toString())
                if(tab!!.position == 0) {
                    val view1 = layoutInflater.inflate(R.layout.custom_my_course_text_view, null)
                    view1.findViewById<TextView>(R.id.custom_tab_text_view)
                            .setTextColor(SetColorUtils.get(this@MainActivity, R.color.divideLineColor))
                    tab.customView = null
                    tab.customView = view1
                } else {
                    val view1 = layoutInflater.inflate(R.layout.custom_search_text_view, null)
                    view1.findViewById<TextView>(R.id.custom_tab_text_view)
                            .setTextColor(SetColorUtils.get(this@MainActivity, R.color.divideLineColor))
                    tab.customView = null
                    tab.customView = view1
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                main_content_view_pager.currentItem = tab!!.position
                if(tab!!.position == 0) {
                    val view1 = layoutInflater.inflate(R.layout.custom_my_course_text_view, null)
                    view1.findViewById<TextView>(R.id.custom_tab_text_view)
                            .setTextColor(SetColorUtils.get(this@MainActivity, R.color.black))
                    tab.customView = null
                    tab.customView = view1
                } else {
                    val view1 = layoutInflater.inflate(R.layout.custom_search_text_view, null)
                    view1.findViewById<TextView>(R.id.custom_tab_text_view)
                            .setTextColor(SetColorUtils.get(this@MainActivity, R.color.black))
                    tab.customView = null
                    tab.customView = view1
                }
            }

        })
    }

}
