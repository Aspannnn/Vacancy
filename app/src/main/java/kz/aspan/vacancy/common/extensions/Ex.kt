package kz.aspan.vacancy.common

import android.content.res.Resources
import android.view.ViewGroup
import androidx.annotation.Px
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.PieChart

fun PieChart.setCustomLegendRenderer() {
    val field = Chart::class.java.getDeclaredField("mLegendRenderer").apply { isAccessible = true }
    field.set(this, CustomLegendRender(viewPortHandler, legend))
}

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()