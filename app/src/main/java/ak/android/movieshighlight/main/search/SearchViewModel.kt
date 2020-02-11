package ak.android.movieshighlight.main.search

import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.CombinedLiveData
import ak.android.movieshighlight.common.errLog
import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import retrofit2.HttpException
import java.io.IOException

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val category = context.resources.getStringArray(R.array.tab_title)

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val movieResults = Transformations.map(SearchRepository.movieResults) { results ->
        // Show the header as "Movies"
        mutableListOf<Any>().apply {
            add(category[0])

            // Check if there is data found or not
            if (results.isNotEmpty()) {
                addAll(results)
            } else {
                add(context.resources.getString(R.string.msg_no_data))
            }
        }.toList()
    }

    private val tvSeriesResults = Transformations.map(SearchRepository.tvSeriesResult) { results ->
        // Show the header as "TV Series"
        mutableListOf<Any>().apply {
            add(category[1])

            // Check if there is data found or not
            if (results.isNotEmpty()) {
                addAll(results)
            } else {
                add(context.resources.getString(R.string.msg_no_data))
            }
        }.toList()
    }

    // Combine the results
    val searchResults = CombinedLiveData(movieResults, tvSeriesResults) { a, b ->
        mutableListOf<Any>().apply {
            if (a != null && b != null) {
                addAll(a)
                addAll(b)
            }
        }.toList()
    }

    fun searchByTitle(keyword: String) {
        _isError.value = false

        viewModelScope.launch {
            try {
                SearchRepository.searchMovie(keyword)
                SearchRepository.searchTvSeries(keyword)
            } catch (e: HttpException) {
                errLog("Error getting movie/TV. ${e.localizedMessage}")
                _isError.value = true
            } catch (e: IOException) {
                errLog("Error getting movie/TV. ${e.localizedMessage}")
                _isError.value = true
            } catch (e: Throwable) {
                errLog("Error getting movie/TV. ${e.localizedMessage}")
                _isError.value = true
            }
        }
    }
}
