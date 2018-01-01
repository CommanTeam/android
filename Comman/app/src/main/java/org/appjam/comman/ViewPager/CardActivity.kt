package org.appjam.comman.ViewPager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.appjam.comman.R

/**
 * Created by KSY on 2017-12-31.
 */
class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_viewpager.currentItem = 0

    }
    }
}