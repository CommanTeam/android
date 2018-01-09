package org.appjam.comman.ui.card

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import org.appjam.comman.R
import org.appjam.comman.network.data.NextLectureData

/**
 * Created by KSY on 2018-01-01.
 */
class CardLastFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private var NextLectureResponse : NextLectureData.NextLectureResponse? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_card_second, container, false)

        if (arguments != null) {
        }
        return v
    }
}