package com.project.farmingappss.utilities

val String.Companion.Empty: String by lazy { "" }

val String?.value get() = this ?: String.Empty