package com.example.rfeventapp.android.ui.composable.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.rfeventapp.utils.AppColor
import kotlinx.coroutines.launch

@Composable
fun PasswordInputTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    label: String = "",
    onValueChange: (String) -> Unit = {},
    onVisibleChange: () -> Unit = {},
    visible: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    TextField(
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth(),
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
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = MaterialTheme.shapes.large.copy(CornerSize(60.dp)),
        trailingIcon = {
            val image = if (visible)
                Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            // TODO: LOCALE --
            val description =
                if (visible) "Hide password" else "Show password"

            IconButton(onClick = onVisibleChange) {
                Icon(imageVector = image, description)
            }
        }
    )
}