package ak.android.movieshighlight.main.discover

import ak.android.movieshighlight.main.discover.movie.MovieFragment
import ak.android.movieshighlight.main.discover.tv.TvSeriesFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class DiscoverFragmentPagerAdapter(fm: FragmentManager, private val tabTitle: Array<String>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            MovieFragment.newInstance()
        } else {
            TvSeriesFragment.newInstance()
        }
    }

    override fun getCount(): Int = tabTitle.size

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitle[position]
    }

}