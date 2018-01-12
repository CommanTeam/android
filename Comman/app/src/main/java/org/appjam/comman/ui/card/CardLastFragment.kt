package org.appjam.comman.ui.card

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_card_second.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.network.data.LectureData
import org.appjam.comman.network.data.NextLectureData
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads
import org.appjam.comman.youtube.YoutubePracticeActivity



/**
 * Created by KSY on 2018-01-01.
 */
class CardLastFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private var nextLectureResponse : NextLectureData.NextLectureResponse? = null
    private var isCompleted = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_card_second, container, false)

        if (arguments != null) {
            val lectureId = arguments.getInt(LectureData.LECTURE_ID_KEY)
            disposables.add(APIClient.apiService.registerFinishLecture(
                    PrefUtils.getUserToken(context), lectureId)
                    .setDefaultThreads()
                    .subscribe({
                    }, { failure ->
                        Log.i(YoutubePracticeActivity.TAG, "on Failure ${failure.message}")
                    }))

            disposables.add(APIClient.apiService.getNextLectureInfo(        //다음 강의 정보 얻어오기
                    PrefUtils.getUserToken(context), lectureId)
                    .setDefaultThreads()
                    .subscribe({ response ->
                        nextLectureResponse = response
                        isCompleted = true
                    }, { failure ->
                        Log.i(YoutubePracticeActivity.TAG, "on Failure ${failure.message}")

                    }))
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments != null) {
            view.card_next_lecture_btn.setOnClickListener {
                val intent = ListUtils.linkToNextLecture(context, nextLectureResponse, arguments.getInt(CoursesData.COURSE_ID_KEY))
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }
}