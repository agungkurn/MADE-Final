package ak.android.movieshighlight.main.discover

import ak.android.movieshighlight.main.discover.movie.DiscoverMovieFragment
import ak.android.movieshighlight.main.discover.tv.DiscoverTvSeriesFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class DiscoverPagerAdapter(fm: FragmentManager, private val tabTitle: Array<String>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            DiscoverMovieFragment.newInstance()
        } else {
            DiscoverTvSeriesFragment.newInstance()
        }
    }

    override fun getCount(): Int = tabTitle.size

    override fun getPageTitle(position: Int): CharSequence? = tabTitle[position]
}