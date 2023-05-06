package com.project.farmingappss.view.plantml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.farmingappss.R
import com.project.farmingappss.databinding.ActivityMlbaseBinding

class MLBaseActivity : AppCompatActivity() {

    var _binding: ActivityMlbaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mlbase)

        _binding = ActivityMlbaseBinding.inflate(layoutInflater)

    }
}