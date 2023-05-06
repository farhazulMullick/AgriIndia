package com.project.farmingappss.view.plantml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.project.farmingappss.R
import com.project.farmingappss.databinding.ActivityMlbaseBinding
import com.project.farmingappss.utilities.ACTION_TYPE
import com.project.farmingappss.utilities.CROP_DISEASE_PREDICTION_SCREEN
import com.project.farmingappss.utilities.CROP_PREDICTION
import com.project.farmingappss.view.plantml.cropdiseaseprediction.CropDiseasePredictionFragment
import com.project.farmingappss.view.plantml.cropprediction.CropPredictionFragment

class MLBaseActivity : AppCompatActivity() {

    var _binding: ActivityMlbaseBinding? = null
    private val binding get() = _binding!!
    private var fragmentManager : FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mlbase)

        fragmentManager = supportFragmentManager
        _binding = ActivityMlbaseBinding.inflate(layoutInflater)

        intent?.getStringExtra(ACTION_TYPE) .apply {

            when(this) {
                CROP_DISEASE_PREDICTION_SCREEN -> {
                    fragmentManager?.beginTransaction()
                        ?.replace(
                            binding.fragContainer.id,
                            CropDiseasePredictionFragment.newInstance(),
                            CropDiseasePredictionFragment::class.java.name)
                        ?.commitAllowingStateLoss()
                }

                CROP_PREDICTION -> {
                    fragmentManager?.beginTransaction()
                        ?.replace(
                            binding.fragContainer.id,
                            CropPredictionFragment.newInstance(),
                            CropPredictionFragment::class.java.name)
                        ?.commitAllowingStateLoss()
                }
            }
        }

    }
}