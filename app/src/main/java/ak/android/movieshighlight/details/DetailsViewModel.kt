package ak.android.movieshighlight.details

import ak.android.movieshighlight.R
import ak.android.movieshighlight.database.FavoriteDatabase
import ak.android.movieshighlight.database.FilmInfo
import android.app.Application
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val db = FavoriteDatabase.getDatabase(context).getFavorite()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            DetailsRepository.fetchGenres()
        }
    }

    val genres: (List<Int>) -> LiveData<List<String>> =
        { genreId ->
            // Transform the data (GenreItems to String)
            Transformations.map(DetailsRepository.genres) { apiResult ->
                if (!apiResult.isNullOrEmpty()) {
                    apiResult.filter { genresItem ->
                        // Find by ids
                        genreId.any { requested ->
                            genresItem.id == requested
                        }
                    }.map { genresItem ->
                        // Take name ONLY
                        genresItem.name
                    }.requireNoNulls()
                } else {
                    emptyList()
                }
            }
        }

    val isFavorite: (Int) -> LiveData<Boolean> =
        { id ->
            Transformations.map(db.getFavoriteById(id)) {
                it.isNotEmpty()
            }
        }

    /**
     * @param picassoRequest: RequestCreator from Picasso.get().load()
     * @param onComplete: A function called when a color palette generated, takes generated color as its param
     */
    fun generateActionBarColor(picassoRequest: RequestCreator, onComplete: (color: Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = picassoRequest.get()
            val palette = Palette.from(bitmap).generate()

            withContext(Dispatchers.Main) {
                onComplete(
                    palette.getVibrantColor(ContextCompat.getColor(context, R.color.colorPrimary))
                )
            }
        }
    }

    fun addToFavorite(filmInfo: FilmInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            db.addToFavorite(filmInfo)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context, context.getString(R.string.msg_added_db), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun removeFromFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.removeFromFavorite(id)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context, context.getString(R.string.msg_removed_db), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}