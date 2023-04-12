package com.example.rfeventapp.android.ui.composable.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.rfeventapp.android.R
import com.example.rfeventapp.domain.Event
import com.example.rfeventapp.utils.AppColor
import com.example.rfeventapp.utils.IMAGE_URL_BASE
import com.example.rfeventapp.utils.IMAGE_URL_MEDIA
import java.net.URLDecoder
import java.net.URLEncoder

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EventCardBig(
    modifier: Modifier = Modifier,
    image: String,
    event: Event,
    onEventClick: () -> Unit,
) {
    var imgUrl = ""
    if (image.isNotEmpty()) {
        imgUrl = "$IMAGE_URL_BASE${image.split("&")[0]}$IMAGE_URL_MEDIA${image.split("&")[1]}"
    }
    Box(
        modifier = modifier.padding(26.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .background(Color.Gray, RoundedCornerShape(45.dp))
                .clickable(onClick = {
                    onEventClick()
                })
        ) {
            Image(
                painter = rememberImagePainter(data = if (image.isEmpty()) R.drawable.orange_scene_waiting else imgUrl,
                    builder = {
                        crossfade(true)
                    }),
                contentDescription = "title",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(45.dp))
                    .background(color = Color(AppColor.backgroundColor))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.33f)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color(AppColor.darkGray).copy(alpha = .85f),
                        shape = RoundedCornerShape(bottomStart = 45.dp, bottomEnd = 45.dp)
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    /*Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = event.title,
                            maxLines = 1,
                            color = Color(AppColor.textColor),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = event.campName,
                            maxLines = 1,
                            color = Color(AppColor.orange),
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                    }*/
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(color = Color(AppColor.textColor), fontSize = 20.sp)
                            ) {
                                append(event.title)
                            }
                            withStyle(
                                style = SpanStyle(color = Color.Transparent, fontSize = 15.sp)
                            ) {
                                append(" ")
                            }
                            withStyle(
                                style = SpanStyle(color = Color(AppColor.orange), fontSize = 14.sp)
                            ) {
                                append(event.campName)
                            }
                        },
                        maxLines = 1)
                    Row(

                    ) {
                        Column(
                            modifier = Modifier.padding(end = 5.dp)
                        ) {
                            Text(
                                text = event.dayTime,
                                color = Color(AppColor.textColor),
                                maxLines = 1,
                                fontSize = 12.sp,
                            )
                            Row(
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.users),
                                    contentDescription = "Users",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color(AppColor.textColor)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = event.participant.toString(),
                                    color = Color(AppColor.textColor),
                                    maxLines = 1,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(Color.Gray)
                        )
                        Text(
                            text = event.description,
                            color = Color(AppColor.textColor),
                            fontSize = 12.sp,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
            }
        }
    }
}