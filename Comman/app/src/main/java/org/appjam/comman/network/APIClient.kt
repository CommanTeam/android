package org.appjam.comman.network

import io.reactivex.Observable
import org.appjam.comman.network.data.CardData
import org.appjam.comman.network.data.CoursesData
import org.appjam.comman.network.data.LectureData
import org.appjam.comman.network.data.SearchedCoursesData
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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
        @GET("/users/main/progressLecture/")
        fun getRegisteredCourses(@Header("authorization") tokenValue : String) : Observable<CoursesData.CoursesResponse>

        //카드 뉴스
        @GET("/content/lecturepicture/{lectureID]")
        fun getLectureCards(@Header("authorization") tokenValue: String,
                            @Path("lectureID") lectureID: Int) : Observable<CardData.CardResponse>

        //강의정보얻기
        @GET("/content/lectures")
        fun getLectureInfo(@Header("authorization") tokenValue : String) : Observable<LectureData.LectureResponse>

        //강좌검색
        @POST("/search/courses")
        fun getSearchedCourses(
//                @Header("authorization") tokenValue : String,
                @Body searchPost : SearchedCoursesData.SearchedcoursesPost
        ) : Observable<SearchedCoursesData.SearchedCoursesResponse>

    }
}