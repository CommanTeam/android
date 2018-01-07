package org.appjam.comman.ui.lecture

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lecture_list.*
import kotlinx.android.synthetic.main.chapter_explanation_item.view.*
import kotlinx.android.synthetic.main.lecture_list_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.ChapterData
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.setDefaultThreads
import java.util.*

/**
 * Created by ChoGyuJin on 2017-12-31.
 */
class LectureListActivity : AppCompatActivity(), View.OnClickListener  {

    companion object {
        const val TAG = "LectureListActivity"
    }

    private var lectureList : RecyclerView? = null
    private var lectureData : ArrayList<LectureData> = arrayListOf()
    private var lectureAdapter : LectureAdapter? = null

    private var chapterInfo: ChapterData.ChapterInfo? = null
    private var lectureListInChapter : List<ChapterData.LectureListinChapterData>? = null
    private val disposables = CompositeDisposable()

    data class LectureData(
            var lectureImage:Int,
            var lectureNum:String,
            var lectureName:String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_list)

        lectureList = lecture_list
        lectureList!!.layoutManager = LinearLayoutManager(this)

        lectureData.add(LectureData(R.drawable.picture_icon,"28 페이지","01 필렛 넣기"))
        lectureData.add(LectureData(R.drawable.video_icon,"10: 18","02 리볼브 하기"))
        lectureData.add(LectureData(R.drawable.quiz_icon,"20 문제","03 챔퍼 넣기"))
        lectureData.add(LectureData(R.drawable.video_icon,"15 페이지","04 면만들기"))
        disposables.add(APIClient.apiService.getChapterInfo(intent.getIntExtra(ChapterData.CHAPTER_ID_KEY, 0))
                .setDefaultThreads()
                .subscribe({
                    response ->
                        chapterInfo = response.data[0]
                        lectureList?.adapter?.notifyDataSetChanged()
                }, {
                    failure -> Log.i(TAG, failure.message)
                }))
        disposables.add(APIClient.apiService.getLectureListInChapter( intent.getIntExtra("chapterID",0))
                .setDefaultThreads()
                .subscribe({ response1 ->
                    lectureListInChapter = response1.result
                    lectureList?.adapter?.notifyDataSetChanged()
                },{
                    failure -> Log.i(TAG, failure.message)
                }))

        lectureAdapter = LectureAdapter()
//        lectureAdapter!!.setOnItemClickListener(this)
        lectureList!!.adapter = lectureAdapter
    }
    override fun onClick(v: View?) {
        val idx : Int = lectureList!!.getChildAdapterPosition(v)
        val name : String? = lectureData!!.get(idx).lectureName

        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }
    inner class ChapterExpViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        fun bind() {
            //var chaptertitle: TextView = itemView!!.findViewById(R.id.chapter_title_tv) as TextView
            itemView.chapter_title_tv.text = chapterInfo?.title
            //var chaptercont: TextView = itemView!!.findViewById(R.id.chapter_content_tv) as TextView
            itemView.chapter_content_tv.text = chapterInfo?.info
        }
    }
    inner class LectureViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int) {
            //var lectureimage: ImageView = itemView!!.findViewById(R.id.lecture_list_img) as ImageView
//            Glide.with(this@LectureListActivity)
//                    .load(lectureListInChapter?.watchedFlag)
//                    .into(itemView.lecture_list_img)
            itemView.lecture_list_img.setImageResource(lectureData[position].lectureImage)
            //var lecturenum: TextView = itemView!!.findViewById(R.id.lecture_list_num_tv) as TextView
            itemView.lecture_list_num_tv.text = lectureData!![position].lectureNum
            itemView.lecture_list_name_tv.text = lectureData!![position].lectureName
           // itemView.lecture_list_num_tv.text = lectureListInChapter?.get(position)?.size.toString()
            //itemView.lecture_list_name_tv.text = lectureListInChapter?.get(position)?.lectureTitle
        }
    }

    inner class FootViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    inner class LectureAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        private var onItemClick : View.OnClickListener? = null
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        //  val mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.lecture_list_item, parent, false)
        //  mainView.setOnClickListener(onItemClick)
        // return LectureViewHolder(mainView)11

            return if (viewType == ListUtils.TYPE_HEADER) {
                val mainView : View = layoutInflater.inflate(R.layout.chapter_explanation_item, parent, false)
                mainView.setOnClickListener(onItemClick)
                ChapterExpViewHolder(mainView)
            }
            else if (viewType == ListUtils.TYPE_FOOTER){
                val footView : View = layoutInflater.inflate(R.layout.course_item_footer, parent, false)
                footView.setOnClickListener(onItemClick)
                FootViewHolder(footView)
            }

            else {
                val elemView : View = layoutInflater.inflate(R.layout.lecture_list_item, parent, false)
                elemView.setOnClickListener(onItemClick)
                LectureViewHolder(elemView)
            }
        }


        override fun getItemCount()= lectureListInChapter?.size?: 0 + 2

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as LectureViewHolder).bind(position - 1)
            } else if(holder?.itemViewType == ListUtils.TYPE_HEADER) {
                (holder as ChapterExpViewHolder).bind()
            }
            else if(holder?.itemViewType == ListUtils.TYPE_FOOTER){
                holder as FootViewHolder
            }

//            holder!!.lectureimage.setImageResource(datalist!!.get(position).lectureimage)
//            holder!!.lecturenum.text = datalist!!.get(position).lecturenum
//            holder!!.lecturename.text = datalist!!.get(position).lecturename
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) ListUtils.TYPE_HEADER
            else if (position == itemCount - 1) ListUtils.TYPE_FOOTER
            else ListUtils.TYPE_ELEM
        }

    }
//disposables은 불렀으면 반드시 파괴해주어야한다.
    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
