package org.appjam.comman.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.course_search_item.view.*
import kotlinx.android.synthetic.main.course_search_result.view.*
import org.appjam.comman.R
import org.appjam.comman.network.data.SearchedCoursesData

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
        return inflater?.inflate(R.layout.course_search_result, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val recyclerView = view!!.course_search_recyclerView
        recyclerView.adapter = SearchCourseListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    inner class SearchCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position:Int){
            itemView.course_name_list_tv.text = courseInfoList?.result?.get(position)?.title ?: ""
        }
    }

    inner class SearchCourseListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            (holder as SearchCourseViewHolder).bind(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return SearchCourseViewHolder(layoutInflater.inflate(R.layout.course_search_item,parent,false))
        }

        override fun getItemCount() = courseInfoList?.result?.size?: 0

    }
}