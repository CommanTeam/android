package org.appjam.comman.ui.lectureSubsection

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Adapter
import org.appjam.comman.R

/**
 * Created by KSY on 2018-01-03.
 */
class LectureSubActivity : AppCompatActivity(), View.OnClickListener {
    private var chapterList : RecyclerView?=null
    private var chapterListDatas : ArrayList<ChapterListData>?=null
    private var chapterAdapter : Adapter?=null

    private var courseList : RecyclerView?=null
    private var courseListDatas : ArrayList<CourseData>?=null
    private var courseAdapter : Adapter?=null

    private var videoList : RecyclerView?=null
    private var videoDatas : ArrayList<VideotData>?=null
    private var videoAdapter : Adapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_subsection)

        chapterList = findViewById(R.id.main_list) as RecyclerView
        pokemonList!!.layoutManager = LinearLayoutManager(this)

        }
}