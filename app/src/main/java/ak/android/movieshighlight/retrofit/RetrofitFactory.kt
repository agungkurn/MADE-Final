package ak.android.movieshighlight.retrofit

import ak.android.movieshighlight.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private var instance: FilmInterface? = null

    fun getMovieOrTvShow(): FilmInterface {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(httpClient)
                .addConverterFactory(
                    GsonConverterFactory.create(GsonBuilder().serializeNulls().create())
                ).build()
                .create(FilmInterface::class.java)
        }

        return instance!!
    }
}