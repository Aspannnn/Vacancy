package kz.aspan.vacancy.presentation.viewpager

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.vacancy.R
import kz.aspan.vacancy.adapter.ViewPagerAdapter
import kz.aspan.vacancy.data.remote.BasicAuthInterceptor
import kz.aspan.vacancy.databinding.FragmentHomeBinding
import kz.aspan.vacancy.presentation.employer.EmployerFragment
import kz.aspan.vacancy.presentation.skill.SkillFragment
import kz.aspan.vacancy.presentation.profession.ProfessionFragment
import javax.inject.Inject

@AndroidEntryPoint
class ViewPagerFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private val viewModel: ViewPagerViewModel by viewModels()

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        requireActivity().findViewById<TextView>(R.id.toolBarTitle).text =
            getString(R.string.vacancies)



        viewModel.tokenMutableLiveData.observe(viewLifecycleOwner) {
            basicAuthInterceptor.token = it

            val fragmentList = arrayListOf(
                ProfessionFragment(),
                SkillFragment(),
                EmployerFragment()
            )

            val adapter = ViewPagerAdapter(
                fragmentList,
                childFragmentManager,
                lifecycle
            )

            binding.viewPager.adapter = adapter

            binding.progressBar.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}