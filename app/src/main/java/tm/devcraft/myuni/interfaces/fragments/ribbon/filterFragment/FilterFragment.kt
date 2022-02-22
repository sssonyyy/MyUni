package tm.devcraft.myuni.interfaces.fragments.ribbon.filterFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tm.devcraft.myuni.R

class FilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = LayoutInflater
            .from(inflater.context)
            .inflate(R.layout.fragment_ribbon_filter, container, false)

        return view
    }

}