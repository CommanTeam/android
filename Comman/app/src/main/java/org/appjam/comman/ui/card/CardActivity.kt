package org.appjam.comman.ui.card

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_card.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CardData
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.network.data.LectureData
import org.appjam.comman.ui.lecture.LectureListActivity
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.SetColorUtils
import org.appjam.comman.util.setDefaultThreads

/**
 * Created by KSY on 2017-12-31.
 */

@Suppress("UNREACHABLE_CODE")
class CardActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CardActivity"
    }

    private val disposables = CompositeDisposable()
    private var cardInfoList: List<CardData.CardInfo> = listOf()
    private var lectureID: Int = 0
    private var courseID: Int = 0
    private var pageCount: Int = 0
    private var lectureTitle: String = ""
    private var chapterID : Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

//        secondString=getIntent().getStringExtra("firstData")
//        getText=findViewById(R.id.main2_get_text) as TextView
//        getText!!.text=secondString

        chapterID=intent.getIntExtra("chapterID",0)

        card_back_btn.setOnClickListener {
            val intent = Intent(this, LectureListActivity::class.java)
            intent.putExtra("chapterID", chapterID)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()

        }


        courseID = intent.getIntExtra("courseID",0)
        lectureID = intent.getIntExtra("lectureID", 0)
        var current_page : Int = 1

        PrefUtils.putCurrentLectureID(this, lectureID)
        PrefUtils.putLectureOfCourseID(this, lectureID, courseID)

        card_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            @SuppressLint("ResourceAsColor")
            override fun onPageSelected(position: Int) {
                PrefUtils.putLectureOfCoursePosition(this@CardActivity, position, courseID)
                when (position) {
                    card_view_pager.adapter.count - 1 -> {
                        card_prev_tv.setTextColor(SetColorUtils.get(this@CardActivity, R.color.mainTextColor))
                        card_next_tv.setTextColor(SetColorUtils.get(this@CardActivity, R.color.grayMainTextColor))
                        card_prev_btn.setBackgroundResource(R.drawable.view_pager_prev_btn)
                        card_next_btn.setBackgroundResource(R.drawable.unclickable_view_pager_next_btn)
                        card_question_btn.visibility = View.VISIBLE
                    }
                    0 -> {
                        card_prev_tv.setTextColor(SetColorUtils.get(this@CardActivity, R.color.grayMainTextColor))
                        card_prev_btn.setBackgroundResource(R.drawable.unclickable_view_pager_prev_btn)
                        card_next_tv.setTextColor(SetColorUtils.get(this@CardActivity, R.color.mainTextColor))
                        card_next_btn.setBackgroundResource(R.drawable.view_pager_next_btn)
                        card_question_btn.visibility = View.GONE
                    }
                    else -> {
                        card_prev_tv.setTextColor(SetColorUtils.get(this@CardActivity, R.color.mainTextColor))
                        card_next_tv.setTextColor(SetColorUtils.get(this@CardActivity, R.color.mainTextColor))
                        card_prev_btn.setBackgroundResource(R.drawable.view_pager_prev_btn)
                        card_next_btn.setBackgroundResource(R.drawable.view_pager_next_btn)
                        card_question_btn.visibility = View.GONE
                    }
                }
                card_count_tv.text = "${position + 1} / $pageCount"
            }
        })

        card_prev_layout.setOnClickListener {
            card_view_pager.currentItem = card_view_pager.currentItem - 1
        }
        card_next_layout.setOnClickListener {
            card_view_pager.currentItem = card_view_pager.currentItem + 1
        }
        card_question_btn.setOnClickListener {
            val intent = Intent(this@CardActivity, QuestionActivity::class.java)
            intent.putExtra("lectureID", lectureID)
            intent.putExtra("lectureTitle", lectureTitle)
            startActivity(intent)
        }

        disposables.add(APIClient.apiService.getLectureInfo(
                PrefUtils.getUserToken(this), lectureID)
                .setDefaultThreads()
                .subscribe({ response ->
                    lectureTitle = response.data.title
                    if(response.data.priority < 10)
                        card_lecture_name_tv.text = "0${response.data.priority}. ${response.data.title}"
                    else
                        card_lecture_name_tv.text = "${response.data.priority}. ${response.data.title}"
                    lectureTitle = card_lecture_name_tv.text.toString()

                }, { failure ->
                    Log.i(CardActivity.TAG, "on Failure ${failure.message}")
                }))

        disposables.add(APIClient.apiService.getLectureCards(
                PrefUtils.getUserToken(this), lectureID)
                .setDefaultThreads()
                .subscribe({ response ->
                        cardInfoList = response.result
                        pageCount = cardInfoList.size + 1
                        card_count_tv.text = "$current_page / $pageCount"
                        card_view_pager.adapter = CardPagerAdapter(supportFragmentManager)

                        if(lectureID == PrefUtils.getRecentLectureOfCourseID(this, courseID)) {
                            card_view_pager.currentItem = PrefUtils.getRecentLectureOfCoursePosition(this, courseID)
                            current_page = card_view_pager.currentItem + 1
                        }
                }, { failure ->
                    Log.i(CardActivity.TAG, "on Failure ${failure.message}")
                }))
    }

    inner class CardPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){

        override fun getItem(position:Int): Fragment {
            return if (position < count - 1) {
                val cardFragment = CardFragment()
                val bundle = Bundle()
                bundle.putString("image_url", cardInfoList[position].image_path)
                cardFragment.arguments = bundle
                cardFragment
            } else {
                val cardLastFragment = CardLastFragment()
                val bundle = Bundle()
                bundle.putInt(CoursesData.COURSE_ID_KEY,courseID)
                bundle.putInt(LectureData.LECTURE_ID_KEY, lectureID)
                cardLastFragment.arguments = bundle
                cardLastFragment
            }
        }

        override fun getCount(): Int= pageCount
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
