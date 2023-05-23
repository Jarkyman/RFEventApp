package com.example.rfeventapp.android.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rfeventapp.android.R
import com.example.rfeventapp.android.ui.composable.background.Background
import com.example.rfeventapp.usecase.loggedInUser
import com.example.rfeventapp.utils.AppColor
import com.example.rfeventapp.utils.Capitalizer
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    onCreateEventClick: () -> Unit,
    onMyEventsClick: () -> Unit,
    onEditClick: () -> Unit,
    onEditPassClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    val viewModel: ProfileViewModel = koinViewModel()
    Background(paddingValues)
    if (viewModel.showLanguageDialog.collectAsState().value) {
        PopupLanguage(viewModel)
    }
    if (viewModel.showNotificationDialog.collectAsState().value) {
        PopupNotification(viewModel)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        WelcomeMsg(Capitalizer().capitalizeWords("${loggedInUser!!.firstName} ${loggedInUser!!.lastName}"))
        Row(
            //horizontalArrangement = Arrangement.SpaceBetween,
            //modifier = Modifier.fillMaxWidth()
        ) {
            InfoField(description = "E-mail", info = Capitalizer().capitalizeFirstLetter(loggedInUser!!.email))
            //if (FirebaseAuth.getInstance().currentUser?.isEmailVerified != true) {
                Image(
                    painter = painterResource(id = R.drawable.icon_warning),
                    contentDescription = "Ikke verifiseret",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 10.dp),
                    //colorFilter = ColorFilter.tint(Color.Yellow)
                )
            //}
        }
        InfoField(description = "Camp", info = Capitalizer().capitalizeWords(viewModel.campNameCheck(loggedInUser!!.campName)))
        Divider(
            startIndent = 12.dp,
            thickness = 1.dp,
            color = Color(AppColor.orange),
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                    Text(
                        text = "Begivenheder",
                        color = Color(AppColor.gray),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Opret begivenhed",
                        color = Color(AppColor.textColor),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable(onClick = onCreateEventClick)
                    )
                    Text(
                        text = "Mine begivenheder",
                        color = Color(AppColor.textColor),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .clickable(onClick = onMyEventsClick)
                    )
            } else {
                    Text(
                        text = "Begivenheder",
                        color = Color(AppColor.gray),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Du skal verifiser din email for at kunne oprette begivenheder",
                        color = Color(AppColor.textColor),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .clickable(onClick = {
                                //Send email
                            })
                    )
                }

            }
        }
        Divider(
            startIndent = 12.dp,
            thickness = 1.dp,
            color = Color(AppColor.orange),
            modifier = Modifier.padding(vertical = 10.dp)
        )
        SettingsField(onEditClick, onEditPassClick, onSignOutClick, viewModel)
    }
}

@Composable
fun WelcomeMsg(name: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxHeight(.3f)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Hej",
                color = Color(AppColor.textColor),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = name,
                maxLines = 1,
                color = Color(AppColor.textColor),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun InfoField(description: String, info: String) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = description,
                color = Color(AppColor.gray),
                fontSize = 14.sp
            )
            Text(
                text = info,
                color = Color(AppColor.textColor),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SettingsField(
    onEditClick: () -> Unit,
    onEditPassClick: () -> Unit,
    onSignOutClick: () -> Unit,
    viewModel: ProfileViewModel
) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Indstillinger",
                color = Color(AppColor.gray),
                fontSize = 14.sp
            )
            Text(
                text = "Rediger profil",
                color = Color(AppColor.textColor),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.clickable(onClick = { onEditClick() })
            )
            Text(
                text = "Skift kode",
                color = Color(AppColor.textColor),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable(onClick = {
                        onEditPassClick()
                    })
            )
            /*Text(
                text = "Notifikationer",
                color = Color(AppColor.textColor),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable(onClick = {
                        viewModel.toggleNotificationDialog()
                    })
            )*/ //TODO: Add notifications
            /*Text(
                text = "Sprog",
                color = Color(AppColor.textColor),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable(onClick = {
                        viewModel.toggleLanguageDialog()
                    })
            )*/ //TODO: Add Danish and English
            Text(
                text = "Log ud",
                color = Color(AppColor.textColor),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable(onClick = {
                        onSignOutClick()
                    })
            )
        }
    }
}

@Composable
fun PopupNotification(viewModel: ProfileViewModel) {
    AlertDialog(
        backgroundColor = Color(AppColor.darkGray),
        shape = RoundedCornerShape(30.dp),
        onDismissRequest = {
            viewModel.toggleNotificationDialog()
        },
        buttons = {
            //TODO: Tilføj en lukke funktion
        },
        title = {
            Text(
                "Notification",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(AppColor.textColor),
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                //verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = "Påmindelser",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(AppColor.textColor)
                    )
                    Switch(
                        checked = viewModel.notificationReminder.collectAsState().value,
                        onCheckedChange = { viewModel.toggleNotificationReminder() }
                    )

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = "Nyheder",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(AppColor.textColor)
                    )
                    Switch(
                        checked = viewModel.notificationNews.collectAsState().value,
                        onCheckedChange = { viewModel.toggleNotificationNews() }
                    )

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = "Ændringer",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(AppColor.textColor)
                    )
                    Switch(
                        checked = viewModel.notificationChanges.collectAsState().value,
                        onCheckedChange = { viewModel.toggleNotificationChanges() }
                    )

                }
            }
        }
    )
}

@Composable
fun PopupLanguage(viewModel: ProfileViewModel) {
    AlertDialog(
        backgroundColor = Color(AppColor.darkGray),
        shape = RoundedCornerShape(30.dp),
        onDismissRequest = {
            viewModel.toggleLanguageDialog()
        },
        buttons = {
            Text(
                "Sprog",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            )
            //TODO: Tilføj en lukke funktion
        },
        title = {
            Text(
                "Sprog",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(AppColor.textColor),
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.dk),
                    contentDescription = "dk",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(80.dp)
                        .aspectRatio(1f)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .clickable(onClick = {
                            //TODO: skift sporg til dansk
                        })
                )

                Spacer(Modifier.width(20.dp))

                Image(
                    painter = painterResource(R.drawable.us),
                    contentDescription = "us",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(80.dp)
                        .aspectRatio(1f)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .clickable(onClick = {
                            //TODO: skift sporg til engelsk
                        })
                )

            }
        }
    )
}