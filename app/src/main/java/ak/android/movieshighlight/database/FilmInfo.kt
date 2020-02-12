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
) : Parcelable

fun List<FilmInfo>.toMovieModel() = map {
    if (it.category == "movie") {
        MovieResultsItem(
            id = it.id,
            title = it.title,
            originalTitle = it.originalTitle,
            originalLanguage = it.originalLanguage,
            genreIds = it.genreId.split(", ").map { it.toInt() },
            voteAverage = it.rate,
            adult = it.adult,
            releaseDate = it.dateReleased,
            overview = it.overview,
            posterPath = it.poster,
            backdropPath = it.banner
        )
    } else {
        throw TypeCastException("${it.category} can not be converted to MovieResultsItem")
    }
}

fun List<FilmInfo>.toTvModel() = map {
    if (it.category == "tv") {
        TvResultsItem(
            id = it.id,
            name = it.title,
            originalName = it.originalTitle,
            originalLanguage = it.originalLanguage,
            genreIds = it.genreId.split(", ").map { it.toInt() },
            voteAverage = it.rate,
            firstAirDate = it.dateReleased,
            overview = it.overview,
            posterPath = it.poster,
            backdropPath = it.banner
        )
    } else {
        throw TypeCastException("${it.category} can not be converted to TvResultsItem")
    }
}