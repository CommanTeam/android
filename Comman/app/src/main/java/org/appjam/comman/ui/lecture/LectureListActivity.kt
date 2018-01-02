package org.appjam.comman.ui.lecture

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.appjam.comman.R
import java.util.*

/**
 * Created by ChoGyuJin on 2017-12-31.
 */
class LectureListActivity : AppCompatActivity(), View.OnClickListener  {

    private var lectureList : RecyclerView? = null
    private var lectureData : ArrayList<LectureData>? = null
    private var lectureAdapter : LectureAdapter? = null

    data class LectureData(
            var lectureimage:Int,
            var lecturenum:String,
            var lecturename:String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_list)

        lectureList = findViewById(R.id.lecture_list) as RecyclerView
        lectureList!!.layoutManager = LinearLayoutManager(this)

        lectureData = ArrayList<LectureData>()
        lectureData!!.add(LectureData(R.drawable.picture_icon,"28 페이지","01 필렛 넣기"))
        lectureData!!.add(LectureData(R.drawable.video_icon,"10: 18","02 리볼브 하기"))
        lectureData!!.add(LectureData(R.drawable.quiz_icon,"20 문제","03 챔퍼 넣기"))
        lectureData!!.add(LectureData(R.drawable.picture_icon,"15 페이지","04 면만들기"))

        lectureAdapter = LectureAdapter(lectureData)
        lectureAdapter!!.setOnItemClickListener(this)
        lectureList!!.adapter = lectureAdapter
    }
    override fun onClick(v: View?) {
        val idx : Int = lectureList!!.getChildAdapterPosition(v)
        val name : String? = lectureData!!.get(idx).lecturename

        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }

    inner class LectureViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var lectureimage: ImageView = itemView!!.findViewById(R.id.lecture_list_img) as ImageView
        var lecturenum: TextView = itemView!!.findViewById(R.id.lecture_list_num_tv) as TextView
        var lecturename: TextView = itemView!!.findViewById(R.id.lecture_list_name_tv) as TextView
    }
    inner class LectureAdapter(var datalist : ArrayList<LectureData>?) : RecyclerView.Adapter<LectureViewHolder>(){

        private var onItemClick : View.OnClickListener? = null
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LectureViewHolder {
            val mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.lecture_list_item, parent, false)
            mainView.setOnClickListener(onItemClick)
            return LectureViewHolder(mainView)
        }

        override fun getItemCount(): Int = datalist!!.size

        override fun onBindViewHolder(holder: LectureViewHolder?, position: Int) {
            holder!!.lectureimage.setImageResource(datalist!!.get(position).lectureimage)
            holder!!.lecturenum.text = datalist!!.get(position).lecturenum
            holder!!.lecturename.text = datalist!!.get(position).lecturename
        }

        fun setOnItemClickListener(l: View.OnClickListener){
            onItemClick = l
        }
    }
}
