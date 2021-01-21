package com.example.together

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.skt.Tmap.TMapGpsManager
import com.skt.Tmap.TMapView

/**
 * A simple [Fragment] subclass.
 * Use the [SurroundingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SurroundingsFragment : Fragment() {
    var mapView: RelativeLayout? = null
    var tMapView: TMapView? = null
    var tmapgps: TMapGpsManager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_surroundings, container, false)

        mapView = view.findViewById(R.id.map)
        tMapView = TMapView(activity)

        //Setting view of Map
        tMapView!!.setSKTMapApiKey("l7xx52f2020a27a646b995dab1ba21acdfd7")
        //tMapView!!.setLocationPoint(126.970325, 37.556152)
        //tMapView!!.setCenterPoint(126.970325, 37.556152)
        //tMapView!!.setCompassMode(false)
        //tMapView!!.setIconVisibility(true)
        //tMapView!!.zoomLevel = 8
        //tmapgps!!.setMinTime(1000)
        //tmapgps!!.setMinDistance(5F)
        //tmapgps!!.setProvider(TMapGpsManager.GPS_PROVIDER) //gps 기반 위치 탐색
        //tmapgps!!.setProvider(TMapGpsManager.NETWORK_PROVIDER) //네트워크 기반 위치 탐색
        //tmapgps!!.OpenGps()

        //화면 현재 위치로 이동
        tMapView!!.setTrackingMode(true)
        tMapView!!.setSightVisible(true)
        tMapView!!.mapType = TMapView.MAPTYPE_STANDARD
        //tMapView!!.setLanguage(TMapView.LANGUAGE_KOREAN)
        mapView!!.addView(tMapView)
        return view
    }
}
