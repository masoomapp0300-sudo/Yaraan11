package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.ui.components.AvatarFrame
import com.example.ui.components.YaraanAssetImage
import kotlinx.coroutines.launch

// ==============================================================
// YARAAN - FINAL LEVEL SYSTEM (1 to 150) UTILITY LOGIC
// ==============================================================

object LevelSystem {
    fun getExpForLevel(lvl: Int): Long {
        if (lvl <= 1) return 0L
        return Math.floor(Math.pow(lvl.toDouble(), 2.5) * 2000.0).toLong()
    }

    fun getUserLevel(exp: Long): Int {
        var lvl = 1
        while (lvl < 150 && exp >= getExpForLevel(lvl + 1)) {
            lvl++
        }
        return lvl
    }

    fun getLevelTier(lvl: Int): Int {
        return Math.min(15, Math.max(1, Math.ceil(lvl.toDouble() / 10.0).toInt()))
    }

    fun getTierTitle(tier: Int): String {
        return when (tier) {
            1 -> "Bronze Pioneer"
            2 -> "Silver Knight"
            3 -> "Gold Noble"
            4 -> "Emerald Baron"
            5 -> "Cyan Count"
            6 -> "Sapphire Duke"
            7 -> "Diamond Royal"
            8 -> "Magenta Prince"
            9 -> "Ruby Monarch"
            10 -> "Crown Emperor"
            11 -> "Galactic Legend"
            12 -> "Plasma Mythic"
            13 -> "Solar Immortal"
            14 -> "Dragon Supreme"
            else -> "Godly Cosmic"
        }
    }

    fun getTierColors(tier: Int): Pair<Color, Color> {
        return when (tier) {
            1 -> Color(0xFFCD7F32) to Color(0xFF8B4513)
            2 -> Color(0xFFC0C0C0) to Color(0xFF616161)
            3 -> Color(0xFFFFD700) to Color(0xFFFF8C00)
            4 -> Color(0xFF00E676) to Color(0xFF00897B)
            5 -> Color(0xFF00E5FF) to Color(0xFF00838F)
            6 -> Color(0xFF29B6F6) to Color(0xFF1565C0)
            7 -> Color(0xFFAB47BC) to Color(0xFF4A148C)
            8 -> Color(0xFFFF4081) to Color(0xFFC2185B)
            9 -> Color(0xFFFF1744) to Color(0xFFB71C1C)
            10 -> Color(0xFFFF3D00) to Color(0xFFDD2C00)
            11 -> Color(0xFFD500F9) to Color(0xFF651FFF)
            12 -> Color(0xFF00B0FF) to Color(0xFF00E5FF)
            13 -> Color(0xFFFFC400) to Color(0xFFFFD700)
            14 -> Color(0xFFFF1744) to Color(0xFFFFD700)
            else -> Color(0xFFE040FB) to Color(0xFF00E5FF)
        }
    }

    fun getLevelRewardDetails(lvl: Int): List<LevelRewardItem> {
        val list = mutableListOf<LevelRewardItem>()
        when {
            lvl == 10 -> list.add(LevelRewardItem("coins", 50000, "svip1_badge.svg", "50k Coins"))
            lvl == 20 -> {
                list.add(LevelRewardItem("vip", 0, "green_vip.svg", "VIP Green", "name-vip-green"))
                list.add(LevelRewardItem("coins", 100000, "svip1_badge.svg", "100k Coins"))
            }
            lvl == 30 -> {
                list.add(LevelRewardItem("vip", 0, "yellow_vip.svg", "VIP Yellow", "name-vip-yellow"))
                list.add(LevelRewardItem("coins", 150000, "svip2_badge.svg", "150k Coins"))
            }
            lvl == 40 -> list.add(LevelRewardItem("coins", 300000, "svip2_badge.svg", "300k Coins"))
            lvl == 50 -> {
                list.add(LevelRewardItem("vip", 0, "pink_vip.svg", "VIP Pink", "name-vip-pink"))
                list.add(LevelRewardItem("coins", 500000, "svip3_badge.svg", "500k Coins"))
            }
            lvl == 60 -> list.add(LevelRewardItem("coins", 1000000, "svip3_badge.svg", "1M Coins"))
            lvl == 70 -> {
                list.add(LevelRewardItem("vip", 0, "colorful_vip.svg", "VIP Colorful", "name-vip-colorful"))
                list.add(LevelRewardItem("coins", 5000000, "svip4_badge.svg", "5M Coins"))
            }
            lvl == 80 -> list.add(LevelRewardItem("coins", 10000000, "svip4_badge.svg", "10M Coins"))
            lvl == 90 -> list.add(LevelRewardItem("coins", 20000000, "svip5_badge.svg", "20M Coins"))
            lvl == 100 -> {
                list.add(LevelRewardItem("vip", 0, "colorful_vip.svg", "VIP Supreme", "name-vip-colorful"))
                list.add(LevelRewardItem("coins", 50000000, "v_badge.svg", "50M Coins"))
            }
            lvl > 100 && lvl % 10 == 0 -> {
                list.add(LevelRewardItem("vip", 0, "colorful_vip.svg", "VIP God", "name-vip-colorful"))
                list.add(LevelRewardItem("coins", lvl * 1000000, "v_badge.svg", "${lvl}M Coins"))
            }
            else -> {
                val coinsVal = lvl * 1000
                val formatted = if (coinsVal >= 1000000) "${coinsVal/1000000}M Coins" else "${coinsVal/1000}k Coins"
                list.add(LevelRewardItem("coins", coinsVal, "svip1_badge.svg", formatted))
            }
        }
        return list
    }
}

data class LevelRewardItem(
    val type: String, // "coins" or "vip"
    val valAmount: Int,
    val imgAsset: String,
    val name: String,
    val vipKey: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelScreen(
    userProfile: UserProfile,
    userExp: Long = 81992682L, // Default Level 70
    userCoins: Int = 58200,
    claimedLevelRewards: Set<Int> = emptySet(),
    onClaimReward: (Int, Int) -> Unit = { _, _ -> },
    onClaimAllRewards: (List<Int>, Int) -> Unit = { _, _ -> },
    onBack: () -> Unit,
    onOpenWallet: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val currentLevel = remember(userExp) { LevelSystem.getUserLevel(userExp) }
    val currentTier = remember(currentLevel) { LevelSystem.getLevelTier(currentLevel) }
    
    var selectedTierFilter by remember { mutableIntStateOf(0) } // 0: All, 1..15 for Tiers
    var showRulesSheet by remember { mutableStateOf(false) }
    var claimSuccessDialogData by remember { mutableStateOf<Pair<Int, List<LevelRewardItem>>?>(null) }

    val prevExp = LevelSystem.getExpForLevel(currentLevel)
    val nextExp = LevelSystem.getExpForLevel((currentLevel + 1).coerceAtMost(150))
    val percent = if (currentLevel >= 150) 1f else {
        ((userExp - prevExp).toFloat() / (nextExp - prevExp).toFloat().coerceAtLeast(1f)).coerceIn(0f, 1f)
    }

    // Filter levels based on tier tab or full list 2..150
    val displayLevels = remember(selectedTierFilter) {
        if (selectedTierFilter == 0) {
            (2..150).toList()
        } else {
            val start = (selectedTierFilter - 1) * 10 + 1
            val end = (selectedTierFilter * 10).coerceAtMost(150)
            (start..end).filter { it >= 2 }
        }
    }

    // Unclaimed levels up to user current level
    val unclaimedUnlockedLevels = remember(currentLevel, claimedLevelRewards) {
        (2..currentLevel).filter { !claimedLevelRewards.contains(it) }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "AuraPulse")
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraScale"
    )

    Scaffold(
        containerColor = Color(0xFF0F081D),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF230C46),
                            Color(0xFF130928),
                            Color(0xFF06020E)
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // TOP BAR WITH TRANSPARENT STATUS BAR EXTENSION
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MilitaryTech,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Level System (لیول سسٹم)",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = { showRulesSheet = true },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.HelpOutline,
                            contentDescription = "Level Rules",
                            tint = Color.White
                        )
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // HERO USER LEVEL HEADER CARD
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .shadow(16.dp, RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF3B156E),
                                                Color(0xFF1C0D38),
                                                Color(0xFF2A085C)
                                            )
                                        )
                                    )
                                    .border(
                                        width = 1.5.dp,
                                        brush = Brush.linearGradient(
                                            listOf(
                                                Color(0xFFFFD700).copy(alpha = 0.7f),
                                                Color(0xFFE040FB).copy(alpha = 0.4f)
                                            )
                                        ),
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                    .padding(18.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // User Avatar + Tier Glow
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.graphicsLayer {
                                            scaleX = auraScale
                                            scaleY = auraScale
                                        }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(105.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    Brush.radialGradient(
                                                        colors = listOf(
                                                            LevelSystem.getTierColors(currentTier).first.copy(alpha = 0.5f),
                                                            Color.Transparent
                                                        )
                                                    )
                                                )
                                        )
                                        AvatarFrame(
                                            avatarRes = com.example.R.drawable.img_user_avatar,
                                            size = 80.dp,
                                            frameAsset = "svip4_frame.svg"
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = userProfile.nickname,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Dynamic Level Badge
                                    TierBadgePill(level = currentLevel, scale = 1.2f)

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // EXP Text
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "%,d EXP".format(userExp),
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (currentLevel >= 150) "MAX LEVEL 150" else "Next Lv.%d: %,d EXP".format(currentLevel + 1, nextExp),
                                            color = Color(0xFFFFD700),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Animated EXP Progress bar
                                    ClipProgressBar(progress = percent)

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // User Balance Row & Auto Claim Button
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            YaraanAssetImage(
                                                assetName = "svip1_badge.svg",
                                                contentDescription = "Coins",
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "%,d Coins".format(userCoins),
                                                color = Color(0xFFFFD700),
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        if (unclaimedUnlockedLevels.isNotEmpty()) {
                                            Button(
                                                onClick = {
                                                    var totalCoins = 0
                                                    val rewardItems = mutableListOf<LevelRewardItem>()
                                                    unclaimedUnlockedLevels.forEach { lvl ->
                                                        val rewards = LevelSystem.getLevelRewardDetails(lvl)
                                                        rewards.forEach { r ->
                                                            if (r.type == "coins") totalCoins += r.valAmount
                                                            rewardItems.add(r)
                                                        }
                                                    }
                                                    onClaimAllRewards(unclaimedUnlockedLevels, totalCoins)
                                                    claimSuccessDialogData = currentLevel to rewardItems
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                                shape = RoundedCornerShape(20.dp),
                                                modifier = Modifier
                                                    .background(
                                                        Brush.horizontalGradient(
                                                            listOf(Color(0xFF00E676), Color(0xFF00B0FF))
                                                        ),
                                                        shape = RoundedCornerShape(20.dp)
                                                    )
                                                    .height(36.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.AutoAwesome,
                                                    contentDescription = null,
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "CLAIM ALL (${unclaimedUnlockedLevels.size})",
                                                    color = Color.Black,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // TIER TABS SELECTOR (15 TIERS + ALL)
                    item {
                        Column(modifier = Modifier.padding(vertical = 10.dp)) {
                            Text(
                                text = "Select Tier (150 Levels)",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                item {
                                    val isSelected = selectedTierFilter == 0
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                if (isSelected) Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color(0xFFFF6F00)))
                                                else Brush.linearGradient(listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.08f)))
                                            )
                                            .clickable { selectedTierFilter = 0 }
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = "All Levels (1-150)",
                                            color = if (isSelected) Color.Black else Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                items(15) { index ->
                                    val tierNum = index + 1
                                    val isSelected = selectedTierFilter == tierNum
                                    val (color1, color2) = LevelSystem.getTierColors(tierNum)
                                    val startLvl = (tierNum - 1) * 10 + 1
                                    val endLvl = tierNum * 10

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                if (isSelected) Brush.horizontalGradient(listOf(color1, color2))
                                                else Brush.linearGradient(listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.08f)))
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = if (isSelected) Color.White else color1.copy(alpha = 0.4f),
                                                shape = RoundedCornerShape(20.dp)
                                            )
                                            .clickable { selectedTierFilter = tierNum }
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = "Tier $tierNum (Lv.$startLvl-$endLvl)",
                                            color = if (isSelected) Color.Black else Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // LEVEL REWARDS LIST HEADER
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (selectedTierFilter == 0) "All Level Rewards" else "Tier $selectedTierFilter Rewards",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Total ${displayLevels.size} Levels",
                                color = Color(0xFFFFD700),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // 150 LEVEL REWARD CARDS
                    items(
                        items = displayLevels,
                        key = { it }
                    ) { lvl ->
                        val isUnlocked = currentLevel >= lvl
                        val isClaimed = claimedLevelRewards.contains(lvl)
                        val rewards = remember(lvl) { LevelSystem.getLevelRewardDetails(lvl) }

                        LevelRewardCardItem(
                            level = lvl,
                            isUnlocked = isUnlocked,
                            isClaimed = isClaimed,
                            rewards = rewards,
                            onClaim = {
                                var coinReward = 0
                                rewards.forEach { r -> if (r.type == "coins") coinReward += r.valAmount }
                                onClaimReward(lvl, coinReward)
                                claimSuccessDialogData = lvl to rewards
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

            // BOTTOM QUICK UPGRADE / RECHARGE BAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xFF06020E))
                        )
                    )
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onOpenWallet,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFFFB300), Color(0xFFFF6F00), Color(0xFFE040FB))
                            ),
                            shape = RoundedCornerShape(28.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Upgrade Level (گولڈ سکے حاصل کریں)",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // CONGRATULATIONS CLAIM SUCCESS DIALOG
    if (claimSuccessDialogData != null) {
        val (claimedLvl, rewards) = claimSuccessDialogData!!
        ModalBottomSheet(
            onDismissRequest = { claimSuccessDialogData = null },
            containerColor = Color(0xFF1E1035)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🎉 Level $claimedLvl Rewards Claimed! 🎉",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rewards.forEach { reward ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.3f))
                                .padding(10.dp)
                        ) {
                            YaraanAssetImage(
                                assetName = reward.imgAsset,
                                contentDescription = reward.name,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "+${reward.name}",
                                color = if (reward.type == "vip") Color(0xFFE040FB) else Color(0xFFFFD700),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { claimSuccessDialogData = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Awesome!", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }

    // RULES DIALOG SHEET
    if (showRulesSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRulesSheet = false },
            containerColor = Color(0xFF1E1035)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Level System Rules (لیول کے قوانین)",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "1. Level EXP is upgraded automatically by spending Coins & sending gifts in Voice Rooms.\n\n" +
                            "2. Level Rewards unlock from Level 2 to Level 150 including Coins & VIP Name colors.\n\n" +
                            "3. Level EXP points never decay and remain permanent for your Yaraan profile.\n\n" +
                            "4. Higher levels grant exclusive VIP Green, VIP Yellow, VIP Pink, and VIP Colorful badges.",
                    color = Color.White.copy(alpha = 0.88f),
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { showRulesSheet = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Understand", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun LevelRewardCardItem(
    level: Int,
    isUnlocked: Boolean,
    isClaimed: Boolean,
    rewards: List<LevelRewardItem>,
    onClaim: () -> Unit
) {
    val tier = LevelSystem.getLevelTier(level)
    val (color1, color2) = LevelSystem.getTierColors(tier)

    val boxStyleColor = when {
        isClaimed -> Color.White.copy(alpha = 0.08f)
        isUnlocked -> Color.White.copy(alpha = 0.12f)
        else -> Color.Black.copy(alpha = 0.4f)
    }

    val borderColor = when {
        isClaimed -> Color(0xFF00E676).copy(alpha = 0.4f)
        isUnlocked -> Color(0xFFFFD700).copy(alpha = 0.6f)
        else -> Color.White.copy(alpha = 0.05f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = boxStyleColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Level Header Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TierBadgePill(level = level)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LEVEL $level",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Rewards Badges Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rewards.forEach { reward ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (reward.type == "vip") Color(0xFF4A148C).copy(alpha = 0.5f)
                                    else Color(0xFF332000).copy(alpha = 0.5f)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (reward.type == "vip") Color(0xFFE040FB).copy(alpha = 0.5f)
                                    else Color(0xFFFFD700).copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            YaraanAssetImage(
                                assetName = reward.imgAsset,
                                contentDescription = reward.name,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = reward.name,
                                color = if (reward.type == "vip") Color(0xFFE040FB) else Color(0xFFFFD700),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Status Button
            when {
                isClaimed -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF00E676).copy(alpha = 0.2f))
                            .border(1.dp, Color(0xFF00E676).copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "CLAIMED",
                                color = Color(0xFF00E676),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
                isUnlocked -> {
                    Button(
                        onClick = onClaim,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color(0xFFFF6F00))),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .height(34.dp)
                    ) {
                        Text(
                            text = "CLAIM",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LOCKED",
                                color = Color.White.copy(alpha = 0.5f),
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

@Composable
fun TierBadgePill(level: Int, scale: Float = 1.0f) {
    val tier = LevelSystem.getLevelTier(level)
    val (color1, color2) = LevelSystem.getTierColors(tier)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.horizontalGradient(listOf(color1, color2)))
            .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(horizontal = (8 * scale).dp, vertical = (3 * scale).dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "LV.$level",
            color = Color.Black,
            fontSize = (11 * scale).sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun ClipProgressBar(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White.copy(alpha = 0.15f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFFFD700), Color(0xFFFF6F00), Color(0xFFE040FB))
                    )
                )
        )
    }
}
