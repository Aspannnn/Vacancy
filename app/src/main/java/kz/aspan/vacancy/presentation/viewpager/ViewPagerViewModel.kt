package kz.aspan.vacancy.presentation.viewpager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.aspan.vacancy.domain.model.User
import kz.aspan.vacancy.domain.repository.VacancyRepository
import javax.inject.Inject

@HiltViewModel
class ViewPagerViewModel @Inject constructor(
    private val repository: VacancyRepository
) : ViewModel() {
    val tokenMutableLiveData = MutableLiveData<String>()

    init {
        getToken()
    }

    private fun getToken() {
        viewModelScope.launch {
            try {
                val result =
                    repository.getToken(User("77073098583", "5d5b8590cc091303c4acfb10", "curs2019"))
                tokenMutableLiveData.postValue(result.token)
            } catch (e: Exception) {

            }
        }
    }
}