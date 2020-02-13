package ak.android.myfavoritefilms

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    /** The authority of this content provider. */
    private val AUTHORITY = "ak.android.movieshighlight.provider"
    /** The URI for the "favorite" table. */
    private val FAVORITE_URI = Uri.parse("content://$AUTHORITY/favorite")

    private lateinit var adapter: FilmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_favorite.setHasFixedSize(true)
        rv_favorite.layoutManager = GridLayoutManager(this, 2)
        adapter = FilmAdapter()
        rv_favorite.adapter = adapter

        LoaderManager.getInstance(this).initLoader(1, null, loaderCallback)
    }

    private val loaderCallback = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> = CursorLoader(
            applicationContext,
            FAVORITE_URI,
            arrayOf("poster", "title", "originalTitle"),
            null,
            null,
            null
        )

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            adapter.setCursor(data)
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            adapter.setCursor(null)
        }

    }
}
