package com.github.ked4ama.composecolortool.picker

import android.graphics.*
import android.graphics.Color.HSVToColor
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.graphics.SweepGradient
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.twotone.ChangeHistory
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.applyCanvas
import com.github.ked4ama.composecolortool.ui.ComposeColorToolTheme
import java.util.*
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun ColorPicker(modifier: Modifier = Modifier) {
    var baseColor by remember { mutableStateOf(Color.White.toArgb()) }
    var selectedColor by remember { mutableStateOf(Color.White.toArgb()) }
    Column(modifier.fillMaxSize()) {
        ColorPickerPalette(
            Modifier.fillMaxWidth().aspectRatio(1F),
            updatePointer = {
                baseColor = it
            }
        )
        ColorPickerValueBar(
            Modifier.fillMaxWidth(),
            baseColor = baseColor,
            updatePointer = {
                selectedColor = it
            }
        )
        Text(text = "#%6x".format(selectedColor).toUpperCase(Locale.getDefault()))
    }
}

@Preview
@Composable
fun ColorPickerPreview() {
    ComposeColorToolTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ColorPicker()
        }
    }
}

@Composable
fun ColorPickerValueBar(
    modifier: Modifier = Modifier,
    @ColorInt baseColor: Int = Color.White.toArgb(),
    updatePointer: (Int) -> Unit = { _ -> }
) {
    // (0.0, 0.0) - (1.0, 1.0)
    var pickPosition by remember { mutableStateOf(0.0F) }
    ConstraintLayout(modifier.wrapContentHeight()) {
        val horiGuide = createGuidelineFromStart(0.1F + pickPosition * 0.8F)
        val (spacer, marker, palette) = createRefs()
        Spacer(modifier = Modifier.constrainAs(spacer) {
            this.top.linkTo(parent.top)
            width = Dimension.percent(0.08F)
        }.aspectRatio(1F))
        Image(
            imageVector = Icons.TwoTone.ChangeHistory,
            modifier = Modifier
                .constrainAs(marker) {
                    this.top.linkTo(parent.top)
                    this.start.linkTo(horiGuide)
                    this.end.linkTo(horiGuide)
                    width = Dimension.percent(0.08F)
                }.aspectRatio(1F).rotate(180F),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.Gray)
        )
        ColorPickerValueBarCanvas(
            modifier = Modifier.constrainAs(palette) {
                this.top.linkTo(spacer.bottom)
                centerHorizontallyTo(parent)
                width = Dimension.percent(0.8F)
            }.aspectRatio(10F).clip(RoundedCornerShape(4.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
            baseColor = baseColor,
            updatePointer = { xp, color ->
                pickPosition = xp
                updatePointer(color)
            }
        )
    }
}

@Preview
@Composable
fun ColorPickerValueBarPreview() {
    ComposeColorToolTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxWidth()) {
            ColorPickerValueBar()
        }
    }
}

@Composable
fun ColorPickerValueBarCanvas(
    modifier: Modifier = Modifier,
    @ColorInt baseColor: Int = Color.White.toArgb(),
    updatePointer: (xPercent: Float, color: Int) -> Unit = { _, _ -> }
) {
    var canvasSize = remember { Size(1F, 1F) }
    var xp by remember { mutableStateOf(0F) }
    val width = 256 * 3 - 1
    val paletteBitmap = remember(baseColor) {
        Bitmap.createBitmap(
            width + 1, 1, Bitmap.Config.ARGB_8888
        ).applyCanvas {
            val linearGradient = LinearGradient(
                ((width + 1) / 256).toFloat(), 0F,
                (width + 1).toFloat(), 0F,
                baseColor, Color.Black.toArgb(),
                TileMode.CLAMP
            )
            val p = Paint()
            p.isDither = true
            p.shader = linearGradient

            drawRect(0F, 0F, (width + 1).toFloat(), 1F, p)
        }.also {
            if (width > 10) {
                println((width * xp).toInt().coerceIn(0, width))
                val color = it.getPixel((width * xp).toInt().coerceIn(0, width), 0)
                updatePointer(xp, color)
            }
        }
    }
    Canvas(modifier = modifier.fillMaxSize().pointerInteropFilter {
        xp = (it.x / canvasSize.width).coerceIn(0.0F, 1.0F)
        val color = paletteBitmap.getPixel((width * xp).toInt().coerceIn(0, width), 0)
        when (it.action) {
            MotionEvent.ACTION_DOWN -> {
                updatePointer(xp, color)
            }
            MotionEvent.ACTION_MOVE -> {
                updatePointer(xp, color)
            }
        }
        true
    }) {
        canvasSize = size
        drawImage(
            paletteBitmap.asImageBitmap(),
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )
    }
}

//@Preview
@Composable
fun ColorPickerValueBarCanvasPreview() {
    ComposeColorToolTheme {
        Surface(modifier = Modifier.fillMaxWidth().aspectRatio(10F)) {
            ColorPickerValueBarCanvas()
        }
    }
}

@Composable
fun ColorPickerPalette(
    modifier: Modifier = Modifier,
    updatePointer: (Int) -> Unit = { _ -> }
) {
    // (0.0, 0.0) - (1.0, 1.0)
    var pickPosition by remember { mutableStateOf(Pair(0.5F, 0.5F)) }
    ConstraintLayout(modifier) {
        val horiGuide = createGuidelineFromStart(0.1F + pickPosition.first * 0.8F)
        val vertGuide = createGuidelineFromTop(0.1F + pickPosition.second * 0.8F)
        val (palette, marker) = createRefs()
        ColorPickerPaletteCanvas(
            Modifier.constrainAs(palette) {
                centerTo(parent)
                width = Dimension.percent(0.8F)
                height = Dimension.percent(0.8F)
            },
            updatePointer = { xp, yp, color ->
                pickPosition = xp to yp
                if (color != 0) {
                    updatePointer(color)
                }
            }
        )
        // TODO set shadow (for user visibility)
        Image(
            imageVector = Icons.Filled.Place,
            modifier = Modifier
                .constrainAs(marker) {
                    this.bottom.linkTo(vertGuide)
                    this.start.linkTo(horiGuide)
                    this.end.linkTo(horiGuide)
                    width = Dimension.percent(0.1F)
                    height = Dimension.percent(0.1F)
                },
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.Gray)
        )
    }
}

@Preview
@Composable
fun ColorPickerPalettePreview() {
    ComposeColorToolTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxWidth().aspectRatio(1F)) {
            ColorPickerPalette()
        }
    }
}

@Composable
fun ColorPickerPaletteCanvas(
    modifier: Modifier = Modifier,
    updatePointer: (xPercent: Float, yPercent: Float, color: Int) -> Unit = { _, _, _ -> }
) {
    var canvasSize = remember {
        Size(1F, 1F)
    }
    val width = 256 * 3 - 1
    val height = 256 * 3 - 1
    val paletteBitmap = remember {
        Bitmap.createBitmap(
            width + 1, height + 1, Bitmap.Config.ARGB_8888
        ).applyCanvas {
            val cx = (width + 1).toFloat() / 2
            val cy = (height + 1).toFloat() / 2
            val radius = min(cx, cy)
            val radialGradient = RadialGradient(
                cx, cy, radius,
                -0x1,
                0x00FFFFFF,
                Shader.TileMode.CLAMP
            )
            val colors = IntArray(13)
            val hsv = FloatArray(3)
            hsv[1] = 1F
            hsv[2] = 1F
            for (i in 0..11) {
                hsv[0] = (360 / 12 * i).toFloat()
                colors[i] = HSVToColor(hsv)
            }
            colors[12] = colors[0]

            val sweepGradient = SweepGradient(cx, cy, colors, null)
            val shader = ComposeShader(radialGradient, sweepGradient, PorterDuff.Mode.DST_OVER)

            val p = Paint()
            p.isDither = true
            p.shader = shader

            drawCircle(cx, cy, radius, p)
            p.isDither = false
            p.shader = null
            p.color = android.graphics.Color.WHITE // white
            // enable to get white, draw white on the center
            drawCircle(cx, cy, 10F, p)
        }
    }
    Canvas(modifier = modifier.aspectRatio(1F).fillMaxSize().pointerInteropFilter {
        val point = circlePoint(min(canvasSize.width, canvasSize.height) / 2, it.x, it.y)
        val xp = (point.x / canvasSize.width).coerceIn(0.0F, 1.0F)
        val yp = (point.y / canvasSize.height).coerceIn(0.0F, 1.0F)
        val color = paletteBitmap.getPixel(
            (width * xp).toInt().coerceIn(0, width),
            (height * yp).toInt().coerceIn(0, height)
        )
        when (it.action) {
            MotionEvent.ACTION_DOWN -> {
                updatePointer(xp, yp, color)
            }
            MotionEvent.ACTION_MOVE -> {
                updatePointer(xp, yp, color)
            }
        }
        true
    }) {
        canvasSize = size
        drawImage(
            paletteBitmap.asImageBitmap(),
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )
    }
}

fun circlePoint(radial: Float, x: Float, y: Float): PointF {
    val l = sqrt((x - radial).pow(2) + (y - radial).pow(2))
    return PointF(
        min(radial, l) * (x - radial) / l + radial,
        min(radial, l) * (y - radial) / l + radial,
    )
}