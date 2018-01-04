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

class LectureVideo1Activity : AppCompatActivity() {

    private var lecVideoList : RecyclerView? = null
    private var lecVideoDatas : ArrayList<LecVideoData>? = null
    private var lecVideoAdapter : LecVideoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_video1)

        lecVideoList = findViewById<View>(R.id.lec_list) as RecyclerView
        lecVideoList!!.layoutManager = LinearLayoutManager(this)

        lecVideoDatas = ArrayList<LecVideoData>()
        lecVideoDatas!!.add(LecVideoData(R.drawable.home_video_icon, "멀라", "1장"))


        lecVideoAdapter = LecVideoAdapter(lecVideoDatas)

        lecVideoList!!.adapter = lecVideoAdapter
    }
    /**ViewHolder는 각 리스트에 어떤 뷰가 들어가는지 설정해주는 부분입니다. 한 번 설정해주면 몇번이고 재사용이 가능합니다
     * */
    private inner class LecVideoViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        var Img : ImageView = itemView!!.findViewById(R.id.lecVideo_img)
        var time : TextView = itemView!!.findViewById(R.id.lecVideo_time_tv)
        var title : TextView = itemView!!.findViewById(R.id.lecVideo_title_tv)
    }

    data class LecVideoData (
            var Img : Int,
            var time : String,
            var title : String
    )

    /***
     * 어댑터는 데이터와 화면 출력을 이어주는 객체입니다 여기서는 QuizResultData에 넣은 데이터들을 ViewHolder로 연결하기 위해 쓰였습니다 **/
    private inner class LecVideoAdapter(var dataList : ArrayList<LecVideoData>?) : RecyclerView.Adapter<LecVideoViewHolder>() {

        override fun onBindViewHolder(holder: LecVideoViewHolder?, position: Int) {
            holder!!.Img.setImageResource(dataList!!.get(position).Img)
            holder!!.time.text = dataList!!.get(position).time
            holder!!.title.text = dataList!!.get(position).title
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LecVideoViewHolder {
            val mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.lecvideo_list_items, parent, false)
            return LecVideoViewHolder(mainView)
        }

        override fun getItemCount(): Int = dataList!!.size

    }
}