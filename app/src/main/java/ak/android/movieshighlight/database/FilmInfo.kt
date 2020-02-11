package ak.android.movieshighlight.database

import ak.android.movieshighlight.model.movie.MovieResultsItem
import ak.android.movieshighlight.model.tv.TvResultsItem
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorite")
@Parcelize
data class FilmInfo(
    @PrimaryKey
    val id: Int,

    val title: String?,

    val originalTitle: String,

    val originalLanguage: String,

    val genreId: String,

    val rate: Double,

    val adult: Boolean,

    val dateReleased: String,

    val overview: String,

    val poster: String,

    val banner: String,

    val category: String
) : Parcelable {
    fun toMovieModel() = MovieResultsItem(
        id = id,
        title = title,
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        genreIds = genreId.split(", ").map { it.toInt() },
        voteAverage = rate,
        adult = adult,
        releaseDate = dateReleased,
        overview = overview,
        posterPath = poster,
        backdropPath = banner
    )

    fun toTvSeriesModel() = TvResultsItem(
        id = id,
        name = title,
        originalName = originalTitle,
        originalLanguage = originalLanguage,
        genreIds = genreId.split(", ").map { it.toInt() },
        voteAverage = rate,
        firstAirDate = dateReleased,
        overview = overview,
        posterPath = poster,
        backdropPath = banner
    )
}