package org.appjam.comman.quiz

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_quiz.*
import org.appjam.comman.R

class QuizActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {

    }

    var pagePosition : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        quiz_view_pager.adapter = QuizPagerAdapter(supportFragmentManager)

        quiz_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                pagePosition = position
                if(position == quiz_view_pager.adapter.count - 1)
                    quiz_prev_next_layout.visibility = View.INVISIBLE
                else
                    quiz_prev_next_layout.visibility = View.VISIBLE
            }
        })

        if(quiz_prev_next_layout.visibility == View.INVISIBLE) run {
            Log.v("visible_check", "It's INVISIBLE")
            quiz_view_pager.adapter = null
        }

        quiz_prev_layout.setOnClickListener {
            if(pagePosition != 0) {
                quiz_view_pager.currentItem = quiz_view_pager.currentItem - 1
            }
        }

        quiz_next_layout.setOnClickListener {
            quiz_view_pager.currentItem = quiz_view_pager.currentItem + 1
        }

    }

    inner class QuizPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return if (position < count - 1) {
                QuizQuestionFragment()
            } else {
                QuizSubmitFragment()
            }
        }

        override fun getCount(): Int = 10

    }
}
