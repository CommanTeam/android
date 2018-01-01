package org.appjam.comman.ViewPager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.appjam.comman.Fragment.FirstFragActivity
import org.appjam.comman.Fragment.SecondFragActivity
import org.appjam.comman.R

/**
 * Created by KSY on 2017-12-31.
 */
@Suppress("UNREACHABLE_CODE")
class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_viewpager.adapter=CardPagerAdapter(supportFragmentManager)
    }

    inner class CardPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){


        override fun getItem(position:Int): Fragment {
            return if(position<count-1){
                FirstFragActivity()
            }else{
                SecondFragActivity()
            }
            }
        override fun getCount():Int=10
        }
    }
