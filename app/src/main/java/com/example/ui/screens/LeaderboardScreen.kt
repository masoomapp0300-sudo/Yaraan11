package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserProfile
import com.example.ui.components.AvatarFrame
import com.example.ui.components.LevelBadge
import com.example.ui.components.SvipBadge
import com.example.ui.components.YaraanAssetImage
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.ui.theme.YaraanPinkPrimary

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val score: Long,
    val level: Int,
    val svip: String,
    val isOfficial: Boolean = false,
    val countryFlag: String = "🇵🇰",
    val gender: String = "male"
)

@Composable
fun LeaderboardScreen(
    userProfile: UserProfile,
    initialMainTab: Int = 0,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var selectedMainTab by remember { mutableIntStateOf(initialMainTab) } // 0: Contribution, 1: Charm, 2: Room
    var selectedSubTab by remember { mutableStateOf("Daily") } // "Daily", "Weekly", "Monthly"

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = initialMainTab, pageCount = { 3 })

    LaunchedEffect(pagerState.currentPage) {
        selectedMainTab = pagerState.currentPage
    }

    LaunchedEffect(initialMainTab) {
        if (pagerState.currentPage != initialMainTab) {
            pagerState.animateScrollToPage(initialMainTab)
        }
    }

    val sampleContributionDaily = remember {
        listOf(
            LeaderboardUser(1, "❤ Mašoom", 2850400L, 70, "SVIP3", isOfficial = true),
            LeaderboardUser(2, "Sahil ❤", 1920100L, 65, "SVIP2"),
            LeaderboardUser(3, "Rohan_King", 1250800L, 58, "SVIP1"),
            LeaderboardUser(4, "Ayesha", 980500L, 52, "SVIP1", gender = "female"),
            LeaderboardUser(5, "Zain_Pro", 840200L, 48, "VIP"),
            LeaderboardUser(6, "Zara_Voice", 720100L, 45, "VIP", gender = "female"),
            LeaderboardUser(7, "Sadaf_Queen", 610000L, 40, "VIP", gender = "female"),
            LeaderboardUser(8, "Ali_Boss", 520400L, 38, "VIP"),
            LeaderboardUser(9, "Usman_PK", 430100L, 35, "VIP"),
            LeaderboardUser(10, "Fatima", 380900L, 32, "VIP", gender = "female")
        )
    }

    val sampleCharmDaily = remember {
        listOf(
            LeaderboardUser(1, "Zara_Voice", 3410900L, 68, "SVIP3", gender = "female"),
            LeaderboardUser(2, "Sadaf_Queen", 2150300L, 62, "SVIP2", gender = "female"),
            LeaderboardUser(3, "Ayesha", 1820400L, 55, "SVIP1", gender = "female"),
            LeaderboardUser(4, "❤ Mašoom", 1450200L, 70, "SVIP3", isOfficial = true),
            LeaderboardUser(5, "Sahil ❤", 1120000L, 65, "SVIP2"),
            LeaderboardUser(6, "Rohan_King", 940500L, 58, "SVIP1"),
            LeaderboardUser(7, "Fatima", 810300L, 42, "VIP", gender = "female"),
            LeaderboardUser(8, "Zain_Pro", 690100L, 48, "VIP"),
            LeaderboardUser(9, "Maryam", 580000L, 30, "VIP", gender = "female"),
            LeaderboardUser(10, "Hamza", 490200L, 28, "VIP")
        )
    }

    val sampleRoomDaily = remember {
        listOf(
            LeaderboardUser(1, "Urdu Shayari & Music Party 🎵", 5820400L, 70, "SVIP3", isOfficial = true),
            LeaderboardUser(2, "Pakistani Friends Club 🇵🇰", 4120800L, 65, "SVIP2"),
            LeaderboardUser(3, "Ludo Master Championship 🏆", 3250100L, 58, "SVIP1"),
            LeaderboardUser(4, "Brands Family Tree 🌲", 2810500L, 60, "SVIP2"),
            LeaderboardUser(5, "CP Star Live Couple 💖", 2150900L, 52, "SVIP1"),
            LeaderboardUser(6, "Late Night GupShup 🌙", 1820400L, 45, "VIP"),
            LeaderboardUser(7, "Singing Voice Stage 🎤", 1410200L, 40, "VIP"),
            LeaderboardUser(8, "Poetry & Ghazal Room 📜", 1100500L, 38, "VIP"),
            LeaderboardUser(9, "Friendship Zone 🌟", 950300L, 35, "VIP"),
            LeaderboardUser(10, "Desi Adda ☕", 820100L, 30, "VIP")
        )
    }

    val currentDataList = when (selectedMainTab) {
        0 -> sampleContributionDaily
        1 -> sampleCharmDaily
        else -> sampleRoomDaily
    }

    val scoreIcon = when (selectedMainTab) {
        0 -> "🪙"
        1 -> "💎"
        else -> "🎁"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1E113A), Color(0xFF150C28), Color(0xFF0F0C29))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp) // Space for sticky My Rank bar
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar Navigation & Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Yaraan Leaderboard 🏆",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "⏱ Resets daily at 05:00 AM PKT",
                        fontSize = 10.sp,
                        color = Color(0xFFFFD54F),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Box(modifier = Modifier.size(40.dp)) // Spacer balance
            }

            // Main Category Tabs (Contribution, Charm, Room)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.10f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MainTabItem(
                    title = "Contribution",
                    iconAsset = "contribution_rank.svg",
                    isSelected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(0) }
                    },
                    modifier = Modifier.weight(1f)
                )
                MainTabItem(
                    title = "Charm",
                    iconAsset = "charm_rank.svg",
                    isSelected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(1) }
                    },
                    modifier = Modifier.weight(1f)
                )
                MainTabItem(
                    title = "Room",
                    iconAsset = "room_rank.svg",
                    isSelected = pagerState.currentPage == 2,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(2) }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // Time Sub Tabs (Daily, Weekly, Monthly)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Daily", "Weekly", "Monthly").forEach { timeTab ->
                    val isSelected = selectedSubTab == timeTab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) YaraanPinkPrimary else Color.Transparent
                            )
                            .clickable { selectedSubTab = timeTab }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = timeTab,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFFB39DDB)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Horizontal Pager for Contribution, Charm, and Room
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val dataListForPage = when (page) {
                    0 -> sampleContributionDaily
                    1 -> sampleCharmDaily
                    else -> sampleRoomDaily
                }
                val iconForPage = when (page) {
                    0 -> "🪙"
                    1 -> "💎"
                    else -> "🎁"
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    // TOP 3 PODIUM SECTION (Rank 1 Center, Rank 2 Left, Rank 3 Right)
                    if (dataListForPage.size >= 3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(28.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color(0xFF2E175B),
                                            Color(0xFF1F103E),
                                            Color(0xFF140B2B)
                                        )
                                    )
                                )
                                .border(1.dp, Color(0xFFFFD54F).copy(alpha = 0.3f), RoundedCornerShape(28.dp))
                                .padding(vertical = 20.dp, horizontal = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                // Rank 2 (Left - Silver)
                                PodiumUserCard(
                                    user = dataListForPage[1],
                                    rank = 2,
                                    scoreIcon = iconForPage,
                                    modifier = Modifier.weight(1f)
                                )

                                // Rank 1 (Center - Gold)
                                PodiumUserCard(
                                    user = dataListForPage[0],
                                    rank = 1,
                                    scoreIcon = iconForPage,
                                    modifier = Modifier.weight(1.2f)
                                )

                                // Rank 3 (Right - Bronze)
                                PodiumUserCard(
                                    user = dataListForPage[2],
                                    rank = 3,
                                    scoreIcon = iconForPage,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // RANKED LIST (Rank 4 to End)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (i in 3 until dataListForPage.size) {
                            LeaderboardListItem(
                                user = dataListForPage[i],
                                scoreIcon = iconForPage
                            )
                        }
                    }
                }
            }
        }

        // STICKY BOTTOM BAR - MY RANKING FOOTER
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF2C1A4D), Color(0xFF170D31))
                    )
                )
                .border(width = 1.dp, color = Color(0xFFFFD54F).copy(alpha = 0.4f))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "No. 1",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFD54F),
                        modifier = Modifier.width(42.dp)
                    )

                    AvatarFrame(
                        size = 40.dp,
                        showDesignerFrame = true,
                        avatarUrl = userProfile.avatarUrl.ifBlank { null }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userProfile.nickname,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = " ${userProfile.locationFlag}",
                                fontSize = 12.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            LevelBadge(userProfile.level)
                            Spacer(modifier = Modifier.width(4.dp))
                            SvipBadge(userProfile.svipLevel)
                        }
                    }
                }

                // Score pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(1.dp, Color(0xFFFFD54F).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "2,850,400 $scoreIcon",
                        color = Color(0xFFFFD54F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun MainTabItem(
    title: String,
    iconAsset: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) Brush.horizontalGradient(
                    listOf(YaraanPinkPrimary, Color(0xFF7C4DFF))
                ) else Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            YaraanAssetImage(
                assetName = iconAsset,
                contentDescription = title,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFFB39DDB),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PodiumUserCard(
    user: LeaderboardUser,
    rank: Int, // 1 = Gold, 2 = Silver, 3 = Bronze
    scoreIcon: String,
    modifier: Modifier = Modifier
) {
    val crownColor = when (rank) {
        1 -> Color(0xFFFFD54F) // Gold
        2 -> Color(0xFFE0E0E0) // Silver
        else -> Color(0xFFFF8A65) // Bronze
    }

    val leaderboardSvg = when (rank) {
        1 -> "leaderboard_1.svg"
        2 -> "leaderboard_2.svg"
        else -> "leaderboard_3.svg"
    }

    val frameSize = if (rank == 1) 136.dp else 112.dp
    val avatarSize = if (rank == 1) 54.dp else 44.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large SVG Podium Frame with DP Avatar perfectly centered inside
        Box(
            modifier = Modifier.size(frameSize),
            contentAlignment = Alignment.Center
        ) {
            // User Avatar image centered inside frame
            YaraanAssetImage(
                assetName = "yaraan_dp.png",
                contentDescription = user.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
            )

            // Overlaid Leaderboard SVG Frame (Rank 1, 2, 3 SVG)
            YaraanAssetImage(
                assetName = leaderboardSvg,
                contentDescription = "Rank $rank Frame",
                modifier = Modifier.size(frameSize),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Name
        Text(
            text = user.name,
            color = if (rank == 1) Color(0xFFFFD54F) else Color.White,
            fontSize = if (rank == 1) 12.sp else 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Level / SVIP badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            LevelBadge(user.level)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Score Pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .border(1.dp, crownColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = "${user.score / 1000}k $scoreIcon",
                color = crownColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LeaderboardListItem(
    user: LeaderboardUser,
    scoreIcon: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Rank position
                Text(
                    text = "${user.rank}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFB39DDB),
                    modifier = Modifier.width(28.dp)
                )

                AvatarFrame(size = 42.dp, showDesignerFrame = true)

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = " ${user.countryFlag}",
                            fontSize = 11.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        LevelBadge(user.level)
                        Spacer(modifier = Modifier.width(4.dp))
                        SvipBadge(user.svip)
                    }
                }
            }

            // Score Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "${user.score} $scoreIcon",
                    color = Color(0xFFFFD54F),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
