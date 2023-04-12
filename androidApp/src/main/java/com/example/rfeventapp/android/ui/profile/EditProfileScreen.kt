package com.example.rfeventapp.android.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp

import com.example.rfeventapp.android.ui.composable.background.Background
import com.example.rfeventapp.android.ui.composable.button.DefaultButton
import com.example.rfeventapp.android.ui.composable.input.DatePickerField
import com.example.rfeventapp.android.ui.composable.input.DefaultInputTextField
import com.example.rfeventapp.android.ui.composable.button.IconButton
import com.example.rfeventapp.utils.AppColor

import org.koin.androidx.compose.koinViewModel

@Composable
fun EditProfileScreen(onBackClick: () -> Unit, onSaveClick: () -> Unit) {
    val viewModel: EditProfileViewModel = koinViewModel()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(AppColor.backgroundColor))
    ) {
        Background()
        if (viewModel.loading.collectAsState().value) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
            IconButton(
                modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                onClick = { onBackClick() },
                icon = Icons.Filled.ArrowBackIosNew,
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
                                focusDirection = FocusDirection.Down
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
                            focusManager.clearFocus()
                        },
                        startDate = viewModel.getLocaleDateBirthday()
                    )
                }
                item {
                    DefaultButton(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = { viewModel.updateProfile(onBackClick, context) },
                        title = "Gem"
                    )
                }
            }
        }
    }
}
