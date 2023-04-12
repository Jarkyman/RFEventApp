package com.example.rfeventapp.android.ui.composable.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rfeventapp.utils.AppColor

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
) {
    Button(
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(AppColor.orange)
        ),
        shape = MaterialTheme.shapes.large.copy(CornerSize(60.dp)),
        onClick = onClick,
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            color = Color(AppColor.textColor)
        )
    }
}