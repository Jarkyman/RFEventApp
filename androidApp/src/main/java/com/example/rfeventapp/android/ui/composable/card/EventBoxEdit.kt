package com.example.rfeventapp.android.ui.composable.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rfeventapp.utils.AppColor

@Composable
fun EventBoxEdit(
    title: String, campName: String, time: String, strikethrough: Boolean, onEventClick: () -> Unit
) {
    Box(modifier = Modifier
        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        .clickable { onEventClick() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(AppColor.darkGray), shape = RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = Color(AppColor.textColor), fontSize = 18.sp)
                    ) {
                        append(title)
                    }
                    withStyle(
                        style = SpanStyle(color = Color.Transparent, fontSize = 15.sp)
                    ) {
                        append(" ")
                    }
                    withStyle(
                        style = SpanStyle(color = Color(AppColor.orange), fontSize = 14.sp)
                    ) {
                        append(campName)
                    }
                },
                maxLines = 2)
            Text(
                text = time,
                fontSize = 16.sp,
                color = if (strikethrough) Color(AppColor.gray) else Color(AppColor.textColor),
                maxLines = 1,
                style = if (strikethrough) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(
                    textDecoration = TextDecoration.None
                )
            )
        }
    }
}