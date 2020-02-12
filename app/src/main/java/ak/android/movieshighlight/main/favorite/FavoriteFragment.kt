package ak.android.movieshighlight.main.favorite

import ak.android.movieshighlight.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_favorite, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabTitle = resources.getStringArray(R.array.tab_title)
        vp_favorite.adapter = FavoritePagerAdapter(childFragmentManager, tabTitle)

        tab_favorite.setupWithViewPager(vp_favorite)
    }
}
