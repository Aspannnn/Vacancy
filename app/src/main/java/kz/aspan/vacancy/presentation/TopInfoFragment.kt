package kz.aspan.vacancy.presentation

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.vacancy.R
import kz.aspan.vacancy.adapter.VacancyListAdapter
import kz.aspan.vacancy.common.Constants.EMPLOYER
import kz.aspan.vacancy.common.Constants.PROFESSION
import kz.aspan.vacancy.common.Constants.SKILL
import kz.aspan.vacancy.common.MarginItemDecoration
import kz.aspan.vacancy.common.extensions.navigateSafely
import kz.aspan.vacancy.common.extensions.px
import kz.aspan.vacancy.databinding.FragmentTopInfoBinding
import kz.aspan.vacancy.domain.model.SimpleData
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class TopInfoFragment : Fragment(R.layout.fragment_top_info) {
    private var _binding: FragmentTopInfoBinding? = null
    private val binding: FragmentTopInfoBinding
        get() = _binding!!


    @Inject
    lateinit var adapterVacancy: VacancyListAdapter

    private val args: TopInfoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTopInfoBinding.bind(view)

        val simple = args.vacancyList

        val count = if (simple.count >= 5) 5 else simple.count

        when (args.query) {
            PROFESSION -> requireActivity().findViewById<TextView>(R.id.toolBarTitle).text =
                getString(R.string.top_title, count, "професии")
            SKILL -> requireActivity().findViewById<TextView>(R.id.toolBarTitle).text =
                getString(R.string.top_title, count, "навыков")
            EMPLOYER -> requireActivity().findViewById<TextView>(R.id.toolBarTitle).text =
                getString(R.string.top_title, count, "работодатели")
        }

        adapterVacancy.submitList(simple.data)
        binding.rv.adapter = adapterVacancy
        binding.rv.addItemDecoration(MarginItemDecoration(10.px))

        adapterVacancy.setOnVacancyClickListener {
            findNavController().navigateSafely(R.id.action_detailFragment_to_vacanciesFragment,
                Bundle().apply {
                    putString("vacancy_id", it.id)
                    putInt("query", args.query)
                })

        }



        setUpPieChart(count)
        loadChartData(simple.data.take(5))
        fillLegend()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun fillLegend() {
        val entries = binding.pieChart.legend.entries

        for (entry in entries) {
            val entryColor = entry.formColor
            val text = entry.label

            val color = getColorWithAlpha(entryColor, 100f)
            val circleColor = setIconColor(color, entryColor)

            val textView = createTextView(text, circleColor)
            binding.legend.addView(textView)
        }
    }

    private fun getColorWithAlpha(color: Int, ratio: Float): Int {
        return Color.argb(
            (Color.alpha(color) * ratio).roundToInt(),
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }

    private fun setIconColor(color: Int, strokeColor: Int): GradientDrawable {
        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.circle) as GradientDrawable
        drawable.setStroke(8, strokeColor)
        drawable.setColor(color)
        return drawable
    }

    private fun setUpPieChart(center: Int) {
        binding.pieChart.apply {
            setDrawEntryLabels(false)
            setDrawMarkers(false)
            isDrawHoleEnabled = true
            holeRadius = 68f
            transparentCircleRadius = 75f
            centerText = getString(R.string.top_pie_chart, center)
            setCenterTextSize(15f)
            description.isEnabled = false
        }
        binding.pieChart.legend.isEnabled = false
    }

    private fun loadChartData(list: List<SimpleData>) {
        val entries = mutableListOf<PieEntry>()

        for (simpleData in list) {
            entries.add(PieEntry(simpleData.numberOfVacancies.toFloat(), simpleData.title))
        }

        val colors = mutableListOf<Int>()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }

        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }

        val dataSet = PieDataSet(entries, null)
        dataSet.sliceSpace = 3f
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setDrawValues(false)
        data.setValueFormatter(PercentFormatter(binding.pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)

        binding.pieChart.data = data
        binding.pieChart.invalidate()
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    private fun createTextView(text: String, d: Drawable): MaterialTextView {
        val textView = MaterialTextView(requireContext())
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 10)
        textView.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null)
        textView.compoundDrawablePadding = 14
        textView.text = text
        textView.textSize = 12f
        textView.layoutParams = params
        return textView
    }
}