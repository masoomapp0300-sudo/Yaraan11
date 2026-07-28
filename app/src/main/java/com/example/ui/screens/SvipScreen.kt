package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.ui.components.AvatarFrame
import com.example.ui.components.SvipBadge
import com.example.ui.components.YaraanAssetImage
import com.example.ui.components.INFINITE

data class SvipTierConfig(
    val level: Int,
    val name: String,
    val unlockReq: Int,
    val maintReq: Int,
    val primaryColor: Color,
    val secondaryColor: Color
)

val SVIP_TIERS = listOf(
    SvipTierConfig(1, "SVIP1", 15000, 7500, Color(0xFF4DB6AC), Color(0xFF00897B)),
    SvipTierConfig(2, "SVIP2", 50000, 25000, Color(0xFF66BB6A), Color(0xFF2E7D32)),
    SvipTierConfig(3, "SVIP3", 100000, 50000, Color(0xFF42A5F5), Color(0xFF1565C0)),
    SvipTierConfig(4, "SVIP4", 150000, 75000, Color(0xFFAB47BC), Color(0xFF6A1B9A)),
    SvipTierConfig(5, "SVIP5", 200000, 100000, Color(0xFFFF7043), Color(0xFFD84315))
)

enum class PrivilegeType {
    MEDAL,
    BADGE,
    BUBBLE,
    FRAME,
    WIBE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SvipScreen(
    userProfile: UserProfile,
    userActualLevel: Int = 2,
    totalRecharge: Int = 58200,
    periodRecharge: Int = 18500,
    daysLeft: Int = 48,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)
    var selectedLevel by remember { mutableIntStateOf(userActualLevel.coerceIn(1, 5)) }
    var selectedPrivilegePreview by remember { mutableStateOf<PrivilegeType?>(null) }
    val currentTier = SVIP_TIERS[selectedLevel - 1]

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = Color(0xFF0A0A0A),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(selectedLevel) {
                    var totalDrag = 0f
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (totalDrag < -60f && selectedLevel < 5) {
                                selectedLevel++
                            } else if (totalDrag > 60f && selectedLevel > 1) {
                                selectedLevel--
                            }
                            totalDrag = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            totalDrag += dragAmount
                        }
                    )
                }
        ) {
            // TOP SECTION WITH EMBLEM & TABS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                // Stage background SVG filling top header
                YaraanAssetImage(
                    assetName = "svip_bg.svg",
                    contentDescription = "SVIP Stage Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(modifier = Modifier.fillMaxSize()) {
                    // Header Bar with statusBarsPadding so background extends into status bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Text(
                            text = "SVIP",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 2.sp
                        )

                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = "Help",
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Level Selector Tabs Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SVIP_TIERS.forEach { tier ->
                            val isSelected = tier.level == selectedLevel
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedLevel = tier.level }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = tier.name,
                                    fontSize = if (isSelected) 20.sp else 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.45f)
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .width(28.dp)
                                            .height(3.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(
                                                        Color(0xFFFFE082),
                                                        Color(0xFFFFB300)
                                                    )
                                                )
                                            )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Large Center Emblem Display
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        SvipLargeEmblem(
                            level = selectedLevel,
                            primaryColor = currentTier.primaryColor,
                            secondaryColor = currentTier.secondaryColor
                        )
                    }
                }
            }

            // BOTTOM SCROLLABLE CONTENT AREA
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // PROGRESS CARD
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A150E)),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3A2A18)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (selectedLevel == userActualLevel) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF3E1212))
                                        .border(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "⏳ $daysLeft Days Left",
                                        color = Color(0xFFFF8A80),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.width(1.dp))
                            }

                            Text(
                                text = currentTier.name,
                                color = Color(0xFFFFB300),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Calculate Progress Percentage & Label Texts
                        val (percentage, descText, pointsText) = calculateProgressInfo(
                            selectedLevel = selectedLevel,
                            userActualLevel = userActualLevel,
                            totalRecharge = totalRecharge,
                            periodRecharge = periodRecharge,
                            tier = currentTier
                        )

                        // Progress Bar Track
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color(0xFF000000))
                                .border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(5.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(percentage)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFFFFE082),
                                                Color(0xFFFFB300),
                                                Color(0xFFFF6F00)
                                            )
                                        )
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = descText,
                                color = Color(0xFFD7CCC8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = pointsText,
                                color = Color(0xFFFFD54F),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // PRIVILEGES SECTION HEADER
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color(0xFFFFB300))
                                )
                            )
                    )
                    Text(
                        text = "PRIVILEGES",
                        color = Color(0xFFFFB300),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFFFB300), Color.Transparent)
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // PRIVILEGE CARDS 3-COLUMN GRID
                val isLocked = userActualLevel < selectedLevel

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Row 1: Medal, Badge, Chat Bubble
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PrivilegeCard(
                            title = "SVIP Medal",
                            isLocked = isLocked,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedPrivilegePreview = PrivilegeType.MEDAL }
                        ) {
                            SvipMedalGraphic(level = selectedLevel)
                        }

                        PrivilegeCard(
                            title = "SVIP Badge",
                            isLocked = isLocked,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedPrivilegePreview = PrivilegeType.BADGE }
                        ) {
                            SvipBadgeGraphic(level = selectedLevel)
                        }

                        PrivilegeCard(
                            title = "Chat Bubble",
                            isLocked = isLocked,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedPrivilegePreview = PrivilegeType.BUBBLE }
                        ) {
                            SvipBubbleGraphic(level = selectedLevel)
                        }
                    }

                    // Row 2: Frame & Voice Effect
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PrivilegeCard(
                            title = "SVIP Frame",
                            isLocked = isLocked,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedPrivilegePreview = PrivilegeType.FRAME }
                        ) {
                            SvipFrameGraphic(level = selectedLevel)
                        }

                        PrivilegeCard(
                            title = "Voice Effect",
                            isLocked = isLocked,
                            modifier = Modifier.weight(1f),
                            onClick = { selectedPrivilegePreview = PrivilegeType.WIBE }
                        ) {
                            SvipVoiceEffectGraphic(level = selectedLevel)
                        }

                        // Placeholder empty spacer to align grid evenly
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // PREVIEW BOTTOM SHEET
        selectedPrivilegePreview?.let { privilegeType ->
            ModalBottomSheet(
                onDismissRequest = { selectedPrivilegePreview = null },
                sheetState = sheetState,
                containerColor = Color(0xFF1A130C),
                contentColor = Color.White
            ) {
                PrivilegePreviewSheetContent(
                    privilegeType = privilegeType,
                    level = selectedLevel,
                    userProfile = userProfile,
                    onClose = { selectedPrivilegePreview = null }
                )
            }
        }
    }
}

// --------------------------------------------------
// HELPER COMPOSABLES & GRAPHICS
// --------------------------------------------------

@Composable
private fun SvipLargeEmblem(
    level: Int,
    primaryColor: Color,
    secondaryColor: Color
) {
    val medalAsset = "svip_${level.coerceIn(1, 5)}.svg"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        contentAlignment = Alignment.Center
    ) {
        // Centered Animated SVIP Medal Asset on Stage (svip_1.svg .. svip_5.svg)
        YaraanAssetImage(
            assetName = medalAsset,
            contentDescription = "SVIP Medal $level",
            modifier = Modifier.size(210.dp),
            autoPlay = true,
            loops = INFINITE
        )
    }
}

@Composable
private fun PrivilegeCard(
    title: String,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .height(115.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF2A2015), Color(0xFF15100A))
                )
            )
            .border(1.dp, Color(0xFF3A2A18), RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLocked) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .border(1.dp, Color(0xFFFFB300).copy(alpha = 0.6f), CircleShape)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (isLocked) 0.4f else 1f)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }

                Text(
                    text = title,
                    color = Color(0xFFE0E0E0),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

// Graphics for privileges inside grid items customized per SVIP Level
@Composable
private fun SvipMedalGraphic(level: Int) {
    val medalAsset = "svip_${level.coerceIn(1, 5)}.svg"
    YaraanAssetImage(
        assetName = medalAsset,
        contentDescription = "SVIP Medal $level",
        modifier = Modifier.size(54.dp),
        autoPlay = true,
        loops = INFINITE
    )
}

@Composable
private fun SvipBadgeGraphic(level: Int) {
    val badgeAsset = "svip${level.coerceIn(1, 5)}_badge.svg"
    Box(contentAlignment = Alignment.Center) {
        YaraanAssetImage(
            assetName = badgeAsset,
            contentDescription = "SVIP Badge $level",
            modifier = Modifier.size(52.dp),
            autoPlay = true,
            loops = INFINITE
        )
    }
}

@Composable
private fun SvipBubbleGraphic(level: Int) {
    val bubbleAsset = "chat_bubble${level.coerceIn(1, 5)}.svg"
    Box(contentAlignment = Alignment.Center) {
        YaraanAssetImage(
            assetName = bubbleAsset,
            contentDescription = "Chat Bubble $level",
            modifier = Modifier.size(50.dp),
            autoPlay = true,
            loops = INFINITE
        )
    }
}

@Composable
private fun SvipFrameGraphic(level: Int) {
    val frameAsset = "svip${level.coerceIn(1, 5)}_frame.svg"
    YaraanAssetImage(
        assetName = frameAsset,
        contentDescription = "SVIP Frame $level",
        modifier = Modifier.size(52.dp),
        autoPlay = true,
        loops = INFINITE
    )
}

@Composable
private fun SvipVoiceEffectGraphic(level: Int) {
    val soundWibeAsset = "svip${level.coerceIn(1, 5)}_sound_wibe.svg"
    Box(contentAlignment = Alignment.Center) {
        YaraanAssetImage(
            assetName = soundWibeAsset,
            contentDescription = "Voice Effect $level",
            modifier = Modifier.size(46.dp),
            autoPlay = true,
            loops = INFINITE
        )
    }
}

// --------------------------------------------------
// PREVIEW SHEET CONTENT
// --------------------------------------------------

@Composable
private fun PrivilegePreviewSheetContent(
    privilegeType: PrivilegeType,
    level: Int,
    userProfile: UserProfile,
    onClose: () -> Unit
) {
    val titleText: String
    val descText: String

    when (privilegeType) {
        PrivilegeType.MEDAL -> {
            titleText = "SVIP$level Honor Medal Preview"
            descText = "Prestigious badge of honor shown on your full profile card and user details sheet."
        }
        PrivilegeType.BADGE -> {
            titleText = "SVIP$level Name Badge Preview"
            descText = "Exclusive name tag badge displayed next to your username in room chat and profile views."
        }
        PrivilegeType.BUBBLE -> {
            titleText = "SVIP$level Chat Bubble Preview"
            descText = "Custom chat message background that highlights your text messages inside voice chat rooms."
        }
        PrivilegeType.FRAME -> {
            titleText = "SVIP$level Frame Preview"
            descText = "Exclusive avatar frame displayed around your profile picture across rooms and leaderboards."
        }
        PrivilegeType.WIBE -> {
            titleText = "SVIP$level Voice Effect Preview"
            descText = "Special glowing wave effect displayed on your mic seat while speaking in voice rooms."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Drag Handle
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White.copy(alpha = 0.3f))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Live Display Stage
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF000000)),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB300).copy(alpha = 0.3f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (privilegeType) {
                    PrivilegeType.FRAME -> {
                        Box(contentAlignment = Alignment.Center) {
                            AvatarFrame(size = 80.dp, showDesignerFrame = true)
                        }
                    }
                    PrivilegeType.BUBBLE -> {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            AvatarFrame(size = 40.dp, showDesignerFrame = false)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = userProfile.nickname,
                                    color = Color(0xFFFFD54F),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp, bottomEnd = 14.dp, bottomStart = 2.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFF3E2723), Color(0xFF1A0A00))
                                            )
                                        )
                                        .border(1.dp, Color(0xFFFFB300), RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp, bottomEnd = 14.dp, bottomStart = 2.dp))
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Hello! SVIP$level Chat Bubble Preview 🎉",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                    PrivilegeType.WIBE -> {
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFB300).copy(alpha = 0.25f))
                                    .border(2.dp, Color(0xFFFFB300), CircleShape)
                            )
                            AvatarFrame(size = 72.dp, showDesignerFrame = false)
                        }
                    }
                    PrivilegeType.MEDAL -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            YaraanAssetImage(
                                assetName = "svip_${level.coerceIn(1, 5)}.svg",
                                contentDescription = "SVIP $level Medal",
                                modifier = Modifier.size(90.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Black)
                                    .border(1.dp, Color(0xFFFFB300).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("SVIP$level Medal", color = Color(0xFFFFD54F), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                    PrivilegeType.BADGE -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF111111)),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB300).copy(alpha = 0.3f)),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AvatarFrame(size = 48.dp, showDesignerFrame = false)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = userProfile.nickname,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                SvipBadge(svip = "SVIP$level")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = titleText,
            color = Color(0xFFFFD54F),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = descText,
            color = Color(0xFFCCCCCC),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onClose,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFFFC107), Color(0xFFFF8F00))
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Text(
                text = "CLOSE PREVIEW",
                color = Color.Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }
    }
}

// --------------------------------------------------
// PROGRESS CALCULATOR HELPER
// --------------------------------------------------

private fun calculateProgressInfo(
    selectedLevel: Int,
    userActualLevel: Int,
    totalRecharge: Int,
    periodRecharge: Int,
    tier: SvipTierConfig
): Triple<Float, String, String> {
    return when {
        selectedLevel == userActualLevel -> {
            val maintReq = tier.maintReq
            val diff = maintReq - periodRecharge
            if (diff <= 0) {
                Triple(
                    1f,
                    "Task Completed! Your SVIP$selectedLevel is maintained for next cycle.",
                    "%,d / %,d".format(periodRecharge, maintReq)
                )
            } else {
                val percentage = (periodRecharge.toFloat() / maintReq.toFloat()).coerceIn(0f, 1f)
                Triple(
                    percentage,
                    "60-Day Task: Recharge %,d more to keep SVIP$selectedLevel".format(diff),
                    "%,d / %,d".format(periodRecharge, maintReq)
                )
            }
        }
        userActualLevel > selectedLevel -> {
            Triple(
                1f,
                "Unlocked (Current Level: SVIP$userActualLevel)",
                "Unlocked"
            )
        }
        else -> {
            val unlockReq = tier.unlockReq
            val diff = (unlockReq - totalRecharge).coerceAtLeast(0)
            val percentage = (totalRecharge.toFloat() / unlockReq.toFloat()).coerceIn(0f, 1f)
            Triple(
                percentage,
                "Recharge %,d total points to unlock SVIP$selectedLevel".format(diff),
                "%,d / %,d".format(totalRecharge, unlockReq)
            )
        }
    }
}
