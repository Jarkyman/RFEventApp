package com.example.rfeventapp.android.ui.composable.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.rfeventapp.android.R
import com.example.rfeventapp.domain.Event
import com.example.rfeventapp.utils.AppColor
import com.example.rfeventapp.utils.IMAGE_URL_BASE
import com.example.rfeventapp.utils.IMAGE_URL_MEDIA

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EventCardSmall(
    modifier: Modifier = Modifier, image: String, event: Event, onEventClick: () -> Unit
) {
    var imgUrl = ""
    if (image.isNotEmpty()) {
        val url = "${IMAGE_URL_BASE}${image.split("&")[0]}${IMAGE_URL_MEDIA}${image.split("&")[1]}"
        imgUrl = url
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = {
                onEventClick()
            }), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(data = if (image.isEmpty()) R.drawable.orange_scene_waiting else imgUrl,
                builder = {
                    crossfade(true)
                }),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 1.dp, color = Color(AppColor.orange), shape = RoundedCornerShape(10.dp)
                )
                .background(color = Color(AppColor.backgroundColor))
        )
        Box(
            modifier = Modifier
                .background(
                    color = Color(AppColor.darkGray),
                    shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                )
                .height(125.dp)
                .width(180.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = event.title,
                    color = Color(AppColor.textColor),
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.users),
                            contentDescription = "users",
                            modifier = Modifier.size(20.dp),
                            tint = Color(AppColor.orange)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = event.participant.toString(),
                            color = Color(AppColor.gray),
                            maxLines = 1,
                            fontSize = 12.sp
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = event.dayTime,
                            color = Color(AppColor.gray),
                            maxLines = 1,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = "calendar",
                            modifier = Modifier.size(20.dp),
                            tint = Color(AppColor.orange)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = event.description,
                    color = Color(AppColor.gray),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}