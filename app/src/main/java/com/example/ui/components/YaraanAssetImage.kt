package com.example.ui.components

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest

private val assetCache = mutableMapOf<String, String>()

fun resolveAssetName(context: Context, requestedName: String): String {
    if (requestedName.isBlank()) return requestedName
    assetCache[requestedName]?.let { return it }

    val assetsList = try {
        context.assets.list("yaraan") ?: emptyArray()
    } catch (e: Exception) {
        emptyArray()
    }

    if (assetsList.contains(requestedName)) {
        assetCache[requestedName] = requestedName
        return requestedName
    }

    val baseName = requestedName.substringBeforeLast(".")
    val candidateExtensions = listOf(".svg", ".svga", ".json", ".webp", ".png", ".gif", ".lottie")

    for (ext in candidateExtensions) {
        val candidate = "$baseName$ext"
        if (assetsList.contains(candidate)) {
            assetCache[requestedName] = candidate
            return candidate
        }
    }

    for (ext in candidateExtensions) {
        val candidate = "$requestedName$ext"
        if (assetsList.contains(candidate)) {
            assetCache[requestedName] = candidate
            return candidate
        }
    }

    assetCache[requestedName] = requestedName
    return requestedName
}

@Composable
fun YaraanAssetImage(
    assetName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    autoPlay: Boolean = true,
    loops: Int = Int.MAX_VALUE,
    useAnimatedWebView: Boolean = false
) {
    val context = LocalContext.current
    val resolvedName = remember(assetName) { resolveAssetName(context, assetName) }

    val isLottieCandidate = resolvedName.endsWith(".json", ignoreCase = true) ||
            resolvedName.endsWith(".lottie", ignoreCase = true) ||
            resolvedName.endsWith(".svga", ignoreCase = true)

    key(resolvedName, autoPlay, loops) {
        if (isLottieCandidate) {
            val compositionResult = rememberLottieComposition(LottieCompositionSpec.Asset("yaraan/$resolvedName"))
            val composition = compositionResult.value

            if (composition != null) {
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = if (loops == Int.MAX_VALUE) LottieConstants.IterateForever else loops,
                    isPlaying = autoPlay
                )
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    contentScale = contentScale,
                    modifier = modifier
                )
            } else if (!compositionResult.isLoading) {
                CoilAssetImage(
                    resolvedName = resolvedName,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    contentScale = contentScale
                )
            }
        } else {
            CoilAssetImage(
                resolvedName = resolvedName,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
    }
}

@Composable
private fun CoilAssetImage(
    resolvedName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalContext.current
    val isSvg = resolvedName.endsWith(".svg", ignoreCase = true)

    val model = remember(resolvedName) {
        ImageRequest.Builder(context)
            .data("file:///android_asset/yaraan/$resolvedName")
            .memoryCacheKey(resolvedName)
            .diskCacheKey(resolvedName)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .apply {
                if (isSvg) {
                    decoderFactory(SvgDecoder.Factory())
                } else if (Build.VERSION.SDK_INT >= 28) {
                    decoderFactory(ImageDecoderDecoder.Factory())
                } else {
                    decoderFactory(GifDecoder.Factory())
                }
            }
            .crossfade(true)
            .allowHardware(true)
            .build()
    }

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}





