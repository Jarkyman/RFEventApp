package com.example.rfeventapp.android.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rfeventapp.android.ui.composable.background.BackgroundWithPicture
import com.example.rfeventapp.android.ui.composable.button.DefaultButton
import com.example.rfeventapp.android.ui.composable.button.IconButton
import com.example.rfeventapp.android.ui.composable.input.DefaultInputTextField
import com.example.rfeventapp.utils.AppColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotScreen(onBackClick: () -> Unit) {
    val viewModel: ForgotViewModel = koinViewModel()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(AppColor.backgroundColor))
    ) {
        BackgroundWithPicture()
        IconButton(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp),
            onClick = onBackClick,
            icon = Icons.Filled.ArrowBackIosNew
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Glemt din kodeord?",
                maxLines = 1,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(AppColor.textColor),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Bare rolig, vi ville sende dig en email hvor du kan lave en ny kode.",
                maxLines = 3,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(AppColor.gray),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 30.dp)
            )
            Column(
                modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DefaultInputTextField(
                    value = viewModel.email.collectAsState().value,
                    placeholder = "E-mail",
                    label = "E-mail",
                    onValueChange = { viewModel.updateEmail(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                )
                Spacer(modifier = Modifier.height(16.dp))
                DefaultButton(
                    modifier = Modifier
                        .padding(top = 48.dp, start = 20.dp, end = 20.dp),
                    onClick = { viewModel.handleForgot(context) },
                    title = "Få ny kode"
                )
            }
        }
    }
}