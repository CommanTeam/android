package org.appjam.comman.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidquery.AQuery
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_course_search_result.view.*
import kotlinx.android.synthetic.main.search_result_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CategoryData
import org.appjam.comman.ui.CourseSubsection.CourseSubActivity
import org.appjam.comman.ui.courseNonRegist.CourseNonRegistActivity
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.SpaceItemDecoration
import org.appjam.comman.util.setDefaultThreads

/**
 * Created by ChoGyuJin on 2018-01-04.
 */
class SearchCategoryResultFragment : Fragment() {

    private var coursesInfo: List<CategoryData.CoursesOfCategory> = listOf()
    private val disposables = CompositeDisposable()

    companion object {
        const val TAG = "SearchCategoryResultFragment"
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_course_search_result,container,false)
    }


    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val recyclerView = view!!.course_result_recyclerview
        recyclerView.adapter = CourseSearchResultAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(SpaceItemDecoration(context, 9, 9, 0, 0))

        if(arguments != null) {
            val categoryID = arguments.getInt("categoryID")
            disposables.add(APIClient.apiService.getLecturesOfCategory(PrefUtils.getUserToken(context), categoryID)
                    .setDefaultThreads()
                    .subscribe({
                        response -> coursesInfo = response.result
                        recyclerView.adapter.notifyDataSetChanged()
                    }, {
                        failure -> Log.i(TAG, "on Failure ${failure.message}")
                    }))
        }

    }

    inner class ElemViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("LongLogTag")
        fun bind(position: Int){
            val aQuery = AQuery(context)
            aQuery.id(itemView.course_result_img).image(coursesInfo[position].image_path)
//            Glide.with(context)
//                    .load(coursesInfo[position].image_path)
//                    //fitCenter 할까 말까
//                    .centerCrop()
//                    .into(itemView.course_result_img)

            itemView.course_title_tv.text = coursesInfo[position].title
            itemView.course_content_tv.text = coursesInfo[position].info

            val hit = coursesInfo[position].hit
            itemView.course_people_tv.text = "$hit 명이 수강중입니다."

            itemView.setOnClickListener {
                disposables.add(APIClient.apiService.checkRegisterCourse(
                        PrefUtils.getUserToken(context), coursesInfo[position].id)
                        .setDefaultThreads()
                        .subscribe({
                            response ->
                            if(response.result == 1) {
                                val intent = Intent(context, CourseSubActivity::class.java)
                                intent.putExtra("courseID", coursesInfo[position].id)
                                startActivity(intent)
                            } else {
                                val intent = Intent(context, CourseNonRegistActivity::class.java)
                                intent.putExtra("courseID", coursesInfo[position].id)
                                startActivity(intent)
                            }
                        }, {
                            failure -> Log.i(TAG, "on Failure ${failure.message}")
                        }))
            }

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

        override fun getItemCount() = coursesInfo.size + 2

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