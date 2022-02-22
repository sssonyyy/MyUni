package tm.devcraft.myuni.interfaces.selectBottomNav

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_select_bottom_nav.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.userData
import tm.devcraft.myuni.interfaces.fragments.account.AccountFragment
import tm.devcraft.myuni.interfaces.fragments.chat.ChatFragment
import tm.devcraft.myuni.interfaces.fragments.create.CreateFragment
import tm.devcraft.myuni.interfaces.fragments.home.HomeFragment
import tm.devcraft.myuni.interfaces.fragments.ribbon.RibbonFragment
import tm.devcraft.myuni.viewModels.ChatVM
import tm.devcraft.myuni.viewModels.UserVM


class SelectBottomNavActivity : AppCompatActivity(R.layout.activity_select_bottom_nav) {
    private val fb = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    lateinit var uvm : UserVM
    lateinit var chatvm : ChatVM

    private val fragments : List<Fragment> = listOf(AccountFragment(), ChatFragment(), CreateFragment(), RibbonFragment(),HomeFragment())

    private val frg = supportFragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var user = userData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        uvm = ViewModelProvider(this).get(UserVM::class.java)
        //println(FirebaseAuth.getInstance().currentUser?.isEmailVerified.toString())
        uvm.loadDataUser()
        replaceFragment(fragments[4])

        /*db.collection("users").document(fb.uid.toString()).get().addOnSuccessListener {
                doc ->
            if(doc != null){
                user = doc.toObject<userData>()!!
                Paper.book().write("userData",user)


                println(Paper.book().read<userData>("userData").toString())
                initListeners()
            }
            else{
                initListeners()
            }
        }*/
        chatvm = ViewModelProvider(this).get(ChatVM::class.java)
        uvm.user.observe(this,{
            chatvm.userData = it
        })

        initListeners()
    }




    fun addFragment(frgActive: Fragment, tag: String){

        /*var fragment = frg.findFragmentById(R.id.nav_host_fragment)
        println(frgActive)
        if (fragment != frgActive) {
            fragment = frgActive
        }*/
        //throw RuntimeException("Test Crash")

        fragmentTransaction=frg.beginTransaction().setTransition(TRANSIT_FRAGMENT_OPEN)
        if(frg.findFragmentByTag(tag) != null){
            fragmentTransaction.replace(R.id.nav_host_fragment, frg.findFragmentByTag(tag)!!,tag)
            println(frg.findFragmentByTag(tag))
            println("HAAAAAAAAAAAAAAAAAAAAA")
        }else{
            println("PIZDAAAAAAAAAAAAAAA")
            fragmentTransaction.replace(R.id.nav_host_fragment,frgActive,tag)

        }
        fragmentTransaction.addToBackStack(tag)

        fragmentTransaction.commit()
    }

    private fun initListeners() {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_acc_ -> {
                    addFragment(fragments[0],"acc")
                    true
                }
                R.id.nav_chat_ -> {
                    addFragment(fragments[1],"chat")
                    true
                }
                R.id.nav_create -> {
                    //replaceFragment(fragments[2])
                    addFragment(fragments[2],"create")
                    true
                }
                R.id.nav_ribbon_ -> {
                    addFragment(fragments[3],"feed")
                    true
                }
                R.id.nav_home_ -> {
                    addFragment(fragments[4],"home")
                    uvm.testAdd("sdfsdf")
                    uvm.testAdd("fsdfsdfds")
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment,fragment)
            commit()
        }

    fun showBottomNav(){
        bottom_navigation.visibility = View.VISIBLE
    }

    fun hideBottomNav(){
        bottom_navigation.visibility = View.GONE
    }


}



