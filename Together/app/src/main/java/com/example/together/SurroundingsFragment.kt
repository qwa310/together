package com.example.together

import android.content.Context
import android.os.Bundle
import android.os.WorkSource
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Transformations.map
import com.example.together.databinding.FragmentSurroundingsBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource


/**
 * A simple [Fragment] subclass.
 * Use the [SurroundingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SurroundingsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource  // 최적의 위치를 반환
    private lateinit var naverMap: NaverMap

    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mContext = container!!.context

        val binding = DataBindingUtil.inflate<FragmentSurroundingsBinding>(inflater,
                R.layout.fragment_surroundings, container, false)

        // 지도를 화면에 나타내는 부분
        // 현재 fragment 안에 MapFragment를 배치해야하므로 childFragmentManager 사용
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
                ?: MapFragment.newInstance().also { location ->
                    fm.beginTransaction().add(R.id.map, location).commit()
                }

        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        return binding.root
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {  // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
                Log.i("PermissionResult", "권한 거부됨")
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        Log.i("onMapReady", "onMapReady 호출")

        // 위치 추적 활성화, 카메라와 베어링이 사용자를 따라 움직임
        this.naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 위치 변경 이벤트, 위치 추적 테스트용
//        this.naverMap.addOnLocationChangeListener {
//            Toast.makeText(mContext, "${it.latitude}, ${it.longitude}",
//                    Toast.LENGTH_SHORT).show()
//        }
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
