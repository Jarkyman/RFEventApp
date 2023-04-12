package com.example.rfeventapp.android.ui.composable.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.rfeventapp.utils.AppColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DatePickerField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    placeholder: String = "",
    label: String = "",
    onValueChange: (TextFieldValue) -> Unit,
    onOkClicked: () -> Unit = {},
    startDate: LocalDate = LocalDate.of(1997, 3, 8),
) {
    val dateDialog = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dateDialog,
        backgroundColor = Color(AppColor.backgroundColor),
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = TextStyle(
                    color = Color(AppColor.textColor)
                ),
                onClick = onOkClicked
            )
            negativeButton(
                text = "Cancel",
                textStyle = TextStyle(
                    color = Color(AppColor.textColor)
                )
            )
        }
    ) {
        datepicker(
            initialDate = startDate,
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = Color(AppColor.orange),
                headerTextColor = Color(AppColor.textColor),
                calendarHeaderTextColor = Color(AppColor.textColor),
                dateActiveBackgroundColor = Color(AppColor.orange),
                //dateInactiveBackgroundColor= Color.Green,
                dateActiveTextColor = Color(AppColor.backgroundColor),
                dateInactiveTextColor = Color(AppColor.textColor)
            )
        ) { date ->
            val formattedDate = date.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
            onValueChange(TextFieldValue(formattedDate))
        }
    }

    Box {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .height(60.dp)
                .fillMaxWidth(),
            readOnly = true,
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
            shape = MaterialTheme.shapes.large.copy(CornerSize(60.dp)),

            )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = { dateDialog.show() }),
        )
    }
}