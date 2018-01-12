package org.appjam.comman.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.category_information_item.view.*
import kotlinx.android.synthetic.main.fragment_main_search_category.view.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.CategoryData
import org.appjam.comman.util.ListUtils
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.SpaceItemDecoration
import org.appjam.comman.util.setDefaultThreads

/**
 * Created by junhoe on 2017. 12. 31..
 */
class SearchCategoryFragment : Fragment() {

    private var categoryInfoList : List<CategoryData.CategoryInfo> = listOf()
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)
         = inflater?.inflate(R.layout.fragment_main_search_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.main_searchCategory_rv
        recyclerView.adapter = CategoryAdapter()
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.addItemDecoration(SpaceItemDecoration(context,2))
        (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int
                                = if ((recyclerView.adapter.getItemViewType(position) == ListUtils.TYPE_HEADER) or
                                (recyclerView.adapter.getItemViewType(position) == ListUtils.TYPE_FOOTER)) 2 else 1
                    }

        disposables.add(APIClient.apiService.getCategoryInfos(PrefUtils.getUserToken(context))
                .setDefaultThreads()
                .subscribe({
                    response -> categoryInfoList = response.result
                                recyclerView.adapter.notifyDataSetChanged()
                }, {
                    failure -> Log.i(MyCourseFragment.TAG, "on Failure ${failure.message}")
                }))

    }

    inner class CategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == ListUtils.TYPE_HEADER) {
                HeaderViewHolder(layoutInflater.inflate(R.layout.category_header_item, parent, false))
            }
            else if (viewType == ListUtils.TYPE_FOOTER){
                FootViewHolder((layoutInflater.inflate(R.layout.course_item_footer,parent,false)))
            }
            else {
                ElemViewHolder(layoutInflater.inflate(R.layout.category_information_item, parent, false))
            }
        }

        override fun getItemCount() = categoryInfoList.size + 2

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as ElemViewHolder).bind(position - 1)
            }
            else if(holder?.itemViewType == ListUtils.TYPE_HEADER)
                    (holder as HeaderViewHolder).bind()
            else if(holder?. itemViewType == ListUtils.TYPE_FOOTER)
                holder as FootViewHolder
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) ListUtils.TYPE_HEADER
            else if (position == itemCount - 1) ListUtils.TYPE_FOOTER
            else ListUtils.TYPE_ELEM
        }

    }

    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind(position: Int) {

            itemView.categoryTitle_tv.text = categoryInfoList[position].categoryName
            var categoryContent = ""
            val titles = categoryInfoList[position].title
            for (index in 0 until titles.size - 1) {
                categoryContent += "${titles[index]},"
            }

            itemView.categoryContent_tv.text = categoryContent

            itemView.setOnClickListener {
                val hide = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hide.hideSoftInputFromWindow(itemView.windowToken, 0)

                val bundle = Bundle()
                bundle.putInt("categoryID", categoryInfoList[position].categoryID)
                (parentFragment as SearchFragment).ReplaceFragment(SearchCategoryResultFragment(), bundle, "searchResult")

            }
            Glide.with(context)
                    .load(categoryInfoList[position].categoryImg)
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .into(itemView.category_img)

            Toast.makeText(context, categoryInfoList[position].categoryImg,Toast.LENGTH_SHORT).show()


        }
    }
    inner class FootViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener {
                val hide = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hide.hideSoftInputFromWindow(itemView.windowToken, 0)

            }
        }
    }
}