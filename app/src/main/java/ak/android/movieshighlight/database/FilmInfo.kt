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

fun List<FilmInfo>.toMovieModel() = map { info ->
    if (info.category == "movie") {
        MovieResultsItem(
            id = info.id,
            title = info.title,
            originalTitle = info.originalTitle,
            originalLanguage = info.originalLanguage,
            genreIds = info.genreId.split(", ").map { it.toInt() },
            voteAverage = info.rate,
            adult = info.adult,
            releaseDate = info.dateReleased,
            overview = info.overview,
            posterPath = info.poster,
            backdropPath = info.banner
        )
    } else {
        throw TypeCastException("${info.category} can not be converted to MovieResultsItem")
    }
}

fun List<FilmInfo>.toTvModel() = map { info ->
    if (info.category == "tv") {
        TvResultsItem(
            id = info.id,
            name = info.title,
            originalName = info.originalTitle,
            originalLanguage = info.originalLanguage,
            genreIds = info.genreId.split(", ").map { it.toInt() },
            voteAverage = info.rate,
            firstAirDate = info.dateReleased,
            overview = info.overview,
            posterPath = info.poster,
            backdropPath = info.banner
        )
    } else {
        throw TypeCastException("${info.category} can not be converted to TvResultsItem")
    }
}