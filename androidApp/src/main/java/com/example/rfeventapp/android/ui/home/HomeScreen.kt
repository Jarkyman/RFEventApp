package com.example.rfeventapp.android.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rfeventapp.android.ui.composable.background.Background
import com.example.rfeventapp.android.ui.composable.card.EventCardBig
import com.example.rfeventapp.android.ui.composable.card.EventCardSmall
import com.example.rfeventapp.domain.Event
import com.example.rfeventapp.utils.AppColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartScreen(
    paddingValues: PaddingValues,
    onEventClick: (event: Event) -> Unit,
) {
    val viewModel: HomeViewModel = koinViewModel()
    val popularState = viewModel.popularList.collectAsState()
    val upcomingState = viewModel.upcomingList.collectAsState()
    val firstLoad = viewModel.firstLoad.collectAsState()

    Background(paddingValues)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        item {
            println("IS Popular empty? =  ${popularState.value.isNotEmpty()}")
            if (popularState.value.isNotEmpty()) {
                PopularList(popularState.value, onEventClick, viewModel)
                Divider(
                    startIndent = 12.dp,
                    thickness = 1.dp,
                    color = Color(AppColor.orange),
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
        //if (state.value.items.isEmpty() && firstLoad.value) {
        if (upcomingState.value.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillParentMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No events",
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        color = Color(AppColor.textColor)
                    )
                    Text(
                        text = "Der gik noget galt, prøv igen senere",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color(AppColor.gray)
                    )
                }
            }
        } else {
            item {
                Text(
                    text = "Kommende",
                    color = Color(AppColor.textColor),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                )
            }
            /*items(state.value.items.size) { i ->
                val event = state.value.items[i]
                //println("### Check -- ${i >= state.value.items.size - 1} && ${!state.value.endReached} && ${!state.value.isLoading}")
                if (i >= state.value.items.size - 1 && !state.value.endReached && !state.value.isLoading) {
                    viewModel.loadNextItems()
                }
                EventCardBig(
                    image = painterResource(id = R.drawable.beer_img),
                    event = event,
                    onEventClick = {
                        onEventClick(event)
                    })
            }*/
            items(upcomingState.value.size) { i ->
                val event = upcomingState.value[i]
                EventCardBig(image = event.image, event = event, onEventClick = {
                    onEventClick(event)
                })
            }
        }
        /*item {
            if (state.value.isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }*/
    }
}

@Composable
fun PopularList(
    events: List<Event>, onEventClick: (event: Event) -> Unit, viewModel: HomeViewModel
) {
    val scrollState = rememberScrollState()
    Column() {
        Text(
            text = "Populær",
            color = Color(AppColor.textColor),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(16.dp)
        ) {
            for (event in events) {
                EventCardSmall(image = event.image,
                    event = event,
                    onEventClick = { onEventClick(event) })

            }
        }
    }
}

