package org.appjam.comman.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.appjam.comman.R

/**
 * Created by KSY on 2018-01-01.
 */
class   SecondFragActivity : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_card_second, container, false)

        return v
    }
}