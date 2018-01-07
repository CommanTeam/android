package org.appjam.comman.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_newalarm_request.view.*
import kotlinx.android.synthetic.main.main_notice_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.util.ListUtils

/**
 * Created by ChoGyuJin on 2018-01-05.
 */
class SearchNewCourseFragment : Fragment() {

    private val myCourseAlarmList =  mutableListOf<CoursesData.MyCourseAlarmItem>()

    init {
        myCourseAlarmList.add(CoursesData.MyCourseAlarmItem(R.drawable.profile_image,"[Rhino] 반지 모델링하기 강좌의 답변이 도착했도다"))
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_newalarm_request, container, false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val recyclerView = view!!.newalarm_request_rv
        recyclerView.adapter = MyCourseAlarmAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    inner class MyCourseAlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int){
            itemView.main_profile_img.setImageResource(myCourseAlarmList[position].myCourseProfile)
            itemView.main_notice_tv.text = myCourseAlarmList[position].myCourseAlarm
        }
    }

    inner class CourseResistRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class MyCourseAlarmAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if(holder?.itemViewType == ListUtils.TYPE_HEADER){
                (holder as MyCourseAlarmViewHolder).bind(position)
            }
            else if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                holder as CourseResistRequestViewHolder
            }
        }

        override fun getItemCount(): Int = myCourseAlarmList.size + 1
        override fun getItemViewType(position: Int): Int {
            return if (position==0) ListUtils.TYPE_HEADER
            else ListUtils.TYPE_ELEM
        }
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == ListUtils.TYPE_HEADER){
                MyCourseAlarmViewHolder(layoutInflater.inflate(R.layout.main_notice_item, parent, false))
            }
            else {
                CourseResistRequestViewHolder(layoutInflater.inflate(R.layout.no_resist_course_item, parent, false))
            }
        }

    }

}