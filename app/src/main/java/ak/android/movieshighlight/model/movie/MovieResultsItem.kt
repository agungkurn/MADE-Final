package ak.android.movieshighlight.model.movie

import ak.android.movieshighlight.database.FilmInfo
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResultsItem(

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("original_language")
    val originalLanguage: String? = null,

    @field:SerializedName("original_title")
    val originalTitle: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("genre_ids")
    val genreIds: List<Int?>? = null,

    @field:SerializedName("poster_path")
    val posterPath: String? = null,

    @field:SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @field:SerializedName("release_date")
    val releaseDate: String? = null,

    @field:SerializedName("vote_average")
    val voteAverage: Double? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("adult")
    val adult: Boolean? = null
) : Parcelable {
    fun toDatabaseModel() = FilmInfo(
        id ?: 0,
        title.toString(),
        originalTitle.toString(),
        originalLanguage.toString(),
        genreIds?.joinToString { it.toString() }.toString(),
        voteAverage ?: 0.0,
        adult ?: false,
        releaseDate.toString(),
        overview.toString(),
        posterPath.toString(),
        backdropPath.toString(),
        "movie"
    )
}