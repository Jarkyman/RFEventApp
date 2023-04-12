package com.example.rfeventapp.android.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

import com.example.rfeventapp.android.R
import com.example.rfeventapp.android.ui.composable.background.Background
import com.example.rfeventapp.android.ui.composable.button.DefaultButton
import com.example.rfeventapp.android.ui.composable.button.IconButton
import com.example.rfeventapp.utils.AppColor
import com.example.rfeventapp.utils.IMAGE_URL_BASE
import com.example.rfeventapp.utils.IMAGE_URL_MEDIA

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EventDetailScreen(onBackClick: () -> Unit, viewModel: EventDetailViewModel) {
    val scrollState = rememberScrollState()

    val isSubscribe = viewModel.isSubscribed.collectAsState()
    val eventState = viewModel.event.collectAsState()

    Background()
    Box(Modifier.fillMaxSize()) {
        var imgUrl: Any = R.drawable.beer_img
        if (eventState.value.image.isNotEmpty()) {
            val url = "$IMAGE_URL_BASE${eventState.value.image.split("&")[0]}$IMAGE_URL_MEDIA${
                eventState.value.image.split("&")[1]
            }"
            imgUrl = url

        }
        Image(
            painter = rememberImagePainter(data = if (eventState.value.image.isEmpty()) R.drawable.orange_scene_waiting else imgUrl,//TODO: Change to load image
                builder = {
                    crossfade(true)
                }),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(2f / 5f)
                .background(color = Color(AppColor.backgroundColor))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(3.5f / 5f)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(AppColor.darkOrange), Color(AppColor.backgroundColor)
                        )
                    ), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = eventState.value.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(AppColor.textColor)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = eventState.value.campName,
                    fontSize = 16.sp,
                    color = Color(AppColor.orange)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        IconField(
                            icon = R.drawable.calendar,
                            text = eventState.value.dayTime.split(" ")[0]
                        )
                        IconField(
                            icon = R.drawable.clock, text = eventState.value.dayTime.split(" ")[1]
                        )
                        if (eventState.value.location.isNotEmpty()) {
                            IconField(
                                icon = R.drawable.location, text = eventState.value.location
                            )
                        }
                        IconField(
                            icon = R.drawable.users, text = eventState.value.participant.toString()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = eventState.value.description, color = Color(AppColor.textColor)
                    )
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        DefaultButton(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomCenter), onClick = {
                viewModel.subscribeToggle()
            }, title = if (isSubscribe.value) "Deltag ikke" else "Deltag"
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
    ) {
        IconButton(
            onClick = { onBackClick() }, icon = Icons.Filled.ArrowBackIosNew
        )
        if (eventState.value.facebookLink.isNotEmpty()) {
            IconButton(
                onClick = { /*TODO: Add facebook link*/ }, icon = Icons.Filled.Facebook
            )
        }
    }
}

@Composable
fun IconField(icon: Int, text: String) {
    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Info",
            tint = Color(AppColor.textColor),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = Color(AppColor.textColor),
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}