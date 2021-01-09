package com.example.together

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.together.databinding.FragmentSurroundingsBinding


/**
 * A simple [Fragment] subclass.
 * Use the [SurroundingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SurroundingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var binding = DataBindingUtil.inflate<FragmentSurroundingsBinding>(inflater,
                R.layout.fragment_surroundings, container, false)

        return binding.root
    }
}