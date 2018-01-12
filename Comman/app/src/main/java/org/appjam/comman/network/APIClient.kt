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
        fun getPostToken(@Body loginData : LoginData.LoginPost) : Observable<LoginData.LoginResponse>
        @POST("/users/insert_user_info")
        fun getPostToken(@Header("authorization") tokenValue: String,
                         @Body loginData : LoginData.LoginPost) : Observable<LoginData.LoginResponse>


        //수강중인 강좌
        @GET("/users/main/progressCourse/")
        fun getRegisteredCourses(@Header("authorization") tokenValue : String
        ) : Observable<CoursesData.CoursesResponse>

        //카드 뉴스
        @GET("/content/lecturepicture/{lectureID}")
        fun getLectureCards(@Header("authorization") tokenValue: String,
                            @Path("lectureID") lectureID: Int) : Observable<CardData.CardResponse>

        //강의id로 강의정보 가져오기
        @GET("/content/lectures")
        fun getLectureInfo(@Header("authorization") tokenValue : String,
                           @Query("lectureID") lectureID: Int) : Observable<LectureData.LectureResponse>

        //최근 강의 정보얻기
        @GET("/users/lectureRecentWatch/{lectureID}")
        fun getRecentLecture(@Header("authorization") tokenValue: String,
                             @Path("lectureID") lectureID: Int) : Observable<LectureData.RecentLectureResponse>

        //챕터id로 챕터정보 가져오기
        @GET("/content/chapters")
        fun getChapterInfo(@Header("authorization") tokenValue : String,
                           @Query("chapterID") chapterID: Int) : Observable<ChapterData.InfoResponse>

        //강의페이지 중 강의목록
        @GET("/content/lecturepage/lectureList")
        fun getLectureListInChapter(@Header("authorization") token : String,
                                    @Query("chapterID") chapterID: Int) : Observable<ChapterData.LectureListInChapterResponse>

        //강의에 대한 퀴즈정보
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

        //다음 강의 정보 가져오기
        @GET("/content/lecturepage/nextLecture")
        fun getNextLectureInfo(
                @Header("authorization") tokenValue : String,
                @Query("lectureID") lectureID : Int
        ) : Observable<NextLectureData.NextLectureResponse>



        //강좌id로 강좌정보 가져오기
        @GET("/content/courses")
        fun getPopupTitleInfos(
                @Header("authorization")tokenValue: String,
                @Query("courseID") courseID:Int) : Observable<PopupData.PopupTitleResponse>

        //강좌별 챕터정보
        @GET("/content/courses/{courseID}/chapters")
        fun getPopupContentInfos(
                @Header("authorization")tokenValue: String,
                @Path("courseID") courseID :Int) :Observable<PopupData.PopupContentResponse>

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

        //유저별 강좌 구매
        @PUT("/users/purchase/{courseID}")
        fun purchaseCourse(
                @Header("authorization") tokenValue: String,
                @Path("courseID") courseID :Int
        ) : Observable<CoursesData.PurchaseCourse>

        //유저별 강좌 등록
        @POST("/users/register")
        fun registerCourse(
                @Header("authorization") tokenValue: String,
                @Body registerData : CoursesData.RegisterPost
        ) : Observable<CoursesData.RegisterCourse>

        //유저의 수강 종료 갱신
        @PUT("/users/lectureHistory/{lectureID}")
        fun registerFinishLecture(
                @Header("authorization") tokenValue: String,
                @Path("lectureID") lectureID: Int
        ) : Observable<LectureData.FinishLecture>

        //강의ID로 비디오강의정보 가져오기
        @GET("/content/lecturevideo/{lectureID}")
        fun getVideoLectureInfo(
                @Header("authorization") tokenValue: String,
                @Path("lectureID") lectureID: Int
        ) : Observable<VideoData.VideoLectureResponse>

        //강의ID로 질문, 답변 가져오기
        @GET("/content/lecturequestion/{lectureID}")
        fun getQuestionOfLecture(
                @Header("authorization") tokenValue: String,
                @Path("lectureID") lectureID: Int
        ) : Observable<QuestionData.QuestionResponse>

        //강의ID로 질문 DB에 삽입
        @POST("/content/lecturequestion/insertquestion")
        fun registerQuestion(
                @Header("authorization") tokenValue: String,
                @Body questionPost : QuestionData.QuestionPost
        ) : Observable<QuestionData.RegisterQuestionResponse>
    }
}