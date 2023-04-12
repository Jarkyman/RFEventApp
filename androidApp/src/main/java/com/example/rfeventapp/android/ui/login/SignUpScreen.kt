package com.example.rfeventapp.android.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rfeventapp.android.ui.composable.background.BackgroundWithPicture
import com.example.rfeventapp.android.ui.composable.button.DefaultButton
import com.example.rfeventapp.utils.AppColor
import com.example.rfeventapp.android.ui.composable.button.IconButton
import com.example.rfeventapp.android.ui.composable.input.DatePickerField
import com.example.rfeventapp.android.ui.composable.input.DefaultInputTextField
import com.example.rfeventapp.android.ui.composable.input.PasswordInputTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(navController: NavController) {
    val viewModel: SignUpViewModel = koinViewModel()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(AppColor.backgroundColor))
    ) {
        BackgroundWithPicture()
        if (viewModel.loading.collectAsState().value) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
            IconButton(
                modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                onClick = { navController.popBackStack() },
                icon = Icons.Filled.ArrowBackIosNew
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 68.dp, start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    DefaultInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.firstName.collectAsState().value,
                        placeholder = "First name",
                        label = "First name",
                        onValueChange = { viewModel.updateFirstName(it) },
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Words
                        ),
                    )
                }
                item {
                    DefaultInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.lastName.collectAsState().value,
                        placeholder = "Last name",
                        label = "Last name",
                        onValueChange = { viewModel.updateLastName(it) },
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Words,
                        ),
                    )
                }
                item {
                    DefaultInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.email.collectAsState().value,
                        placeholder = "E-mail",
                        label = "E-mail",
                        onValueChange = { viewModel.updateEmail(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.None
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Down
                            )
                        }),
                    )
                }
                item {
                    DefaultInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.campName.collectAsState().value,
                        placeholder = "Camp name",
                        label = "Camp name",
                        onValueChange = { viewModel.updateCampName(it) },
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Next
                            )
                        }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences,
                        ),
                    )
                }
                item {
                    DatePickerField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.birthday.collectAsState().value,
                        placeholder = "Birthday",
                        label = "Birthday",
                        onValueChange = { viewModel.updateBirthday(it) },
                        onOkClicked = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Down
                            )
                        },
                    )
                }
                item {
                    PasswordInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.password.collectAsState().value,
                        placeholder = "Password",
                        label = "Password",
                        onValueChange = { viewModel.updatePassword(it) },
                        onVisibleChange = { viewModel.passwordVisibleToggle() },
                        visible = viewModel.isPasswordVisible.collectAsState().value,
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                        ),
                    )
                }
                item {
                    PasswordInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.passwordConfirm.collectAsState().value,
                        placeholder = "Confirm Password",
                        label = "Confirm Password",
                        onValueChange = { viewModel.updatePasswordConfirm(it) },
                        onVisibleChange = { viewModel.passwordConfirmVisibleToggle() },
                        visible = viewModel.isPasswordConfirmVisible.collectAsState().value,
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                        ),
                    )
                }
                item {
                    DefaultButton(
                        modifier = Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp),
                        onClick = { viewModel.handleSignUp(navController, context) },
                        title = "Sign up"
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewSignUpPage() {
    MaterialTheme {
        SignUpScreen(rememberNavController())
    }
}
