package com.project.farmingappss.view.plantml.adapter

import androidx.fragment.app.Fragment

data class ViewPagerFragInfo(
    val position: Int = 0,
    val fragment: Fragment,
    val pageTitle: String
) {
}