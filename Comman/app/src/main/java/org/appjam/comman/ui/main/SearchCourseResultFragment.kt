package org.appjam.comman.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_course_search_result.view.*
import kotlinx.android.synthetic.main.search_result_item.view.*
import org.appjam.comman.R
import org.appjam.comman.util.ListUtils

/**
 * Created by ChoGyuJin on 2018-01-04.
 */
class SearchCourseResultFragment : Fragment() {

    private val searchCourseItemList = mutableListOf<searchCourseItem>()
    data class searchCourseItem(
            val courseImage:Int,
            val courseTitle:String,
            val courseContent:String,
            val coursePeople:String
    )
    init {
        searchCourseItemList.add(searchCourseItem(R.drawable.picture_icon,"모바일 결제 기초","1오십자제한오십자제한오십자제한오십자제한오십자제한","13.2 천 명이 수강 중입니다."))
        searchCourseItemList.add(searchCourseItem(R.drawable.picture_icon,"모바일 결제 기초","2오십자제한오십자제한오십자제한오십자제한오십자제한","23.2 천 명이 수강 중입니다."))
        searchCourseItemList.add(searchCourseItem(R.drawable.picture_icon,"모바일 결제 기초","3오십자제한오십자제한오십자제한오십자제한오십자제한","33.2 천 명이 수강 중입니다."))
        searchCourseItemList.add(searchCourseItem(R.drawable.picture_icon,"모바일 결제 기초","4오십자제한오십자제한오십자제한오십자제한오십자제한","43.2 천 명이 수강 중입니다."))
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_course_search_result,container,false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val recyclerView = view!!.course_result_recyclerview
        recyclerView.adapter = CourseSearchResultAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)

    }
    inner class ElemViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int){
            itemView.course_result_img.setImageResource(searchCourseItemList[position]!!.courseImage)
            itemView.course_title_tv.text = searchCourseItemList[position].courseTitle
            itemView.course_content_tv.text = searchCourseItemList[position].courseContent
            itemView.course_peaple_tv.text = searchCourseItemList[position].coursePeople
        }
    }
    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class CourseSearchResultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if(holder?.itemViewType == ListUtils.TYPE_FOOTER){
                holder as FooterViewHolder
            }
            else if(holder?.itemViewType == ListUtils.TYPE_HEADER){
                holder as HeaderViewHolder
            }
            else if (holder?.itemViewType == ListUtils.TYPE_ELEM){
                (holder as ElemViewHolder).bind(position-1)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == itemCount - 1 ) ListUtils.TYPE_FOOTER
            else if (position == 0) ListUtils.TYPE_HEADER
            else ListUtils.TYPE_ELEM
        }

        override fun getItemCount() = searchCourseItemList.size + 1

        @SuppressLint("NewApi")
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return if(viewType == ListUtils.TYPE_FOOTER) {
                FooterViewHolder(layoutInflater.inflate(R.layout.course_item_footer, parent, false))
            }
            else if (viewType == ListUtils.TYPE_HEADER){
                HeaderViewHolder(layoutInflater.inflate(R.layout.category_header_item, parent, false))
            }
            else {
                ElemViewHolder(layoutInflater.inflate(R.layout.search_result_item, parent,false))
            }
        }
    }
}