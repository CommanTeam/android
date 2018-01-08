package org.appjam.comman.network.data

/**
 * Created by RyuDongIl on 2018-01-07.
 */
object GreetingData {
    data class GreetingResponse(
            val result : GreetingResult
    )

    data class GreetingResult (
            val ment : String,
            val userImg : String
    )
}