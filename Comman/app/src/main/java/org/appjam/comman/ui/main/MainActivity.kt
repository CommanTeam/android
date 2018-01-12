package org.appjam.comman.ui.main

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.SetColorUtils
import org.appjam.comman.util.setDefaultThreads


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG : String = "MainActivity"
    }

    private val bundle = Bundle()
    private val disposables = CompositeDisposable()
    private var courseResponse: CoursesData.CoursesResponse? = null
    private var onItemClick : View.OnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_layout.setOnClickListener {
            val hide = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hide.hideSoftInputFromWindow(main_layout.windowToken, 0)
        }


        val view1 = layoutInflater.inflate(R.layout.custom_my_course_text_view, null)
        main_tab_layout.addTab(main_tab_layout.newTab().setCustomView(view1))
        val view2 = layoutInflater.inflate(R.layout.custom_search_text_view, null)
        main_tab_layout.addTab(main_tab_layout.newTab().setCustomView((view2)))

        var mainTabAdapter = MainTabAdapter(supportFragmentManager, main_tab_layout.tabCount)

        disposables.add(APIClient.apiService.getRegisteredCourses(PrefUtils.getUserToken(this@MainActivity))
                .setDefaultThreads()
                .subscribe({
                    response ->
                        courseResponse = response
                        val gson = Gson()
                        bundle.putString("courseInfoList", gson.toJson(courseResponse))
                        main_content_view_pager.adapter = mainTabAdapter
                        main_content_view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tab_layout))
                        if(response.result.isEmpty()) {         //수강중인 강좌 없을 경우 두번째 탭에서 시작하도록
                            main_content_view_pager.currentItem += 1
                        }
                }, {
                    failure -> Log.i(MyCourseFragment.TAG, "on Failure ${failure.message}")
                }))

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
                val hide = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hide.hideSoftInputFromWindow(main_layout.windowToken, 0)
            }
        })
    }

    inner class MainTabAdapter(fm : FragmentManager?) : FragmentStatePagerAdapter(fm) {
        var tabCount : Int = 0
        var firstTab : MyCourseFragment? = null
        var firstTab2 : SearchNewCourseFragment? = null
        var secondTab : SearchFragment? = null

        constructor(fm : FragmentManager?, tabCount : Int) : this(fm) {
            this.tabCount = tabCount
            this.firstTab = MyCourseFragment()
            this.firstTab2 = SearchNewCourseFragment()
            this.secondTab = SearchFragment()
        }

        override fun getItem(position: Int): Fragment? {
            when(position) {
                0 -> {
                    firstTab?.arguments = bundle
                    return if(courseResponse?.result?.isEmpty() != false) firstTab2 else firstTab
                }
                1 -> return secondTab
            }
            return null
        }

        override fun getCount(): Int = tabCount
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

}
