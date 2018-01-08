package org.appjam.comman.ui.quiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_quiz.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.QuizData
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.SetColorUtils
import org.appjam.comman.util.setDefaultThreads

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val TAG = "QuizActivity"
    }

    interface NetworkParsedListener {
        fun onNext()
    }

    override fun onClick(p0: View?) {

    }

    private val disposables = CompositeDisposable()
    var quizInfoList: List<QuizData.QuizInfo> = listOf()
    var pagePosition: Int = 0
    var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        viewPager = quiz_view_pager

        disposables.add(APIClient.apiService.getQuizResult(PrefUtils.getUserToken(this), 1)
                .setDefaultThreads()
                .subscribe({ response ->
                    quizInfoList = response.result
                    quiz_view_pager.adapter = QuizPagerAdapter(supportFragmentManager)
                }, { failure ->
                    Log.i(QuizQuestionFragment.TAG, "on Failure ${failure.message}")
                })
        )
        quiz_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            @SuppressLint("ResourceAsColor")
            override fun onPageSelected(position: Int) {
                pagePosition = position
                when (position) {
                    quiz_view_pager.adapter.count - 1 -> {
                        quiz_next_tv.setTextColor(SetColorUtils.get(this@QuizActivity, R.color.grayMainTextColor))
                        quiz_next_btn.setBackgroundResource(R.drawable.unclickable_view_pager_next_btn)
                    }
                    0 -> {
                        quiz_prev_tv.setTextColor(SetColorUtils.get(this@QuizActivity, R.color.grayMainTextColor))
                        quiz_prev_btn.setBackgroundResource(R.drawable.unclickable_view_pager_prev_btn)
                    }
                    else -> {
                        quiz_prev_tv.setTextColor(SetColorUtils.get(this@QuizActivity, R.color.mainTextColor))
                        quiz_next_tv.setTextColor(SetColorUtils.get(this@QuizActivity, R.color.mainTextColor))
                        quiz_prev_btn.setBackgroundResource(R.drawable.view_pager_prev_btn)
                        quiz_next_btn.setBackgroundResource(R.drawable.view_pager_next_btn)
                    }
                }
            }
        })

        quiz_prev_layout.setOnClickListener {
            if (pagePosition != 0) {
                quiz_view_pager.currentItem = quiz_view_pager.currentItem - 1
            }
        }

        quiz_next_layout.setOnClickListener {
            quiz_view_pager.currentItem = quiz_view_pager.currentItem + 1
        }

    }

    inner class QuizPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return if (position < count - 1) {
                val bundle = Bundle()
                bundle.putInt("position", position)
                val fragment = QuizQuestionFragment()
                fragment.arguments = bundle
                fragment

            } else {
                QuizSubmitFragment()
            }
        }

        override fun getCount(): Int = quizInfoList.size + 1

    }
}


