package org.appjam.comman.network

import io.reactivex.Observable
import org.appjam.comman.network.data.*
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
        //로그인 토큰 가져오기
        @POST("/users/insert_user_info")
        fun getPostToken(@Body loginData : LoginData.LoginInfo) : Observable<LoginData.LoginResponse>
        @POST("/users/insert_user_info")
        fun getPostToken(@Header("authorization") tokenValue: String,
                         @Body loginData : LoginData.LoginInfo) : Observable<LoginData.LoginResponse>


        //수강중인 강좌
        @GET("/users/main/progressLecture/")
        fun getRegisteredCourses(@Header("authorization") tokenValue : String
        ) : Observable<CoursesData.CoursesResponse>

        //카드 뉴스
        @GET("/content/lecturepicture/{lectureID}")
        fun getLectureCards(@Header("authorization") tokenValue: String,
                            @Path("lectureID") lectureID: Int) : Observable<CardData.CardResponse>

        //강의페이지 중 개요
        fun getLectureInfo(@Header("authorization") tokenValue : String,
                           @Query("lectureID") lectureID: Int) : Observable<LectureData.LectureResponse>

        //최근 강의 정보얻기
        @GET("/users/lectureRecentWatch/{lectureID}")
        fun getRecentLecture(@Header("authorization") tokenValue: String,
                             @Path("lectureID") lectureID: Int) : Observable<LectureData.RecentLectureResponse>


        @GET("/content/chapters")
        fun getChapterInfo(@Header("authorization") token : String,@Query("chapterID") chapterID: Int) : Observable<ChapterData.InfoResponse>

        //강의페이지 중 강의목록
        @GET("/content/lecturepage/lectureList")
        fun getLectureListInChapter(@Header("authorization") token : String,
                                    @Query("chapterID") chapterID: Int) : Observable<ChapterData.LectureListInChapterResponse>


        @GET("/content/lecturequiz/{lectureID}")
        fun getQuizResult(@Header("authorization") tokenValue: String,
                          @Path("lectureID") lectureId : Int) :Observable<QuizData.QuizResponse>

        //강좌검색
        @POST("/search/courses")
        fun getSearchedCourses(
                @Header("authorization") tokenValue : String,
                @Body searchPost : SearchedCoursesData.SearchedCoursesPost
        ) : Observable<SearchedCoursesData.SearchedCoursesResponse>

        //내 강좌 인사말 가져오기
        @GET("/users/main/greeting")
        fun getGreetingInfo(
                @Header("authorization") tokenValue : String
        ) : Observable<GreetingData.GreetingResponse>

        //전체 카테고리 정보
        @GET("/content/categories/course")
        fun getCategoryInfos(
                @Header("authorization") tokenVale : String
        ) : Observable<CategoryData.CategoryResponse>

        //다음 강좌 정보 가져오기
        @GET("/content/lecturepage/nextLecture?courseID={courseID}&chapterID={chapterID}&lectureID={lectureID}")
        fun getNextLectureInfo(
                @Header("authorization") tokenValue : String,
                @Query("courseID") courseID : Int,
                @Query("chapterID") chapterID : Int,
                @Query("lectureID") lectureID : Int
        ) : Observable<NextLectureData.NextLectureResponse>

        //강좌별 챕터정보
        @GET("/content/courses/")
        fun getPopupTitleInfos(@Header("authorization")tokenValue: String, @Query("courseID") courseID:Int) : Observable<PopupData.PopupTitleResponse>

        //강좌id로 강좌정보 가져오기
        @GET("/content/courses/{courseID}/chapters")
        fun getPopupContentInfos(@Header("authorization")tokenValue: String, @Path("courseID") courseID :Int) :Observable<PopupData.PopupContentResponse>

        //카테고리 검색 결과
        @GET("/search/courses/categories/{categoryID}")
        fun getLecturesOfCategory(
                @Header("authorization") tokenValue: String,
                @Path("categoryID") categoryID : Int
        ) : Observable<CategoryData.CoursesOfCategoryResponse>

        //유저별 강좌 등록 여부
        @GET("/users/register/{courseID}")
        fun checkRegisterCourse(
                @Header("authorization") tokenValue: String,
                @Path("courseID") courseID : Int
        ) : Observable<CoursesData.CheckRegistered>

        //유저별 강좌 구매 여부
        @GET("/users/purchase/{courseID}")
        fun checkPurchaseCourse(
                @Header("authorization") tokenValue: String,
                @Path("courseID") courseID : Int
        ) : Observable<CoursesData.CheckPurchased>

        //강좌id로 강좌 정보 가져오기
        @GET("/content/courses")
        fun getCourseMetaInfo(
                @Header("authorization") tokenValue: String,
                @Query("courseID") courseID : Int
        ) : Observable<CoursesData.CourseMetaResponse>
    }
}