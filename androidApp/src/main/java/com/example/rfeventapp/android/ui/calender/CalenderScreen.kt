package com.example.rfeventapp.android.ui.calender

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rfeventapp.android.ui.composable.background.Background
import com.example.rfeventapp.android.ui.composable.card.EventBox
import com.example.rfeventapp.android.ui.composable.card.EventCardBig
import com.example.rfeventapp.domain.Event
import com.example.rfeventapp.utils.AppColor
import com.example.rfeventapp.utils.DATE_LIST
import com.example.rfeventapp.utils.KEY_EVENT_TIMESTAMP
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun CalenderScreen(paddingValues: PaddingValues, onEventClick: (event: Event) -> Unit) {
    val viewModel: CalenderViewModel = koinViewModel()

    Background(paddingValues)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        DatePicker(viewModel)
        Divider(
            startIndent = 12.dp,
            thickness = 1.dp,
            color = Color(AppColor.orange),
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )
        EventList(onEventClick = onEventClick, viewModel)
    }
}

@Composable
fun DatePicker(viewModel: CalenderViewModel) {

    ScrollableTabRow(
        selectedTabIndex = viewModel.selectedDate.collectAsState().value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        backgroundColor = Color.Transparent,
        edgePadding = 16.dp,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = Color.Transparent, modifier = Modifier.tabIndicatorOffset(
                    currentTabPosition = tabPositions[viewModel.selectedDate.collectAsState().value],
                )
            )
        },
    ) {
        for (i in DATE_LIST.indices) {
            Box(
                modifier = Modifier.padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clickable {
                            viewModel.updateSelectedDate(i)
                        }
                        .height(80.dp)
                        .width(70.dp)
                        .background(
                            getColorForIndex(
                                i, viewModel.selectedDate.collectAsState().value
                            ), RoundedCornerShape(10.dp)
                        )
                        .wrapContentSize(Alignment.Center),
                ) {
                    Text(
                        text = DATE_LIST[i],
                        color = if (viewModel.selectedDate.collectAsState().value == i) Color(
                            AppColor.darkGray
                        ) else Color(AppColor.orange),
                        fontWeight = if (viewModel.selectedDate.collectAsState().value == i) FontWeight.W900 else FontWeight.W600,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }
}

@Composable
fun EventList(
    onEventClick: (event: Event) -> Unit, viewModel: CalenderViewModel
) {
    val events = viewModel.events.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            //.verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        if (events.value.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillParentMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ingen begivenheder",
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        color = Color(AppColor.textColor)
                    )
                    Text(
                        text = "Du har ingen begivenheder i dag.",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color(AppColor.gray)
                    )
                }
            }
        } else {
            items(events.value.size) {i ->
                val event = events.value[i]
                val eventDate =
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(event.timestamp), ZoneOffset.UTC)
                println(LocalDateTime.now().toString() + " / " + eventDate.toString())
                EventBox(
                    title = event.title,
                    campName = event.campName,
                    time = event.dayTime.split(" ")[1],
                    strikethrough = LocalDateTime.now().isAfter(eventDate),
                    onEventClick = { onEventClick(event) }
                )
            }
            /*for (event in events.value) {
                val eventDate =
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(event.timestamp), ZoneOffset.UTC)
                println(LocalDateTime.now().toString() + " / " + eventDate.toString())
                EventBox(
                    title = event.title,
                    campName = event.campName,
                    time = event.dayTime.split(" ")[1],
                    strikethrough = LocalDateTime.now().isAfter(eventDate),
                    onEventClick = { onEventClick(event) }
                )
            }*/
        }
    }
}


private fun getColorForIndex(index: Int, selected: Int): Color {
    return if (index == selected) {
        Color(AppColor.orange)
    } else {
        Color(AppColor.darkGray)
    }
}
