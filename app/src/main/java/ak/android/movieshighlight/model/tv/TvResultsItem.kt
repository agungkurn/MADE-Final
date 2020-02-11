package ak.android.movieshighlight.model.tv

import ak.android.movieshighlight.database.FilmInfo
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvResultsItem(

    @field:SerializedName("first_air_date")
    val firstAirDate: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("original_language")
    val originalLanguage: String? = null,

    @field:SerializedName("genre_ids")
    val genreIds: List<Int?>? = null,

    @field:SerializedName("poster_path")
    val posterPath: String? = null,

    @field:SerializedName("origin_country")
    val originCountry: List<String?>? = null,

    @field:SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @field:SerializedName("original_name")
    val originalName: String? = null,

    @field:SerializedName("vote_average")
    val voteAverage: Double? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
) : Parcelable {
    fun toDatabaseModel() = FilmInfo(
        id ?: 0,
        name.toString(),
        originalName.toString(),
        originalLanguage.toString(),
        genreIds?.joinToString { it.toString() }.toString(),
        voteAverage ?: 0.0,
        false,
        firstAirDate.toString(),
        overview.toString(),
        posterPath.toString(),
        backdropPath.toString(),
        "tv"
    )
}