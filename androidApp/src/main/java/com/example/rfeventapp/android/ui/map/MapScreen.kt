package com.example.rfeventapp.android.ui.map

import android.view.LayoutInflater
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView

import com.example.rfeventapp.android.R
import com.example.rfeventapp.android.ZoomableImage
import com.example.rfeventapp.android.ui.composable.background.Background
import com.example.rfeventapp.utils.AppColor
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.FitPolicy

import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(paddingValues: PaddingValues) {
    val viewModel: MapViewModel = koinViewModel()
    Background(paddingValues)
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.map_layout, null, false)

            val pdfView = view.findViewById<PDFView>(R.id.pdfView)
            pdfView.fromAsset("rf_map_2022.pdf")
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .spacing(20) // spacing between pages in dp. To define spacing color, set view background
                .pageFitPolicy(FitPolicy.HEIGHT) // mode to fit pages in the view
                .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                .pageSnap(false) // snap pages to screen boundaries
                .pageFling(false) // make a fling change only a single page like ViewPager
                .nightMode(false) // toggle night mode
                .load()

            view
        },
        update = { view ->
            // Update the view
        }
    )
    ExpandableInfoCard(header = "Map", color = Color(AppColor.orange), viewModel, paddingValues)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableInfoCard(
    header: String,
    color: Color,
    viewModel: MapViewModel,
    paddingValues: PaddingValues,
) {
    Box(modifier = Modifier.padding(paddingValues)) {
        Card(modifier = Modifier.padding(8.dp),
            backgroundColor = Color(AppColor.backgroundColor),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, color),
            onClick = {
                viewModel.toggleExpanded()
            }) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = header,
                        color = Color(AppColor.textColor),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(.9f)
                            .padding(start = 16.dp)
                    )
                    IconButton(modifier = Modifier
                        .rotate(viewModel.rotationState.value)
                        .weight(.1f),
                        onClick = {
                            viewModel.toggleExpanded()
                        }) {
                        Icon(
                            imageVector = if (viewModel.expanded.collectAsState().value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            tint = color, // Icon Color
                            contentDescription = "Drop Down Arrow"
                        )
                    }
                }
                AnimatedVisibility(visible = viewModel.expanded.collectAsState().value) {
                    MapInfoExpand()
                }
            }
        }
    }
}

@Composable
fun MapRow() {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.height(80.dp).weight(1f/3f)
            ) {
                HexShape()
                Text(text = "Gate", color = Color(AppColor.gray), maxLines = 2)
            }
            BoxShape(
                color = Color(0xFFA6D155),
                borderColor = Color(AppColor.orange),
                text = "Festival Site",
                modifier = Modifier.weight(1f/3f)
            )

            BoxShape(
                color = Color(0xFF33B754), borderColor = Color.Transparent, text = "Regular Camping",
                modifier = Modifier.weight(1f/3f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            BoxShape(
                color = Color(0xFF1B8329),
                borderColor = Color.Transparent,
                text = "Community Special Camp",
                modifier = Modifier.weight(1f/3f)
            )

            BoxShape(
                color = Color(0xFFC3BB90), borderColor = Color.White, text = "Closed area",
                modifier = Modifier.weight(1f/3f)
            )

            BoxShape(
                color = Color(0xFF80A15A), borderColor = Color.White, text = "Parking",
                modifier = Modifier.weight(1f/3f)
            )

        }
    }
}

@Composable
fun BoxShape(color: Color, borderColor: Color, text: String, modifier: Modifier = Modifier) {
    val hex = RoundedCornerShape(12.dp)
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(80.dp),
    ) {
        Box(modifier = Modifier
            .width(40.dp)
            .height(40.dp)
            .graphicsLayer {
                shadowElevation = 0f
                clip = true
                shape = hex
            }
            .background(color = color)
            .border(
                border = BorderStroke(
                    width = 1.dp, color = borderColor
                ), shape = hex
            ))
        Text(
            text = text,
            color = Color(AppColor.gray),
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HexShape() {
    val hex = CutCornerShape(12.dp)
    Box(modifier = Modifier
        .width(40.dp)
        .height(40.dp)
        .graphicsLayer {
            shadowElevation = 0f
            clip = true
            shape = hex
        }
        .background(color = Color(0xFF005D94))
        .border(
            border = BorderStroke(
                width = 1.dp, color = Color.White
            ), shape = hex
        )) {

    }
}

@Composable
fun MapInfoExpand() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        item {
            MapRow()
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
            ) {
                InfoIcon(
                    text = "Agora",
                    icon = R.drawable.icon_agora,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(0.45f)
                )
                InfoIcon(
                    text = "Gear rental",
                    icon = R.drawable.icon_gear_rental,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
        item {
            Row() {
                InfoIcon(
                    text = "Recycling station",
                    icon = R.drawable.icon_recycling_station,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(0.45f)
                )
                InfoIcon(
                    text = "Supermarket",
                    icon = R.drawable.icon_supermarket,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
        item {
            Row() {
                InfoIcon(
                    text = "Refund station",
                    icon = R.drawable.icon_refund_station,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(0.45f)
                )
                InfoIcon(
                    text = "Merchandise",
                    icon = R.drawable.icon_merchandise,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
        item {
            Row() {
                InfoIcon(
                    text = "Disabled toilets",
                    icon = R.drawable.icon_disabled_toilets,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(0.45f)
                )
                InfoIcon(
                    text = "Lost & found",
                    icon = R.drawable.icon_lost_and_found,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
        item {
            Row() {
                InfoIcon(
                    text = "Showers",
                    icon = R.drawable.icon_showers,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(0.45f)
                )
                InfoIcon(
                    text = "Information",
                    icon = R.drawable.icon_information,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
        item {
            Row() {
                InfoIcon(
                    text = "First aid",
                    icon = R.drawable.icon_first_aid,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(0.45f)
                )
                InfoIcon(
                    text = "Laundromat",
                    icon = R.drawable.icon_laundromat,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
        item {
            Row() {
                InfoIcon(
                    text = "Pharmacy",
                    icon = R.drawable.icon_pharmacy,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(0.45f)
                )
                InfoIcon(
                    text = "Service",
                    icon = R.drawable.icon_service,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
        item {
            InfoIcon(
                text = "Chip wristband support",
                icon = R.drawable.icon_chip_wristband_support,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
            )
        }
        item {
            InfoIconLong(
                text = "Parking (Regular, bicycles, disabled)", iconList = listOf(
                    R.drawable.icon_parking_regular,
                    R.drawable.icon_parking_bicycles,
                    R.drawable.icon_parking_disabled
                )
            )
        }
        item {
            InfoIconLong(
                text = "Train/Shuttle Bus/Taxi", iconList = listOf(
                    R.drawable.icon_bus, R.drawable.icon_train, R.drawable.icon_taxi
                )
            )
        }
    }
}

@Composable
fun InfoIcon(text: String, icon: Int, modifier: Modifier) {
    Row(
        modifier = modifier
            .padding(bottom = 4.dp)
            .height(48.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = text,
            modifier = Modifier
                .height(32.dp)
                .width(32.dp)
        )
        Text(
            text = text,
            color = Color(AppColor.gray),
            textAlign = TextAlign.Start,
            maxLines = 2,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
fun InfoIconLong(text: String, iconList: List<Int>) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp, bottom = 4.dp)
            .height(48.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconList.forEach { icon ->
            Image(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = text,
            color = Color(AppColor.gray),
            textAlign = TextAlign.Start,
            maxLines = 2,
            modifier = Modifier.padding(5.dp)
        )
    }
}
