package kz.aspan.vacancy.presentation.vacancies

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.vacancy.R
import kz.aspan.vacancy.adapter.JobListAdapter
import kz.aspan.vacancy.common.MarginItemDecoration
import kz.aspan.vacancy.common.extensions.navigateSafely
import kz.aspan.vacancy.common.extensions.px
import kz.aspan.vacancy.databinding.FragmentVacanciesBinding
import javax.inject.Inject

@AndroidEntryPoint
class VacanciesFragment : Fragment(R.layout.fragment_vacancies) {
    private var _binding: FragmentVacanciesBinding? = null
    private val binding: FragmentVacanciesBinding
        get() = _binding!!

    private val viewModel: VacanciesViewModel by viewModels()

    @Inject
    lateinit var adapterVacancy: JobListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVacanciesBinding.bind(view)
        requireActivity().findViewById<TextView>(R.id.toolBarTitle).text =
            getString(R.string.list_of_vacancies)

        adapterVacancy.setOnVacancyClickListener {
            findNavController().navigateSafely(
                R.id.action_vacanciesFragment_to_detailInformationFragment,
                Bundle().apply {
                    putSerializable("vacancy", it)
                }
            )

        }


        binding.rv.adapter = adapterVacancy
        binding.rv.addItemDecoration(MarginItemDecoration(8.px))

        viewModel.vacancyMutableLiveData.observe(viewLifecycleOwner) {
            adapterVacancy.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}