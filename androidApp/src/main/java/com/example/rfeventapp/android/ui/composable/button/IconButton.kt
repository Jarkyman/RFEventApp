package com.example.rfeventapp.android.ui.composable.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.rfeventapp.utils.AppColor

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String = ""
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .height(24.dp)
            .width(24.dp)
            .background(color = Color(AppColor.gray).copy(alpha = 0.75f), CircleShape),
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
        )
    }
}