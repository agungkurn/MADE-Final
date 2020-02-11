package ak.android.movieshighlight.main.discover.tv

import ak.android.movieshighlight.common.errLog
import ak.android.movieshighlight.model.tv.TvResultsItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TvSeriesViewModel : ViewModel() {
    val tvSeries: LiveData<List<TvResultsItem>> = TvSeriesRepository.tvSeries

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun discoverTvSeries() {
        _isError.value = false

        viewModelScope.launch {
            try {
                TvSeriesRepository.discoverTvSeries()
            } catch (e: HttpException) {
                errLog("Error getting TV series. ${e.localizedMessage}")
                _isError.value = true
            } catch (e: IOException) {
                errLog("Error getting TV series. ${e.localizedMessage}")
                _isError.value = true
            } catch (e: Throwable) {
                errLog("Error getting TV series. ${e.localizedMessage}")
                _isError.value = true
            }
        }
    }
}
