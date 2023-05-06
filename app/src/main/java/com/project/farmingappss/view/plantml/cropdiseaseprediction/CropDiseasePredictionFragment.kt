package com.project.farmingappss.view.plantml.cropdiseaseprediction

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.farmingappss.R
import com.project.farmingappss.databinding.FragmentCropDiseasePredictionBinding
import com.project.farmingappss.utilities.CAMERA_REQUEST

/**
 * A simple [Fragment] subclass.
 * Use the [CropDiseasePredictionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CropDiseasePredictionFragment : Fragment() {

    private var _binding: FragmentCropDiseasePredictionBinding? = null
    private val binding get() = _binding!!

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

            }
        }
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