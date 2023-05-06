package com.project.farmingappss.view.plantml.cropdiseaseprediction

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.project.farmingappss.R
import com.project.farmingappss.databinding.FragmentCropDiseasePredictionBinding
import com.project.farmingappss.utilities.CAMERA_REQUEST
import com.project.farmingappss.view.plantml.adapter.PagerAdapter
import kotlinx.android.synthetic.main.layout_perst_bttm_sheet_crop.tab_layout
import kotlinx.android.synthetic.main.layout_perst_bttm_sheet_crop.view_pager

/**
 * A simple [Fragment] subclass.
 * Use the [CropDiseasePredictionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CropDiseasePredictionFragment : Fragment() {

    private var _binding: FragmentCropDiseasePredictionBinding? = null
    private val binding get() = _binding!!
    private var sheetBehavior: BottomSheetBehavior<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            startActivityForResult(
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                }, CAMERA_REQUEST
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCropDiseasePredictionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST -> {
                makeApiCalls()
            }
        }
    }

    private fun makeApiCalls () {
        setViewPager()
    }

    private fun setViewPager() {
        val list = listOf<Fragment>(

        )

        PagerAdapter(list, childFragmentManager)
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            CropDiseasePredictionFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}