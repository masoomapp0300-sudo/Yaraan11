package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserProfile
import com.example.ui.components.AvatarFrame
import com.example.ui.theme.YaraanPinkPrimary

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import coil.compose.AsyncImage
import com.example.ui.components.UploadPhotoDialog

@Composable
fun EditProfileScreen(
    userProfile: UserProfile,
    onSaveProfile: (String, String, String, String) -> Unit,
    onAddPhoto: (String) -> Unit = {},
    onRemovePhoto: (Int) -> Unit = {},
    onUpdateAvatarUrl: (String) -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)
    var nickname by remember { mutableStateOf(userProfile.nickname) }
    var birthday by remember { mutableStateOf(userProfile.birthday) }
    var constellation by remember { mutableStateOf(userProfile.constellation) }
    var bio by remember { mutableStateOf(userProfile.bio) }
    var showAddPhotoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FE))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Top Navigation Bar: "Edit" & "Save"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1F1D2B)
                )
            }

            Text(
                text = "Edit Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1D2B)
            )

            TextButton(
                onClick = { onSaveProfile(nickname, birthday, constellation, bio) }
            ) {
                Text(
                    text = "Save",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7C4DFF)
                )
            }
        }

        // Center Profile Avatar with Camera Button Overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                AvatarFrame(
                    size = 96.dp,
                    showDesignerFrame = false,
                    avatarUrl = userProfile.avatarUrl.ifBlank { null }
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.35f))
                        .clickable { showAddPhotoDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Upload Photo",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // Photo Upload Grid Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile Gallery (${userProfile.photos.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1D2B)
                )

                Text(
                    text = "+ Add Photo",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = YaraanPinkPrimary,
                    modifier = Modifier.clickable { showAddPhotoDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Multi-Photo Upload Box Grid
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Add Photo Button Box
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF3F4F8))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
                        .clickable { showAddPhotoDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Photo",
                            tint = Color(0xFF7C4DFF),
                            modifier = Modifier.size(28.dp)
                        )
                        Text("Upload", fontSize = 10.sp, color = Color(0xFF7C4DFF), fontWeight = FontWeight.Bold)
                    }
                }

                // Uploaded photo thumbnails with delete badge
                userProfile.photos.forEachIndexed { index, photoUri ->
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        if (photoUri.startsWith("http://") || photoUri.startsWith("https://")) {
                            AsyncImage(
                                model = photoUri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.img_user_avatar),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Delete overlay button
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                                .clickable { onRemovePhoto(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = "Tap '+ Upload' to add new photos to your profile gallery.",
                fontSize = 12.sp,
                color = Color(0xFFBDBDBD),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Fields Card Section
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                // NickName Item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("NickName", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F1D2B))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = nickname,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F1D2B),
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFF0F0F0))
                )

                // BirthDay Item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { birthday = "1998-05-15" }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("BirthDay", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F1D2B))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = birthday,
                            fontSize = 14.sp,
                            color = Color(0xFF757575)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFF0F0F0))
                )

                // Constellation Item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { constellation = "Taurus" }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Constellation", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F1D2B))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = constellation,
                            fontSize = 14.sp,
                            color = Color(0xFF757575)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Add Bio Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Add bio",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1D2B)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Bio Input Card Box
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8FC)),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = Color(0xFF616161),
                            modifier = Modifier.size(20.dp)
                        )

                        OutlinedTextField(
                            value = bio,
                            onValueChange = { if (it.length <= 280) bio = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 14.sp,
                                color = Color(0xFF212121),
                                textAlign = TextAlign.End
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp)
                        )
                    }

                    Text(
                        text = "${bio.length}/280",
                        fontSize = 12.sp,
                        color = Color(0xFFBDBDBD),
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (showAddPhotoDialog) {
        UploadPhotoDialog(
            onDismiss = { showAddPhotoDialog = false },
            onSetAsDp = { url ->
                onUpdateAvatarUrl(url)
                showAddPhotoDialog = false
            },
            onAddToGallery = { url ->
                onAddPhoto(url)
                showAddPhotoDialog = false
            }
        )
    }
}
