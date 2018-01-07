package org.appjam.comman.ui.card

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_card.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CardData
import org.appjam.comman.ui.main.MyCourseFragment
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
    private var cardResponse : CardData.CardResponse? = null
    var pagePosition : Int = 0
    var pageCount : Int = 0
    var currentPage : Int = 0
    val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        card_view_pager.adapter=CardPagerAdapter(supportFragmentManager)

        card_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            @SuppressLint("ResourceAsColor")
            override fun onPageSelected(position: Int) {
                pagePosition = position
                when (position) {
                    card_view_pager.adapter.count - 1 -> {
                        card_next_tv.setTextColor(SetColorUtils.get(this@CardActivity, R.color.grayMainTextColor))
                        card_next_btn.setBackgroundResource(R.drawable.unclickable_view_pager_next_btn)
                    }
                    0 -> {
                        card_prev_tv.setTextColor(SetColorUtils.get(this@CardActivity, R.color.grayMainTextColor))
                        card_prev_btn.setBackgroundResource(R.drawable.unclickable_view_pager_prev_btn)
                    }
                    else -> {
                        card_prev_tv.setTextColor(SetColorUtils.get(this@CardActivity, R.color.mainTextColor))
                        card_next_tv.setTextColor(SetColorUtils.get(this@CardActivity, R.color.mainTextColor))
                        card_prev_btn.setBackgroundResource(R.drawable.view_pager_prev_btn)
                        card_next_btn.setBackgroundResource(R.drawable.view_pager_next_btn)
                    }
                }
            }
        })

        card_prev_layout.setOnClickListener {
            if(pagePosition != 0) {
                card_view_pager.currentItem = card_view_pager.currentItem - 1
            }
        }

        card_next_layout.setOnClickListener {
            card_view_pager.currentItem = card_view_pager.currentItem + 1
        }

        disposables.add(APIClient.apiService.getLectureCards(1)
                .setDefaultThreads()
                .subscribe({
                    response -> cardResponse?.result = response.result[2].image_priority
                    bundle.putStringArrayList("result", cardResponse?.result)
                    pageCount = cardResponse!!.result.size + 1
                    currentPage = cardResponse!!.result.
//                    cardResponse?.lectureImageUrlArr = response.lectureImageUrlArr
//                    cardResponse?.nextLectureID = response.nextLectureID
//                    bundle.putStringArrayList("image_url", cardResponse?.lectureImageUrlArr as ArrayList<String>?)
//                    bundle.putString("nextLectureID", cardResponse?.nextLectureID)
//                    pageCount = cardResponse!!.lectureImageUrlArr.size + 1

                            card_view_pager.adapter.notifyDataSetChanged()

                }, {
                    failure -> Log.i(TAG, "on Failure ${failure.message}")
                })
        )
        //        disposables.add(APIClient.apiService.getRegisteredCourses(1)
//                .setDefaultThreads()
//                .subscribe ({
//                    response ->
//                        recyclerView.adapter = MyLectureAdapter(response.result)
//                }, {
//                    failure -> Log.i(TAG, "on Failure ${failure.message}")
//                })
//        )
    }

    inner class CardPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){

        override fun getItem(position:Int): Fragment {
            return if(position<count-1) {
                val cardFragment = CardFragment()
                cardFragment.arguments = bundle
                cardFragment
            } else {
                val cardLastFragment = CardLastFragment()
                cardLastFragment.arguments = bundle
                cardLastFragment
            }
        }
        override fun getCount():Int= pageCount
    }


}