package org.appjam.comman.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.appjam.comman.R

/**
 * Created by KSY on 2017-12-31.
 */
class FirstFragActivity : Fragment(){
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_card_first, container, false)

        return v
    }
}

