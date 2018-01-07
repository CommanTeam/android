package org.appjam.comman.util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by junhoe on 2018. 1. 4..
 */
fun <T> Observable<T>.setDefaultThreads() : Observable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

//fun Bundle.putList(list : List<Any>) : Bundle {
//    Bundle.
//}

