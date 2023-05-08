package com.project.farmingappss.view.plantml.cropdiseaseprediction

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.project.farmingappss.R
import com.project.farmingappss.databinding.FragmentCropDiseasePredictionBinding
import com.project.farmingappss.databinding.LayoutPerstBttmSheetCropBinding
import com.project.farmingappss.utilities.CAMERA_REQUEST
import com.project.farmingappss.utilities.Empty
import com.project.farmingappss.view.plantml.adapter.PagerAdapter
import com.project.farmingappss.view.plantml.common.AboutFragment
import com.project.farmingappss.view.plantml.common.DiseaseFragment
import com.project.farmingappss.view.plantml.common.Fertilizer

/**
 * A simple [Fragment] subclass.
 * Use the [CropDiseasePredictionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CropDiseasePredictionFragment : Fragment() {

    private var _binding: FragmentCropDiseasePredictionBinding? = null
    private var binding2 :LayoutPerstBttmSheetCropBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DiseasePredictionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivityForResult()
    }

    private fun startActivityForResult (){
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

        binding.ivCapture .setOnClickListener {
            startActivityForResult()
        }
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
            DiseaseFragment.newInstance(String.Empty, String.Empty),
            Fertilizer.newInstance(String.Empty, String.Empty),
            AboutFragment.newInstance(String.Empty,String.Empty)
        )

        val includedView =  binding.root.findViewById<View>(R.id.bottom_sheet) ?: return

        val bind = LayoutPerstBttmSheetCropBinding.bind(includedView)

        bind.pager.adapter = PagerAdapter(list, childFragmentManager)

        bind.tabLayout
            .setupWithViewPager(bind.pager)


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