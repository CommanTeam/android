package org.appjam.comman.ui.quiz

/**
 * Created by yeahen on 2017-12-31.
 */


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_quiz_result.*
import kotlinx.android.synthetic.main.quiz_result_header_items.view.*
import kotlinx.android.synthetic.main.quiz_result_items.view.*
import org.appjam.comman.R
import org.appjam.comman.util.ListUtils

class QuizResultActivity : AppCompatActivity(), View.OnClickListener {

    private var quizResultList : RecyclerView? = null
    private var quizResultDatas : ArrayList<QuizResultData>? = arrayListOf()
    private var quizResultAdapter : QuizResultAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        quizResultList = quizResult_result_list
        quizResultList!!.layoutManager = LinearLayoutManager(this)

        quizResultDatas?.add(QuizResultData(R.drawable.quiz_correct_mark, "Q.Q1","축하합니다.통과하셨습니다~!"))
        quizResultDatas?.add(QuizResultData(R.drawable.quiz_correct_mark, "Q.Q2", "축하합니다.통과하셨습니다~!"))
        quizResultDatas?.add(QuizResultData(R.drawable.quiz_wrong_mark, "Q.Q3", "축하합니다.통과하셨습니다~!"))
        quizResultDatas?.add(QuizResultData(R.drawable.quiz_correct_mark, "Q.Q4", "축하합니다.통과하셨습니다~!"))
        quizResultDatas?.add(QuizResultData(R.drawable.quiz_wrong_mark, "Q.Q5", "축하합니다.통과하셨습니다~!"))



        quizResultAdapter = QuizResultAdapter(quizResultDatas)
        quizResultAdapter!!.setOnItemClickListener(this)

        quizResultList!!.adapter = quizResultAdapter

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //바깥 레이어 클릭시 안닫히게 한다.
        if (MotionEvent.ACTION_OUTSIDE == event!!.action) {
            return false
        }
        return true
    }

    //팝업 띄우기
    override fun onClick(v: View?) {
        val idx : Int = quizResultList!!.getChildAdapterPosition(v)

        val intent = Intent(this, PopupExplainActivity::class.java)
        intent.putExtra("quiz_num", "Q.13")
        intent.putExtra("quiz_text", "다음 중, 문서의 여백을 설정할 수 있는 방법으로 옳은 것을 고르시오.")
        intent.putExtra("quiz_answer_num", "정답: 3번")
        intent.putExtra("quiz_answer_text", "문서의 디자인 편집을 클릭하여 레이아웃 편집을 누른다. 양 옆의 여백의 사이즈를 입력하여 문서의 여백을 설정해야하므로 정답은..........." +
                "......................................................................................................................................................" +
                "....................................................................................................................")
        startActivity(intent)
    }

    private inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.quizResult_result_tv.text = quizResultDatas!![position].result

        }
    }
    private inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.quizResult_list_img.setImageResource(quizResultDatas!![position].resultImg)
            itemView.quizResult_list_tv.text = quizResultDatas!![position].answer

        }
    }

    private inner class FooterViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    data class QuizResultData (
            var resultImg : Int,
            var answer : String,
            var result : String
    )

    /***
     * 어댑터는 데이터와 화면 출력을 이어주는 객체입니다 여기서는 QuizResultData에 넣은 데이터들을 ViewHolder로 연결하기 위해 쓰였습니다 **/
    private inner class QuizResultAdapter(var dataList : ArrayList<QuizResultData>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var onItemClick : View.OnClickListener? = null

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if(holder?.itemViewType == ListUtils.TYPE_HEADER) {

            }
            else if(holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as ElemViewHolder).bind(position - 1)
            }
            else {
                holder as QuizResultActivity.FooterViewHolder
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            if(viewType == ListUtils.TYPE_HEADER) {
                return HeaderViewHolder(layoutInflater.inflate(R.layout.quiz_result_header_items, parent, false))
            }

            else if(viewType == ListUtils.TYPE_ELEM) {
                val view : View = layoutInflater.inflate(R.layout.quiz_result_items, parent, false)
                view.setOnClickListener(onItemClick)
                return ElemViewHolder(view)
            }

           else {
                return FooterViewHolder(layoutInflater.inflate(R.layout.quiz_result_footer, parent, false))
            }
        }

        override fun getItemViewType(position: Int): Int
            = when(position) {
            0 -> ListUtils.TYPE_HEADER
            (itemCount - 1) -> ListUtils.TYPE_FOOTER
            else -> ListUtils.TYPE_ELEM
        }


        override fun getItemCount(): Int = quizResultDatas!!.size + 2

        fun setOnItemClickListener(l:View.OnClickListener){
            onItemClick = l
        }

    }
}





