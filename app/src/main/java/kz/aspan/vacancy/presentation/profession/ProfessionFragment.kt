package kz.aspan.vacancy.presentation.profession

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.vacancy.R
import kz.aspan.vacancy.common.Constants.PROFESSION
import kz.aspan.vacancy.common.navigateSafely
import kz.aspan.vacancy.common.px
import kz.aspan.vacancy.common.setCustomLegendRenderer
import kz.aspan.vacancy.databinding.FragmentProfessionBinding
import kz.aspan.vacancy.domain.model.Simple
import kz.aspan.vacancy.domain.model.SimpleData
import kotlin.math.roundToInt

@AndroidEntryPoint
class ProfessionFragment : Fragment(R.layout.fragment_profession) {
    private var _binding: FragmentProfessionBinding? = null
    private val binding: FragmentProfessionBinding
        get() = _binding!!

    private val viewModel: ProfessionViewModel by viewModels()


    private lateinit var simple: Simple

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfessionBinding.bind(view)

        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
        navigate(viewPager)

        setTabTitle()
        subscribeToObservers()

        binding.professionPage.detailButton.setOnClickListener {
            findNavController().navigateSafely(
                R.id.action_homeFragment_to_detailFragment,
                Bundle().apply {
                    putSerializable("vacancy_list", simple)
                    putInt("query", PROFESSION)
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun navigate(viewPager: ViewPager2) {
        binding.professionPage.skillTabTitle.setOnClickListener {
            viewPager?.currentItem = 1
        }

        binding.professionPage.skillIndicator.setOnClickListener {
            viewPager?.currentItem = 1
        }

        binding.professionPage.employerTabTitle.setOnClickListener {
            viewPager?.currentItem = 2
        }

        binding.professionPage.employerIndicator.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }

    private fun setTabTitle() {
        binding.professionPage.professionIndicator.background.setTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.congress_lue
            )
        )
        binding.professionPage.professionTabTitle.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.congress_lue
            )
        )
        binding.professionPage.topInfoTv.text = getString(R.string.top, "профессии")
    }

    private fun subscribeToObservers() {
        viewModel.vacancyLiveData.observe(viewLifecycleOwner) {
            val top5 = it.data.take(5)
            createItemProfession(top5)

            simple = it

            setUpPieChart()
            loadChartData(top5)
            fillLegend()
        }
    }

    private fun fillLegend() {
        val entries = binding.professionPage.pieChart.legend.entries

        for (entry in entries) {
            val entryColor = entry.formColor
            val text = entry.label

            val color = getColorWithAlpha(entryColor, 100f)
            val circleColor = setIconColor(color, entryColor)

            val textView = createTextView(text, circleColor)
            binding.professionPage.legend.addView(textView)
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

    private fun setUpPieChart() {
        binding.professionPage.pieChart.apply {
            setDrawEntryLabels(false)
            setDrawMarkers(false)
            isDrawHoleEnabled = true
            holeRadius = 68f
            transparentCircleRadius = 75f
            centerText = "TOP 5"
            setCenterTextSize(15f)
            description.isEnabled = false
        }
        binding.professionPage.pieChart.legend.isEnabled = false
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
        data.setValueFormatter(PercentFormatter(binding.professionPage.pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)

        binding.professionPage.pieChart.data = data
        binding.professionPage.pieChart.invalidate()
        binding.professionPage.pieChart.animateY(1400, Easing.EaseInOutQuad)
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

    private fun createItemProfession(professions: List<SimpleData>) {

        val top5size = professions.size
        for (i in 0 until top5size) {
            val view = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_vacancy_info, binding.professionPage.parent, false)

            val title = view.findViewById<TextView>(R.id.titleTv)
            val subTitle = view.findViewById<TextView>(R.id.subTitleTv)
            val button = view.findViewById<Button>(R.id.vacancyButton)
            val description = view.findViewById<TextView>(R.id.descriptionTv)

            title.text = professions[i].title
            button.text = getString(R.string.count_vacancies, professions[i].numberOfVacancies)

            if (professions[i].description.isNullOrBlank()) {
                description.visibility = View.GONE
            } else {
                description.visibility = View.VISIBLE
                description.text = professions[i].description
            }

            if (professions[i].subTitle.isNullOrBlank()) {
                subTitle.visibility = View.GONE
            } else {
                subTitle.visibility = View.VISIBLE
                subTitle.text = professions[i].subTitle
            }


            val params = view.layoutParams as ViewGroup.MarginLayoutParams

            when (i) {
                0 -> params.setMargins(14.px, 12.px, 14.px, 0.px)
                top5size - 1 -> params.setMargins(14.px, 12.px, 14.px, 12.px)
                else -> params.setMargins(14.px, 6.px, 14.px, 0.px)
            }

            view.layoutParams = params

            binding.professionPage.parent.addView(view)

            button.setOnClickListener {
                findNavController().navigateSafely(R.id.action_homeFragment_to_vacanciesFragment,
                    Bundle().apply {
                        putString("vacancy_id", professions[i].id)
                        putInt("query", PROFESSION)
                    })
            }
        }

    }
}