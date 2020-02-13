package ak.android.movieshighlight.database

import ak.android.movieshighlight.model.movie.MovieResultsItem
import ak.android.movieshighlight.model.tv.TvResultsItem
import android.content.ContentValues
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorite")
@Parcelize
class FilmInfo(
    @PrimaryKey
    var id: Int = 0,

    var title: String? = null,

    var originalTitle: String? = null,

    var originalLanguage: String? = null,

    var genreId: String? = null,

    var rate: Double? = null,

    var adult: Boolean = false,

    var dateReleased: String? = null,

    var overview: String? = null,

    var poster: String? = null,

    var banner: String? = null,

    var category: String? = null
) : Parcelable {
    companion object {
        fun fromContentValues(values: ContentValues?): FilmInfo {
            return FilmInfo(
                id = values?.getAsInteger("id") ?: 0,
                title = values?.getAsString("title"),
                originalTitle = values?.getAsString("originalTitle"),
                originalLanguage = values?.getAsString("originalLanguage"),
                genreId = values?.getAsString("genreId"),
                rate = values?.getAsDouble("rate"),
                adult = values?.getAsBoolean("adult") ?: false,
                dateReleased = values?.getAsString("dateReleased"),
                overview = values?.getAsString("overview"),
                poster = values?.getAsString("poster"),
                banner = values?.getAsString("banner"),
                category = values?.getAsString("category")
            )
        }
    }
}

fun List<FilmInfo>.toMovieModel() = map { info ->
    if (info.category == "movie") {
        MovieResultsItem(
            id = info.id,
            title = info.title,
            originalTitle = info.originalTitle,
            originalLanguage = info.originalLanguage,
            genreIds = info.genreId?.split(", ")?.map { it.toInt() },
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
            genreIds = info.genreId?.split(", ")?.map { it.toInt() },
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