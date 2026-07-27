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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import com.example.R
import com.example.ui.components.YaraanBottomNav

data class GameHubItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val startColor: Long,
    val endColor: Long
)

@Composable
fun GamesScreen(
    bottomNavIndex: Int,
    onBottomTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = { onBottomTabSelected(0) })
    val games = listOf(
        GameHubItem("Ludo Star Party", "Play live in voice rooms", Icons.Filled.Casino, 0xFFFF3377, 0xFFFF7597),
        GameHubItem("Lucky Wheel Spin", "Win 50,000 Coins daily", Icons.Filled.Gamepad, 0xFFFFB300, 0xFFFF8F00),
        GameHubItem("CP Dating Wheel", "Match your soulmate", Icons.Filled.Favorite, 0xFF8E24AA, 0xFF7C4DFF),
        GameHubItem("Treasure Hunt", "Unlock rare frames & rides", Icons.Filled.Extension, 0xFF00E5FF, 0xFF0091EA)
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
                text = "Voice Room Games 🎮",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1D2B)
            )

            Text(
                text = "Play together with room members while audio chatting",
                fontSize = 13.sp,
                color = Color(0xFF757575),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(games) { game ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(game.startColor), Color(game.endColor))
                                )
                            )
                            .clickable { }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = game.title,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = game.subtitle,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = game.icon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
