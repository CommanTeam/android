package org.appjam.comman.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.appjam.comman.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.contentLayout, SearchFragment())
        fragmentTransaction.commit()
    }
}
