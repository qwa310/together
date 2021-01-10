package com.example.together

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Transformations.map
import com.example.together.databinding.FragmentSurroundingsBinding
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback


/**
 * A simple [Fragment] subclass.
 * Use the [SurroundingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SurroundingsFragment : Fragment(), OnMapReadyCallback {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var binding = DataBindingUtil.inflate<FragmentSurroundingsBinding>(inflater,
                R.layout.fragment_surroundings, container, false)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.map, it).commit()
                }

        return binding.root
    }

    override fun onMapReady(p0: NaverMap) {
        TODO("Not yet implemented")
    }
}