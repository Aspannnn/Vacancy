package kz.aspan.vacancy.common

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.renderer.LegendRenderer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlin.math.roundToInt

class CustomLegendRender(viewPortHandler: ViewPortHandler, legend: Legend) :
    LegendRenderer(viewPortHandler, legend) {

    override fun drawForm(c: Canvas, x: Float, y: Float, entry: LegendEntry, legend: Legend) {
        val form: Legend.LegendForm = when (entry.form) {
            Legend.LegendForm.DEFAULT -> legend.form
            else -> entry.form
        }

        if (form == Legend.LegendForm.DEFAULT || form == Legend.LegendForm.CIRCLE) {
            drawFormCircle(c, x, y, entry, legend)
        } else {
            super.drawForm(c, x, y, entry, legend)
        }
    }

    private fun drawFormCircle(c: Canvas, x: Float, y: Float, entry: LegendEntry, legend: Legend) {
        mLegendFormPaint.color = getColorWithAlpha(entry.formColor, 100f)
        mLegendFormPaint.style = Paint.Style.FILL

        val halfFormSize = 0.5f * Utils.convertDpToPixel(
            if (java.lang.Float.isNaN(entry.formSize)) legend.formSize else entry.formSize
        )

        c.drawCircle(x + halfFormSize, y, halfFormSize, mLegendFormPaint)
        mLegendFormPaint.color = entry.formColor
        mLegendFormPaint.style = Paint.Style.STROKE
        mLegendFormPaint.strokeWidth = 8f
        c.drawCircle(x + halfFormSize, y, halfFormSize, mLegendFormPaint)
    }

    private fun getColorWithAlpha(color: Int, ratio: Float): Int {
        return Color.argb(
            (Color.alpha(color) * ratio).roundToInt(),
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }
}

