package tm.devcraft.myuni.interfaces.fragments.ribbon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_ribbon.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.interfaces.fragments.ribbon.filterFragment.FilterFragment
import tm.devcraft.myuni.interfaces.fragments.ribbon.tabsFragment.EventsFragment
import tm.devcraft.myuni.interfaces.fragments.ribbon.tabsFragment.PeopleFragment
import tm.devcraft.myuni.interfaces.fragments.ribbon.tabsFragment.PosterFragment

class RibbonFragment(var inFastNav: String = "") : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return LayoutInflater
            .from(inflater.context)
            .inflate(R.layout.fragment_ribbon, container, false)
    }

    override fun onStart() {
        super.onStart()
        initListeners()
        if(inFastNav != ""){
            if(inFastNav == "announces"){
                tab_layout.getTabAt(0)?.select()
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.tabs_navigation, PosterFragment())
                    commit()
                }
            }
            else if (inFastNav == "people"){
                tab_layout.getTabAt(2)?.select()
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.tabs_navigation, PeopleFragment())
                    commit()
                }
            }
            else if(inFastNav == "events"){
                tab_layout.getTabAt(1)?.select()
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.tabs_navigation, EventsFragment())
                    commit()
                }
            }
        }else{
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.tabs_navigation, PosterFragment())
                commit()
            }
        }
        //Запуск первого фрагмента

    }

    private fun initListeners() {
        initTabs()

        btn_search.setOnClickListener {

            /*if (search_field.isFocusableInTouchMode) {
                ViewCompat.animate(title).withStartAction { title.visibility = View.GONE }
                    .alpha(0f)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .setDuration(400)
                    .start()
                ViewCompat.animate(search_field)
                    .withStartAction { search_field.visibility = View.VISIBLE }
                    .alpha(1f)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .setDuration(800)
                    .start()
                search_field.requestFocus()
            }
            else {
                title.visibility = View.VISIBLE
                search_field.visibility = View.GONE
            }*/
        }
        btn_filter.setOnClickListener {
            if (btn_filter.isChecked) {
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.tabs_navigation, FilterFragment())
                    commit()
                }
            } else {
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.tabs_navigation, PosterFragment())
                    commit()}
            }
        }
    }

    private fun initTabs() {
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val pointTab: String = tab!!.text.toString()
                println(pointTab)
                if (pointTab == "Люди") {
                    activity?.supportFragmentManager?.beginTransaction()!!.setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN
                    ).apply {
                        replace(R.id.tabs_navigation, PeopleFragment())
                        btn_filter.isChecked = false
                        commit()
                    }
                }
                if (pointTab == "События") {
                    activity?.supportFragmentManager?.beginTransaction()!!.setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN
                    ).apply {
                        replace(R.id.tabs_navigation, EventsFragment())
                        btn_filter.isChecked = false
                        commit()
                    }
                }
                if (pointTab == "Афиша") {
                    activity?.supportFragmentManager?.beginTransaction()!!.setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN
                    ).apply {
                        replace(R.id.tabs_navigation, PosterFragment())
                        btn_filter.isChecked = false
                        commit()
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
    }
}