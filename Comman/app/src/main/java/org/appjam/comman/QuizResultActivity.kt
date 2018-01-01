package org.appjam.comman

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class QuizResultActivity : AppCompatActivity() {

    private var quizResultList : RecyclerView? = null
    private var quizResultDatas : ArrayList<QuizResultData>? = null
    private var quizResultAdapter : QuizResultAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        quizResultList = findViewById<View>(R.id.quizResult_result_list) as RecyclerView
        quizResultList!!.layoutManager = LinearLayoutManager(this)

        quizResultDatas = ArrayList<QuizResultData>()
        quizResultDatas!!.add(QuizResultData(R.drawable.kakaotalk_icon, "Q.Q1"))
        quizResultDatas!!.add(QuizResultData(R.drawable.kakaotalk_icon, "Q.Q2"))
        quizResultDatas!!.add(QuizResultData(R.drawable.kakaotalk_icon, "Q.Q3"))
        quizResultDatas!!.add(QuizResultData(R.drawable.kakaotalk_icon, "Q.Q4"))
        quizResultDatas!!.add(QuizResultData(R.drawable.kakaotalk_icon, "Q.Q5"))

        quizResultAdapter = QuizResultAdapter(quizResultDatas)
        quizResultList!!.adapter = quizResultAdapter
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
        override fun onBindViewHolder(holder: QuizResultViewHolder?, position: Int) {
            holder!!.resultImg.setImageResource(dataList!!.get(position).resultImg)
            holder!!.answer.text = dataList!!.get(position).answer
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): QuizResultViewHolder {
            val mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.quiz_result_items, parent, false)
            return QuizResultViewHolder(mainView)
        }

        override fun getItemCount(): Int = dataList!!.size

    }




}





