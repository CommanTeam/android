package org.appjam.comman.util

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View

/**
 * Created by ChoGyuJin on 2018-01-09.
 */
class PopupItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect.left = convertDpToPixel(context, 19)
        outRect.right = convertDpToPixel(context, 19)
        outRect.bottom = convertDpToPixel(context, 5)
        outRect.top = convertDpToPixel(context, 5)
    }

    private fun convertDpToPixel(context: Context, dp: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }
}