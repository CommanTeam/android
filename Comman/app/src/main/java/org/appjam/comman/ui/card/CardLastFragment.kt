package org.appjam.comman.ui.card

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_card.*
import kotlinx.android.synthetic.main.fragment_card_second.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CardData
import org.appjam.comman.network.data.NextLectureData
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads

/**
 * Created by KSY on 2018-01-01.
 */
class CardLastFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private var NextLectureResponse : NextLectureData.NextLectureResponse? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_card_second, container, false)

        disposables.add(APIClient.apiService.getLectureCards(PrefUtils.getUserToken(this), ,)
                .setDefaultThreads()
                .subscribe({
                    response ->
                    NextLectureResponse = response
                    pageCount = response.result.size + 1
                    val gson = Gson()
                    card_count_tv.text = "1 / $pageCount"
                    card_view_pager.adapter=CardPagerAdapter(supportFragmentManager)
                }, {
                    failure -> Log.i(CardActivity.TAG, "on Failure ${failure.message}")
                }))

        if (arguments != null) {
            v.card_next_lecture_btn.setOnClickListener {
                arguments.getString("nextLectureID")

            }

        }
        return v
    }
}