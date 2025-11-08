package com.rodrigmatrix.weatheryou.components.particle

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.components.extensions.getGradientList
import com.rodrigmatrix.weatheryou.components.preview.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.core.state.produceParticleTick
import kotlin.math.absoluteValue

@Composable
fun Clouds(
    tint: ColorFilter,
    particleAnimationIteration: Long,
    cloudCount: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val cloudsParameters = PrecipitationsParameters(
            particleCount = cloudCount,
            distancePerStep = 1,
            minSpeed = 0.2f,
            maxSpeed = 0.6f,
            minAngle = 0,
            maxAngle = 0,
            shape = PrecipitationShape.Image(
                image = ImageBitmap.imageResource(R.drawable.image_cloud_1),
                minWidth = 1000,
                maxWidth = 1920,
                minHeight = 800,
                maxHeight = 1000,
                colorFilter = tint,
                alpha = 0.4f,
            ),
            sourceEdge = PrecipitationSourceEdge.RIGHT
        )

        Particles(
            modifier = Modifier.fillMaxSize(),
            iteration = particleAnimationIteration,
            parameters = cloudsParameters
        )
    }
}


data class Particle(
    var n: Long = 0,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
    var speed: Float,
    var angle: Int,
)


class ParticleSystemHelper(
    private val parameters: PrecipitationsParameters,
    private val frameWidth: Int,
    private val frameHeight: Int
) {

    private val _particles = mutableListOf<Particle>()
    val particles: List<Particle> = _particles

    fun generateParticles() {
        while (_particles.size < parameters.particleCount) {
            _particles.add(createParticle())
        }
    }

    private fun createParticle(): Particle {
        val randomWidth = getRandomWidth()
        val randomHeight = getRandomHeight(randomWidth)
        val angle = computeAngle()

        return Particle(
            x = generateX(),
            y = generateY(),
            width = randomWidth,
            height = randomHeight,
            speed = Random.nextFloat() * (parameters.maxSpeed - parameters.minSpeed) + parameters.minSpeed,
            angle = angle
        )
    }

    private fun generateX(startFromSourceEdge: Boolean = false): Float {
        val randomX = Random.nextInt(frameWidth.absoluteValue.ifZero { 1 }).toFloat()
        return when (parameters.sourceEdge) {
            PrecipitationSourceEdge.TOP -> randomX
            PrecipitationSourceEdge.RIGHT -> if (startFromSourceEdge) frameWidth.toFloat() else randomX
            PrecipitationSourceEdge.BOTTOM -> randomX
            PrecipitationSourceEdge.LEFT -> if (startFromSourceEdge) 0f else randomX
        }
    }

    private fun generateY(startFromSourceEdge: Boolean = false): Float {
        val randomY = Random.nextInt(frameHeight.absoluteValue.ifZero { 1 }).toFloat()
        return when (parameters.sourceEdge) {
            PrecipitationSourceEdge.TOP -> if (startFromSourceEdge) 0f else randomY
            PrecipitationSourceEdge.RIGHT -> randomY
            PrecipitationSourceEdge.BOTTOM -> if (startFromSourceEdge) frameHeight.toFloat() else randomY
            PrecipitationSourceEdge.LEFT -> randomY
        }
    }

    private fun isOutOfFrame(particle: Particle): Boolean {
        return when (parameters.sourceEdge) {
            PrecipitationSourceEdge.TOP -> {
                particle.y > frameHeight || particle.x < 0 || particle.x > frameWidth
            }
            PrecipitationSourceEdge.RIGHT -> {
                val result = particle.y - particle.height > frameHeight ||
                        particle.y + particle.height < 0 ||
                        particle.x + particle.width < 0

                if (result) {
                    Log.d("isOutOfFrame", "$result $particle $frameWidth $frameHeight")
                }
                result
            }
            PrecipitationSourceEdge.BOTTOM -> {
                particle.y < 0 || particle.x < 0 || particle.x > frameWidth
            }
            PrecipitationSourceEdge.LEFT -> {
                particle.y - particle.height > frameHeight ||
                        particle.y + particle.height < 0 ||
                        particle.x - particle.width > frameWidth
            }
        }
    }

    fun updateParticles(iteration: Long) {
        particles.forEach { particle ->
            if (isOutOfFrame(particle)) {
                val randomWidth = getRandomWidth()
                val randomHeight = getRandomHeight(randomWidth)
                val angle = computeAngle()

                particle.n = iteration
                particle.width = randomWidth
                particle.height = randomHeight
                particle.x = generateX(startFromSourceEdge = true)
                particle.y = generateY(startFromSourceEdge = true)
                particle.speed =
                    Random.nextFloat() * (parameters.maxSpeed - parameters.minSpeed) + parameters.minSpeed
                particle.angle = angle
            } else {
                particle.n = iteration
                particle.x -= (parameters.distancePerStep * particle.speed) * cos(
                    Math.toRadians(particle.angle.toDouble())
                ).toFloat()
                particle.y -= (parameters.distancePerStep * particle.speed) * sin(
                    Math.toRadians(particle.angle.toDouble())
                ).toFloat()
            }
        }
    }

    private fun getRandomWidth(): Float {
        return when (parameters.shape) {
            is PrecipitationShape.Circle -> Random.nextInt(
                parameters.shape.minRadius,
                parameters.shape.maxRadius
            ).toFloat()
            is PrecipitationShape.Line -> Random.nextInt(
                parameters.shape.minStrokeWidth,
                parameters.shape.maxStrokeWidth
            ).toFloat()
            is PrecipitationShape.Image -> Random.nextInt(
                parameters.shape.minWidth,
                parameters.shape.maxWidth
            ).toFloat()
        }
    }

    private fun getRandomHeight(width: Float): Float {
        return when (parameters.shape) {
            is PrecipitationShape.Circle -> width
            is PrecipitationShape.Line -> Random.nextInt(
                parameters.shape.minHeight,
                parameters.shape.maxHeight
            ).toFloat()
            is PrecipitationShape.Image -> Random.nextInt(
                parameters.shape.minHeight,
                parameters.shape.maxHeight
            ).toFloat()
        }
    }

    private fun computeAngle(): Int {
        return if (parameters.minAngle == parameters.maxAngle) {
            parameters.maxAngle
        } else {
            Random.nextInt(parameters.minAngle, parameters.maxAngle)
        }
    }
}

private fun Int.ifZero(function: () -> Int): Int {
    return if (this <= 0) {
        function()
    } else {
        this
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Particles(
    modifier: Modifier = Modifier,
    iteration: Long,
    blinkAnimation: Boolean = false,
    parameters: PrecipitationsParameters
) {

    var particles by remember {
        mutableStateOf(
            listOf<Particle>()
        )
    }
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        val particleGenerator by remember {
            mutableStateOf(
                ParticleSystemHelper(
                    parameters, constraints.maxWidth, constraints.maxHeight
                )
            )
        }
        val blinkAnimationList = if (blinkAnimation) {
            val infiniteTransition = rememberInfiniteTransition(label = "")
            particleGenerator.particles.map {
                infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(Random.nextInt(from = 1000, until = 5000)),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )
            }
        } else {
            emptyList()
        }
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                particleGenerator.generateParticles()
                particleGenerator.updateParticles(iteration)
                particles = particleGenerator.particles
                particleGenerator.particles.forEachIndexed { index, particle ->
                    when (parameters.shape) {
                        is PrecipitationShape.Circle -> {
                            drawCircle(
                                color = parameters.shape.color,
                                radius = particle.width,
                                center = Offset(particle.x, particle.y),
                                alpha = blinkAnimationList.getOrNull(index)?.value ?: 1f,
                            )
                        }
                        is PrecipitationShape.Line -> {
                            val endX = particle.x - particle.height * cos(
                                Math.toRadians(particle.angle.toDouble())
                            ).toFloat()
                            val endY = particle.y - particle.height * sin(
                                Math.toRadians(particle.angle.toDouble())
                            ).toFloat()
                            drawLine(
                                color = parameters.shape.color,
                                pathEffect = PathEffect.cornerPathEffect(20f),
                                start = Offset(particle.x, particle.y),
                                end = Offset(endX, endY),
                                strokeWidth = particle.width
                            )
                        }
                        is PrecipitationShape.Image -> {
                            val x = particle.x.toInt()
                            val y = particle.y.toInt()
                            drawImage(
                                image = parameters.shape.image,
                                dstOffset = IntOffset(x, y),
                                dstSize = IntSize(particle.width.toInt(), particle.height.toInt()),
                                colorFilter = parameters.shape.colorFilter,
                                alpha = parameters.shape.alpha,
                            )
                        }
                    }
                }
            }
        )
    }
}

sealed class PrecipitationShape {

    data class Circle(
        val minRadius: Int,
        val maxRadius: Int,
        val color: Color,
    ) : PrecipitationShape()

    data class Line(
        val minStrokeWidth: Int,
        val maxStrokeWidth: Int,
        val minHeight: Int,
        val maxHeight: Int,
        val color: Color,
    ) : PrecipitationShape()

    data class Image(
        val image: ImageBitmap,
        val minWidth: Int,
        val maxWidth: Int,
        val minHeight: Int,
        val maxHeight: Int,
        val colorFilter: ColorFilter,
        val alpha: Float = 1f,
    ) : PrecipitationShape()
}

enum class PrecipitationSourceEdge {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT
}

data class PrecipitationsParameters(
    val particleCount: Int,
    val distancePerStep: Int,
    val minSpeed: Float,
    val maxSpeed: Float,
    val minAngle: Int,
    val maxAngle: Int,
    val shape: PrecipitationShape,
    val sourceEdge: PrecipitationSourceEdge
)

val snowParameters = PrecipitationsParameters(
    particleCount = 200,
    distancePerStep = 5,
    minSpeed = 0.1f,
    maxSpeed = 1f,
    minAngle = 260,
    maxAngle = 280,
    shape = PrecipitationShape.Circle(
        minRadius = 1,
        maxRadius = 10,
        color = Color.White,
    ),
    sourceEdge = PrecipitationSourceEdge.TOP
)

val rainParameters = PrecipitationsParameters(
    particleCount = 600,
    distancePerStep = 30,
    minSpeed = 0.7f,
    maxSpeed = 1f,
    minAngle = 265,
    maxAngle = 285,
    shape = PrecipitationShape.Line(
        minStrokeWidth = 1,
        maxStrokeWidth = 3,
        minHeight = 10,
        maxHeight = 15,
        color = Color(0xffb3e5fe),
    ),
    sourceEdge = PrecipitationSourceEdge.TOP
)

val hailParameters = PrecipitationsParameters(
    particleCount = 200,
    distancePerStep = 10,
    minSpeed = 0.6f,
    maxSpeed = 1f,
    minAngle = 260,
    maxAngle = 280,
    shape = PrecipitationShape.Circle(
        minRadius = 3,
        maxRadius = 6,
        color = Color.Gray,
    ),
    sourceEdge = PrecipitationSourceEdge.TOP
)

val starsParameters = PrecipitationsParameters(
    particleCount = 600,
    distancePerStep = 0,
    minSpeed = 0f,
    maxSpeed = 1f,
    minAngle = 265,
    maxAngle = 285,
    shape = PrecipitationShape.Circle(
        minRadius = 1,
        maxRadius = 4,
        color = Color.White,
    ),
    sourceEdge = PrecipitationSourceEdge.TOP
)

@Preview
@Composable
fun RainPreview() {
    val particleTick = produceParticleTick()
    Box(
        modifier = Modifier.background(
            Brush.linearGradient(
                PreviewWeatherLocation.getGradientList()
            )
        )
    ) {
        Particles(iteration = particleTick.toLong(), parameters = rainParameters)
    }
}

@Preview
@Composable
fun SnowPreview() {
    val particleTick = produceParticleTick()
    Box(
        modifier = Modifier.background(
            Brush.linearGradient(
                PreviewWeatherLocation.getGradientList()
            )
        )
    ) {
        Particles(iteration = particleTick.toLong(), parameters = snowParameters)
    }
}


@Preview
@Composable
fun CloudsPreview() {
    val particleTick = produceParticleTick()
    Box(
        modifier = Modifier.background(
            Brush.linearGradient(
                PreviewWeatherLocation.getGradientList()
            )
        )
    ) {
        Clouds(
            tint = ColorFilter.tint(
                Color.Black.copy(alpha = 0.2f),
                BlendMode.SrcAtop
            ),
            particleAnimationIteration = particleTick.toLong(),
            cloudCount = 8
        )
    }
}


@Preview
@Composable
fun ThunderstormPreview() {
    val particleTick = produceParticleTick()
    Box(
        modifier = Modifier.background(
            Brush.linearGradient(
                PreviewWeatherLocation.getGradientList()
            )
        )
    ) {
        Thunder(
            particleAnimationIteration = particleTick.toLong(),
            width = 600,
            height = 400,
        )
    }
}