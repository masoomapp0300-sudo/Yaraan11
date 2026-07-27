package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import com.example.R
import com.example.ui.components.AvatarFrame
import com.example.ui.components.YaraanBottomNav

data class ChatConversation(
    val id: String,
    val name: String,
    val lastMsg: String,
    val time: String,
    val unreadCount: Int,
    val isOfficial: Boolean = false
)

@Composable
fun MessagesScreen(
    bottomNavIndex: Int,
    onBottomTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = { onBottomTabSelected(0) })
    val conversations = listOf(
        ChatConversation("c1", "Yaraan Official Team", "🎉 Welcome to Yaraan! Check out SVIP rewards.", "12:30 PM", 1, isOfficial = true),
        ChatConversation("c2", "Sahil ❤", "Salam Mašoom! Joining your voice room now! 💕", "11:45 AM", 2),
        ChatConversation("c3", "Ayesha", "Thanks for the CP heart gift! ✨", "Yesterday", 0),
        ChatConversation("c4", "Zain_Pro", "Ready for the Ludo tournament tonight?", "Yesterday", 0)
    )

    Scaffold(
        bottomBar = {
            YaraanBottomNav(
                selectedIndex = bottomNavIndex,
                onTabSelected = onBottomTabSelected
            )
        },
        containerColor = Color(0xFFF8F9FE),
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Text(
                text = "Messages",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1D2B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(conversations) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (item.isOfficial) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF3377)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Notifications,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            } else {
                                AvatarFrame(size = 52.dp, showDesignerFrame = false)
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F1D2B)
                                    )
                                    Text(
                                        text = item.time,
                                        fontSize = 11.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.lastMsg,
                                        fontSize = 13.sp,
                                        color = Color(0xFF757575),
                                        maxLines = 1,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (item.unreadCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .padding(start = 8.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFF1744))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "${item.unreadCount}",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
