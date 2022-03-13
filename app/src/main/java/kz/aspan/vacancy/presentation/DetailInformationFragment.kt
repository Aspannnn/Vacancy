package kz.aspan.vacancy.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kz.aspan.vacancy.R
import kz.aspan.vacancy.databinding.FragmentDetailInformationBinding

class DetailInformationFragment : Fragment(R.layout.fragment_detail_information) {
    private var _binding: FragmentDetailInformationBinding? = null
    private val binding: FragmentDetailInformationBinding
        get() = _binding!!

    private val args: DetailInformationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailInformationBinding.bind(view)


        val vacancy = args.vacancy
        requireActivity().findViewById<TextView>(R.id.toolBarTitle).text = vacancy.profession

        binding.apply {
            professionTv.text = vacancy.profession
            companyTv.text = vacancy.company
            industryTv.text = vacancy.industry
            conditionsTv.text = vacancy.conditions
            descriptionTv.text = vacancy.description
            companyDescription.text = vacancy.companyDescription
            site.text = vacancy.site
            contacts.text = vacancy.contacts
            address.text = vacancy.address
            city.text = vacancy.сity

            for (skill in vacancy.skills) {
                addNewChip(requireContext(), skillsChipGroup, skill)
            }


        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addNewChip(context: Context, parent: ChipGroup, text: String) {
        val chip = LayoutInflater.from(context).inflate(R.layout.single_chip, parent, false) as Chip
        chip.text = text
        parent.addView(chip)
    }
}