package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
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
    userCoins: Int = 58200,
    userActualLevel: Int = 2,
    totalRecharge: Int = 58200,
    periodRecharge: Int = 18500,
    daysLeft: Int = 48,
    onBack: () -> Unit,
    onOpenWallet: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)
    var selectedLevel by remember { mutableIntStateOf(userActualLevel.coerceIn(1, 5)) }
    var selectedPrivilegePreview by remember { mutableStateOf<PrivilegeType?>(null) }

    val currentSvipTier = SVIP_TIERS[selectedLevel - 1]
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = Color(0xFF08080C),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // HEADER BAR & SVIP STAGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                // Background Asset
                YaraanAssetImage(
                    assetName = "svip_bg.svg",
                    contentDescription = "SVIP Stage Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    useAnimatedWebView = true
                )

                Column(modifier = Modifier.fillMaxSize()) {
                    // Top Bar
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
                            text = "SVIP ROYALTY",
                            color = Color(0xFFFFD54F),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )

                        // Balance Chip
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black.copy(alpha = 0.6f))
                                .border(1.dp, Color(0xFFFFD54F).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                                .clickable { onOpenWallet() }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Coins",
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "%,d".format(userCoins),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // SVIP LEVEL SELECTOR TABS
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
                                    fontSize = if (isSelected) 18.sp else 13.sp,
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
                                                    listOf(Color(0xFFFFE082), Color(0xFFFFB300))
                                                )
                                            )
                                    )
                                }
                            }
                        }
                    }

                    // Large Center SVIP Emblem
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        SvipLargeEmblem(
                            level = selectedLevel,
                            primaryColor = currentSvipTier.primaryColor,
                            secondaryColor = currentSvipTier.secondaryColor
                        )
                    }
                }
            }

            // BODY SCROLL CONTENT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // SVIP PROGRESS CARD
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
                                text = currentSvipTier.name,
                                color = Color(0xFFFFB300),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val (percentage, descText, pointsText) = calculateProgressInfo(
                            selectedLevel = selectedLevel,
                            userActualLevel = userActualLevel,
                            totalRecharge = totalRecharge,
                            periodRecharge = periodRecharge,
                            tier = currentSvipTier
                        )

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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(Brush.horizontalGradient(listOf(Color.Transparent, Color(0xFFFFB300))))
                    )
                    Text(
                        text = "ROYALTY PRIVILEGES",
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
                            .background(Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color.Transparent)))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val isLocked = userActualLevel < selectedLevel

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // PREVIEW SHEET
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

// GRAPHICS & EMBLEMS
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
        YaraanAssetImage(
            assetName = medalAsset,
            contentDescription = "SVIP Medal $level",
            modifier = Modifier.size(210.dp),
            useAnimatedWebView = true
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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title,
                    color = Color(0xFFFFE082),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SvipMedalGraphic(level: Int) {
    YaraanAssetImage(
        assetName = "svip_${level.coerceIn(1, 5)}.svg",
        contentDescription = "SVIP Medal",
        modifier = Modifier.size(50.dp),
        useAnimatedWebView = true
    )
}

@Composable
private fun SvipBadgeGraphic(level: Int) {
    SvipBadge(svip = "SVIP$level")
}

@Composable
private fun SvipBubbleGraphic(level: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF3E2723), Color(0xFF1A0C08))
                )
            )
            .border(1.dp, Color(0xFFFFB300), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text("SVIP$level Chat", color = Color(0xFFFFD54F), fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SvipFrameGraphic(level: Int) {
    AvatarFrame(
        size = 48.dp,
        showDesignerFrame = true
    )
}

@Composable
private fun SvipVoiceEffectGraphic(level: Int) {
    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = "Voice Effect",
        tint = Color(0xFFFFD54F),
        modifier = Modifier.size(36.dp)
    )
}

@Composable
private fun PrivilegePreviewSheetContent(
    privilegeType: PrivilegeType,
    level: Int,
    userProfile: UserProfile,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when (privilegeType) {
                PrivilegeType.MEDAL -> "SVIP$level Medal Preview"
                PrivilegeType.BADGE -> "SVIP$level Badge Preview"
                PrivilegeType.BUBBLE -> "SVIP$level Chat Bubble Preview"
                PrivilegeType.FRAME -> "SVIP$level Profile Frame Preview"
                PrivilegeType.WIBE -> "SVIP$level Voice Effect Preview"
            },
            color = Color(0xFFFFD54F),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .border(2.dp, Color(0xFFFFB300), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            when (privilegeType) {
                PrivilegeType.MEDAL -> SvipMedalGraphic(level = level)
                PrivilegeType.BADGE -> SvipBadgeGraphic(level = level)
                PrivilegeType.BUBBLE -> SvipBubbleGraphic(level = level)
                PrivilegeType.FRAME -> SvipFrameGraphic(level = level)
                PrivilegeType.WIBE -> SvipVoiceEffectGraphic(level = level)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Unlocked automatically upon reaching SVIP$level tier.",
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun calculateProgressInfo(
    selectedLevel: Int,
    userActualLevel: Int,
    totalRecharge: Int,
    periodRecharge: Int,
    tier: SvipTierConfig
): Triple<Float, String, String> {
    return if (selectedLevel <= userActualLevel) {
        val pct = (periodRecharge.toFloat() / tier.maintReq).coerceIn(0f, 1f)
        Triple(
            pct,
            "Maintenance: %,d / %,d EXP".format(periodRecharge, tier.maintReq),
            "%,d / %,d".format(periodRecharge, tier.maintReq)
        )
    } else {
        val pct = (totalRecharge.toFloat() / tier.unlockReq).coerceIn(0f, 1f)
        Triple(
            pct,
            "To unlock: %,d / %,d EXP".format(totalRecharge, tier.unlockReq),
            "%,d / %,d".format(totalRecharge, tier.unlockReq)
        )
    }
}
