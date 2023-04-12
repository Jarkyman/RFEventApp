package com.example.rfeventapp.android.ui.composable.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rfeventapp.android.navigation.AuthScreen
import com.example.rfeventapp.utils.AppColor

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
) {
    androidx.compose.material.TextButton(
        modifier = modifier
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        onClick = onClick) {
        Text(
            text = title,
            fontSize = 24.sp,
            color = Color(AppColor.lightGray)
        )
    }
}