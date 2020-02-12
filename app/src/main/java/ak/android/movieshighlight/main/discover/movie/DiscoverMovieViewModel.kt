package ak.android.movieshighlight.main.discover.movie

import ak.android.movieshighlight.common.errLog
import ak.android.movieshighlight.model.movie.MovieResultsItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DiscoverMovieViewModel : ViewModel() {
    val movies: LiveData<List<MovieResultsItem>> = DiscoverMovieRepository.movies

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun discoverMovies() {
        _isError.value = false

        viewModelScope.launch {
            try {
                DiscoverMovieRepository.discoverMovies()
            } catch (e: HttpException) {
                errLog("Error getting movie. ${e.localizedMessage}")
                _isError.value = true
            } catch (e: IOException) {
                errLog("Error getting movie. ${e.localizedMessage}")
                _isError.value = true
            } catch (e: Throwable) {
                errLog("Error getting movie. ${e.localizedMessage}")
                _isError.value = true
            }
        }
    }
}