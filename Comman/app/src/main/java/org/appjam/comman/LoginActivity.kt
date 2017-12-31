package org.appjam.comman

/**
 * Created by yeahen on 2017-12-31.
 */

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var callback: SessionCallback? = null

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

        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)

        // 카카오톡 로그인 버튼
        login_kakaoLogin_btn.setOnTouchListener { v, event ->
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

        // 로그인 성공 시 사용할 뷰
        login_logout_btn.setOnClickListener {
            if (Session.getCurrentSession().isOpened) {
                requestLogout()
            }
        }

        if (Session.getCurrentSession().isOpened) {
            requestMe()
        } else {
            login_success_layout.visibility = View.GONE
            login_kakaoLogin_btn.visibility = View.VISIBLE
        }
    }


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

    private fun requestLogout() {
        login_success_layout.visibility = View.GONE
        login_kakaoLogin_btn.visibility = View.VISIBLE
        UserManagement.requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                runOnUiThread { Toast.makeText(this@LoginActivity, "로그아웃 성공", Toast.LENGTH_SHORT).show() }
            }
        })
    }

    private fun requestMe() {
        login_success_layout.visibility = View.VISIBLE
        login_kakaoLogin_btn.visibility = View.GONE

        UserManagement.requestMe(object : MeResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                Log.e("onFailure", errorResult!!.toString() + "")
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                Log.e("onSessionClosed", errorResult.toString() + "")
            }

            override fun onSuccess(userProfile: UserProfile) {
                Log.e("onSuccess", userProfile.toString())
            }

            override fun onNotSignedUp() {
                Log.e("onNotSignedUp", "onNotSignedUp")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }
}

