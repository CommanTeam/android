package org.appjam.comman.ui.user

/**
 * Created by yeahen on 2017-12-31.
 */

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.androidquery.AQuery
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*
import org.appjam.comman.R
import org.appjam.comman.network.APIClient
import org.appjam.comman.network.data.LoginData
import org.appjam.comman.ui.CourseSubsection.CourseSubActivity
import org.appjam.comman.ui.main.MainActivity
import org.appjam.comman.util.PrefUtils
import org.appjam.comman.util.setDefaultThreads
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException




class LoginActivity : AppCompatActivity() {
    companion object {
        const val TAG = "LoginActivity"
    }
    private var callback: SessionCallback? = null
    private val disposables = CompositeDisposable()

    //카카오톡 프로필 사진은 이미지 url 형태로 제공하는데 해당 라이브러리를 사용하면 url 을 ImageView id만 매핑시켜 주면 한줄의 코드로 매우 편리하게 적용가능합니다.
    var aQuery : AQuery? = null

    //인터넷 연결상태 확인
    val isConnected: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        aQuery = AQuery(this)

        login_kakaoLogin_btn.visibility = View.GONE

        val handler = Handler()
        handler.postDelayed({
            login_kakaoLogin_btn.visibility = View.VISIBLE
        }, 2000)


        //AUTHORIZATION_FAILED: invalid android_key_hash or ios_bundle_id or web_site_url 해결하기 위해 코드 추가
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }


        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)

        // 카카오톡 로그인 버튼
        login_kakaoLogin_btn.setOnTouchListener { v : View, event : MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (!isConnected) {
                    Toast.makeText(this@LoginActivity, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }

            !isConnected
        }

        if(Session.getCurrentSession().isOpened){
            requestMe()
        }

    }

    //간편 로그인시 호출되는 부분
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            //access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태. 일반적으로 로그인 후의 다음 activity로 이동한다.
            if (Session.getCurrentSession().isOpened) { // 한 번더 세션을 체크해주었습니다.
                requestMe()

            }
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Logger.e(exception)
            }
        }
    }

    private fun requestMe() {
        login_splashTitle_layout.visibility = View.VISIBLE

        UserManagement.requestMe(object : MeResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                Log.e("onFailure", errorResult!!.toString() + "")
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                Log.e("onSessionClosed", errorResult.toString() + "")
            }

            override fun onSuccess(userProfile: UserProfile) {
                Log.e("onSuccess", userProfile.toString())
                var user_nickName = userProfile.nickname
                var user_email = userProfile.email
                lateinit var user_profile_img : String

                if(userProfile.thumbnailImagePath != null)
                    user_profile_img = userProfile.thumbnailImagePath
                else
                    user_profile_img = ""

                disposables.add(APIClient.apiService.getPostToken(LoginData.LoginInfo(user_nickName, user_profile_img, user_email))
                        .setDefaultThreads()
                        .subscribe ({
                            response -> PrefUtils.putUserToken(this@LoginActivity, response.token)
                            Log.i(TAG,"aaaaaaaaaaaaaaaaa ${response.token}")
                            Toast.makeText(this@LoginActivity,response.token, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, CourseSubActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            startActivity(intent)
                        }, {
                            failure -> Log.i(LoginActivity.TAG, "on Failure ${failure.message}")
                        })
                )

                //성공하면 MainActivity로 이동
                //프로필 이미지 url과 이메일 값 디비에 삽입하기

                //싱글탑, 클리어탑 고민
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                if(PrefUtils.getUserToken(this@LoginActivity) != null) {
                    disposables.add(APIClient.apiService.getPostToken(PrefUtils.getUserToken(this@LoginActivity),
                                                                    LoginData.LoginInfo(user_nickName, user_profile_img, user_email))
                            .setDefaultThreads()
                            .subscribe ({
                                response -> PrefUtils.putUserToken(this@LoginActivity, response.token)
                                Toast.makeText(this@LoginActivity, PrefUtils.getUserToken(this@LoginActivity), Toast.LENGTH_SHORT).show()

//                                val intent = Intent(this@LoginActivity, CourseSubActivity::class.java)
//                                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                                startActivity(intent)
                            }, {
                                failure -> Log.i(LoginActivity.TAG, "on Failure ${failure.message}")
                            }))
                } else {
                    disposables.add(APIClient.apiService.getPostToken(LoginData.LoginInfo(user_nickName, user_profile_img, user_email))
                            .setDefaultThreads()
                            .subscribe ({
                                response -> PrefUtils.putUserToken(this@LoginActivity, response.token)
                                val intent = Intent(this@LoginActivity, CourseSubActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                                startActivity(intent)
                            }, {
                                failure -> Log.i(LoginActivity.TAG, "on Failure ${failure.message}")
                            }))
                }
            }

            override fun onNotSignedUp() {
                Log.e("onNotSignedUp", "onNotSignedUp")
            }
        })
    }

    //이 부분이 없는 경우 누적 로그인이 될 수 있음
    override fun onDestroy() {
        Session.getCurrentSession().removeCallback(callback)
        disposables.clear()
        super.onDestroy()
    }
}

