package com.example.rfeventapp.android.ui.composable.input

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rfeventapp.utils.AppColor

@Composable
fun BigInputTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    label: String = "",
    letters: Int = 0,
    maxLetters: Int = 1000,
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = false,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(AppColor.textColor),
                disabledTextColor = Color.Transparent,
                backgroundColor = Color(AppColor.gray).copy(alpha = 0.75f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = Color(AppColor.darkGray)
            ),
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = MaterialTheme.shapes.large.copy(CornerSize(30.dp)),
            modifier = modifier
                .height(230.dp)
                .fillMaxWidth(),
        )
        Text(
            modifier = Modifier.padding(top = 20.dp, end = 15.dp).align(Alignment.TopEnd),
            text = "${letters}/${maxLetters}"
        )
    }

}