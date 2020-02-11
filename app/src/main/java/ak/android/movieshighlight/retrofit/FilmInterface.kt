package ak.android.movieshighlight.retrofit

import ak.android.movieshighlight.BuildConfig
import ak.android.movieshighlight.model.movie.MovieResponse
import ak.android.movieshighlight.model.genre.GenreResponse
import ak.android.movieshighlight.model.tv.TvResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmInterface {
    @GET("discover/movie")
    suspend fun fetchMovieDiscovered(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<MovieResponse>

    @GET("discover/tv")
    suspend fun fetchTvShowDiscovered(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<TvResponse>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") keyword: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<MovieResponse>

    @GET("search/tv")
    suspend fun searchTvSeries(
        @Query("query") keyword: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<TvResponse>

    @GET("genre/movie/list")
    suspend fun getMovieGenre(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<GenreResponse>

    @GET("genre/tv/list")
    suspend fun getTvSeriesGenre(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<GenreResponse>
}