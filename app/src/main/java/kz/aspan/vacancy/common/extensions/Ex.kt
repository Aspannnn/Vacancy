package kz.aspan.vacancy.common.extensions

import android.content.res.Resources
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.PieChart
import kz.aspan.vacancy.common.CustomLegendRender

fun PieChart.setCustomLegendRenderer() {
    val field = Chart::class.java.getDeclaredField("mLegendRenderer").apply { isAccessible = true }
    field.set(this, CustomLegendRender(viewPortHandler, legend))
}

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()