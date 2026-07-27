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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.activity.compose.BackHandler
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
import com.example.ui.components.AvatarFrame
import com.example.ui.theme.YaraanPinkPrimary

data class StoreItem(
    val id: String,
    val name: String,
    val category: String, // "Frame" or "Ride"
    val price: Int,
    val iconRes: Int
)

@Composable
fun StoreScreen(
    userCoins: Int,
    onBuyItem: (StoreItem) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)
    var selectedCategory by remember { mutableStateOf("Frame") }

    val itemsList = if (selectedCategory == "Frame") {
        listOf(
            StoreItem("f1", "LV.70 Wings Frame", "Frame", 1200, R.drawable.img_user_avatar),
            StoreItem("f2", "SVIP Royal Ring", "Frame", 2500, R.drawable.ic_yaraan_mascot),
            StoreItem("f3", "Phoenix Flame Frame", "Frame", 3800, R.drawable.img_user_avatar),
            StoreItem("f4", "Galaxy Crystal Frame", "Frame", 5000, R.drawable.ic_yaraan_mascot)
        )
    } else {
        listOf(
            StoreItem("r1", "Cyber Sports Car", "Ride", 8000, R.drawable.img_user_avatar),
            StoreItem("r2", "Golden Pegasus", "Ride", 15000, R.drawable.ic_yaraan_mascot),
            StoreItem("r3", "Space Rocket Ride", "Ride", 25000, R.drawable.img_user_avatar)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FE))
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("Prop Store", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF8E1))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(imageVector = Icons.Filled.MonetizationOn, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(18.dp))
                Text(" $userCoins", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F1D2B))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Toggle Tabs
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CategoryTab("Avatar Frames", selectedCategory == "Frame") { selectedCategory = "Frame" }
            CategoryTab("Car & Rides", selectedCategory == "Ride") { selectedCategory = "Ride" }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(itemsList) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (item.category == "Frame") {
                            AvatarFrame(avatarRes = item.iconRes, size = 64.dp, showDesignerFrame = true)
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE1F5FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Filled.DirectionsCar, contentDescription = null, tint = Color(0xFF0288D1), modifier = Modifier.size(32.dp))
                            }
                        }

                        Text(
                            text = item.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F1D2B),
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.MonetizationOn, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                            Text(" ${item.price}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { onBuyItem(item) },
                            colors = ButtonDefaults.buttonColors(containerColor = YaraanPinkPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Buy Frame", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryTab(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) YaraanPinkPrimary else Color.White)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.White else Color(0xFF757575),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
