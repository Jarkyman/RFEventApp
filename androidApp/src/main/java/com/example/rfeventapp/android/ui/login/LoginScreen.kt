package com.example.rfeventapp.android.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rfeventapp.android.MyApplicationTheme
import com.example.rfeventapp.android.R
import com.example.rfeventapp.android.navigation.AuthScreen
import com.example.rfeventapp.android.ui.composable.background.BackgroundWithPicture
import com.example.rfeventapp.android.ui.composable.button.DefaultButton
import com.example.rfeventapp.android.ui.composable.button.TextButton
import com.example.rfeventapp.android.ui.composable.input.DefaultInputTextField
import com.example.rfeventapp.android.ui.composable.input.PasswordInputTextField
import com.example.rfeventapp.utils.AppColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = koinViewModel()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(AppColor.backgroundColor))
    ) {
        BackgroundWithPicture()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.party),
                contentDescription = "party",
                modifier = Modifier.size(180.dp)
            )
            Column(
                modifier = Modifier.padding(top = 48.dp, start = 20.dp, end = 20.dp),
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
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Next
                            )
                        }
                    ),
                )
                Spacer(modifier = Modifier.height(16.dp))
                PasswordInputTextField(
                    value = viewModel.password.collectAsState().value,
                    placeholder = "Password",
                    label = "Password",
                    onValueChange = { viewModel.updatePassword(it) },
                    onVisibleChange = { viewModel.passwordVisibleToggle() },
                    visible = viewModel.isPasswordVisible.collectAsState().value,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                        //TODO: Login
                    )
                )
                Text(
                    text = "Glemt kodeord?",
                    color = Color(AppColor.textColor),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable { navController.navigate(AuthScreen.Forgot.route) }
                        .align(Alignment.End)
                )
                DefaultButton(
                    modifier = Modifier
                        .padding(top = 48.dp, start = 20.dp, end = 20.dp),
                    onClick = { viewModel.handleLogin(navController, context) },
                    title = "Log in"
                )
                TextButton(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp),
                    onClick = { navController.navigate(AuthScreen.SignUp.route) },
                    title = "Sign up"
                )
            }
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun LoginPreview() {
    MyApplicationTheme {
        LoginScreen(rememberNavController())
    }
}