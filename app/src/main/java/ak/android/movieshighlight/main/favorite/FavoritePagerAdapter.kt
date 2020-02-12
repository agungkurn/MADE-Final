package ak.android.movieshighlight.main.favorite

import ak.android.movieshighlight.main.favorite.movie.FavoriteMovieFragment
import ak.android.movieshighlight.main.favorite.tv.FavoriteTvFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FavoritePagerAdapter(fm: FragmentManager, private val tabTitle: Array<String>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            FavoriteMovieFragment.newInstance()
        } else {
            FavoriteTvFragment.newInstance()
        }
    }

    override fun getCount() = tabTitle.size

    override fun getPageTitle(position: Int): CharSequence? = tabTitle[position]
}