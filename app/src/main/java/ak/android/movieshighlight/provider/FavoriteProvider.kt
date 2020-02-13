package ak.android.movieshighlight.provider

import ak.android.movieshighlight.database.AppDatabase
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

/**
 * Source: https://github.com/android/architecture-components-samples/tree/master/PersistenceContentProviderSample
 */

class FavoriteProvider : ContentProvider() {
    companion object {
        /** The match code for some items in the "favorite" table. */
        private const val FILM = 1
        /** The match code for an item in the "favorite" table. */
        private const val FILM_ITEM = 2

        /** The authority of this content provider. */
        private const val AUTHORITY = "ak.android.movieshighlight.provider"

        /** The URI matcher. */
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "favorite", FILM)
            addURI(AUTHORITY, "favorite/*", FILM_ITEM)
        }
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)

        if (code == FILM || code == FILM_ITEM) {
            val ctx = context ?: return null

            val favorite = AppDatabase.getDatabase(ctx).getFavorite()

            return if (code == FILM) {
                favorite.getFavoritesInCursor()
            } else {
                favorite.getFavoriteByIdInCursor(ContentUris.parseId(uri).toInt())
            }.apply {
                setNotificationUri(ctx.contentResolver, uri)
            }
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (MATCHER.match(uri)) {
            FILM -> "vnd.android.cursor.dir/$AUTHORITY.favorite"
            FILM_ITEM -> "vnd.android.cursor.item/$AUTHORITY.favorite"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0
}
