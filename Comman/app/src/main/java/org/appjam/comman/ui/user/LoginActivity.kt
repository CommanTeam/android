package org.appjam.comman.ui.user

/**
 * Created by yeahen on 2017-12-31.
 */

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
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
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_login.*
import org.appjam.comman.R


class LoginActivity : AppCompatActivity() {
    private var callback: SessionCallback? = null
    var user_img : CircleImageView? = null

    //카카오톡 프로필 사진은 이미지 url 형태로 제공하는데 해당 라이브러리를 사용하면 url 을 ImageView id만 매핑시켜 주면 한줄의 코드로 매우 편리하게 적용가능합니다.
    var aQuery : AQuery? = null


    //인터넷 연결상태 확인
    val isConnected: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return if (netInfo != null && netInfo.isConnectedOrConnecting) {
                true
            } else false

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        aQuery = AQuery(this)

        login_kakaoLogin_btn.visibility = View.GONE
//        login_splashTitle_layout.visibility = View.GONE

        val handler = Handler()
        handler.postDelayed({
            login_kakaoLogin_btn.visibility = View.VISIBLE
        }, 2000)

        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)

        // 카카오톡 로그인 버튼
        login_kakaoLogin_btn.setOnTouchListener { v : View, event : MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (!isConnected) {
                    Toast.makeText(this@LoginActivity, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }

            if (isConnected) {
                false
            }
            else {
                true
            }
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
//        login_splashTitle_layout.visibility = View.VISIBLE

        UserManagement.requestMe(object : MeResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                Log.e("onFailure", errorResult!!.toString() + "")
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                Log.e("onSessionClosed", errorResult.toString() + "")
            }

            override fun onSuccess(userProfile: UserProfile) {
                Log.e("onSuccess", userProfile.toString())
                aQuery!!.id(user_img).image(userProfile.thumbnailImagePath) //프로필 이미지
               //성공하면 MainActivity로 이동
//                val intent = Intent(baseContext, MainActivity::class.java)
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                startActivity(intent)
            }

            override fun onNotSignedUp() {
                Log.e("onNotSignedUp", "onNotSignedUp")
            }
        })
    }

    //이 부분이 없는 경우 누적 로그인이 될 수 있음
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }
}

