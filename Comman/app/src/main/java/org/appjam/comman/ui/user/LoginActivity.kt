package org.appjam.comman.ui.user

/**
 * Created by yeahen on 2017-12-31.
 */

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.activity_login.*
import org.appjam.comman.R
import org.appjam.comman.ui.card.CardActivity
import org.appjam.comman.util.PrefUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException



class LoginActivity : AppCompatActivity() {
    private var callback: SessionCallback? = null
    private var shared : SharedPreferences? = null

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
                var user_profile_img = userProfile.thumbnailImagePath
                PrefUtils.putUserProfile(this@LoginActivity, userProfile)
                shared = applicationContext.getSharedPreferences("shared", 0)
                val editor = shared!!.edit()
                editor.putString("profile_img_url", userProfile.profileImagePath)
                //성공하면 MainActivity로 이동
                //프로필 이미지 url과 이메일 값 디비에 삽입하기
                val intent = Intent(baseContext, CardActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra("user_profile_img", user_profile_img)
                intent.putExtra("user_email", user_email)
                intent.putExtra("user_nickName", user_nickName)
                startActivity(intent)
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

