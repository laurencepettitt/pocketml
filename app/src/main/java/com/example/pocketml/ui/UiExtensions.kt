package com.example.pocketml.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.size.Precision
import coil.size.Scale
import com.example.pocketml.DImagesQuery
import com.example.pocketml.R

@BindingAdapter("loadDClass")
fun TextView.loadDClass(dImage: DImagesQuery.DImage?) {
    text = dImage?.dClass ?: ""
}


@BindingAdapter("loadDImage")
fun ImageView.loadDImage(dImage: DImagesQuery.DImage?) {
    dImage?.url?.let { url ->
        this.load(url) {
            precision(Precision.EXACT)
            scale(Scale.FILL)
            placeholder(R.drawable.ic_baseline_image_240)
        }
    }
}
