package org.appjam.comman.ui.card

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_card.*
import org.appjam.comman.R
import org.appjam.comman.util.SetColorUtils

/**
 * Created by KSY on 2017-12-31.
 */
@Suppress("UNREACHABLE_CODE")
class CardActivity : AppCompatActivity() {

    var pagePosition : Int = 0

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

    }

    inner class CardPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){


        override fun getItem(position:Int): Fragment {
            return if(position<count-1) {
                CardFragment()
            } else {
                CardLastFragment()
            }
            }
        override fun getCount():Int=10
        }
    }
