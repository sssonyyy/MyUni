package tm.devcraft.myuni.interfaces.fragments.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_account.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.interfaces.fragments.account.settings.SettingsActivity

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settings.setOnClickListener {
            startActivity(Intent(context, SettingsActivity::class.java))
        }

        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fragment_my_acc, MyAccFragment())
            commit()

        }
    }
    override fun onStart() {
        super.onStart()


    }
}