package com.example.skyqgorecruitmenttest.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Long.toTimeDuration(): String {
    return if (this < 1000) {
        String.format("%d ms", this)
    } else {
        String.format("%.2f s", this / 1000.0)
    }
}