package org.appjam.comman.ui.card

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_card.*
import org.appjam.comman.ui.card.CardFragment
import org.appjam.comman.ui.card.CardLastFragment
import org.appjam.comman.R

/**
 * Created by KSY on 2017-12-31.
 */
@Suppress("UNREACHABLE_CODE")
class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        card_view_pager.adapter=CardPagerAdapter(supportFragmentManager)

    }

    inner class CardPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){


        override fun getItem(position:Int): Fragment {
            return if(position<count-1){
                CardFragment()
            }else{
                CardLastFragment()
            }
            }
        override fun getCount():Int=10
        }
    }
