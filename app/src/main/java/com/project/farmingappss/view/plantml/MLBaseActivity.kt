package com.project.farmingappss.view.plantml

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) return
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}