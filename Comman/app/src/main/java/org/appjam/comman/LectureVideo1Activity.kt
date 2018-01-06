package org.appjam.comman

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_lecture_video1.*
import kotlinx.android.synthetic.main.etc_lecvideo_list_items.view.*
import kotlinx.android.synthetic.main.second_lecvideo_list_items.view.*
import org.appjam.comman.util.ListUtils

class LectureVideo1Activity : AppCompatActivity() {

    private val lecList = mutableListOf<LectureVideo1Activity.LecVideoData>()
    private var lecVideoAdapter : LecVideoAdapter? = null

    init {
        lecList.add(LectureVideo1Activity.LecVideoData(R.drawable.home_video_icon, "멀라", "모른다고"))
        lecList.add(LectureVideo1Activity.LecVideoData(R.drawable.completed_quiz_icon, "멀라", "모른다고"))
        lecList.add(LectureVideo1Activity.LecVideoData(R.drawable.completed_quiz_icon, "멀라", "모른다고"))
        lecList.add(LectureVideo1Activity.LecVideoData(R.drawable.completed_quiz_icon, "멀라", "모른다고"))
        lecList.add(LectureVideo1Activity.LecVideoData(R.drawable.completed_quiz_icon, "멀라", "모른다고"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_video1)

        val recyclerView = lec_list
        lecVideoAdapter = LecVideoAdapter()
        recyclerView.adapter = lecVideoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    data class LecVideoData (
            var Img : Int,
            var time : String,
            var title : String
    )

    /***
     * 어댑터는 데이터와 화면 출력을 이어주는 객체입니다 여기서는 QuizResultData에 넣은 데이터들을 ViewHolder로 연결하기 위해 쓰였습니다 **/
    private inner class LecVideoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_HEADER) { //이건 고정으로 넣는 것이기 때문에 상관하지 않는다.

            }
            else if (holder?.itemViewType == ListUtils.TYPE_SECOND_HEADER) { // 리스트에서 가장 1번 값이다. 하지만 인덱스 상에서는 0번째 값이 되어야 1번째에 올 수 있기에 -1
                (holder as LectureVideo1Activity.SecondViewHolder).bind(position - 1)
            }
            else if(holder?.itemViewType == ListUtils.TYPE_ELEM) { //리스트에서 2번째 값이다. 하지만 인덱스 상에서는 1번째 값이 되어야 2번째에 올 수 있기에 -1
                (holder as LectureVideo1Activity.EtcViewHolder).bind(position - 1)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            if(viewType == ListUtils.TYPE_HEADER) {
                return FirstViewHolder(layoutInflater.inflate(R.layout.first_lecvideo_list_items, parent, false))
            }

            else if(viewType == ListUtils.TYPE_SECOND_HEADER) {
                return SecondViewHolder(layoutInflater.inflate(R.layout.second_lecvideo_list_items, parent, false))
            }
            else {
                val mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.etc_lecvideo_list_items, parent, false)
                return EtcViewHolder(mainView)
            }

        }

        //
        override fun getItemCount(): Int = lecList.size + 1

        override fun getItemViewType(position: Int): Int
                = when (position) {
            0 -> ListUtils.TYPE_HEADER // 가장 첫번째
            1 -> ListUtils.TYPE_SECOND_HEADER // 두번째
            else -> ListUtils.TYPE_ELEM // 여러개 사용할 때
        }


    }

    private inner class FirstViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView)

    private inner class LastViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) //서버 통신할 때, result 값이 -1이면 마지막 강의라는 뜻

    private inner class SecondViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.secLecVideo_img.setImageResource(lecList[position].Img)
            itemView.secLecVideo_time_tv.text = lecList[position].time
            itemView.secLecVideo_title_tv.text = lecList[position].title
        }
    }

    private inner class EtcViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.etcLecVideo_img.setImageResource(lecList[position].Img)
            itemView.etcLecVideo_time_tv.text = lecList[position].time
            itemView.etcLecVideo_title_tv.text = lecList[position].title
        }
    }


}