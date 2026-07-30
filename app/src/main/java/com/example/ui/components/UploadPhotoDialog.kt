package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.YaraanPinkPrimary
import com.example.utils.ImgBbUploader
import kotlinx.coroutines.launch

@Composable
fun UploadPhotoDialog(
    onDismiss: () -> Unit,
    onSetAsDp: (String) -> Unit,
    onAddToGallery: (String) -> Unit,
    titleText: String = "Upload & Change Profile Picture"
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: ImgBB / Link, 1: HD Avatars
    var imageUrlInput by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }
    var uploadStatusMessage by remember { mutableStateOf<String?>(null) }
    var selectedPresetUrl by remember { mutableStateOf<String?>(ImgBbUploader.REALISTIC_AVATARS.first()) }

    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1C2A),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = YaraanPinkPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = titleText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF2B283B),
                    contentColor = YaraanPinkPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = YaraanPinkPrimary
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("ImgBB / URL", color = if (selectedTab == 0) YaraanPinkPrimary else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("HD Realistic Avatars", color = if (selectedTab == 1) YaraanPinkPrimary else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedTab == 0) {
                    // TAB 0: ImgBB API / Custom Direct Link
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ImgBB API Upload Engine (Key: 6d207e02...)",
                            fontSize = 12.sp,
                            color = YaraanPinkPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = imageUrlInput,
                            onValueChange = { imageUrlInput = it },
                            label = { Text("Paste Image URL or ImgBB link", color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.Link, contentDescription = null, tint = YaraanPinkPrimary) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = YaraanPinkPrimary,
                                unfocusedBorderColor = Color(0xFF444455)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Preview box if URL entered
                        if (imageUrlInput.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, YaraanPinkPrimary, CircleShape)
                            ) {
                                AsyncImage(
                                    model = imageUrlInput,
                                    contentDescription = "Preview",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        if (isUploading) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = YaraanPinkPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Uploading image to ImgBB server...", color = Color.White, fontSize = 12.sp)
                            }
                        }

                        uploadStatusMessage?.let { status ->
                            Text(
                                text = status,
                                color = if (status.startsWith("Error")) Color.Red else Color(0xFF4CAF50),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                } else {
                    // TAB 1: Preset Realistic Avatars (Unsplash HD)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Select a realistic high-definition avatar:",
                            fontSize = 12.sp,
                            color = Color(0xFFBDBDBD)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.height(180.dp)
                        ) {
                            items(ImgBbUploader.REALISTIC_AVATARS) { avatarUrl ->
                                val isSelected = selectedPresetUrl == avatarUrl
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) YaraanPinkPrimary else Color(0xFF444455),
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            selectedPresetUrl = avatarUrl
                                            imageUrlInput = avatarUrl
                                        }
                                ) {
                                    AsyncImage(
                                        model = avatarUrl,
                                        contentDescription = "HD Avatar",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons: Set as Profile DP OR Add to Gallery
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val targetUrl = if (selectedTab == 0) imageUrlInput.ifBlank { selectedPresetUrl } else selectedPresetUrl
                            if (!targetUrl.isNullOrBlank()) {
                                isUploading = true
                                coroutineScope.launch {
                                    val result = ImgBbUploader.uploadToImgBb(targetUrl)
                                    isUploading = false
                                    result.onSuccess { uploadedUrl ->
                                        onSetAsDp(uploadedUrl)
                                        onDismiss()
                                    }.onFailure { err ->
                                        // Fallback directly to URL if error
                                        onSetAsDp(targetUrl)
                                        onDismiss()
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = YaraanPinkPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Set as Main Profile Picture (DP)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val targetUrl = if (selectedTab == 0) imageUrlInput.ifBlank { selectedPresetUrl } else selectedPresetUrl
                            if (!targetUrl.isNullOrBlank()) {
                                isUploading = true
                                coroutineScope.launch {
                                    val result = ImgBbUploader.uploadToImgBb(targetUrl)
                                    isUploading = false
                                    result.onSuccess { uploadedUrl ->
                                        onAddToGallery(uploadedUrl)
                                        onDismiss()
                                    }.onFailure { err ->
                                        onAddToGallery(targetUrl)
                                        onDismiss()
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C4DFF)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add to Profile Gallery", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}
