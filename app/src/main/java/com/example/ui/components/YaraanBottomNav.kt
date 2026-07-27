package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CompassCalibration
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import com.example.R
import com.example.ui.theme.YaraanPinkPrimary

@Composable
fun YaraanBottomNav(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shadowElevation = 16.dp,
        color = Color.White,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 0: Explore / Party
            BottomNavItem(
                isSelected = selectedIndex == 0,
                onClick = { onTabSelected(0) },
                icon = {
                    YaraanAssetImage(
                        assetName = if (selectedIndex == 0) "home_icon_shine.svg" else "home_icon_simple.svg",
                        contentDescription = "Explore",
                        modifier = Modifier.size(28.dp)
                    )
                }
            )

            // Tab 1: Games / Rooms
            BottomNavItem(
                isSelected = selectedIndex == 1,
                onClick = { onTabSelected(1) },
                icon = {
                    YaraanAssetImage(
                        assetName = if (selectedIndex == 1) "moment_icon_shine.svg" else "moment_icon_simple.svg",
                        contentDescription = "Games",
                        modifier = Modifier.size(28.dp)
                    )
                }
            )

            // Tab 2: Messages
            BottomNavItem(
                isSelected = selectedIndex == 2,
                onClick = { onTabSelected(2) },
                icon = {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = Color(0xFFFF1744),
                                contentColor = Color.White
                            ) {
                                Text("1", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    ) {
                        YaraanAssetImage(
                            assetName = if (selectedIndex == 2) "inbox_icon_shine.svg" else "inbox_icon_simple.svg",
                            contentDescription = "Messages",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )

            // Tab 3: Profile
            BottomNavItem(
                isSelected = selectedIndex == 3,
                onClick = { onTabSelected(3) },
                icon = {
                    BadgedBox(
                        badge = {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF1744))
                            )
                        }
                    ) {
                        YaraanAssetImage(
                            assetName = if (selectedIndex == 3) "me_icon_shine.svg" else "me_icon_simple.svg",
                            contentDescription = "Profile",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        icon()
        if (isSelected) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(width = 16.dp, height = 3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(YaraanPinkPrimary)
            )
        }
    }
}
