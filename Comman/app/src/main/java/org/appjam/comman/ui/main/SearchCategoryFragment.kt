package org.appjam.comman.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.category_information_item.view.*
import kotlinx.android.synthetic.main.fragment_main_search_category.view.*
import org.appjam.comman.R
import org.appjam.comman.util.ListUtils

/**
 * Created by junhoe on 2017. 12. 31..
 */
class SearchCategoryFragment : Fragment() {

    private val categoryItemList = mutableListOf<CategoryItem>()
    data class CategoryItem(val name: String)

    init {
        // TODO: Implement network data class
        categoryItemList.add(CategoryItem("코딩"))
        categoryItemList.add(CategoryItem("디자인 툴"))
        categoryItemList.add(CategoryItem("문서"))
        categoryItemList.add(CategoryItem("황혼 프로젝트"))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)
        = inflater?.inflate(R.layout.fragment_main_search_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.main_searchCategory_rv
        recyclerView.adapter = CategoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    inner class CategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == ListUtils.TYPE_HEADER) {
                HeaderViewHolder(layoutInflater.inflate(R.layout.category_header_item, parent, false))
            } else {
                ElemViewHolder(layoutInflater.inflate(R.layout.category_information_item, parent, false))
            }
        }

        override fun getItemCount() = categoryItemList.size + 1

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (holder?.itemViewType == ListUtils.TYPE_ELEM) {
                (holder as ElemViewHolder).bind(position - 1)
            }
        }

        override fun getItemViewType(position: Int): Int
                = if (position == 0) ListUtils.TYPE_HEADER else ListUtils.TYPE_ELEM
    }

    inner class ElemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: Implement more detail view binding
        fun bind(position: Int) {
            itemView.main_searchCategoryItem_tv.text = categoryItemList[position].name
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}