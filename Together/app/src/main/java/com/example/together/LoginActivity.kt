package com.example.together

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import com.example.together.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val bKakao = binding.bKakao
        bKakao.setOnClickListener {
            // 로그인 공통 callback 구성
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                Log.e(ContentValues.TAG, "콜백 실행")
                if (error != null) {
                    Log.e(ContentValues.TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i(ContentValues.TAG, "로그인 성공 ${token.accessToken}")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Log.e(ContentValues.TAG, "토큰 생성 실패")
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {

                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
                Log.i("로그인", "카카오톡 로그인으로 이동")
            }
        }
    }
}