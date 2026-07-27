package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VoiceRoom
import com.example.ui.components.AvatarFrame
import com.example.ui.components.LevelBadge
import com.example.ui.components.YaraanAssetImage
import com.example.ui.theme.YaraanPinkPrimary
import com.example.ui.viewmodel.RoomChatMessage

data class RoomSeat(
    val id: Int,
    val name: String,
    val avatarRes: Int?,
    val isHost: Boolean = false,
    val isSpeaking: Boolean = false
)

@Composable
fun VoiceRoomScreen(
    room: VoiceRoom,
    messages: List<RoomChatMessage>,
    isMicMuted: Boolean,
    isSpeakerOn: Boolean,
    onToggleMic: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onSendMessage: (String) -> Unit,
    onMinimizeRoom: () -> Unit,
    onLeaveRoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onMinimizeRoom)
    var messageText by remember { mutableStateOf("") }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "VoiceRoomPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // 8 Sub-Seats
    val seats = listOf(
        RoomSeat(1, "Ayesha", R.drawable.img_user_avatar, isSpeaking = false),
        RoomSeat(2, "Sahil ❤", R.drawable.ic_yaraan_mascot, isSpeaking = true),
        RoomSeat(3, "Zain_Pro", R.drawable.img_user_avatar, isSpeaking = false),
        RoomSeat(4, "Zara_Voice", R.drawable.img_user_avatar, isSpeaking = false),
        RoomSeat(5, "Rohan", null),
        RoomSeat(6, "Anya", null),
        RoomSeat(7, "Empty", null),
        RoomSeat(8, "Empty", null)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1B0B38), Color(0xFF2C1052), Color(0xFF0F0520))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Top Header Action Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onMinimizeRoom) {
                        YaraanAssetImage(
                            assetName = "minimize.png",
                            contentDescription = "Minimize Room",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(modifier = Modifier.padding(start = 6.dp)) {
                        Text(
                            text = room.title,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ID: ${room.id} • 🔥 ${room.listenersCount} Online",
                            color = Color(0xFFFFD54F),
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Setting Button
                    IconButton(onClick = { showSettingsDialog = true }) {
                        YaraanAssetImage(
                            assetName = "setting_room_icon.svg",
                            contentDescription = "Settings",
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    // Exit Room Button
                    IconButton(onClick = onLeaveRoom) {
                        YaraanAssetImage(
                            assetName = "exit.png",
                            contentDescription = "Exit Room",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // TOP OWNER MIC STAGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .border(1.dp, Color(0xFFFFD54F).copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "👑 OWNER STAGE",
                        color = Color(0xFFFFD54F),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Box(contentAlignment = Alignment.Center) {
                        // Sound Wave Wibe asset
                        YaraanAssetImage(
                            assetName = "svip1_sound_wibe.svg",
                            contentDescription = "Sound Wave",
                            modifier = Modifier
                                .size(96.dp)
                                .graphicsLayer(scaleX = pulseScale, scaleY = pulseScale)
                        )

                        // Owner Avatar with Owner Frame
                        AvatarFrame(
                            avatarRes = room.hostAvatarRes,
                            size = 68.dp,
                            showDesignerFrame = true,
                            frameAsset = "owner_frame.svg"
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        LevelBadge(level = 99)
                        Text(
                            text = " ${room.hostName}",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 8 SEATS GRID
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                items(seats) { seat ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(contentAlignment = Alignment.Center) {
                            if (seat.avatarRes != null) {
                                AvatarFrame(
                                    avatarRes = seat.avatarRes,
                                    size = 48.dp,
                                    showDesignerFrame = false,
                                    frameAsset = "svip1_frame.svg"
                                )
                                if (seat.isSpeaking) {
                                    YaraanAssetImage(
                                        assetName = "microphone_gold.svg",
                                        contentDescription = "Mic Speaking",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .align(Alignment.BottomEnd)
                                    )
                                }
                            } else {
                                // Styled Chair Graphic for Empty Seats
                                YaraanAssetImage(
                                    assetName = "styled_chair.svg",
                                    contentDescription = "Empty Seat",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clickable { /* Take Seat */ }
                                )
                            }
                        }

                        Text(
                            text = seat.name,
                            color = if (seat.avatarRes != null) Color.White else Color.White.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chat Message Stream Box
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.45f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(messages) { msg ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (msg.isSystem) Color(0xFFFFD54F).copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${msg.senderName}: ",
                                color = if (msg.isSystem) Color(0xFFFFD54F) else Color(0xFFFF80AB),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = msg.message,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Input Bar & Gold Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Send chat...", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
                        focusedContainerColor = Color.White.copy(alpha = 0.25f),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = YaraanPinkPrimary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        onSendMessage(messageText)
                        messageText = ""
                    },
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color(0xFFFFD54F))
                }

                // Speaker Toggle Button
                Box(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { onToggleSpeaker() },
                    contentAlignment = Alignment.Center
                ) {
                    YaraanAssetImage(
                        assetName = if (isSpeakerOn) "speaker_room_icon.svg" else "speaker_muted_gold.svg",
                        contentDescription = "Speaker Toggle",
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Mic Toggle Button
                Box(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isMicMuted) Color(0xFFFF1744) else Color(0xFF00E676))
                        .clickable { onToggleMic() },
                    contentAlignment = Alignment.Center
                ) {
                    YaraanAssetImage(
                        assetName = if (isMicMuted) "microphone_muted_gold.svg" else "microphone_gold.svg",
                        contentDescription = "Mic Toggle",
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Emoji / Game Button
                Box(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { /* Room Games */ },
                    contentAlignment = Alignment.Center
                ) {
                    YaraanAssetImage(
                        assetName = "emoji_room_icon.svg",
                        contentDescription = "Emoji Games",
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Gift Box Button
                Box(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFFF4081), Color(0xFFFFB300))
                            )
                        )
                        .clickable { /* Send Gift */ },
                    contentAlignment = Alignment.Center
                ) {
                    YaraanAssetImage(
                        assetName = "gift_room_icon.svg",
                        contentDescription = "Gift",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }

    // Room Settings Modal Dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = {
                Text("⚙ Room Settings", fontWeight = FontWeight.Bold, color = Color(0xFF1F1D2B))
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("• Room Lock: Unlocked 🔓")
                    Text("• Background Theme: Royal SVIP Gold")
                    Text("• Announcement: Welcome to Yaraan Official Voice Party!")
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("OK", color = YaraanPinkPrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
