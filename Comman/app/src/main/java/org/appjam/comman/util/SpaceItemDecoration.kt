package org.appjam.comman.util

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View



/**
 * Created by junhoe on 2018. 1. 3..
 */
class SpaceItemDecoration : RecyclerView.ItemDecoration {

    private val leftMargin: Int
    private val rightMargin: Int
    private val topMargin: Int
    private val bottomMargin: Int

    constructor(context: Context, space: Int){
        val pixelSpace = convertDpToPixel(context, space)
        leftMargin = pixelSpace
        rightMargin = pixelSpace
        topMargin = pixelSpace
        bottomMargin = pixelSpace
    }

    constructor(context: Context, left: Int, right: Int, top: Int, bottom: Int) {
        leftMargin = convertDpToPixel(context, left)
        rightMargin = convertDpToPixel(context, right)
        topMargin = convertDpToPixel(context, top)
        bottomMargin = convertDpToPixel(context, bottom)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.left = leftMargin
        outRect.right = rightMargin
        outRect.bottom = bottomMargin
        outRect.top = topMargin
    }

    private fun convertDpToPixel(context: Context, dp: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }
}