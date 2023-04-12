package com.example.rfeventapp.android.ui.composable.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rfeventapp.utils.AppColor

@Composable
fun DefaultInputTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    label: String = "",
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
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
        shape = MaterialTheme.shapes.large.copy(CornerSize(60.dp)),
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth(),
    )
}