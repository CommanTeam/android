package org.appjam.comman.network

import io.reactivex.Observable
import org.appjam.comman.network.data.CardData
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.network.data.LoginData
import org.appjam.comman.network.data.QuizData
import org.appjam.comman.network.data.SearchedCoursesData
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by junhoe on 2018. 1. 4..
 */
object APIClient {

    private val baseUrl = "http://13.125.35.214:3000"
    val apiService by lazy {
        Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(APIService::class.java)
    }

    interface APIService {

        //수강중인 강좌
        @GET("/users/main/progressLecture/{userID}")

        fun getRegisteredCourses(@Path("userID") email : String) : Observable<CoursesData.CoursesResponse>

        @POST("/users/insert_user_info")
        fun getPostToken(@Body loginData : LoginData.LoginInfo) : Observable<LoginData.LoginResponse>

        @GET("/content/lecturequiz/{lectureID}")
        fun getQuizResult(@Path("lectureID") lectureId : Int) :Observable<QuizData.QuizResponse>

        @GET("/content/lecturepicture/{lectureID]")
        fun getLectureCards(@Path("lectureID") lectureID : Int) : Observable<CardData.CardResponse>
        fun getRegisteredCourses(@Path("userID") userId: Int) : Observable<CoursesData.CoursesResponse>

        //강좌검색
        @POST("/search/courses")
        fun getSearchedCourses(
//                @Header("authorization") tokenValue : String,
                @Body searchPost : SearchedCoursesData.SearchedcoursesPost
        ) : Observable<SearchedCoursesData.SearchedCoursesResponse>
    }
}