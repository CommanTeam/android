package org.appjam.comman.network

import io.reactivex.Observable
import org.appjam.comman.network.data.CardData
import org.appjam.comman.network.data.CoursesData
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
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

        @GET("/users/main/progressLecture/{userID}")
        fun getRegisteredCourses(@Path("userID") userId: Int) : Observable<CoursesData.CoursesResponse>

        @GET("/content/lecturepicture/{lectureID]")
        fun getLectureCards(@Path("lectureID") lectureID: Int) : Observable<CardData.CardResponse>
    }
}