package org.appjam.comman.ViewPager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.appjam.comman.R

/**
 * Created by KSY on 2017-12-31.
 */
@Suppress("UNREACHABLE_CODE")
class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.commit()
    }
}
