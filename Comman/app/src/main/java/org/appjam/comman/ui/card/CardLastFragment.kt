package org.appjam.comman.ui.card

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_card_second.view.*
import org.appjam.comman.R

/**
 * Created by KSY on 2018-01-01.
 */
class CardLastFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_card_second, container, false)
        if (arguments != null) {
            v.card_next_lecture_btn.setOnClickListener {
                arguments.getString("nextLectureID")

            }

        }
        return v
    }
}