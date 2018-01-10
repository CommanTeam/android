package org.appjam.comman.ui.main

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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_newalarm_request.view.*
import kotlinx.android.synthetic.main.main_notice_item.view.*
import kotlinx.android.synthetic.main.no_resist_course_item.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.GreetingData
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads

/**
 * Created by ChoGyuJin on 2018-01-05.
 */
class SearchNewCourseFragment : Fragment() {


    private val disposables = CompositeDisposable()
    private var greetingInfo : GreetingData.GreetingResult? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_newalarm_request, container, false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val recyclerView = view!!.newalarm_request_rv
        recyclerView.adapter = MyCourseAlarmAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)

        disposables.add(APIClient.apiService.getGreetingInfo(PrefUtils.getUserToken(context))
                .setDefaultThreads()
                .subscribe({
                    response -> greetingInfo = response.result
                    recyclerView.adapter.notifyDataSetChanged()
                }, {
                    failure -> Log.i(MyCourseFragment.TAG, "on Failure ${failure.message}")
                })
        )
    }

    inner class MyCourseAlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind() {
            var aQuery = AQuery(context)
            val thumbnailUrl = PrefUtils.getString(context, PrefUtils.USER_IMAGE)
            aQuery.id(itemView.main_profile_img).image(thumbnailUrl)
            itemView.main_notice_tv.text = PrefUtils.getString(context, PrefUtils.NICKNAME) + greetingInfo?.ment
        }
    }

    inner class CourseResistRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class MyCourseAlarmAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if(holder?.itemViewType == ListUtils.TYPE_HEADER){
                (holder as MyCourseAlarmViewHolder).bind()
            }
            else if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as CourseResistRequestViewHolder)
            }
        }

        override fun getItemCount(): Int = 2
        override fun getItemViewType(position: Int): Int {
            return if (position==0) ListUtils.TYPE_HEADER
            else ListUtils.TYPE_ELEM
        }
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == ListUtils.TYPE_HEADER){
                MyCourseAlarmViewHolder(layoutInflater.inflate(R.layout.main_notice_item, parent, false))
            }
            else {
                val mainView : View = layoutInflater.inflate(R.layout.no_resist_course_item, parent, false)
                mainView.new_course_request_btn.setOnClickListener {
                    (activity as MainActivity).main_content_view_pager.currentItem += 1
                }
                return CourseResistRequestViewHolder(mainView)
            }
        }
    }

}