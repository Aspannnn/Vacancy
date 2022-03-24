package kz.aspan.vacancy.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.aspan.vacancy.domain.repository.VacancyRepository
import javax.inject.Inject

@HiltViewModel
class PDFViewerViewModel @Inject constructor(
    private val repository: VacancyRepository,
    args: SavedStateHandle
) : ViewModel() {
    val resumeFilePathMutableLiveData = MutableLiveData<String>()

    init {
        val url = args.get<String>("pdf_url")
        url?.let {
            testPDF(it)
        }
    }

    private fun testPDF(url: String) {
        viewModelScope.launch {
            try {
                val result = repository.downloadPDF(url,"student")
                resumeFilePathMutableLiveData.postValue(result)
            } catch (e: Exception) {
               e.printStackTrace()
            }
        }
    }
}