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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.YaraanAssetImage
import com.example.R
import com.example.data.VoiceRoom
import com.example.ui.components.AvatarFrame
import com.example.ui.components.YaraanBottomNav
import com.example.ui.theme.YaraanPinkPrimary

@Composable
fun HomeScreen(
    selectedTopTab: String,
    onTopTabSelected: (String) -> Unit,
    bottomNavIndex: Int,
    onBottomTabSelected: (Int) -> Unit,
    onJoinRoom: (VoiceRoom) -> Unit,
    onOpenCreateRoom: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onToggleSearch: (Boolean) -> Unit,
    onOpenLeaderboard: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isGridView by remember { mutableStateOf(true) }
    var showCreateRoomModal by remember { mutableStateOf(false) }
    var showCpSystemDialog by remember { mutableStateOf(false) }
    var newRoomTitle by remember { mutableStateOf("❤ Mašoom's Royal Lounge") }
    var selectedTag by remember { mutableStateOf("Music 🎤") }

    val coroutineScope = rememberCoroutineScope()
    val topTabs = listOf("Following", "Popular", "Recent")
    val initialPageIndex = topTabs.indexOf(selectedTopTab).coerceAtLeast(0)
    val pagerState = rememberPagerState(initialPage = initialPageIndex, pageCount = { 3 })

    // Sync selectedTopTab with pager state
    LaunchedEffect(pagerState.currentPage) {
        val currentTab = topTabs[pagerState.currentPage]
        if (currentTab != selectedTopTab) {
            onTopTabSelected(currentTab)
        }
    }

    // Sync pagerState when selectedTopTab is set externally
    LaunchedEffect(selectedTopTab) {
        val targetIndex = topTabs.indexOf(selectedTopTab).coerceAtLeast(0)
        if (pagerState.currentPage != targetIndex) {
            pagerState.animateScrollToPage(targetIndex)
        }
    }

    val gridRooms = remember {
        listOf(
            VoiceRoom(
                id = "grid_1",
                title = "Urdu Shayari & Music Party 🎵",
                hostName = "❤ Mašoom",
                hostGender = "male",
                hostAge = 24,
                listenersCount = 2890,
                tag = "Poetry"
            ),
            VoiceRoom(
                id = "grid_2",
                title = "Pakistani Friends Club 🇵🇰",
                hostName = "Ayesha",
                hostGender = "female",
                hostAge = 22,
                listenersCount = 3410,
                tag = "Friends"
            ),
            VoiceRoom(
                id = "grid_3",
                title = "Ludo Master Championship 🏆",
                hostName = "Zain_Pro",
                hostGender = "male",
                hostAge = 25,
                listenersCount = 1850,
                tag = "Gaming"
            ),
            VoiceRoom(
                id = "grid_4",
                title = "Romantic Shayari & CP 💖",
                hostName = "Sadaf_Queen",
                hostGender = "female",
                hostAge = 21,
                listenersCount = 4200,
                tag = "CP Party"
            ),
            VoiceRoom(
                id = "grid_5",
                title = "Late Night GupShup 🌙",
                hostName = "Rohan_King",
                hostGender = "male",
                hostAge = 26,
                listenersCount = 1290,
                tag = "Chitchat"
            ),
            VoiceRoom(
                id = "grid_6",
                title = "Singing Voice Stage 🎤",
                hostName = "Zara_Voice",
                hostGender = "female",
                hostAge = 23,
                listenersCount = 5120,
                tag = "Music"
            )
        )
    }

    Scaffold(
        bottomBar = {
            YaraanBottomNav(
                selectedIndex = bottomNavIndex,
                onTabSelected = onBottomTabSelected
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateRoomModal = true },
                containerColor = Color(0xFFFFB74D),
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "Create Voice Room",
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        containerColor = Color(0xFFF7F8FC),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Header Tabs + Search
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopTabItem("Following", pagerState.currentPage == 0) {
                        coroutineScope.launch { pagerState.animateScrollToPage(0) }
                    }
                    TopTabItem("Popular", pagerState.currentPage == 1) {
                        coroutineScope.launch { pagerState.animateScrollToPage(1) }
                    }
                    TopTabItem("Recent", pagerState.currentPage == 2) {
                        coroutineScope.launch { pagerState.animateScrollToPage(2) }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Room Creation Button Icon
                    IconButton(onClick = { showCreateRoomModal = true }) {
                        YaraanAssetImage(
                            assetName = "room_create_icon.svg",
                            contentDescription = "Create Room",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    IconButton(
                        onClick = { onToggleSearch(!isSearchActive) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF2D2B3A),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Search Bar (Animated visibility if search toggled)
            if (isSearchActive) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search voice rooms, user ID, or tags...", fontSize = 14.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = YaraanPinkPrimary
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }

            // Horizontal Paging for Mine, Popular, and Recent Tabs
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        // Mine Tab (Followed Rooms & My Voice Lounge)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "⭐ My Followed & Favorite Rooms",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F1D2B)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Favorite Voice Room Item
                            val myFavoriteRoom = gridRooms[0].copy(
                                title = "❤ Mašoom's Royal Lounge",
                                hostName = "❤ Mašoom (You)",
                                listenersCount = 3820,
                                tag = "Mine"
                            )
                            VoiceRoomItemCard(
                                title = myFavoriteRoom.title,
                                hostName = myFavoriteRoom.hostName,
                                listeners = "🔥 ${myFavoriteRoom.listenersCount} online",
                                tags = listOf("My Lounge", "Poetry 🎵"),
                                onClick = { onJoinRoom(myFavoriteRoom) }
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "📌 My Joined Voice Parties",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F1D2B)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            gridRooms.take(2).forEach { room ->
                                VoiceRoomItemCard(
                                    title = room.title,
                                    hostName = room.hostName,
                                    listeners = "${room.listenersCount} online",
                                    tags = listOf(room.tag, "Joined"),
                                    onClick = { onJoinRoom(room) }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }

                    1 -> {
                        // Popular Tab (Main Dashboard)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = 16.dp)
                        ) {
                            // Top Auto-Swiping Banner Carousel System
                            BannerCarousel()

                            // 3 Top Leaderboard Cards (Contribution, Charm, Room) - Adaptive Equal Width
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RankingBannerCard(
                                    title = "Contribution\nRanking",
                                    badgeAsset = "contribution_rank.svg",
                                    topRightAsset = "coin_icon.png",
                                    startColor = Color(0xFFFFA000),
                                    endColor = Color(0xFFFF6F00),
                                    onClick = { onOpenLeaderboard(0) },
                                    modifier = Modifier.weight(1f)
                                )
                                RankingBannerCard(
                                    title = "Charm\nRanking",
                                    badgeAsset = "charm_rank.svg",
                                    topRightAsset = "diamond.png",
                                    startColor = Color(0xFFAB47BC),
                                    endColor = Color(0xFF6A1B9A),
                                    onClick = { onOpenLeaderboard(1) },
                                    modifier = Modifier.weight(1f)
                                )
                                RankingBannerCard(
                                    title = "Room\nRanking",
                                    badgeAsset = "room_rank.svg",
                                    topRightAsset = "gift_room_icon.svg",
                                    startColor = Color(0xFFEC407A),
                                    endColor = Color(0xFFAD1457),
                                    onClick = { onOpenLeaderboard(2) },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            // Section Header: 🔥 The brightest social star
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = "🔥",
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = "The brightest social star",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F1D2B)
                                )
                            }

                            // Cards Grid: CP Star and Family Tree
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // CP Star Card (Left)
                                CpStarCard(
                                    room = gridRooms[0].copy(
                                        title = "CP Star",
                                        isCPStar = true,
                                        coupleName1 = "Noo...",
                                        coupleName2 = "Ama..."
                                    ),
                                    onClick = { showCpSystemDialog = true },
                                    modifier = Modifier.weight(1f)
                                )

                                // Family Tree Card (Right)
                                FamilyTreeCard(
                                    room = gridRooms[1].copy(
                                        title = "Brands Family",
                                        isTreeRoom = true
                                    ),
                                    onClick = { onJoinRoom(gridRooms[1]) },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Voice Rooms Grid / List Section
                            Column(
                                modifier = Modifier.padding(horizontal = 20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "🎙 Popular Audio Rooms",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F1D2B)
                                    )

                                    // Toggle Grid / List icon
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFEEEEEE))
                                            .padding(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isGridView) YaraanPinkPrimary else Color.Transparent)
                                                .clickable { isGridView = true }
                                                .padding(6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.GridView,
                                                contentDescription = "Grid View",
                                                tint = if (isGridView) Color.White else Color.Gray,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (!isGridView) YaraanPinkPrimary else Color.Transparent)
                                                .clickable { isGridView = false }
                                                .padding(6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ViewList,
                                                contentDescription = "List View",
                                                tint = if (!isGridView) Color.White else Color.Gray,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                if (isGridView) {
                                    // Room Style Grid Layout (2 columns) displaying Room Name, User Name, Gender Icon
                                    val rows = (gridRooms.size + 1) / 2
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        for (rowIndex in 0 until rows) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                for (colIndex in 0 until 2) {
                                                    val roomIdx = rowIndex * 2 + colIndex
                                                    if (roomIdx < gridRooms.size) {
                                                        val room = gridRooms[roomIdx]
                                                        VoiceRoomGridCard(
                                                            room = room,
                                                            onClick = { onJoinRoom(room) },
                                                            modifier = Modifier.weight(1f)
                                                        )
                                                    } else {
                                                        Spacer(modifier = Modifier.weight(1f))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // Standard List Layout
                                    gridRooms.forEach { room ->
                                        VoiceRoomItemCard(
                                            title = room.title,
                                            hostName = room.hostName,
                                            listeners = "${room.listenersCount} online",
                                            tags = listOf(room.tag, if (room.hostGender == "male") "Male ♂" else "Female ♀"),
                                            onClick = { onJoinRoom(room) }
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Recent Tab (Recently Created & Active Voice Rooms)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "⚡ Recently Active Voice Stages",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F1D2B)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            gridRooms.reversed().forEach { room ->
                                VoiceRoomItemCard(
                                    title = room.title,
                                    hostName = room.hostName,
                                    listeners = "⚡ ${room.listenersCount} active",
                                    tags = listOf(room.tag, "Live Now"),
                                    onClick = { onJoinRoom(room) }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }

        // Room Creation Modal Dialog
        if (showCreateRoomModal) {
            AlertDialog(
                onDismissRequest = { showCreateRoomModal = false },
                title = {
                    Text("🎙 Create Voice Room", fontWeight = FontWeight.Bold, color = Color(0xFF1F1D2B))
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Enter your custom Voice Room details:", fontSize = 13.sp, color = Color.Gray)

                        OutlinedTextField(
                            value = newRoomTitle,
                            onValueChange = { newRoomTitle = it },
                            label = { Text("Room Name") },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text("Select Room Category:", fontSize = 12.sp, fontWeight = FontWeight.Bold)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Music 🎤", "CP Party 💖", "Chitchat 💬").forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (selectedTag == tag) YaraanPinkPrimary else Color(0xFFF0F0F0))
                                        .clickable { selectedTag = tag }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        color = if (selectedTag == tag) Color.White else Color.Black,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showCreateRoomModal = false
                            val customRoom = VoiceRoom(
                                id = "my_custom_room_${System.currentTimeMillis()}",
                                title = newRoomTitle.ifBlank { "❤ My Royal Voice Lounge" },
                                hostName = "❤ Mašoom (You)",
                                hostAvatarRes = R.drawable.img_user_avatar,
                                listenersCount = 1,
                                tag = selectedTag
                            )
                            onJoinRoom(customRoom)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = YaraanPinkPrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("🚀 Create & Enter", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateRoomModal = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }

        // CP System Modal Dialog
        if (showCpSystemDialog) {
            CpSystemDialog(
                onDismiss = { showCpSystemDialog = false },
                onJoinRoom = onJoinRoom,
                room = gridRooms[0]
            )
        }
    }
}

@Composable
private fun TopTabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        Text(
            text = title,
            fontSize = if (isSelected) 22.sp else 17.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color(0xFF1F1D2B) else Color(0xFF8D8B9B)
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(width = 18.dp, height = 3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF1F1D2B))
            )
        }
    }
}

@Composable
private fun RankingBannerCard(
    title: String,
    badgeAsset: String,
    topRightAsset: String,
    startColor: Color,
    endColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(90.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(listOf(startColor, endColor)))
            .clickable { onClick() }
    ) {
        // Original SVG Card Design Background
        YaraanAssetImage(
            assetName = badgeAsset,
            contentDescription = title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Centered 3 Top User DPs (Rank 2 Silver, Rank 1 Gold Center, Rank 3 Bronze)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Centered 3 DPs Podium Group
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rank 2 (Left Silver)
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0xFFE0E0E0), CircleShape)
                    ) {
                        YaraanAssetImage(
                            assetName = "yaraan_dp.png",
                            contentDescription = "Rank 2 DP",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Rank 1 (Center Gold - Slightly bigger)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color(0xFFFFD54F), CircleShape)
                    ) {
                        YaraanAssetImage(
                            assetName = "yaraan_dp.png",
                            contentDescription = "Rank 1 DP",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Rank 3 (Right Bronze)
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0xFFFF8A65), CircleShape)
                    ) {
                        YaraanAssetImage(
                            assetName = "yaraan_dp.png",
                            contentDescription = "Rank 3 DP",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Top Right Asset Icon (Coin / Diamond / Gift)
                YaraanAssetImage(
                    assetName = topRightAsset,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Bottom Text Label
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CpStarCard(
    room: VoiceRoom,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(210.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        // Background Image Asset
        YaraanAssetImage(
            assetName = "cp_bg1.png",
            contentDescription = "CP Star BG",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark gradient overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.2f), Color.Black.copy(alpha = 0.65f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header "CP Star" with CP main icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                YaraanAssetImage(
                    assetName = "cp_main.svg",
                    contentDescription = "CP Main Icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "CP Star",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFF80AB)
                )
            }

            // Two Lovers/Couple Avatars with cp_heart_final
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AvatarFrame(size = 46.dp, showDesignerFrame = true, frameAsset = "svip1_frame.svg")
                    Text(
                        text = room.coupleName1,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                YaraanAssetImage(
                    assetName = "cp_heart_final.png",
                    contentDescription = "CP Heart",
                    modifier = Modifier.size(34.dp)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AvatarFrame(size = 46.dp, showDesignerFrame = true, frameAsset = "svip2_frame.svg")
                    Text(
                        text = room.coupleName2,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Bottom indicator badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.55f))
                    .border(1.dp, Color(0xFFFF80AB).copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    YaraanAssetImage(
                        assetName = "cp_heart2.svg",
                        contentDescription = "CP Heart",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Live Couple 💖", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CpSystemDialog(
    onDismiss: () -> Unit,
    onJoinRoom: (VoiceRoom) -> Unit,
    room: VoiceRoom
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wheel_anim")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    var selectedTab by remember { mutableStateOf("ranking") } // "ranking", "rewards", "weekly"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0F081D)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background Theme Park Night Image (cp_bg1.png) - Primary Container Background
                YaraanAssetImage(
                    assetName = "cp_bg1.png",
                    contentDescription = "CP System Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark vignette overlay at bottom for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent,
                                    Color(0xFF0F081D).copy(alpha = 0.95f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) {
                    // Top Navigation Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back Button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFFE040FB), Color(0xFF8E24AA))
                                    )
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Right Action Buttons (My Rewards + Help)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFFFF4081), Color(0xFFD81B60))
                                        )
                                    )
                                    .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp))
                                    .clickable { selectedTab = "rewards" }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "My Rewards",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF42A5F5))
                                    .clickable { }
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("?", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    // Main Scrollable Body
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 24.dp)
                    ) {
                        // ================= 1. NATIVE 3D FERRIS WHEEL STAGE =================
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Rotating Ferris Wheel Outer Rim & Spoke Frame
                            YaraanAssetImage(
                                assetName = "cp_wheel_bg.svg",
                                contentDescription = "Ferris Wheel Frame",
                                modifier = Modifier
                                    .size(240.dp)
                                    .rotate(angle)
                            )

                            // Neon Glowing 3D Center Badge: "CP Star"
                            Box(
                                modifier = Modifier
                                    .size(105.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                Color(0xFFFFEE58),
                                                Color(0xFFE040FB),
                                                Color(0xFF4A148C)
                                            )
                                        )
                                    )
                                    .border(3.5.dp, Color(0xFFFFD700), CircleShape)
                                    .clickable { onJoinRoom(room); onDismiss() },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "CP",
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "Star",
                                        color = Color(0xFFFFEB3B),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }

                            // 5 Wheel Cabins (Top 1 to Top 5 Ranked Couples) with Anti-Rotation Logic
                            val cabinData = listOf(
                                Triple("1", Pair("fw-c1-u1", "fw-c1-u2"), "Noor & Aman"),
                                Triple("2", Pair("fw-c2-u1", "fw-c2-u2"), "Zara & Ali"),
                                Triple("3", Pair("fw-c3-u1", "fw-c3-u2"), "Sara & Hamza"),
                                Triple("4", Pair("fw-c4-u1", "fw-c4-u2"), "Fatima & Bilal"),
                                Triple("5", Pair("fw-c5-u1", "fw-c5-u2"), "Maham & Saad")
                            )

                            cabinData.forEachIndexed { i, cabin ->
                                val cabinAngleRad = Math.toRadians((angle + i * 72.0))
                                val radiusDp = 110.0
                                val xDp = (radiusDp * Math.cos(cabinAngleRad)).dp
                                val yDp = (radiusDp * Math.sin(cabinAngleRad)).dp

                                // Cabin Container - Positioned along circular path, always upright (anti-rotation)
                                Box(
                                    modifier = Modifier
                                        .offset(x = xDp, y = yDp)
                                        .size(80.dp, 66.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    Color(0xFFFFD700).copy(alpha = 0.95f),
                                                    Color(0xFFFF6D00).copy(alpha = 0.9f)
                                                )
                                            )
                                        )
                                        .border(2.dp, Color(0xFFFFEB3B), RoundedCornerShape(16.dp))
                                        .padding(2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        // Glass reflection top bar
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(4.dp)
                                                .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
                                        )

                                        // Dual Circular User DPs (User 1 & User 2)
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(top = 1.dp)
                                        ) {
                                            // User 1 DP (fw-c1-u1 to fw-c5-u1)
                                            AvatarFrame(
                                                size = 28.dp,
                                                showDesignerFrame = true,
                                                frameAsset = "svip${i + 1}_frame.svg"
                                            )
                                            YaraanAssetImage(
                                                assetName = "cp_heart_final.png",
                                                contentDescription = "Heart Link",
                                                modifier = Modifier
                                                    .size(14.dp)
                                                    .padding(horizontal = 1.dp)
                                            )
                                            // User 2 DP (fw-c1-u2 to fw-c5-u2)
                                            AvatarFrame(
                                                size = 28.dp,
                                                showDesignerFrame = true,
                                                frameAsset = "svip${i + 1}_frame.svg"
                                            )
                                        }

                                        // Golden Wings + Glowing Bottom Rank Badge (#1 to #5)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(
                                                    Brush.horizontalGradient(
                                                        listOf(Color(0xFFD81B60), Color(0xFF8E24AA))
                                                    )
                                                )
                                                .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(10.dp))
                                                .padding(horizontal = 6.dp, vertical = 1.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                YaraanAssetImage(
                                                    assetName = "cp_heart2.svg",
                                                    contentDescription = "Heart Badge",
                                                    modifier = Modifier.size(10.dp)
                                                )
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    text = "#${cabin.first}",
                                                    color = Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ================= 2. COUNTDOWN TIMER BAR =================
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CountDown",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            // Days
                            TimerNumberPill("06")
                            Text(" Days ", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                            // Hours
                            TimerNumberPill("08")
                            Text(" : ", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                            // Minutes
                            TimerNumberPill("50")
                            Text(" : ", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                            // Seconds
                            TimerNumberPill("47")
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // ================= 3. TAB NAVIGATION PILLS =================
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // "Ranking" Tab (ID: cp-tab-ranking)
                            CpTabPill(
                                title = "Ranking",
                                isSelected = selectedTab == "ranking",
                                onClick = { selectedTab = "ranking" },
                                modifier = Modifier.weight(1f)
                            )
                            // "🏆 Rewards" Tab (ID: cp-tab-rewards)
                            CpTabPill(
                                title = "🏆 Rewards",
                                isSelected = selectedTab == "rewards",
                                onClick = { selectedTab = "rewards" },
                                modifier = Modifier.weight(1.3f)
                            )
                            // "🎁 Weekly" Tab (ID: cp-tab-weekly)
                            CpTabPill(
                                title = "🎁 Weekly",
                                isSelected = selectedTab == "weekly",
                                onClick = { selectedTab = "weekly" },
                                modifier = Modifier.weight(1.2f)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // ================= 4. LEADERBOARD RANKING LIST CONTAINER =================
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp)
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp, bottomStart = 20.dp, bottomEnd = 20.dp))
                                .background(Color(0xFF150A21).copy(alpha = 0.9f))
                                .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp, bottomStart = 20.dp, bottomEnd = 20.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                // Header Bar using weekly_ranking_bar.svg background
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(22.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    YaraanAssetImage(
                                        assetName = "weekly_ranking_bar.svg",
                                        contentDescription = "Weekly Ranking Bar",
                                        contentScale = ContentScale.FillBounds,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Text(
                                        text = when (selectedTab) {
                                            "rewards" -> "🏆 Ranking Rewards"
                                            "weekly" -> "🎁 Weekly Reward"
                                            else -> "This Week Ranking"
                                        },
                                        color = Color(0xFFFFD700),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Content based on selected tab
                                when (selectedTab) {
                                    "ranking" -> {
                                        // Dynamic Ranked Couples List
                                        val rankings = listOf(
                                            Triple("Masoom & Sahil", "6 ❤️", "svip1_frame.svg"),
                                            Triple("Noor & Aman", "999,990 ❤️", "svip2_frame.svg"),
                                            Triple("Zara & Ali", "780,500 ❤️", "svip3_frame.svg"),
                                            Triple("Sara & Hamza", "540,200 ❤️", "svip4_frame.svg"),
                                            Triple("Fatima & Bilal", "320,100 ❤️", "svip5_frame.svg")
                                        )

                                        rankings.forEachIndexed { idx, item ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                                    .clip(RoundedCornerShape(16.dp))
                                                    .background(
                                                        Brush.horizontalGradient(
                                                            listOf(
                                                                Color(0xFF2A1B3C),
                                                                Color(0xFF3B1A60)
                                                            )
                                                        )
                                                    )
                                                    .border(1.dp, Color(0xFFFF80AB).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    // Rank Badge
                                                    Text(
                                                        text = "TOP ${idx + 1}",
                                                        color = Color(0xFFFFD700),
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Black,
                                                        modifier = Modifier.width(48.dp)
                                                    )

                                                    // Dual Avatars
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        AvatarFrame(size = 32.dp, showDesignerFrame = true, frameAsset = item.third)
                                                        YaraanAssetImage(
                                                            assetName = "cp_heart_final.png",
                                                            contentDescription = "Heart Link",
                                                            modifier = Modifier
                                                                .size(16.dp)
                                                                .padding(horizontal = 2.dp)
                                                        )
                                                        AvatarFrame(size = 32.dp, showDesignerFrame = true, frameAsset = item.third)
                                                    }

                                                    Spacer(modifier = Modifier.width(10.dp))

                                                    Text(
                                                        text = item.first,
                                                        color = Color.White,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }

                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    YaraanAssetImage(
                                                        assetName = "cp_heart2.svg",
                                                        contentDescription = "Heart Score",
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = item.second,
                                                        color = Color(0xFFFF80AB),
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Black
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    "rewards" -> {
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            RewardCard(
                                                title = "🏆 TOP 1",
                                                coins = "100,000 Coins",
                                                frameTitle = "Gold CP Frame",
                                                borderColor = Color(0xFFFFD700),
                                                frameAsset = "svip1_frame.svg"
                                            )
                                            RewardCard(
                                                title = "🥈 TOP 2",
                                                coins = "50,000 Coins",
                                                frameTitle = "Silver CP Frame",
                                                borderColor = Color(0xFFC0C0C0),
                                                frameAsset = "svip2_frame.svg"
                                            )
                                            RewardCard(
                                                title = "🥉 TOP 3",
                                                coins = "25,000 Coins",
                                                frameTitle = "Bronze CP Frame",
                                                borderColor = Color(0xFFCD7F32),
                                                frameAsset = "svip3_frame.svg"
                                            )
                                        }
                                    }

                                    "weekly" -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(Color.Black.copy(alpha = 0.5f))
                                                .border(1.dp, Color(0xFFFF80AB), RoundedCornerShape(16.dp))
                                                .padding(20.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                YaraanAssetImage(
                                                    assetName = "cp_heart_final.png",
                                                    contentDescription = "Weekly Gift",
                                                    modifier = Modifier.size(48.dp)
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "Weekly CP Rewards 🎁",
                                                    color = Color.White,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "Maintain top intimacy score weekly to receive exclusive animated CP avatar frames & title badges!",
                                                    color = Color.White.copy(alpha = 0.8f),
                                                    fontSize = 11.sp,
                                                    modifier = Modifier.padding(top = 4.dp)
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
}

@Composable
private fun TimerNumberPill(number: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF2A1138))
            .border(1.dp, Color(0xFF8E24AA), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = number,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun CpTabPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) {
                    Brush.horizontalGradient(listOf(Color(0xFFE040FB), Color(0xFFD81B60)))
                } else {
                    Brush.horizontalGradient(listOf(Color(0xFF311B92).copy(alpha = 0.7f), Color(0xFF4A148C).copy(alpha = 0.7f)))
                }
            )
            .border(
                1.dp,
                if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                YaraanAssetImage(
                    assetName = "cp_heart2.svg",
                    contentDescription = "Heart",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RewardCard(
    title: String,
    coins: String,
    frameTitle: String,
    borderColor: Color,
    frameAsset: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF2A1B3C), Color(0xFF1A0F2A))
                )
            )
            .border(2.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    color = borderColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    YaraanAssetImage(
                        assetName = "diamond.png",
                        contentDescription = "Coins",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = coins, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AvatarFrame(size = 40.dp, showDesignerFrame = true, frameAsset = frameAsset)
                Text(
                    text = frameTitle,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun CpTopPodiumCard(
    rank: Int,
    bgAsset: String,
    coupleName1: String,
    coupleName2: String,
    score: String,
    heartAsset: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(175.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        YaraanAssetImage(
            assetName = bgAsset,
            contentDescription = "CP Podium BG",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Rank Badge
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        when (rank) {
                            1 -> Color(0xFFFFD700)
                            2 -> Color(0xFFC0C0C0)
                            else -> Color(0xFFCD7F32)
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "NO. $rank",
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Dual Avatars
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                AvatarFrame(size = 36.dp, showDesignerFrame = true, frameAsset = "svip1_frame.svg")
                YaraanAssetImage(
                    assetName = "cp_heart_final.png",
                    contentDescription = "CP Heart Link",
                    modifier = Modifier
                        .size(22.dp)
                        .padding(horizontal = 2.dp)
                )
                AvatarFrame(size = 36.dp, showDesignerFrame = true, frameAsset = "svip2_frame.svg")
            }

            // Couple Names
            Text(
                text = "$coupleName1 & $coupleName2",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Intimacy Score Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                YaraanAssetImage(
                    assetName = heartAsset,
                    contentDescription = "CP Heart",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = score,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FamilyTreeCard(
    room: VoiceRoom,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(210.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        // Background Image Asset
        YaraanAssetImage(
            assetName = "family_star_bg.png",
            contentDescription = "Family Tree BG",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Tree Avatar Grid (circular nodes representing family tree)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Family Star",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA5D6A7),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MiniTreeAvatar()
                    MiniTreeAvatar()
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MiniTreeAvatar()
                    MiniTreeAvatar()
                    MiniTreeAvatar()
                }
            }

            // Bottom Owner Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_user_avatar),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp).clip(CircleShape)
                        )
                        Text(
                            text = " Brands Family",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text("🌲 Tree", color = Color(0xFFA5D6A7), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MiniTreeAvatar() {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .border(1.dp, Color(0xFFA5D6A7), CircleShape)
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_user_avatar),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun VoiceRoomItemCard(
    title: String,
    hostName: String,
    listeners: String,
    tags: List<String>,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarFrame(size = 54.dp, showDesignerFrame = true)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1D2B)
                )

                Text(
                    text = "Host: $hostName • $listeners",
                    fontSize = 12.sp,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(top = 2.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF3E5F5))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(tag, color = YaraanPinkPrimary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VoiceRoomGridCard(
    room: VoiceRoom,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .clickable { onClick() }
    ) {
        Column {
            // Room Cover Image with Host Avatar & Online Listeners Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF7C4DFF), Color(0xFF651FFF))
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_cover_bg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )

                // Top Tag Pill
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = room.tag,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Top Listeners Online Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(YaraanPinkPrimary)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "🔥 ${room.listenersCount}",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Center Host Avatar
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 6.dp)
                ) {
                    AvatarFrame(avatarAsset = "yaraan_dp.png", size = 52.dp, showDesignerFrame = true)
                }
            }

            // Room Name, User Name, and Gender Icon
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Room Name
                Text(
                    text = room.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1D2B),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                // User Name + Gender Icon Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = room.hostName,
                        fontSize = 11.sp,
                        color = Color(0xFF616161),
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Gender Icon Badge (Male ♂ / Female ♀)
                    val isMale = room.hostGender == "male"
                    val genderBg = if (isMale) Color(0xFF29B6F6) else Color(0xFFEC407A)
                    val genderSymbol = if (isMale) "♂" else "♀"

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(genderBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$genderSymbol ${room.hostAge}",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BannerCarousel(
    modifier: Modifier = Modifier
) {
    val banners = listOf("banner1.webp", "banner2.webp", "banner3.webp", "banner4.webp")
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { banners.size })

    // Auto-swipe banner timer (swipes every 3.5s)
    LaunchedEffect(pagerState) {
        while (true) {
            kotlinx.coroutines.delay(3500)
            if (banners.isNotEmpty()) {
                val nextPage = (pagerState.currentPage + 1) % banners.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFFF4081), Color(0xFF7C4DFF), Color(0xFF00E5FF))
                        )
                    )
            ) {
                YaraanAssetImage(
                    assetName = banners[page],
                    contentDescription = "Yaraan Banner ${page + 1}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Indicator dots
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .background(Color.Black.copy(alpha = 0.35f), CircleShape)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            banners.indices.forEach { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width(if (isSelected) 16.dp else 6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) YaraanPinkPrimary else Color.White.copy(alpha = 0.6f))
                )
            }
        }
    }
}

