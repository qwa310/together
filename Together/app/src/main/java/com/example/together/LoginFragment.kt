package com.example.together

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.together.databinding.FragmentLoginBinding
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater,
                R.layout.fragment_login, container, false)
        val bKakao = binding.bKakao
        bKakao.setOnClickListener {
            // 카카오계정으로 로그인
//            LoginClient.instance.loginWithKakaoAccount(requireContext()) { token, error ->
//                if (error != null) {
//                    Log.e("토큰", token.toString())
//                    Log.e(TAG, "로그인 실패1", error)
//                }
//                else if (token != null) {
//                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
//                }
//            }

            // 로그인 공통 callback 구성
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
//                    val mainActivity = MainActivity()
//                    mainActivity.replaceFragment(LoginFragment())
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (context?.let { it1 -> LoginClient.instance.isKakaoTalkLoginAvailable(it1) }!!) {
                LoginClient.instance.loginWithKakaoTalk(requireContext(), callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }

        return binding.root
    }


}