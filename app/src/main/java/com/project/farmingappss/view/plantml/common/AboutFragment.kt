package com.project.farmingappss.view.plantml.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.farmingappss.R
import com.project.farmingappss.utilities.CROP_NAME
import kotlinx.android.synthetic.main.fragment_about.about_tv
import kotlinx.android.synthetic.main.layout_perst_bttm_sheet_crop.about

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFragment : Fragment() {

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

        about_tv.setText( arguments?.getString(CROP_NAME) )
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            AboutFragment().apply {
                arguments = bundle
            }
    }
}