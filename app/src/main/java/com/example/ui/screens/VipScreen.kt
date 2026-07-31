package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.ui.components.YaraanAssetImage

data class VipTierConfig(
    val level: Int,
    val key: String,
    val name: String,
    val badgeSvg: String,
    val priceCoins: Int,
    val durationDays: Int,
    val color: Color,
    val gradientColors: List<Color>,
    val description: String
)

val VIP_TIERS = listOf(
    VipTierConfig(
        level = 1,
        key = "name-vip-green",
        name = "GREEN VIP",
        badgeSvg = "green_vip.svg",
        priceCoins = 1500000,
        durationDays = 7,
        color = Color(0xFF10B981),
        gradientColors = listOf(Color(0xFF34D399), Color(0xFF059669)),
        description = "Unlocks Glowing Green Username, Green VIP Badge & Priority Room Entrance."
    ),
    VipTierConfig(
        level = 2,
        key = "name-vip-yellow",
        name = "YELLOW VIP",
        badgeSvg = "yellow_vip.svg",
        priceCoins = 3000000,
        durationDays = 7,
        color = Color(0xFFFACC15),
        gradientColors = listOf(Color(0xFFFBBF24), Color(0xFFD97706)),
        description = "Unlocks Radiant Gold Name Tag, Gold VIP Badge & Gold Entrance."
    ),
    VipTierConfig(
        level = 3,
        key = "name-vip-pink",
        name = "PINK VIP",
        badgeSvg = "pink_vip.svg",
        priceCoins = 6000000,
        durationDays = 7,
        color = Color(0xFFEC4899),
        gradientColors = listOf(Color(0xFFF472B6), Color(0xFFDB2777)),
        description = "Unlocks Neon Pink Glow Name, Pink VIP Badge & Magenta Room Entrance."
    ),
    VipTierConfig(
        level = 4,
        key = "name-vip-colorful",
        name = "COLORFUL VIP",
        badgeSvg = "colorful_vip.svg",
        priceCoins = 12000000,
        durationDays = 7,
        color = Color(0xFF818CF8),
        gradientColors = listOf(Color(0xFF818CF8), Color(0xFF4F46E5)),
        description = "Unlocks Animated Rainbow Name, Supreme Badge & Full Room Entrance Flash."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VipScreen(
    userProfile: UserProfile,
    userCoins: Int = 58200,
    activeVipKey: String = "name-vip-green",
    purchasedVipTiers: Set<String> = setOf("name-vip-green"),
    onActivateVip: (String, String) -> Unit = { _, _ -> },
    onBuyVip: (String, String, Int) -> Unit = { _, _, _ -> },
    onBack: () -> Unit,
    onOpenWallet: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)
    var selectedLevel by remember { mutableIntStateOf(1) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showInsufficientCoinsDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var purchasedVipName by remember { mutableStateOf("") }

    val selectedVipTier = VIP_TIERS[(selectedLevel - 1).coerceIn(0, VIP_TIERS.size - 1)]
    val isOwned = purchasedVipTiers.contains(selectedVipTier.key)
    val isActive = activeVipKey == selectedVipTier.key
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
                .verticalScroll(rememberScrollState())
        ) {
            // ==========================================
            // TOP SECTION: HERO STAGE & VIP CENTER
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1E160A),
                                Color(0xFF120E06),
                                Color(0xFF0A0A0A)
                            )
                        )
                    )
            ) {
                // Background Stage Asset
                YaraanAssetImage(
                    assetName = "svip_bg.svg",
                    contentDescription = "VIP Stage Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    useAnimatedWebView = true
                )

                // Golden Spotlight Rays
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    selectedVipTier.color.copy(alpha = 0.35f),
                                    Color.Transparent
                                ),
                                radius = 600f
                            )
                        )
                )

                Column(modifier = Modifier.fillMaxSize()) {
                    // Top Bar (Back, Title, Coins)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = "VIP CENTER",
                            color = Color(0xFFFFD54F),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )

                        // Coins Chip
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.Black.copy(alpha = 0.6f))
                                .border(1.dp, Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                                .clickable { onOpenWallet() }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Coins",
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatCoinsShort(userCoins),
                                color = Color(0xFFFFD54F),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // VIP TABS
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VIP_TIERS.forEach { tier ->
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
                                    fontSize = if (isSelected) 15.sp else 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.35f)
                                )
                                if (isSelected) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(40.dp)
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

                    // STAGE EMBLEM & PEDESTAL
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        // Glowing Pedestal Platform Base
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 10.dp)
                                .width(220.dp)
                                .height(36.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            selectedVipTier.color.copy(alpha = 0.6f),
                                            Color(0xFF2A2015),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .border(1.dp, Color(0xFFFFD54F).copy(alpha = 0.5f), CircleShape)
                        )

                        // Glowing Aura
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                                .background(selectedVipTier.color.copy(alpha = 0.3f))
                        )

                        // Main VIP Badge / Medal
                        YaraanAssetImage(
                            assetName = selectedVipTier.badgeSvg,
                            contentDescription = selectedVipTier.name,
                            modifier = Modifier.size(150.dp),
                            useAnimatedWebView = true
                        )
                    }
                }
            }

            // ==========================================
            // FLOATING PURCHASE BOX
            // ==========================================
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A150E)),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3A2A18)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-16).dp)
                    .shadow(16.dp, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "VALIDITY",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${selectedVipTier.durationDays} Days",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "PRICE",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = "Price",
                                    tint = Color(0xFFFFD54F),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "%,d".format(selectedVipTier.priceCoins),
                                    color = Color(0xFFFFD54F),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // COMPACT ELEGANT PURCHASE BUTTON
                    Button(
                        onClick = {
                            if (isActive) {
                                // Already active
                            } else if (isOwned) {
                                onActivateVip(selectedVipTier.key, selectedVipTier.name)
                                purchasedVipName = selectedVipTier.name
                                showSuccessDialog = true
                            } else {
                                if (userCoins < selectedVipTier.priceCoins) {
                                    showInsufficientCoinsDialog = true
                                } else {
                                    showConfirmDialog = true
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(22.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(
                                if (isActive) Brush.horizontalGradient(listOf(Color(0xFF374151), Color(0xFF1F2937)))
                                else Brush.horizontalGradient(selectedVipTier.gradientColors),
                                shape = RoundedCornerShape(22.dp)
                            )
                    ) {
                        Text(
                            text = if (isActive) "VIP ACTIVE ✓" else if (isOwned) "EQUIP NOW" else "PURCHASE VIP",
                            color = if (isActive) Color(0xFF00E676) else Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ==========================================
            // IDENTIFICATION SECTION
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color.Gray.copy(alpha = 0.5f))
                                )
                            )
                    )
                    Text(
                        text = "IDENTIFICATION",
                        color = Color.Gray,
                        fontSize = 11.sp,
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
                                    listOf(Color.Gray.copy(alpha = 0.5f), Color.Transparent)
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 3 FEATURE CARDS
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 1. VIP Medal
                    IdentificationCard(
                        modifier = Modifier.weight(1f),
                        label = "VIP Medal"
                    ) {
                        YaraanAssetImage(
                            assetName = selectedVipTier.badgeSvg,
                            contentDescription = "VIP Medal",
                            modifier = Modifier.size(46.dp),
                            useAnimatedWebView = true
                        )
                    }

                    // 2. Colored Name
                    IdentificationCard(
                        modifier = Modifier.weight(1f),
                        label = "Colored Name"
                    ) {
                        Text(
                            text = userProfile.nickname.ifBlank { "Yaraan" },
                            color = selectedVipTier.color,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    // 3. Room Premium
                    IdentificationCard(
                        modifier = Modifier.weight(1f),
                        label = "Room Premium"
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Room Premium",
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // CONFIRMATION DIALOG
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                containerColor = Color(0xFF1B1526),
                titleContentColor = Color.White,
                textContentColor = Color.White,
                icon = {
                    YaraanAssetImage(
                        assetName = selectedVipTier.badgeSvg,
                        contentDescription = selectedVipTier.name,
                        modifier = Modifier.size(72.dp),
                        useAnimatedWebView = true
                    )
                },
                title = {
                    Text(
                        text = "Purchase ${selectedVipTier.name}?",
                        fontWeight = FontWeight.Black,
                        fontSize = 19.sp,
                        color = selectedVipTier.color
                    )
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Are you sure you want to purchase ${selectedVipTier.name} for 7 Days?",
                            fontSize = 13.sp,
                            color = Color(0xFFDDDDDD),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.6f))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Price", color = Color.Gray, fontSize = 12.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD54F),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "%,d Coins".format(selectedVipTier.priceCoins),
                                    color = Color(0xFFFFD54F),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (userCoins >= selectedVipTier.priceCoins) {
                                onBuyVip(selectedVipTier.key, selectedVipTier.name, selectedVipTier.priceCoins)
                                showConfirmDialog = false
                                purchasedVipName = selectedVipTier.name
                                showSuccessDialog = true
                            } else {
                                showConfirmDialog = false
                                showInsufficientCoinsDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        modifier = Modifier.background(
                            Brush.horizontalGradient(selectedVipTier.gradientColors),
                            shape = RoundedCornerShape(20.dp)
                        )
                    ) {
                        Text("YES, PURCHASE", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text("CANCEL", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            )
        }

        // INSUFFICIENT COINS DIALOG
        if (showInsufficientCoinsDialog) {
            AlertDialog(
                onDismissRequest = { showInsufficientCoinsDialog = false },
                containerColor = Color(0xFF1E1528),
                titleContentColor = Color(0xFFFF5252),
                textContentColor = Color.White,
                icon = {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(40.dp)
                    )
                },
                title = {
                    Text(
                        text = "Insufficient Coins",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "You do not have enough coins to purchase ${selectedVipTier.name}.",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Required: ", color = Color.Gray, fontSize = 12.sp)
                            Text("%,d".format(selectedVipTier.priceCoins), color = Color(0xFFFFD54F), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Your Balance: ", color = Color.Gray, fontSize = 12.sp)
                            Text("%,d".format(userCoins), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showInsufficientCoinsDialog = false
                            onOpenWallet()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD54F))
                    ) {
                        Text("Recharge Now", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showInsufficientCoinsDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }

        // SUCCESS DIALOG
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                containerColor = Color(0xFF141D17),
                titleContentColor = Color(0xFF00E676),
                textContentColor = Color.White,
                icon = {
                    YaraanAssetImage(
                        assetName = selectedVipTier.badgeSvg,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        useAnimatedWebView = true
                    )
                },
                title = {
                    Text(
                        text = "Success! 🎉",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = Color(0xFF00E676)
                    )
                },
                text = {
                    Text(
                        text = "$purchasedVipName Activated for 7 Days.",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showSuccessDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))
                    ) {
                        Text("OK", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
private fun IdentificationCard(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .height(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF2A2015), Color(0xFF15100A))
                )
            )
            .border(1.dp, Color(0xFF3A2A18), RoundedCornerShape(16.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
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
                text = label,
                color = Color(0xFFCCCCCC),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatCoinsShort(coins: Int): String {
    return when {
        coins >= 1_000_000 -> "%.1fM".format(coins / 1_000_000f)
        coins >= 1_000 -> "%.1fK".format(coins / 1_000f)
        else -> coins.toString()
    }
}
