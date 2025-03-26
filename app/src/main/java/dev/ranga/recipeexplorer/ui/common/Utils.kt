package dev.ranga.recipeexplorer.ui.common

import kotlin.math.roundToInt

fun Double.toPercent(): String = (this * 100).roundToInt().toString() + "%"