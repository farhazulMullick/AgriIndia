package com.project.farmingappss.view.plantml.cropprediction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.project.farmingappss.R
import com.project.farmingappss.databinding.FragmentCropPredictionBinding
import com.project.farmingappss.utilities.URL
import com.project.farmingappss.utilities.value
import com.project.farmingappss.view.plantml.adapter.PagerAdapter
import com.project.farmingappss.view.plantml.adapter.ViewPagerFragInfo
import com.project.farmingappss.view.plantml.common.Fertilizer
import com.project.farmingappss.view.yojna.WebViewFragment
import kotlinx.android.synthetic.main.fragment_crop_prediction.humidity_et
import kotlinx.android.synthetic.main.fragment_crop_prediction.nitrogen_et
import kotlinx.android.synthetic.main.fragment_crop_prediction.ph_et
import kotlinx.android.synthetic.main.fragment_crop_prediction.phosphorous_et
import kotlinx.android.synthetic.main.fragment_crop_prediction.potassium_et
import kotlinx.android.synthetic.main.fragment_crop_prediction.predict_btn
import kotlinx.android.synthetic.main.fragment_crop_prediction.rainfall_et
import kotlinx.android.synthetic.main.fragment_crop_prediction.temp_et
import kotlinx.android.synthetic.main.fragment_dashboard.btn_crop_prediction

/**
 * A simple [Fragment] subclass.
 * Use the [CropPredictionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CropPredictionFragment : Fragment() {

    val viewModel by viewModels<CropPredictionViewModel>()

    private lateinit var includedView: View

    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    private var _binding: FragmentCropPredictionBinding? = null
    private val binding get() =  _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentCropPredictionBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        includedView =  view.findViewById(R.id.bottom_sheet) ?: return
        sheetBehavior = BottomSheetBehavior.from(includedView)

        sheetBehavior.peekHeight = 0
        sheetBehavior.isHideable = true

        predict_btn.setOnClickListener {
            handlePredictBtnClick()
        }

        sheetBehavior.setBottomSheetCallback( object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })

    }

    private fun handlePredictBtnClick() {
        val nitrogen = nitrogen_et.text.toString()
        val phosphorous = phosphorous_et.text.toString()
        val potassium = potassium_et.text.toString()
        val temperature = temp_et.text.toString()
        val humidity = humidity_et.text.toString()
        val soilPh = ph_et.text.toString()
        val rainfall = rainfall_et.text.toString()

        if (nitrogen.value.isEmpty() || phosphorous.value.isEmpty() || potassium.value.isEmpty()
            || temperature.value.isEmpty() || humidity.value.isEmpty() || soilPh.value.isEmpty()
            || rainfall.value.isEmpty()) {

            Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val map = mutableMapOf(
            Pair("N", nitrogen),
            Pair("P", phosphorous),
            Pair("K", potassium),
            Pair("temperature", temperature),
            Pair("humidity", humidity),
            Pair("ph", soilPh),
            Pair("rainfall", rainfall)
        ).toMap()


        viewModel.getPredictionResult(map).observe(viewLifecycleOwner, Observer {
            if (it==null)return@Observer

            if (it.crop.value.isEmpty()) {
                Toast.makeText(requireContext(), "Empty data", Toast.LENGTH_SHORT).show()
                return@Observer
            }

            sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            sheetBehavior.peekHeight = 900
            setViewPager(Bundle().apply {
                putString(URL, "https://en.wikipedia.org/wiki/${it.crop.value}")
            }, it.crop.value)
        })
    }

    private fun setViewPager(bundle: Bundle, cropName: String) {
        val list = listOf(
            ViewPagerFragInfo(1, WebViewFragment.newInstance( bundle ), cropName)
        )

        val pager = includedView.findViewById<ViewPager>(R.id.pager)
        val tabLayout = includedView.findViewById<TabLayout>(R.id.tab_layout)

        pager.adapter = PagerAdapter(list, parentFragmentManager)

        tabLayout.setupWithViewPager(pager)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CropPredictionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            CropPredictionFragment()
    }
}