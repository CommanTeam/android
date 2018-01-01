package org.appjam.comman.Fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.appjam.comman.R

/**
 * Created by KSY on 2017-12-31.
 */
class FirstFragActivity : Fragment(){
        val TAG : String = " LOG::FirstFragment"

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val v = inflater!!.inflate(R.layout.fragment_card_first, container, false)

            Log.v(TAG, "onCreateView")

            //v!!.first_text.text = ApplicationObject.name
            return v
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            Log.v(TAG, "onCreate")
        }

        override fun onResume() {
            super.onResume()
            Log.v(TAG, "onResume")
        }

        override fun onAttach(context: Context?) {
            super.onAttach(context)
            Log.v(TAG, "onAttach")
        }

        override fun onDestroy() {
            super.onDestroy()
            Log.v(TAG, "onDestroy")
        }

        override fun onPause() {
            super.onPause()
//        EventBus.getInstance().unregister(this)
            Log.v(TAG, "onPause")
        }

        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            Log.v(TAG, "onViewCreated")
        }

        override fun onDetach() {
            super.onDetach()
            Log.v(TAG, "onDetach")
        }
        //Attach->Create->CreateView->ViewCreated->Resume
    }

