package org.appjam.comman.util

import android.content.Context
import android.support.v4.content.ContextCompat

/**
 * Created by RyuDongIl on 2018-01-02.
 */
object SetColorUtils {

    fun get(context: Context, colorId: Int): Int = ContextCompat.getColor(context, colorId)
}