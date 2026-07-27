package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LevelBadge(level: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFFFB300), Color(0xFFFF6F00), Color(0xFFD50000))
                )
            )
            .border(1.dp, Color(0xFFFFF59D), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(10.dp)
            )
            Text(
                text = "LV.$level",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SvipBadge(svip: String, modifier: Modifier = Modifier) {
    val num = svip.replace("SVIP", "").trim().toIntOrNull() ?: 1
    val svipAsset = "svip${num.coerceIn(1, 5)}_badge.svg"

    YaraanAssetImage(
        assetName = svipAsset,
        contentDescription = svip,
        modifier = modifier.height(18.dp)
    )
}

@Composable
fun VerificationBadge(modifier: Modifier = Modifier) {
    YaraanAssetImage(
        assetName = "virfication_badge.svg",
        contentDescription = "Verified",
        modifier = modifier.size(16.dp)
    )
}

@Composable
fun VBadge(modifier: Modifier = Modifier) {
    YaraanAssetImage(
        assetName = "v_badge.svg",
        contentDescription = "V Badge",
        modifier = modifier.size(16.dp)
    )
}

@Composable
fun BrandBadge(brand: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFFF9800), Color(0xFFE65100))
                )
            )
            .border(1.dp, Color(0xFFFFE082), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = brand,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun IdBadge(id: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFFF4081), Color(0xFFE040FB))
                )
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ID $id",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun MedalBadge(number: Int, colorStart: Long, colorEnd: Long) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    listOf(Color(colorStart), Color(colorEnd))
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$number",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MedalRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MedalBadge(1, 0xFFE040FB, 0xFF7C4DFF)
        MedalBadge(2, 0xFFFF4081, 0xFFF50057)
        MedalBadge(3, 0xFFFFB300, 0xFFFF6F00)
        MedalBadge(4, 0xFF00E5FF, 0xFF0091EA)
        MedalBadge(6, 0xFF76FF03, 0xFF33691E)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color(0xFFFF1744),
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "100",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}
