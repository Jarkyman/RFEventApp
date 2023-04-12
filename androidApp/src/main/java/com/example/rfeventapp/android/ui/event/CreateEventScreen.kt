package com.example.rfeventapp.android.ui.event

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.rfeventapp.android.R
import com.example.rfeventapp.android.ui.composable.background.Background
import com.example.rfeventapp.android.ui.composable.button.DefaultButton
import com.example.rfeventapp.android.ui.composable.button.IconButton
import com.example.rfeventapp.android.ui.composable.input.BigInputTextField
import com.example.rfeventapp.android.ui.composable.input.DefaultInputTextField
import com.example.rfeventapp.android.ui.composable.input.TimePicker
import com.example.rfeventapp.utils.*

@Composable
fun CreateEventScreen(onBackClick: () -> Unit, viewModel: CreateEventViewModel) {
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(64.dp))
                }
                item {
                    PickImageFromGallery(viewModel)
                }
                item {
                    DefaultInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.title.collectAsState().value,
                        placeholder = "Navn på begivenhed",
                        label = "Navn på begivenhed",
                        onValueChange = { viewModel.updateTitle(it) },
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Down
                            )
                        }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                    )
                }

                item {
                    DefaultInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.campName.collectAsState().value,
                        placeholder = "Camp navn",
                        label = "Camp navn",
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
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .height(60.dp)
                            .fillMaxWidth()
                            .background(
                                Color(AppColor.gray).copy(alpha = 0.75f),
                                shape = MaterialTheme.shapes.large.copy(CornerSize(60.dp))
                            ),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.5f)
                                    .clickable { viewModel.toggleDayDropdown() },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .padding(start = 15.dp),
                                    painter = painterResource(id = R.drawable.calendar),
                                    contentDescription = "Calendar icon"
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                DropdownMenu(
                                    expanded = viewModel.dayDropdown.collectAsState().value,
                                    onDismissRequest = { viewModel.toggleDayDropdown() },
                                    modifier = Modifier.width(100.dp)
                                ) {
                                    DATE_LIST.forEachIndexed { index, day ->
                                        DropdownMenuItem(onClick = {
                                            viewModel.updateDay(index)
                                            viewModel.toggleDayDropdown()
                                        }) {
                                            Text(text = day)
                                        }
                                    }
                                }
                                Text(
                                    text = DATE_LIST[viewModel.day.collectAsState().value],
                                    color = Color(AppColor.textColor),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            TimePicker(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f),
                                value = viewModel.time.collectAsState().value,
                                onValueChange = {
                                    viewModel.updateTime(it)
                                },
                            )
                        }

                    }
                }
                //TODO: Category?
                item {
                    DefaultInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.location.collectAsState().value,
                        placeholder = "Location",
                        label = "Location",
                        onValueChange = { viewModel.updateLocation(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Characters
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Down
                            )
                        }),
                    )
                }
                //TODO: FacebookLink
                /*item {
                DefaultInputTextField(
                    modifier = Modifier.padding(top = 16.dp),
                    value = viewModel.facebookLink.collectAsState().value,
                    placeholder = "Link til begivenhed",
                    label = "Link til begivenhed",
                    onValueChange = { viewModel.updateFacebookLink(it) },
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.moveFocus(
                            focusDirection = FocusDirection.Down
                        )
                    }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.None,
                    ),
                )
            }*/

                item {
                    BigInputTextField(
                        modifier = Modifier.padding(top = 16.dp),
                        value = viewModel.description.collectAsState().value,
                        placeholder = "Lav en beskrivelse af din begivenhed",
                        label = "Beskrivelse",
                        letters = viewModel.letters.collectAsState().value,
                        maxLetters = MAX_DESCRIPTION_LETTERS,
                        onValueChange = { viewModel.updateDescription(it) },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = if (viewModel.letters.collectAsState().value < MAX_DESCRIPTION_LETTERS) ImeAction.Default else ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences,
                        ),
                    )
                }
                item {
                    if (viewModel.isReady.collectAsState().value && !viewModel.isEditMode.collectAsState().value) {
                        DefaultButton(
                            modifier = Modifier
                                .padding(bottom = 16.dp, top = 16.dp)
                                .align(Alignment.BottomCenter),
                            onClick = {
                                viewModel.uploadImage(onBackClick, context)
                            },
                            title = "OPRET"
                        )
                    } else if (viewModel.isReady.collectAsState().value && viewModel.isEditMode.collectAsState().value) {
                        DefaultButton(
                            modifier = Modifier
                                .padding(bottom = 16.dp, top = 16.dp)
                                .align(Alignment.BottomCenter),
                            onClick = { viewModel.saveEvent(onBackClick, context) },
                            title = "GEM"
                        )
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            IconButton(
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp)
                    .align(Alignment.TopStart),
                onClick = onBackClick,
                icon = Icons.Filled.ArrowBackIosNew
            )
            if (viewModel.isEditMode.collectAsState().value) {
                IconButton(
                    modifier = Modifier
                        .padding(top = 20.dp, end = 20.dp)
                        .align(Alignment.TopEnd),
                    onClick = { viewModel.deleteEvent(onBackClick, context) },
                    icon = Icons.Filled.DeleteOutline
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PickImageFromGallery(viewModel: CreateEventViewModel) {
    val imageUriState = viewModel.imageUri.collectAsState()
    val context = LocalContext.current
    val bitmap = viewModel.image.collectAsState()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                viewModel.updateImageUri(uri)
            }
        }
    val width = with(LocalConfiguration.current) { screenWidthDp.dp / 2 }
    Box(
        modifier = Modifier
            .size(width)
            .background(
                Color(AppColor.gray).copy(alpha = 0.75f), RoundedCornerShape(width / 3)
            )
            .border(2.dp, Color(AppColor.orange), RoundedCornerShape(width / 3))
            .clickable(onClick = { launcher.launch("image/*") }),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Photo",
                tint = Color(AppColor.orange),
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Add Photo",
                color = Color(AppColor.orange),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        imageUriState.value.let {
            if (Build.VERSION.SDK_INT < 28) {
                viewModel.updateImage(MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it))
            } else {
                val source = it?.let { it1 ->
                    ImageDecoder.createSource(context.contentResolver,
                        it1
                    )
                }
                source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                    ?.let { it2 -> viewModel.updateImage(it2) }
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(width / 3))
                        .border(2.dp, Color(AppColor.orange), RoundedCornerShape(width / 3))
                )
            }
            if (bitmap.value == null && viewModel.isEditMode.value) {
                var imgUrl = ""
                if (viewModel.event.image.isNotEmpty()) {
                    imgUrl = "$IMAGE_URL_BASE${viewModel.event.image.split("&")[0]}$IMAGE_URL_MEDIA${viewModel.event.image.split("&")[1]}"
                }
                Image(
                    painter = rememberImagePainter(
                        data = if(viewModel.event.image.isEmpty()) R.drawable.beer_img else imgUrl,//TODO: Change to load image
                        builder = {
                            crossfade(false)
                            placeholder(R.drawable.beer_img)
                        }
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(width / 3))
                        .border(2.dp, Color(AppColor.orange), RoundedCornerShape(width / 3))
                )
            }
        }
    }
}
    /*Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp)
                        .padding(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { launcher.launch("image") }) {
            Text(text = "Pick Image")
        }
    }

     */