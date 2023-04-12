package com.example.rfeventapp.android.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.rfeventapp.android.ui.composable.input.PasswordInputTextField
import com.example.rfeventapp.android.ui.composable.button.IconButton
import com.example.rfeventapp.utils.AppColor

import org.koin.androidx.compose.koinViewModel

@Composable
fun EditPassScreen(onBackClick: () -> Unit, onSaveClick: () -> Unit) {
    val viewModel: EditPassViewModel = koinViewModel()
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
                    PasswordInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.passwordOld.collectAsState().value,
                        placeholder = "Password",
                        label = "Password",
                        onValueChange = { viewModel.updatePasswordOld(it) },
                        onVisibleChange = { viewModel.passwordOldVisibleToggle() },
                        visible = viewModel.isPasswordOldVisible.collectAsState().value,
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Next
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
                        value = viewModel.passwordNew.collectAsState().value,
                        placeholder = "New password",
                        label = "New password",
                        onValueChange = { viewModel.updatePasswordNew(it) },
                        onVisibleChange = { viewModel.passwordNewVisibleToggle() },
                        visible = viewModel.isPasswordNewVisible.collectAsState().value,
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Next
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
                        value = viewModel.passwordNewConfirm.collectAsState().value,
                        placeholder = "Confirm password",
                        label = "Confirm password",
                        onValueChange = { viewModel.updatePasswordNewConfirm(it) },
                        onVisibleChange = { viewModel.passwordNewConfirmVisibleToggle() },
                        visible = viewModel.isPasswordNewConfirmVisible.collectAsState().value,
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
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            viewModel.savePassword(onBackClick, context)
                        },
                        title = "Gem",
                    )
                }
            }
        }
    }
}