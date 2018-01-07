package org.appjam.comman.network.data

/**
 * Created by yeahen on 2018-01-06.
 */
object LoginData {
    data class LoginResponse(val token: String)

    data class LoginInfo(val nickName: String, //사용자 닉네임(이름)
                         val thumbnailPath: String, //사진 path
                         val email: String)    //사용자 이메일
}