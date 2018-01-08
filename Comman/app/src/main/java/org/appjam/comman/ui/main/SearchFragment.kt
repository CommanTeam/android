package org.appjam.comman.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main_search.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.SearchedCoursesData
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads



/**
 * Created by junhoe on 2017. 12. 31..
 */
class SearchFragment : Fragment(), View.OnClickListener {

    private val bundle = Bundle()
    private var textWatcher: TextWatcher? = null
    private val disposables = CompositeDisposable()
    private var courseInfoList: SearchedCoursesData.SearchedCoursesResponse? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        textWatcher = editTextWather()
        main_search_et.addTextChangedListener(textWatcher)

        main_cancel_btn.setOnClickListener(this)
        search_layout.setOnClickListener {
            val hide = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hide.hideSoftInputFromWindow(search_layout.windowToken, 0)
        }


        if (!main_search_et.isActivated)
            fragmentTransaction.add(R.id.main_searchCategory_layout, SearchCategoryFragment())
        fragmentTransaction.commit()
    }

    inner class editTextWather : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            disposables.add(APIClient.apiService.getSearchedCourses(
                    PrefUtils.getUserToken(context), SearchedCoursesData.SearchedcoursesPost(p0.toString()))
                    .setDefaultThreads()
                    .subscribe({ response ->
                        courseInfoList = response
                        val gson = Gson()
                        bundle.putString("ans", gson.toJson(courseInfoList))
                        ReplaceFragment(SearchCourseListFragment(), bundle, "search")
                    }, { failure ->
                        Log.i(MyCourseFragment.TAG, "on Failure ${failure.message}")
                    }))
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    }

    override fun onClick(p0: View?) {
        when (p0) {
            main_cancel_btn -> {
                main_search_et.removeTextChangedListener(textWatcher)
                ReplaceFragment(SearchCategoryFragment(), bundle, "cancel")
                main_search_et.clearFocus()
                val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(main_search_et.windowToken, 0)
                main_search_et.text = null
            }
        }
    }

    fun ReplaceFragment(fragment: Fragment, bundle: Bundle, tag: String) {
        val fm = childFragmentManager
        val transaction = fm.beginTransaction()
        fragment.arguments = bundle
        transaction.replace(R.id.main_searchCategory_layout, fragment, tag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()

    }
}