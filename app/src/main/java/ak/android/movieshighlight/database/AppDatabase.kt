package ak.android.movieshighlight.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FilmInfo::class, GenreDb::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFavorite(): FavoriteDao
    abstract fun getGenres(): GenreDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val temp = INSTANCE
            if (temp != null) {
                return temp
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "film_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}