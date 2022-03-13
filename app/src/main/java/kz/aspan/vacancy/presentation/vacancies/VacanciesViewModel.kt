package kz.aspan.vacancy.presentation.vacancies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.aspan.vacancy.common.Constants.EMPLOYER
import kz.aspan.vacancy.common.Constants.PROFESSION
import kz.aspan.vacancy.common.Constants.SKILL
import kz.aspan.vacancy.domain.model.Data
import kz.aspan.vacancy.domain.repository.VacancyRepository
import javax.inject.Inject

@HiltViewModel
class VacanciesViewModel @Inject constructor(
    private val repository: VacancyRepository,
    args: SavedStateHandle
) : ViewModel() {
    val vacancyMutableLiveData = MutableLiveData<List<Data>>()

    init {
        val id = args.get<String>("vacancy_id")
        val query = args.get<Int>("query")

        if (id != null && query != null) {
            getVacancy(id, query)
        }
    }

    private fun getVacancy(id: String, query: Int) {
        viewModelScope.launch {
            try {

                val result = when (query) {
                    PROFESSION -> repository.getProfessionById(id)
                    SKILL -> repository.getSkillById(id)
                    EMPLOYER -> repository.getEmployerById(id)
                    else -> {
                        null
                    }
                }
                vacancyMutableLiveData.postValue(result!!.data)
            } catch (e: Exception) {

            }
        }
    }
}

