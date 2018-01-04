package org.appjam.comman.ui.quiz

/**
 * Created by yeahen on 2017-12-31.
 */


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.appjam.comman.R

class QuizResultActivity : AppCompatActivity(), View.OnClickListener {

    private var quizResultList : RecyclerView? = null
    private var quizResultDatas : ArrayList<QuizResultData>? = null
    private var quizResultAdapter : QuizResultAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        quizResultList = findViewById<View>(R.id.quizResult_result_list) as RecyclerView
        quizResultList!!.layoutManager = LinearLayoutManager(this)

        quizResultDatas = ArrayList()
        quizResultDatas!!.add(QuizResultData(R.drawable.quiz_correct_mark, "Q.Q1"))
        quizResultDatas!!.add(QuizResultData(R.drawable.quiz_correct_mark, "Q.Q2"))
        quizResultDatas!!.add(QuizResultData(R.drawable.quiz_wrong_mark, "Q.Q3"))
        quizResultDatas!!.add(QuizResultData(R.drawable.quiz_correct_mark, "Q.Q4"))
        quizResultDatas!!.add(QuizResultData(R.drawable.quiz_wrong_mark, "Q.Q5"))

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

//        //기능 확인용으로 구현함 - 나중에 삭제 예정
//        val name : String? = quizResultDatas!!.get(idx).answer
//        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()

        val intent = Intent(this, PopupExplainActivity::class.java)
        intent.putExtra("quiz_num", "Q.13")
        intent.putExtra("quiz_text", "다음 중, 문서의 여백을 설정할 수 있는 방법으로 옳은 것을 고르시오.")
        intent.putExtra("quiz_answer_num", "정답: 3번")
        intent.putExtra("quiz_answer_text", "문서의 디자인 편집을 클릭하여 레이아웃 편집을 누른다. 양 옆의 여백의 사이즈를 입력하여 문서의 여백을 설정해야하므로 정답은..........." +
                "......................................................................................................................................................" +
                "....................................................................................................................")
        startActivity(intent)
    }

    /**ViewHolder는 각 리스트에 어떤 뷰가 들어가는지 설정해주는 부분입니다. 한 번 설정해주면 몇번이고 재사용이 가능합니다
     * */
    private inner class QuizResultViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        var resultImg : ImageView = itemView!!.findViewById(R.id.quizResult_list_img)
        var answer : TextView = itemView!!.findViewById(R.id.quizResult_list_tv)
    }

    data class QuizResultData (
            var resultImg : Int,
            var answer : String
    )

    /***
     * 어댑터는 데이터와 화면 출력을 이어주는 객체입니다 여기서는 QuizResultData에 넣은 데이터들을 ViewHolder로 연결하기 위해 쓰였습니다 **/
    private inner class QuizResultAdapter(var dataList : ArrayList<QuizResultData>?) : RecyclerView.Adapter<QuizResultViewHolder>() {
        private var onItemClick : View.OnClickListener? = null

        override fun onBindViewHolder(holder: QuizResultViewHolder?, position: Int) {
            holder!!.resultImg.setImageResource(dataList!!.get(position).resultImg)
            holder!!.answer.text = dataList!!.get(position).answer
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): QuizResultViewHolder {
            val mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.quiz_result_items, parent, false)
            mainView.setOnClickListener(onItemClick)
            return QuizResultViewHolder(mainView)
        }

        override fun getItemCount(): Int = dataList!!.size

        fun setOnItemClickListener(l:View.OnClickListener){
            onItemClick = l
        }

    }
}





