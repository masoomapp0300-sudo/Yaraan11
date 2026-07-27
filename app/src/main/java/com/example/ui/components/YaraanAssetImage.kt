package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun YaraanAssetImage(
    assetName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalContext.current
    val isSvg = assetName.endsWith(".svg", ignoreCase = true)

    val model = ImageRequest.Builder(context)
        .data("file:///android_asset/yaraan/$assetName")
        .apply {
            if (isSvg) {
                decoderFactory(SvgDecoder.Factory())
            }
        }
        .crossfade(true)
        .allowHardware(true)
        .build()

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}
