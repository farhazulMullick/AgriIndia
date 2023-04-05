package com.project.farmingappss.utilities

import com.google.firebase.firestore.DocumentSnapshot

interface CellClickListener<T> {
    fun onCellClickListener(data: T)

}