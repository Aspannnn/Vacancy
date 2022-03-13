package kz.aspan.vacancy.presentation.skill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.aspan.vacancy.domain.model.Simple
import kz.aspan.vacancy.domain.repository.VacancyRepository
import javax.inject.Inject

@HiltViewModel
class SkillViewModel @Inject constructor(
    private val repository: VacancyRepository
) : ViewModel() {

    private val _vacancyMutableLiveData = MutableLiveData<Simple>()
    val vacancyLiveData: LiveData<Simple> = _vacancyMutableLiveData

    init {
        getVacancy()
    }

    private fun getVacancy() {
        viewModelScope.launch {
            try {
                val result = repository.getSkills()
                result.data.sortedByDescending {
                    it.numberOfVacancies
                }
                _vacancyMutableLiveData.postValue(result)
            } catch (e: Exception) {

            }
        }
    }

}