package org.appjam.comman.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_course_search_result.view.*
import kotlinx.android.synthetic.main.search_result_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.data.SearchedCoursesData
import org.appjam.comman.util.ListUtils

/**
 * Created by ChoGyuJin on 2018-01-05.
 */
class SearchCourseListFragment : Fragment() {
    private var courseInfoJson : String? = null
    private var courseInfoList : SearchedCoursesData.SearchedCoursesResponse? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(arguments != null) {
            val gson = Gson()
            courseInfoJson = arguments.getString("ans")
            courseInfoList = gson.fromJson(courseInfoJson, SearchedCoursesData.SearchedCoursesResponse::class.java)
        }
        return inflater?.inflate(R.layout.fragment_course_search_result, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val recyclerView = view!!.course_result_recyclerview
        recyclerView.adapter = SearchCourseListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position:Int){
            itemView.course_title_tv.text = courseInfoList?.result?.get(position)?.title
            itemView.course_content_tv.text = courseInfoList?.result?.get(position)?.info
            val hit = courseInfoList?.result?.get(position)?.hit
            itemView.course_people_tv.text = "$hit 명이 수강중입니다"
        }
    }

    inner class SearchCourseListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if(holder?.itemViewType == ListUtils.TYPE_ELEM)
                (holder as ElemViewHolder).bind(position - 1)
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == itemCount - 1 ) ListUtils.TYPE_FOOTER
            else if (position == 0) ListUtils.TYPE_HEADER
            else ListUtils.TYPE_ELEM
        }

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

        override fun getItemCount() = courseInfoList?.result?.size?: 0

    }
}