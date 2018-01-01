package org.appjam.comman.quiz

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.appjam.comman.R

/**
 * Created by RyuDongIl on 2018-01-01.
 */
class QuizQuestionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_quiz_choice, container, false)
        if(arguments != null) {
        }
        return v
    }
}