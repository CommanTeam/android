package org.appjam.comman.ui.quiz

/**
 * Created by yeahen on 2017-12-31.
 */


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_quiz_result.*
import kotlinx.android.synthetic.main.quiz_result_header_items.view.*
import kotlinx.android.synthetic.main.quiz_result_items.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.QuizData
import org.appjam.comman.realm.RQuizData
import org.appjam.comman.ui.CourseSubsection.CourseSubActivity
import org.appjam.comman.ui.card.CardActivity
import org.appjam.comman.ui.courseNonRegist.ChargePopupActivity
import org.appjam.comman.ui.main.MyCourseFragment
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads
import org.appjam.comman.youtube.YoutubePracticeActivity
import kotlin.properties.Delegates

class QuizResultActivity : AppCompatActivity() {

    companion object {
        private val TAG = "QuizResultActivity"
    }

    private var quizInfoList: List<QuizData.QuizInfo> = listOf()
    private var answerCount : Int = 0
    private var lecturePriority: Int = 0
    private var lectureTitle: String = ""
    private var passValue: Int = 0
    private var lectureID : Int = 0
    private var courseID : Int = 0
    private val disposables = CompositeDisposable()
    private var realm: Realm by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
        realm = Realm.getDefaultInstance()

        quizResult_back_btn.setOnClickListener{
            finish()
        }
        val gson = Gson()
        quizInfoList = gson.fromJson(intent.getStringExtra("quizInfoList"), QuizData.QuizResponse::class.java).result
        lectureTitle = intent.getStringExtra("lectureTitle")
        lecturePriority = intent.getIntExtra("lecturePriority", 0)
        passValue = intent.getIntExtra("passValue", 0)
        lectureID = intent.getIntExtra("lectureID", 0)
        courseID = intent.getIntExtra("courseID", 0)

        var i = 0
        for (quiz in quizInfoList) {
            val rQuizData = realm.where(RQuizData::class.java).equalTo("quizId", quiz.quizID).findFirst()
            if(quiz.questionArr[rQuizData?.answer ?: 0].answerFlag == 1) {
                answerCount++
            }
            i++
        }

        if(lecturePriority < 10) {
            quizResult_lecture_name_tv.text = "0${lecturePriority} ${lectureTitle}"
        } else {
            quizResult_lecture_name_tv.text = "${lecturePriority} ${lectureTitle}"
        }

        quizResult_prev_layout.setOnClickListener {
            val mutableList = mutableListOf<Int>()
            for(i in 1..(quizInfoList.size+1)) { mutableList.add(-1) }
            PrefUtils.putLectureOfCoursePosition(this, 0, courseID)
            val intent = Intent(this, QuizActivity::class.java)
            intent.getIntExtra("lectureID", lectureID)
            intent.getIntExtra("courseID", courseID)
            startActivity(intent)
        }

        quizResult_next_layout.setOnClickListener {
            disposables.add(APIClient.apiService.getNextLectureInfo(
                    PrefUtils.getUserToken(this), lectureID)
                    .setDefaultThreads()
                    .subscribe({ response ->
                        if(response.nextLectureOfCourse.purchaseFlag == 0) {   //구매해야되는 경우
                            val intent = Intent(this@QuizResultActivity, ChargePopupActivity::class.java)
                            intent.putExtra("courseID", courseID)
                            startActivity(intent)
                        }
                        if(response.nextLectureOfCourse.lectureID == -1) {
                            val intent = Intent(this, CourseSubActivity::class.java)
                            intent.putExtra("courseID", courseID)
                            startActivity(intent)
                        }

                        if(response.nextLectureOfCourse.lectureType == 0) {
                            val intent = Intent(this, QuizActivity::class.java)
                            intent.putExtra("lectureID", response.nextLectureOfCourse.lectureID)
                            intent.putExtra("courseID", courseID)
                            startActivity(intent)
                        } else if(response.nextLectureOfCourse.lectureType == 1) {
                            val intent = Intent(this, CardActivity::class.java)
                            intent.putExtra("lectureID", response.nextLectureOfCourse.lectureID)
                            intent.putExtra("courseID", courseID)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, YoutubePracticeActivity::class.java)
                            intent.putExtra("lectureID", response.nextLectureOfCourse.lectureID)
                            intent.putExtra("courseID", courseID)
                            startActivity(intent)
                        }
                    }, {
                        failure -> Log.i(MyCourseFragment.TAG, "on Failure ${failure.message}")
                    }))
        }

        quizResult_result_list.layoutManager = LinearLayoutManager(this)

        val quizResultAdapter = QuizResultAdapter()
        quizResult_result_list.adapter = quizResultAdapter

    }

    private inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            if(answerCount >= passValue) {
                disposables.add(APIClient.apiService.registerFinishLecture(
                        PrefUtils.getUserToken(this@QuizResultActivity), lectureID)
                        .setDefaultThreads()
                        .subscribe({ response -> response.message           //TODO 나중에 수강했습니다 토스트 띄울 건가요??
                        }, { failure ->
                            Log.i(QuizActivity.TAG, "on Failure ${failure.message}")
                        }))
                itemView.quizResult_result_tv.text = "축하합니다. 통과하셨습니다~!"
            } else {
                itemView.quizResult_result_tv.text = "아쉽네요~통과 못 하셨습니다. 다시 도전해 보세요."
            }
        }
    }

    private inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            if(position < 9) {
                itemView.quizResult_list_tv.text = "Q. 0${position+1}"
            } else {
                itemView.quizResult_list_tv.text = "Q. ${position+1}"
            }

            val rQuizData = realm.where(RQuizData::class.java).equalTo("quizId", quizInfoList[position].quizID).findFirst()
            if(quizInfoList[position].questionArr[rQuizData?.answer?: 0].answerFlag == 1) {
                Glide.with(this@QuizResultActivity)
                        .load(R.drawable.quiz_correct_mark)
                        .centerCrop()
                        .into(itemView.quizResult_list_img)
            } else {
//                itemView.quizResult_list_img.setImageResource(R.drawable.quiz_wrong_mark)
                Glide.with(this@QuizResultActivity)
                        .load(R.drawable.quiz_wrong_mark)
                        .centerCrop()
                        .into(itemView.quizResult_list_img)
            }
            itemView.setOnClickListener {
                val intent = Intent(this@QuizResultActivity, PopupExplainActivity::class.java)
                val gson = Gson()
                intent.putExtra("quizInfo", gson.toJson(quizInfoList[position]))
                intent.putExtra("quizCount", quizInfoList.size)
                startActivity(intent)
            }
        }
    }

    private inner class FooterViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    /***
     * 어댑터는 데이터와 화면 출력을 이어주는 객체입니다 여기서는 QuizResultData에 넣은 데이터들을 ViewHolder로 연결하기 위해 쓰였습니다 **/
    private inner class QuizResultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            when {
                holder?.itemViewType == ListUtils.TYPE_HEADER -> (holder as HeaderViewHolder).bind()
                holder?.itemViewType == ListUtils.TYPE_ELEM -> (holder as ElemViewHolder).bind(position - 1)
                else -> holder as QuizResultActivity.FooterViewHolder
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return if(viewType == ListUtils.TYPE_HEADER) {
                HeaderViewHolder(layoutInflater.inflate(R.layout.quiz_result_header_items, parent, false))
            } else if(viewType == ListUtils.TYPE_ELEM) {
                val view : View = layoutInflater.inflate(R.layout.quiz_result_items, parent, false)
                ElemViewHolder(view)
            } else {
                FooterViewHolder(layoutInflater.inflate(R.layout.quiz_result_footer, parent, false))
            }
        }

        override fun getItemViewType(position: Int): Int
            = when(position) {
            0 -> ListUtils.TYPE_HEADER
            (itemCount - 1) -> ListUtils.TYPE_FOOTER
            else -> ListUtils.TYPE_ELEM
        }

        override fun getItemCount(): Int = quizInfoList.size + 2

    }

    override fun onDestroy() {
        disposables.clear()
        realm.close()
        super.onDestroy()
    }
}