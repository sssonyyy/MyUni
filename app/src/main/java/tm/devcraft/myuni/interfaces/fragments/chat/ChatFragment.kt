package tm.devcraft.myuni.interfaces.fragments.chat

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_chat.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.DataConstants
import tm.devcraft.myuni.data.SendingMessage
import tm.devcraft.myuni.data.userData
import tm.devcraft.myuni.interfaces.fragments.chat.adapters.MessageList
import tm.devcraft.myuni.interfaces.selectBottomNav.SelectBottomNavActivity
import tm.devcraft.myuni.utils.FirebaseUtil
import tm.devcraft.myuni.viewModels.ChatVM
import tm.devcraft.myuni.viewModels.UserVM

class ChatFragment : Fragment() {

    private val adapterList: MessageList = MessageList(DataConstants.arr)
    private var user = Paper.book().read<userData>("userData")
    private val db = Firebase.firestore
    lateinit private var possibleDialogs : ArrayList<View>
    lateinit var chatvm: ChatVM
    lateinit var uservm: UserVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatvm = ViewModelProvider(activity as SelectBottomNavActivity).get(ChatVM::class.java)
        chatvm.listenerExistingDialogsIds()
        chatvm.loadPossibleData()
        uservm = ViewModelProvider(activity as SelectBottomNavActivity).get(UserVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    override fun onStart() {
        super.onStart()
        //(activity as SelectBottomNavActivity).toggleBottomNav()
        possibleDialogs = arrayListOf(people_dialog_create_1,people_dialog_create_2,people_dialog_create_3)
        initListeners()
        chatvm.listPossibleDialogData.observe(this,{
            println("POSSIBLE DIALOGS ${it}")
        })
        chatvm.existingDialogsData.observeForever {
            chatvm.listenerExistingDialogs()
        }
        chatvm.dialogData.observeForever{
           // println("DIALOG INFO ${it}")
        }
        chatvm.sendMessage("4hUUAnxUVtXDePetW24jN6dXA3h2", SendingMessage("sfsdfds",FirebaseUtil.getCurrentUid().toString(),FieldValue.serverTimestamp())
        )
        /*uservm.user.observe(this,{
            println(it)
        })*/
        initViews()
            //println((activity as SelectBottomNavActivity).supportFragmentManager.backStackEntryCount)
        //createPossibleDialogs()


    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()
        chatvm.removeExistingDialogsListener()
        chatvm.removeExistiingDialogsIdsListener()
        //(activity as SelectBottomNavActivity).toggleBottomNav()
    }

    private fun initViews() {
        if (container_favorite_list_for_start_dialog != null) container_favorite_list_for_start_dialog.visibility = View.GONE

        if (rv_dialog_list != null) rv_dialog_list.visibility = View.VISIBLE

        rv_dialog_list.adapter = adapterList

        adapterList.setOnItemClickListener(object : MessageList.OnClickListener{
            override fun onClick(pos: Int) {

                        //onPause()
                        //val intent = Intent(requireContext(), DialogOpenActivity::class.java)
                        //intent.putExtra("ID_DIAlOG", pos)
                        //startActivity(intent)
                        //(activity as SelectBottomNavActivity).addFragment(DialogOpenFragment(),"openDialog")
                        //uservm.testAdd("131231232")


            }
        })

        rv_dialog_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initListeners() {
        bnt_search_dialog.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                btn_create_dialog.isChecked = false
                btn_create_dialog.backgroundTintList = null
                bnt_search_dialog.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.purple_200))
            } else {
                bnt_search_dialog.backgroundTintList = null
            }
        }
        btn_create_dialog.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                bnt_search_dialog.isChecked = false
                bnt_search_dialog.backgroundTintList = null

                btn_create_dialog.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.purple_200))
                if (rv_dialog_list != null) rv_dialog_list.visibility = View.GONE

                if (container_favorite_list_for_start_dialog != null) container_favorite_list_for_start_dialog.visibility = View.VISIBLE

            } else {
                btn_create_dialog.backgroundTintList = null

                if (container_favorite_list_for_start_dialog != null) container_favorite_list_for_start_dialog.visibility = View.GONE

                if (rv_dialog_list != null) rv_dialog_list.visibility = View.VISIBLE
                rv_dialog_list.adapter = MessageList(DataConstants.arr)
                rv_dialog_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }


    /*private fun createPossibleDialogs(){
        var peoplePossibleDialogs : ArrayList<String> = arrayListOf()
        for((index, people) in user.savedPeople.asReversed().withIndex()){
            if(index <= 3){
                peoplePossibleDialogs.add(people)
            }
        }
        //fbLoadData(peoplePossibleDialogs)
        peoplePossibleDialogs.forEach {
            db.collection("users").document(it).get().addOnSuccessListener { document ->
                if (document != null) {

                    when(peoplePossibleDialogs.indexOf(it)){
                        0-> name_age.text = document.toObject<userData>()!!.fullname
                        1-> name_age2.text = document.toObject<userData>()!!.fullname
                        2-> name_age3.text = document.toObject<userData>()!!.fullname

                    }
                } else {

                }
            }
                .addOnFailureListener { exception ->

                }
        }

    }*/
}