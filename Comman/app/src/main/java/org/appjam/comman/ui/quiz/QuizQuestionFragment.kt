package org.appjam.comman.ui.quiz

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidquery.AQuery
import com.google.gson.Gson
import io.realm.Realm
import kotlinx.android.synthetic.main.choice_item.view.*
import kotlinx.android.synthetic.main.fragment_quiz_choice.view.*
import kotlinx.android.synthetic.main.quiz_content_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.data.QuizData
import org.appjam.comman.realm.RQuizData
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.SetColorUtils
import org.appjam.comman.util.SpaceItemDecoration
import kotlin.properties.Delegates

/**
 * Created by RyuDongIl on 2018-01-01.
 */
class QuizQuestionFragment : Fragment() {
    companion object {
        const val TAG = "QuizQuestionFragment"
    }

    private var pagePosition: Int = 0
    private var pageCount : Int = 0
    private var quizInfoList: List<QuizData.QuizInfo> = listOf()
    private var recycler_view : RecyclerView? = null
    private var realm: Realm by Delegates.notNull()
    private var selectedList: MutableList<Boolean> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        realm = Realm.getDefaultInstance()
        if(arguments != null) {
            val gson = Gson()
            quizInfoList = gson.fromJson(arguments.getString("quizInfoList"), QuizData.QuizResponse::class.java).result
            pagePosition = arguments.getInt("position")
            pageCount = arguments.getInt("pageCount")
            val choiceSize = quizInfoList[pagePosition].questionArr.size
            for (i in 0 until choiceSize) {
                selectedList.add(false)
            }
            val rQuizDataResult = realm.where(RQuizData::class.java)
                    .equalTo("quizId", quizInfoList[pagePosition].quizID)
                    .findAll()
            if (rQuizDataResult.size == 1 && rQuizDataResult[0]?.answer != null) {
                rQuizDataResult[0]?.answer?.let {
                    answer -> selectedList[answer] = true
                }
            }
        }
        return inflater!!.inflate(R.layout.fragment_quiz_choice, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler_view = view.quiz_quizChoice_rv
        recycler_view?.addItemDecoration(SpaceItemDecoration(context, 0, 0, 6, 6))
        recycler_view?.layoutManager = LinearLayoutManager(context)
        recycler_view?.adapter = QuizAdapter()

    }

    inner class QuizAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ListUtils.TYPE_HEADER -> HeaderViewHolder(layoutInflater.inflate(R.layout.quiz_content_item, parent, false))
                ListUtils.TYPE_FOOTER -> {
                    val mainView : View = layoutInflater.inflate(R.layout.choice_item_footer, parent, false)
                    FooterViewHolder(mainView)
                }
                else -> {
                    val mainView : View = layoutInflater.inflate(R.layout.choice_item, parent, false)
                    ElemViewHolder(mainView)
                }
            }
        }

        override fun getItemCount() = quizInfoList[pagePosition].questionArr.size + 2

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            when {
                holder?.itemViewType == ListUtils.TYPE_ELEM -> (holder as QuizQuestionFragment.ElemViewHolder).bind(position - 1)
                holder?.itemViewType == ListUtils.TYPE_FOOTER -> holder as QuizQuestionFragment.FooterViewHolder
                holder?.itemViewType == ListUtils.TYPE_HEADER -> (holder as QuizQuestionFragment.HeaderViewHolder).bind()
            }

        }

        override fun getItemViewType(position: Int): Int
                = when (position) {
                    0 -> ListUtils.TYPE_HEADER
                    itemCount - 1 -> ListUtils.TYPE_FOOTER
                    else -> ListUtils.TYPE_ELEM
                }
    }



    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind(position: Int) {
            itemView.quiz_choice_number_tv.text = "${position + 1}번"
            itemView.quiz_choice_content_tv.text =
                    quizInfoList[pagePosition].questionArr[position].questionContent

            if(selectedList[position]) {
                    itemView.quiz_choice_layout.setBackgroundColor(SetColorUtils.get(context, R.color.primaryColor))
                    itemView.quiz_choice_number_tv.setTextColor(SetColorUtils.get(context, R.color.white))
                    itemView.quiz_choice_content_tv.setTextColor(SetColorUtils.get(context, R.color.white))
            } else {
                itemView.quiz_choice_layout.setBackgroundColor(SetColorUtils.get(context, R.color.white))
                itemView.quiz_choice_number_tv.setTextColor(SetColorUtils.get(context, R.color.mainTextColor))
                itemView.quiz_choice_content_tv.setTextColor(SetColorUtils.get(context, R.color.mainTextColor))
            }
            itemView.setOnClickListener {
                itemView.quiz_choice_layout.setBackgroundColor(SetColorUtils.get(context, R.color.primaryColor))
                itemView.quiz_choice_number_tv.setTextColor(SetColorUtils.get(context, R.color.white))
                itemView.quiz_choice_content_tv.setTextColor(SetColorUtils.get(context, R.color.white))
                val rQuizDataResult = realm.where(RQuizData::class.java)
                        .equalTo("quizId", quizInfoList[pagePosition].quizID)
                        .findAll()
                realm.executeTransaction {
                    if (rQuizDataResult.size == 1) {
                        rQuizDataResult[0]?.answer = position
                    } else if (rQuizDataResult.size == 0) {
                        val newRQuizData = realm.createObject(RQuizData::class.java)
                        newRQuizData.quizId = quizInfoList[pagePosition].quizID
                        newRQuizData.answer = position
                    }
                }
                for (i in 0 until selectedList.size) {
                    selectedList[i] = false
                }
                selectedList[position] = true
                recycler_view?.adapter?.notifyDataSetChanged()
            }
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            if(pagePosition < 9) {
                itemView.quiz_number_tv.text = "Q. 0${pagePosition + 1}"
            } else {
                itemView.quiz_number_tv.text = "Q. ${pagePosition + 1}"
            }
            itemView.quiz_count_btn.text = "${pagePosition + 1} / $pageCount"
            itemView.quiz_question_tv.text = quizInfoList[pagePosition].quizTitle
            if(quizInfoList[pagePosition].quizImage == "") {
                itemView.quiz_question_img.visibility = View.GONE
            } else {
                itemView.quiz_question_img.visibility = View.VISIBLE
                val aQuery = AQuery(context)
                aQuery.id(itemView.quiz_question_img).image(quizInfoList[pagePosition].quizImage)
            }
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}







