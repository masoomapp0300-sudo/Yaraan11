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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.IntimacyCouple
import com.example.data.IntimacyFriend
import com.example.data.UserProfile
import com.example.ui.components.AvatarFrame
import com.example.ui.components.BrandBadge
import com.example.ui.components.IdBadge
import com.example.ui.components.LevelBadge
import com.example.ui.components.MedalRow
import com.example.ui.components.SvipBadge
import com.example.ui.components.VBadge
import com.example.ui.components.VerificationBadge
import com.example.ui.components.YaraanAssetImage
import com.example.ui.components.YaraanBottomNav
import com.example.ui.theme.YaraanPinkPrimary

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    activeSubTab: String,
    onSubTabSelected: (String) -> Unit,
    bottomNavIndex: Int,
    onBottomTabSelected: (Int) -> Unit,
    onOpenEditProfile: () -> Unit,
    onOpenWallet: () -> Unit,
    onOpenStore: () -> Unit,
    onOpenSvip: () -> Unit = {},
    onAddPhoto: (String) -> Unit = {},
    onRemovePhoto: (Int) -> Unit = {},
    onLogout: () -> Unit,
    intimacyCouple: IntimacyCouple,
    intimacyFriends: List<IntimacyFriend>,
    modifier: Modifier = Modifier
) {
    // State to switch between Settings List mode (Image 3) and Detailed Profile View (Image 4 & 5)
    var isDetailedProfileView by remember { mutableStateOf(false) }

    // Go to Back handler for detailed profile view
    BackHandler(enabled = isDetailedProfileView) {
        isDetailedProfileView = false
    }

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
        if (isDetailedProfileView) {
            DetailedProfileContent(
                userProfile = userProfile,
                activeSubTab = activeSubTab,
                onSubTabSelected = onSubTabSelected,
                onOpenEdit = onOpenEditProfile,
                onOpenSvip = onOpenSvip,
                onAddPhoto = onAddPhoto,
                onRemovePhoto = onRemovePhoto,
                onBack = { isDetailedProfileView = false },
                intimacyCouple = intimacyCouple,
                intimacyFriends = intimacyFriends,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            SettingsListContent(
                userProfile = userProfile,
                onOpenEdit = onOpenEditProfile,
                onOpenDetailed = { isDetailedProfileView = true },
                onOpenWallet = onOpenWallet,
                onOpenStore = onOpenStore,
                onOpenSvip = onOpenSvip,
                onLogout = onLogout,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

// --------------------------------------------------
// VIEW 1: Settings List Content (Matching Image 3)
// --------------------------------------------------
@Composable
private fun SettingsListContent(
    userProfile: UserProfile,
    onOpenEdit: () -> Unit,
    onOpenDetailed: () -> Unit,
    onOpenWallet: () -> Unit,
    onOpenStore: () -> Unit,
    onOpenSvip: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp)
    ) {
        // Header Profile Card (Clickable to detailed view, right at top under status bar)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenDetailed() }
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            AvatarFrame(size = 72.dp, showDesignerFrame = true)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = userProfile.nickname,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F1D2B)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    VerificationBadge()
                    Spacer(modifier = Modifier.width(4.dp))
                    VBadge()
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    IdBadge(id = userProfile.userId)
                    SvipBadge(svip = userProfile.svipLevel)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onOpenEdit) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color(0xFF757575)
                    )
                }
                IconButton(onClick = onOpenDetailed) {
                    Icon(
                        imageVector = Icons.Filled.SwapHoriz,
                        contentDescription = "Switch to Detailed Profile",
                        tint = Color(0xFF757575),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        // Stats Row: Follow | Fans | Visitors | FAMILY
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(count = "${userProfile.followCount}", label = "Follow")
            StatItem(count = "${userProfile.fansCount}", label = "Fans", hasRedDot = true)
            StatItem(count = userProfile.visitorsCount, label = "Visitors")

            // FAMILY Badge Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFFFB300), Color(0xFFFF8F00))
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = " FAMILY",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Quick Action Buttons: "Recharge & Wallet" and "Store (Buy car or frame)"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Recharge & Wallet Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenWallet() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Recharge&W\nallet", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F1D2B))
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFF8E1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Filled.Wallet, contentDescription = null, tint = Color(0xFFFFB300))
                    }
                }
            }

            // Store Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenStore() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Store", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F1D2B))
                        Text("Buy car or frame", fontSize = 10.sp, color = Color(0xFF9E9E9E))
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF3E5F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Filled.Storefront, contentDescription = null, tint = YaraanPinkPrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Settings Items List
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                SettingsRow(
                    icon = Icons.Filled.PersonAdd,
                    title = "Invite friends",
                    badge = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFFF1744))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("new", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF80CBC4))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Supporters", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                )

                SettingsRow(icon = Icons.Filled.Star, title = "Medal")
                SettingsRow(icon = Icons.Filled.Inventory2, title = "Prop Warehouse")
                SettingsRow(icon = Icons.Filled.MilitaryTech, title = "Level", badge = { LevelBadge(70) })
                SettingsRow(
                    icon = Icons.Filled.WorkspacePremium,
                    title = "SVIP",
                    badge = { SvipBadge("SVIP2") },
                    onClick = onOpenSvip
                )
                SettingsRow(icon = Icons.Filled.EmojiEvents, title = "Aristocracy")
                SettingsRow(icon = Icons.Filled.Language, title = "Language")
                SettingsRow(icon = Icons.Filled.HelpOutline, title = "Feedback")
                SettingsRow(icon = Icons.Filled.Settings, title = "Settings")
                SettingsRow(
                    icon = Icons.Filled.ExitToApp,
                    title = "Log Out",
                    titleColor = Color(0xFFFF1744),
                    onClick = onLogout
                )
            }
        }
    }
}

@Composable
private fun StatItem(count: String, label: String, hasRedDot: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = count,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1D2B)
            )
            if (hasRedDot) {
                Box(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF1744))
                )
            }
        }
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF8D8B9B),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    badge: (@Composable () -> Unit)? = null,
    titleColor: Color = Color(0xFF1F1D2B),
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (titleColor == Color(0xFFFF1744)) Color(0xFFFF1744) else YaraanPinkPrimary,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = titleColor,
                modifier = Modifier.padding(start = 14.dp)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            badge?.invoke()
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
}

// --------------------------------------------------
// VIEW 2: Detailed Profile Content (Matching Images 4 & 5)
// --------------------------------------------------
@Composable
private fun DetailedProfileContent(
    userProfile: UserProfile,
    activeSubTab: String,
    onSubTabSelected: (String) -> Unit,
    onOpenEdit: () -> Unit,
    onOpenSvip: () -> Unit,
    onAddPhoto: (String) -> Unit = {},
    onRemovePhoto: (Int) -> Unit = {},
    onBack: () -> Unit,
    intimacyCouple: IntimacyCouple,
    intimacyFriends: List<IntimacyFriend>,
    modifier: Modifier = Modifier
) {
    var showUploadPhotoDialog by remember { mutableStateOf(false) }
    var selectedPhotoIndexForViewer by remember { mutableStateOf<Int?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val subTabs = listOf("Profile", "Intimacy")
    val initialPage = if (activeSubTab == "Intimacy") 1 else 0
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { 2 })

    LaunchedEffect(pagerState.currentPage) {
        val currentTab = subTabs[pagerState.currentPage]
        if (currentTab != activeSubTab) {
            onSubTabSelected(currentTab)
        }
    }

    LaunchedEffect(activeSubTab) {
        val target = if (activeSubTab == "Intimacy") 1 else 0
        if (pagerState.currentPage != target) {
            pagerState.animateScrollToPage(target)
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top Cover Banner with background image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_cover_bg),
                contentDescription = "Cover Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Top Header Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                IconButton(onClick = onOpenEdit) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.White
                    )
                }
            }

            // User Info Overlaid on Cover
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    AvatarFrame(size = 72.dp, showDesignerFrame = true)

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userProfile.nickname,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .padding(start = 6.dp)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF29B6F6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("♂", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            IdBadge(id = userProfile.userId)
                            Text(
                                text = "  ${userProfile.followersCount} followers",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Badges Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LevelBadge(70)
                    Box(modifier = Modifier.clickable { onOpenSvip() }) {
                        SvipBadge("SVIP2")
                    }
                    BrandBadge("Brand")
                }

                Spacer(modifier = Modifier.height(6.dp))
                MedalRow()
            }
        }

        // Sub-Tabs: "Profile" & "Intimacy"
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    ProfileSubTabItem("Profile", pagerState.currentPage == 0) {
                        coroutineScope.launch { pagerState.animateScrollToPage(0) }
                    }
                    ProfileSubTabItem("Intimacy", pagerState.currentPage == 1) {
                        coroutineScope.launch { pagerState.animateScrollToPage(1) }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    if (page == 0) {
                        // TAB 1: Profile View (Matching Image 4)
                    Column {
                        // Location Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF757575),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = " ${userProfile.locationFlag} ${userProfile.location}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F1D2B)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Bio Text Section
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = null,
                                tint = Color(0xFF757575),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = userProfile.bio,
                                fontSize = 14.sp,
                                color = Color(0xFF424242),
                                lineHeight = 20.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Tags Section
                        Text("Tags", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F1D2B))
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF3E5F5))
                                .clickable { onOpenEdit() }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text("+ Edit my tags", color = YaraanPinkPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Photos Gallery Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Photo Gallery (${userProfile.photos.size})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F1D2B)
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF3E5F5))
                                    .clickable { showUploadPhotoDialog = true }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "+ Upload Photo",
                                    color = YaraanPinkPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (userProfile.photos.isEmpty()) {
                            // Empty state illustration
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_yaraan_mascot),
                                    contentDescription = "Empty Photos Mascot",
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                )
                                Text(
                                    text = "No gallery photos yet. Tap '+ Upload Photo' to add!",
                                    fontSize = 13.sp,
                                    color = Color(0xFF9E9E9E),
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        } else {
                            // 3-Column Photo Gallery Grid
                            val photoList = userProfile.photos
                            val rows = (photoList.size + 2) / 3
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                for (rowIndex in 0 until rows) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        for (colIndex in 0 until 3) {
                                            val photoIdx = rowIndex * 3 + colIndex
                                            if (photoIdx < photoList.size) {
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(105.dp)
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(Color(0xFFEEEEEE))
                                                        .clickable { selectedPhotoIndexForViewer = photoIdx }
                                                ) {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.img_user_avatar),
                                                        contentDescription = "Gallery Photo ${photoIdx + 1}",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.fillMaxSize()
                                                    )

                                                    Box(
                                                        modifier = Modifier
                                                            .align(Alignment.BottomEnd)
                                                            .padding(4.dp)
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(Color.Black.copy(alpha = 0.5f))
                                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                                    ) {
                                                        Text(
                                                            text = "#${photoIdx + 1}",
                                                            color = Color.White,
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            } else {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // TAB 2: Intimacy View (Matching Image 5)
                    Column {
                        // Love Store Banner Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F5)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.ShoppingBag,
                                        contentDescription = null,
                                        tint = YaraanPinkPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = " Love Store",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = YaraanPinkPrimary,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }

                                Button(
                                    onClick = { },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text("Go", color = YaraanPinkPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Couple Section Header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            YaraanAssetImage(
                                assetName = "cp_main.svg",
                                contentDescription = "CP Main",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Couple CP", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F1D2B))
                            Icon(
                                imageVector = Icons.Filled.HelpOutline,
                                contentDescription = null,
                                tint = Color(0xFFBDBDBD),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Couple Card (CP Design with SVG assets)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF8E24AA), Color(0xFF7C4DFF), Color(0xFF512DA8))
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Level Badge Pill with CP Heart Icon
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.White.copy(alpha = 0.25f))
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    YaraanAssetImage(
                                        assetName = "cp_heart2.svg",
                                        contentDescription = "CP Heart",
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(intimacyCouple.level, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Dual Avatars with Winged Heart Linkage
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        AvatarFrame(size = 56.dp, showDesignerFrame = true, frameAsset = "svip1_frame.svg")
                                        Text(
                                            text = intimacyCouple.userName,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        YaraanAssetImage(
                                            assetName = "cp_heart_final.png",
                                            contentDescription = "CP Heart Link",
                                            modifier = Modifier.size(38.dp)
                                        )
                                        Text(
                                            text = intimacyCouple.daysText,
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        AvatarFrame(size = 56.dp, showDesignerFrame = true, frameAsset = "svip1_frame.svg")
                                        Text(
                                            text = intimacyCouple.partnerName,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White.copy(alpha = 0.3f))
                                        .padding(horizontal = 20.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        YaraanAssetImage(
                                            assetName = "cp_become.svg",
                                            contentDescription = "CP Become",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("CP Showcase", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Friend Section
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            YaraanAssetImage(
                                assetName = "main_friend_bg.svg",
                                contentDescription = "Friends",
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Best Friends", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F1D2B))
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            intimacyFriends.forEach { friend ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(120.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color(0xFFFF80AB), Color(0xFFEA80FC))
                                            )
                                        )
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(friend.level, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        AvatarFrame(size = 42.dp, showDesignerFrame = false)
                                        Text("${friend.points}", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

        if (showUploadPhotoDialog) {
            UploadPhotoDialog(
                onDismiss = { showUploadPhotoDialog = false },
                onSelectPhoto = { photoUri ->
                    onAddPhoto(photoUri)
                    showUploadPhotoDialog = false
                }
            )
        }

        selectedPhotoIndexForViewer?.let { index ->
            if (index in userProfile.photos.indices) {
                GalleryViewerDialog(
                    photoUri = userProfile.photos[index],
                    photoIndex = index,
                    totalPhotos = userProfile.photos.size,
                    onDismiss = { selectedPhotoIndexForViewer = null },
                    onDelete = {
                        onRemovePhoto(index)
                        selectedPhotoIndexForViewer = null
                    }
                )
            }
        }
    }
}

@Composable
fun GalleryViewerDialog(
    photoUri: String,
    photoIndex: Int,
    totalPhotos: Int,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E2C),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Photo ${photoIndex + 1} of $totalPhotos",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_user_avatar),
                        contentDescription = "Full photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Photo", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun ProfileSubTabItem(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color(0xFF1F1D2B) else Color(0xFF9E9E9E)
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(width = 18.dp, height = 3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(YaraanPinkPrimary)
            )
        }
    }
}
