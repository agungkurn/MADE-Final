package ak.android.movieshighlight.main

import ak.android.movieshighlight.R
import ak.android.movieshighlight.common.hide
import ak.android.movieshighlight.common.show
import ak.android.movieshighlight.common.toast
import ak.android.movieshighlight.main.search.SearchViewModel
import ak.android.movieshighlight.notification.NotificationReceiver
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application).create(SearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findNavController(R.id.host_fragment).also {
            setupActionBarWithNavController(
                it, AppBarConfiguration(setOf(R.id.nav_discover, R.id.nav_favorite))
            )
            nav_bar.setupWithNavController(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView?

        setupSearchViewExpandCollapse(menu)
        setupSearchViewConfig(searchView, searchManager)

        return true
    }

    private fun setupSearchViewExpandCollapse(menu: Menu?) {
        val searchMenuItem = menu?.findItem(R.id.menu_search)

        searchMenuItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            var state: Bundle? = null

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                findNavController(R.id.host_fragment).also {
                    state = it.saveState()
                }.navigate(R.id.nav_search)

                nav_bar.hide()

                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                findNavController(R.id.host_fragment).also {
                    it.restoreState(state)
                }.navigateUp()

                nav_bar.show()

                return true
            }
        })
    }

    private fun setupSearchViewConfig(searchView: SearchView?, searchManager: SearchManager) {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.host_fragment)

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.queryHint = resources.getString(R.string.action_search)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isEmpty() == true) {
                    toast(getString(R.string.search_insert_keyword))
                }

                query?.let {
                    if (it.length <= 3) {
                        toast(getString(R.string.search_more_3))
                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fragment?.view?.let {
                    it.findViewById<RecyclerView>(R.id.rv_search)?.hide()
                }

                if (!newText.isNullOrEmpty()) {
                    if (newText.length > 3) {
                        fragment?.view?.let {
                            it.findViewById<TextView>(R.id.tv_retry)?.hide()
                            it.findViewById<ProgressBar>(R.id.pb_search)?.show()
                        }
                        viewModel.searchByTitle(newText.toString())
                    }
                }

                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val notification = NotificationReceiver()
        notification.setDailyNotification(this)
    }
}