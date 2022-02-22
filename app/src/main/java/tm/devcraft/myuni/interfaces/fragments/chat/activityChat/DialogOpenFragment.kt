package tm.devcraft.myuni.interfaces.fragments.chat.activityChat

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_chat_dialog_open.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.interfaces.selectBottomNav.SelectBottomNavActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialogOpenFragment : Fragment() {
    // TODO: Rename and change types of parameters
   /* private var param1: String? = null
    private var param2: String? = null*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_chat_dialog_open, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

    }

    override fun onStart() {
        super.onStart()
        (activity as SelectBottomNavActivity).hideBottomNav()
    }

    override fun onPause() {
        super.onPause()
        (activity as SelectBottomNavActivity).showBottomNav()
    }

    private fun initListeners() {
        open_menu.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                open_menu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.purple_200))
            } else {
                open_menu.backgroundTintList = null
            }
        }
        btn_back.setOnClickListener {
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DialogOpenFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}