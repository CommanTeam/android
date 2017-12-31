package org.appjam.comman.util

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager

/**
 * Created by junhoe on 2017. 12. 31..
 */
object ListUtils {

    const val TYPE_ELEM = 0
    const val TYPE_HEADER = 1
    const val TYPE_FOOTER = 2

    class UnscrollableLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
        override fun canScrollVertically() = false
    }
}