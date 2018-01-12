package org.appjam.comman.realm

import io.realm.RealmObject

/**
 * Created by junhoe on 2018. 1. 12..
 */
open class RQuizData : RealmObject() {
    var quizId: Int? = null
    var answer: Int? = null
}
