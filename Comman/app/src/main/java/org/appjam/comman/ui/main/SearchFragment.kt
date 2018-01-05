package org.appjam.comman.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.appjam.comman.R

/**
 * Created by junhoe on 2017. 12. 31..
 */
class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.main_searchHeader_layout, SearchHeaderFragment())
        fragmentTransaction.add(R.id.main_searchCategory_layout, SearchCourseListFragment())
        fragmentTransaction.commit()
    }
}

