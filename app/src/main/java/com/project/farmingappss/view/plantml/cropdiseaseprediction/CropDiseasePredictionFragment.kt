package com.project.farmingappss.view.plantml.cropdiseaseprediction

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.project.farmingappss.R
import com.project.farmingappss.databinding.FragmentCropDiseasePredictionBinding
import com.project.farmingappss.databinding.LayoutPerstBttmSheetCropBinding
import com.project.farmingappss.utilities.CAMERA_REQUEST
import com.project.farmingappss.utilities.CROP_DISEASE
import com.project.farmingappss.utilities.CROP_NAME
import com.project.farmingappss.utilities.CROP_REMEDY
import com.project.farmingappss.utilities.URL
import com.project.farmingappss.utilities.show
import com.project.farmingappss.utilities.value
import com.project.farmingappss.view.plantml.adapter.PagerAdapter
import com.project.farmingappss.view.plantml.adapter.ViewPagerFragInfo
import com.project.farmingappss.view.plantml.common.AboutFragment
import com.project.farmingappss.view.plantml.common.DiseaseFragment
import com.project.farmingappss.view.plantml.common.Fertilizer
import com.project.farmingappss.view.yojna.WebViewFragment

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
                Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                CAMERA_REQUEST
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
        binding.setLifecycleOwner { viewLifecycleOwner.lifecycle }
        binding.vm = viewModel

        binding.ivCapture .setOnClickListener {
            startActivityForResult()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) return
        when (requestCode) {
            CAMERA_REQUEST-> {
                val imageBitmap = data.extras?.get("data") as? Bitmap ?: return
                showPreview (imageBitmap)
                makeApiCalls(imageBitmap)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showPreview (bitmap: Bitmap) {
        Glide.with(this).load(bitmap).into(binding.ivCrop)
    }

    private fun makeApiCalls(bitmap: Bitmap) {

        viewModel.getResultsForCropImage( viewModel.imageToBase64(bitmap)) .observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.plantDiseaseTv.text = it.disease.value

                setViewPager(
                    Bundle().apply {
                        putString(CROP_NAME, it.plantName.value)
                        putString(CROP_DISEASE, it.disease.value)
                        putString(CROP_REMEDY, it.remedy.value)
                        putString(URL," https://en.wikipedia.org/wiki/${it.disease.value}")
                    }
                )
            }

        })


    }

    private fun setViewPager(bundle: Bundle) {
        val list = listOf<ViewPagerFragInfo>(
            ViewPagerFragInfo(0, Fertilizer.newInstance( bundle ), "Remedy")
            ,
            ViewPagerFragInfo(1, WebViewFragment.newInstance( bundle ), "About Disease")
        )

        val includedView =  binding.root.findViewById<View>(R.id.bottom_sheet) ?: return

        val pager = includedView.findViewById<ViewPager>(R.id.pager)
        val tabLayout = includedView.findViewById<TabLayout>(R.id.tab_layout)

        pager.adapter = PagerAdapter(list, parentFragmentManager)

        tabLayout.setupWithViewPager(pager)


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