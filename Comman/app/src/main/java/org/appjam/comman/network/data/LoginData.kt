package org.appjam.comman.network.data

/**
 * Created by yeahen on 2018-01-06.
 */
object LoginData {
    data class LoginResponse(val result: LoginInfo)

    data class LoginInfo(
            val message : String,
            val token : String,
            val user : UserData
    )

    data class UserData(
            val nickname : String,
            val thumbnail_image : String
    )

    data class LoginPost(val accessToken: String)    //사용자 이메일
}