package org.appjam.comman.ui.card

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
=======
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
>>>>>>> commit_solution
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.question_elem_item.view.*
import kotlinx.android.synthetic.main.question_head_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.QuestionData
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads

class QuestionActivity : AppCompatActivity() {

    companion object {
        const val TAG = "QuestionActivity"
    }

    private val disposables = CompositeDisposable()
    private var questionInfoList: MutableList<QuestionData.QuestionInfo> = mutableListOf()
    private var lectureID: Int = 0
    private var lectureTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        question_back_btn.setOnClickListener {
            finish()
        }

        lectureTitle = intent.getStringExtra("lectureTitle")
        question_lecture_name_tv.text = lectureTitle
        lectureID = intent.getIntExtra("lectureID", 0)

        question_qna_rv.layoutManager = LinearLayoutManager(this)
        question_qna_rv.adapter = QuestionAdapter()

        disposables.add(APIClient.apiService.getQuestionOfLecture(
                PrefUtils.getUserToken(this@QuestionActivity), lectureID)
                .setDefaultThreads()
                .subscribe({ response ->
                    questionInfoList = response.result as MutableList<QuestionData.QuestionInfo>
                    question_qna_rv.adapter.notifyDataSetChanged()
                }, { failure ->
                    Log.i(TAG, "on Failure ${failure.message}")
                }))

    }

    inner class QuestionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ListUtils.TYPE_HEADER -> {
                    val mainView: View = layoutInflater.inflate(R.layout.question_head_item, parent, false)
                    HeaderViewHolder(mainView)
                }
                ListUtils.TYPE_FOOTER -> {
                    val footView: View = layoutInflater.inflate(R.layout.course_item_footer, parent, false)
                    FooterViewHolder(footView)
                }
                else -> {
                    val elemView: View = layoutInflater.inflate(R.layout.question_elem_item, parent, false)
                    ElemViewHolder(elemView)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            when {
                holder?.itemViewType == ListUtils.TYPE_ELEM ->
                    (holder as ElemViewHolder).bind(questionInfoList[position - 1])
                holder?.itemViewType == ListUtils.TYPE_HEADER -> (holder as HeaderViewHolder).bind()
                holder?.itemViewType == ListUtils.TYPE_FOOTER -> holder as FooterViewHolder
            }
        }

        override fun getItemCount(): Int = questionInfoList.size + 2

        override fun getItemViewType(position: Int): Int {
            return if (position == itemCount - 1) ListUtils.TYPE_FOOTER
            else if (position == 0) ListUtils.TYPE_HEADER
            else ListUtils.TYPE_ELEM
        }

    }

    inner class HeaderViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.question_item_cnt_tv.text = "${questionInfoList.size}개"
            itemView.question_item_finish_img.setOnClickListener {
                finish()
            }
            itemView.question_item_regist_btn.setOnClickListener {
                if (itemView.question_item_et.text.isEmpty()) {

                } else {
                    disposables.add(APIClient.apiService.registerQuestion(
                            PrefUtils.getUserToken(this@QuestionActivity),
                            QuestionData.QuestionPost(lectureID, itemView.question_item_et.text.toString()))
                            .setDefaultThreads()
                            .subscribe({ response ->
                                questionInfoList.add(0, QuestionData.QuestionInfo(1, PrefUtils.getString(this@QuestionActivity, PrefUtils.NICKNAME)
                                        , 1, response.result.question_text, response.result.question_date, response.result.flag
                                        , 0, 1, "", "", ""))
                                itemView.question_item_et.text = null
                                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(itemView.question_item_et.windowToken, 0)
                                question_qna_rv.adapter.notifyDataSetChanged()
                            }, { failure ->
                                Log.i(TAG, "on Failure ${failure.message}")
                            }))
                }
            }
        }
    }

    inner class ElemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: QuestionData.QuestionInfo) {
            itemView.question_user_name_tv.text = data.l_question_user_nickname
            itemView.question_date_tv.text = data.l_question_date
            itemView.question_content_tv.text = data.question_text
            if (data.l_question_flag == 0) {
                itemView.question_answer_layout.visibility = View.GONE
            } else {
                itemView.question_answer_layout.visibility = View.VISIBLE
                itemView.answer_user_name_tv.text = data.supplier_name
                itemView.answer_date_tv.text = data.l_answer_date
                itemView.answer_content_tv.text = data.l_answer_text
            }
        }
    }

    inner class FooterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}