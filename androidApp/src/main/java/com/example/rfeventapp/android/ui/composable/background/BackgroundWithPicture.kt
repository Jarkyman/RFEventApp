package com.example.rfeventapp.android.ui.composable.background

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.rfeventapp.android.R
import com.example.rfeventapp.utils.AppColor

@Composable
fun BackgroundWithPicture() {
    Image(
        painter = painterResource(id = R.drawable.orange_scene),
        contentDescription = "Orange scene",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(AppColor.darkOrange).copy(0.3f),
                        Color(AppColor.backgroundColor).copy(0.3f)
                    )
                )
            )
    )
}