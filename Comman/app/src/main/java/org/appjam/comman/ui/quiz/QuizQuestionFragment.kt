package org.appjam.comman.ui.quiz

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.choice_item.view.*
import kotlinx.android.synthetic.main.choice_item_footer.view.*
import kotlinx.android.synthetic.main.fragment_quiz_choice.view.*
import org.appjam.comman.R
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.SetColorUtils
import org.appjam.comman.util.SpaceItemDecoration

/**
 * Created by RyuDongIl on 2018-01-01.
 */
class QuizQuestionFragment : Fragment() {
    private val questionContentsList = mutableListOf<QuizQuestionFragment.ContentItem>()
    data class ContentItem(
            val number: String,
            val content: String
    )
    private var adapter : QuizAdapter? = null

    init {
        // TODO: Implement network data class
        questionContentsList.add(QuizQuestionFragment.ContentItem("1번", "보기 > 디자인 설정 > 여백 바꾸기"))
        questionContentsList.add(QuizQuestionFragment.ContentItem("2번", "보기 > 디자인 설정 > 여백 바꾸기"))
        questionContentsList.add(QuizQuestionFragment.ContentItem("3번", "보기 > 디자인 설정 > 여백 바꾸기"))
        questionContentsList.add(QuizQuestionFragment.ContentItem("4번", "보기 > 디자인 설정 > 여백 바꾸기"))
        questionContentsList.add(QuizQuestionFragment.ContentItem("5번", "보기 > 디자인 설정 > 여백 바꾸기"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.quiz_quizChoice_rv
        adapter = QuizAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpaceItemDecoration(context, 0, 0, 6, 6))
        recyclerView.layoutManager = LinearLayoutManager(context)

    }

    inner class QuizAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var onItemClick : View.OnClickListener? = null

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == ListUtils.TYPE_HEADER) {
                return HeaderViewHolder(layoutInflater.inflate(R.layout.quiz_content_item, parent, false))
            } else if (viewType == ListUtils.TYPE_FOOTER) {
                val mainView : View = layoutInflater.inflate(R.layout.choice_item_footer, parent, false)
                mainView.setOnClickListener(onItemClick)
                return FooterViewHolder(mainView)
            } else {
                val mainView : View = layoutInflater.inflate(R.layout.choice_item, parent, false)
                mainView.setOnClickListener(onItemClick)
                return ElemViewHolder(mainView)
            }
        }

        override fun getItemCount() = questionContentsList.size + 1

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as QuizQuestionFragment.ElemViewHolder).bind(position - 1)
            } else if(holder?.itemViewType == ListUtils.TYPE_FOOTER) {
                (holder as QuizQuestionFragment.FooterViewHolder).bind(position - 1)
            }

        }

        override fun getItemViewType(position: Int): Int
                = when (position) {
                    0 -> ListUtils.TYPE_HEADER
                    itemCount - 1 -> ListUtils.TYPE_FOOTER
                    else -> ListUtils.TYPE_ELEM
                }

        fun setOnItemClickListener(l : View.OnClickListener) {
            onItemClick = l
        }


    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
         = inflater!!.inflate(R.layout.fragment_quiz_choice, container, false)

    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind(position: Int) {
            itemView.quiz_choice_number_tv.text = questionContentsList[position].number
            itemView.quiz_choice_content_tv.text = questionContentsList[position].content
            itemView.setOnClickListener {
                itemView.quiz_choice_layout.setBackgroundColor(SetColorUtils.get(context, R.color.primaryColor))
                itemView.quiz_choice_number_tv.setTextColor(SetColorUtils.get(context, R.color.white))
                itemView.quiz_choice_content_tv.setTextColor(SetColorUtils.get(context, R.color.white))
            }
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind(position: Int) {
            itemView.quiz_footer_choice_number_tv.text = questionContentsList[position].number
            itemView.quiz_footer_choice_content_tv.text = questionContentsList[position].content
            itemView.setOnClickListener {
                itemView.quiz_footer_choice_layout.setBackgroundColor(SetColorUtils.get(context, R.color.primaryColor))
                itemView.quiz_footer_choice_number_tv.setTextColor(SetColorUtils.get(context, R.color.white))
                itemView.quiz_footer_choice_content_tv.setTextColor(SetColorUtils.get(context, R.color.white))
            }
        }
    }

}