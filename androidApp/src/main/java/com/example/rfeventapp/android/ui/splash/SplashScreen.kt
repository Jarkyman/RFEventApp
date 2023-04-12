package com.example.rfeventapp.android.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rfeventapp.android.R
import com.example.rfeventapp.android.firebase.FirebaseAuthRepo
import com.example.rfeventapp.android.navigation.Graph
import com.example.rfeventapp.utils.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: SplashViewModel = koinViewModel()
    val context = LocalContext.current
    viewModel.handelAppInfo(navController, context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(AppColor.backgroundColor))
    ) {
        Image(
            painter = painterResource(id = R.drawable.orange_scene),
            contentDescription = "Orange scene",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(AppColor.darkOrange).copy(0.3f),
                            Color(AppColor.backgroundColor).copy(0.3f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.party),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = Color.Black)
        }
    }
}
