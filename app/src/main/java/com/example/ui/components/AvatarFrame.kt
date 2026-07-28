package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R

@Composable
fun AvatarFrame(
    avatarRes: Int = R.drawable.img_user_avatar,
    size: Dp = 80.dp,
    showDesignerFrame: Boolean = true,
    frameAsset: String? = "svip1_frame.svg",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Inner Avatar image
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "User Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size * 0.72f)
                .clip(CircleShape)
                .border(1.5.dp, Color.White, CircleShape)
        )

        if (showDesignerFrame && frameAsset != null) {
            // Animated SVGA / SVG Frame overlay
            YaraanAssetImage(
                assetName = frameAsset,
                contentDescription = "Avatar Frame",
                modifier = Modifier.size(size),
                contentScale = ContentScale.Fit,
                autoPlay = true,
                loops = INFINITE
            )
        } else if (showDesignerFrame) {
            // Outer golden-winged glow circle fallback
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            listOf(
                                Color(0xFFFFD700),
                                Color(0xFFFF4081),
                                Color(0xFF7C4DFF),
                                Color(0xFF00E5FF),
                                Color(0xFFFFD700)
                            )
                        )
                    )
                    .padding(3.dp)
            )
        }
    }
}
