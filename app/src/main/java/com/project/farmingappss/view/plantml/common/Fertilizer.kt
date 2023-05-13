package com.project.farmingappss.view.plantml.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.farmingappss.R
import com.project.farmingappss.utilities.CROP_REMEDY
import com.project.farmingappss.utilities.value
import kotlinx.android.synthetic.main.fragment_fertilizer.fertilizer_tv

/**
 * A simple [Fragment] subclass.
 * Use the [Fertilizer.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fertilizer : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_fertilizer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fertilizer_tv.setText( arguments?.getString(CROP_REMEDY) .value)
    }

    companion object {
        /**

         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(bundle:Bundle) =
            Fertilizer().apply {
                arguments = bundle
            }
    }
}