package com.example.rfeventapp.android.ui.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rfeventapp.android.ui.composable.background.Background
import com.example.rfeventapp.android.ui.composable.button.IconButton
import com.example.rfeventapp.android.ui.composable.card.EventBoxEdit
import com.example.rfeventapp.domain.Event
import com.example.rfeventapp.utils.AppColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyEventsScreen(onBackClick: () -> Unit, onEventClick: (event: Event) -> Unit) {
    val viewModel: MyEventsViewModel = koinViewModel()
    Background()
    EventList(onEventClick = onEventClick, viewModel = viewModel)
    IconButton(
        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
        onClick = onBackClick,
        icon = Icons.Filled.ArrowBackIosNew
    )
}

@Composable
fun EventList(
    onEventClick: (event: Event) -> Unit, viewModel: MyEventsViewModel
) {
    val scrollState = rememberScrollState()
    val events = viewModel.events.collectAsState()
    if (events.value.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .height(100.dp),
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
                text = "Du har ikke oprettet nogle begivenheder endnu.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color(AppColor.gray)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            for (event in events.value) {
                EventBoxEdit(
                    title = event.title,
                    campName = event.campName,
                    time = event.dayTime,
                    strikethrough = false,
                    onEventClick = { onEventClick(event) }
                )
            }
        }
    }
}