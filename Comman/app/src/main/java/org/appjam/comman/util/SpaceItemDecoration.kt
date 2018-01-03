package org.appjam.comman.util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by junhoe on 2018. 1. 3..
 */
class SpaceItemDecoration : RecyclerView.ItemDecoration {

    private val leftMargin: Int
    private val rightMargin: Int
    private val topMargin: Int
    private val bottomMargin: Int

    constructor(space: Int) {
        leftMargin = space
        rightMargin = space
        topMargin = space
        bottomMargin = space
    }

    constructor(left: Int, right: Int, top: Int, bottom: Int) {
        leftMargin = left
        rightMargin = right
        topMargin = top
        bottomMargin = bottom
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.left = leftMargin
        outRect.right = rightMargin
        outRect.bottom = bottomMargin
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = 0
        } else {
            outRect.top = topMargin
        }
    }
}