package org.appjam.comman.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_course_search_result.view.*
import kotlinx.android.synthetic.main.search_result_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.SearchedCoursesData
import org.appjam.comman.ui.CourseSubsection.CourseSubActivity
import org.appjam.comman.ui.courseNonRegist.CourseNonRegistActivity
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads

/**
 * Created by ChoGyuJin on 2018-01-05.
 */
class SearchCourseListFragment : Fragment() {

    companion object {
        private val TAG = "SearchCourseListFragment"
    }

    private var courseInfoJson : String? = null
    private var courseInfoList : List<SearchedCoursesData.SearchedCourseInfo> = listOf()
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(arguments != null) {
            val gson = Gson()
            courseInfoJson = arguments.getString("ans")
            courseInfoList = gson.fromJson(courseInfoJson, SearchedCoursesData.SearchedCoursesResponse::class.java).result
        }
        return inflater?.inflate(R.layout.fragment_course_search_result, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val recyclerView = view!!.course_result_recyclerview
        recyclerView.adapter = SearchCourseListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        view.setOnClickListener {
            val hide = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hide.hideSoftInputFromWindow(view.windowToken, 0)

        }
        }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener{
                val hide = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hide.hideSoftInputFromWindow(itemView.windowToken, 0)

            }
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener {
                val hide = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hide.hideSoftInputFromWindow(itemView.windowToken, 0)

            }
        }
    }

    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("LongLogTag")
        fun bind(position:Int){
            itemView.course_title_tv.text = courseInfoList[position].title
            itemView.course_content_tv.text = courseInfoList[position].info
            val hit = courseInfoList[position].hit
            itemView.course_people_tv.text = "$hit 명이 수강중입니다"
            itemView.setOnClickListener {
                val hide = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hide.hideSoftInputFromWindow(itemView.windowToken, 0)

                disposables.add(APIClient.apiService.checkRegisterCourse(
                        PrefUtils.getUserToken(context), courseInfoList[position].id)
                        .setDefaultThreads()
                        .subscribe({
                            response ->
                                if(response.result == 1) {
                                    val intent = Intent(context, CourseSubActivity::class.java)
                                    intent.putExtra("courseID", courseInfoList[position].id)
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(context, CourseNonRegistActivity::class.java)
                                    intent.putExtra("courseID", courseInfoList[position].id)
                                    startActivity(intent)
                                }
                        }, {
                            failure -> Log.i(TAG, "on Failure ${failure.message}")
                        }))
            }
        }
    }

    inner class SearchCourseListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if(holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as ElemViewHolder).bind(position - 1)
            } else if(holder?.itemViewType == ListUtils.TYPE_HEADER) {
                (holder as HeaderViewHolder).bind()
            }
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

        override fun getItemCount() = courseInfoList.size + 2

    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }
}