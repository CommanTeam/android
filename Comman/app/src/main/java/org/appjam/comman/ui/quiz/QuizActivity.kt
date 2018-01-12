package org.appjam.comman.ui.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_quiz.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.QuizData
import org.appjam.comman.ui.lecture.LectureListActivity
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.SetColorUtils
import org.appjam.comman.util.setDefaultThreads

class QuizActivity : AppCompatActivity() {

    companion object {
        val TAG = "QuizActivity"
    }

    private val disposables = CompositeDisposable()
    private var quizInfoList: QuizData.QuizResponse? = null
    private var lectureID: Int = 10
    private var courseID: Int = 0
    private var pageCount: Int = 0
    private var pagePosition: Int = 0
    private var lecturePriority: Int = 0
    private var lectureTitle: String = ""
    private var passValue: Int = 0
    private var chapterID : Int =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        chapterID = intent.getIntExtra("chapterID", 0)


        quiz_back_btn.setOnClickListener{
            val intent = Intent(this, LectureListActivity::class.java)
            intent.putExtra("chapterID", chapterID)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

            finish()
        }
        quiz_view_pager.adapter = QuizPagerAdapter(supportFragmentManager)

        courseID = intent.getIntExtra("courseID",0)       //TODO lectureID 테스트를 위해 10으로 고정해놨음. 나중에 해결바람
        lectureID = intent.getIntExtra("lectureID", 0)

        PrefUtils.putCurrentLectureID(this, lectureID)
        PrefUtils.putLectureOfCourseID(this, lectureID, courseID)

        quiz_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            @SuppressLint("ResourceAsColor")
            override fun onPageSelected(position: Int) {
                PrefUtils.putLectureOfCoursePosition(this@QuizActivity, position, courseID)
                when (position) {
                    pageCount - 1 -> {
                        quiz_prev_tv.setTextColor(SetColorUtils.get(this@QuizActivity, R.color.mainTextColor))
                        quiz_next_tv.setTextColor(SetColorUtils.get(this@QuizActivity, R.color.grayMainTextColor))
                        quiz_prev_btn.setBackgroundResource(R.drawable.view_pager_prev_btn)
                        quiz_next_btn.setBackgroundResource(R.drawable.unclickable_view_pager_next_btn)
                    }
                    0 -> {
                        quiz_prev_tv.setTextColor(SetColorUtils.get(this@QuizActivity, R.color.grayMainTextColor))
                        quiz_next_tv.setTextColor(SetColorUtils.get(this@QuizActivity, R.color.mainTextColor))
                        quiz_prev_btn.setBackgroundResource(R.drawable.unclickable_view_pager_prev_btn)
                        quiz_next_btn.setBackgroundResource(R.drawable.view_pager_next_btn)
                    }
                    else -> {
                        quiz_prev_tv.setTextColor(SetColorUtils.get(this@QuizActivity, R.color.mainTextColor))
                        quiz_next_tv.setTextColor(SetColorUtils.get(this@QuizActivity, R.color.mainTextColor))
                        quiz_prev_btn.setBackgroundResource(R.drawable.view_pager_prev_btn)
                        quiz_next_btn.setBackgroundResource(R.drawable.view_pager_next_btn)
                    }
                }
                pagePosition = position
            }
        })

        disposables.add(APIClient.apiService.getQuizResult(
                PrefUtils.getUserToken(this), lectureID)
                .setDefaultThreads()
                .subscribe({ response ->
                    quizInfoList = response
                    Log.i(TAG, "Response : ${response}")
                    pageCount = response.result.size + 1
                    Log.i(TAG, "question size: $pageCount")
                    quiz_view_pager.adapter = QuizPagerAdapter(supportFragmentManager)
                    if(lectureID == PrefUtils.getRecentLectureOfCourseID(this, courseID)) { //최근 강좌의 강의면 처리해줘야 할 것
                        if(pageCount - 1 == PrefUtils.getRecentLectureOfCoursePosition(this, courseID))
                            quiz_view_pager.currentItem = PrefUtils.getRecentLectureOfCoursePosition(this, courseID) - 1
                        else
                            quiz_view_pager.currentItem = PrefUtils.getRecentLectureOfCoursePosition(this, courseID)
                    } else {
                        val mutableList = mutableListOf<Int>()
                        for(i in 1..pageCount) { mutableList.add(-1) }
                    }
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))

        disposables.add(APIClient.apiService.getLectureInfo(
                PrefUtils.getUserToken(this), lectureID)
                .setDefaultThreads()
                .subscribe({ response ->
                    if(response.data.priority < 10) {
                        quiz_lecture_name_tv.text = "0${response.data.priority} ${response.data.title}"
                    } else {
                        quiz_lecture_name_tv.text = "${response.data.priority} ${response.data.title}"
                    }
                    passValue = response.data.pass_value
                    lecturePriority = response.data.priority
                    lectureTitle = response.data.title
                    quiz_view_pager.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))

        quiz_prev_layout.setOnClickListener {
            quiz_view_pager.currentItem = quiz_view_pager.currentItem - 1
        }

        quiz_next_layout.setOnClickListener {
            quiz_view_pager.currentItem = quiz_view_pager.currentItem + 1
        }

    }

    inner class QuizPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt("position", position)
            bundle.putInt("pageCount", pageCount)
            val gson = Gson()
            bundle.putString("quizInfoList", gson.toJson(quizInfoList))
            return if (position < count - 1) {
                val quizQuestionFragment = QuizQuestionFragment()
                quizQuestionFragment.arguments = bundle
                quizQuestionFragment
            } else {
                bundle.putInt("passValue", passValue)
                bundle.putInt("lecturePriority", lecturePriority)
                bundle.putString("lectureTitle", lectureTitle)
                bundle.putInt("lectureID", lectureID)
                bundle.putInt("courseID", courseID)
                val quizSubmitFragment = QuizSubmitFragment()
                quizSubmitFragment.arguments = bundle
                quizSubmitFragment
            }
        }

        override fun getCount(): Int = pageCount

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}


