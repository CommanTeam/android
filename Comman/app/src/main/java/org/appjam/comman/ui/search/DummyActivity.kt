package org.appjam.comman.ui.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.appjam.comman.R

class DummyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.contentLayout, SearchFragment())
        fragmentTransaction.commit()
    }
}
