package tm.devcraft.myuni.interfaces.fragments.ribbon.tabsFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.item_people_from_ribbon.view.*
import kotlinx.android.synthetic.main.tab_fragment_people.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.userData
import tm.devcraft.myuni.interfaces.fragments.ribbon.tabsFragment.PeopleRecycler.PeopleRecyclerVH
import tm.devcraft.myuni.utils.Utils

class PeopleFragment : Fragment() {
    private var db = FirebaseFirestore.getInstance()
    private lateinit var adapter: FirestoreRecyclerAdapter<*, *>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tab_fragment_people, container, false)
    }

    override fun onStart() {
        super.onStart()

        var query = db.collection("users").whereNotEqualTo("uid",FirebaseAuth.getInstance().currentUser?.uid.toString())
        val options: FirestoreRecyclerOptions<userData> = FirestoreRecyclerOptions.Builder<userData>()
            //.setLifecycleOwner(parentFragment)
            .setQuery(query, userData::class.java)
            .build()

        adapter = object : FirestoreRecyclerAdapter<userData, PeopleRecyclerVH>(options) {
            override fun onBindViewHolder(
                holder: PeopleRecyclerVH,
                position: Int,
                model: userData
            ): Unit = holder.itemView.run {
                var user : userData
                share.setOnClickListener {
                    var intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT,model.dLink)
                    startActivity(Intent.createChooser(intent,"Share Link"))
                    db.collection("users").whereEqualTo("uid",model.uid).get().addOnSuccessListener {
                            docs ->
                        for (doc in docs){
                            doc.reference.update("reposted",model.reposted+1)
                        }
                    }
                }
                save.setOnClickListener {
                    db.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).update("savedPeople",
                        FieldValue.arrayUnion(model.uid))
                    db.collection("users").whereEqualTo("uid",model.uid).get().addOnSuccessListener {
                            docs ->
                        for (doc in docs){
                            var model = doc.toObject<userData>()
                            doc.reference.update("saved",model.saved+1)
                        }
                    }
                }
                share.setOnClickListener {
                    var intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT,model.dLink)
                    startActivity(Intent.createChooser(intent,"Share Link"))
                    db.collection("users").whereEqualTo("uid",model.uid).get().addOnSuccessListener {
                            docs ->
                        for (doc in docs){
                            var model = doc.toObject<userData>()
                            doc.reference.update("reposted",model.reposted+1)
                        }
                    }
                }

                //user = doc.toObject<userData>()!!
                fullname_age.text = model.fullname + ", " + model.date?.let  {
                    java.util.Date(
                        it.seconds * 1000 + it.nanoseconds
                    )
                }
                    ?.let { Utils.getAge(it) } + " лет"
                if (model.photo["0"]?.isNotEmpty() == true){
                    Glide.with(context).load(Uri.parse(model.photo["0"])).fitCenter().into(photo)
                    //Picasso.get().load(Uri.parse(user.photo["0"])).into(photo)
                }
               vuz.text = model.nameOfInstitution
                speciality.text = model.speciality
                //created_date.text = Date(model.created.seconds*1000+model.created.nanoseconds)

            //Paper.book().write("userData",user)


            }

            override fun onCreateViewHolder(group: ViewGroup, i: Int): PeopleRecyclerVH {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                val view: View = LayoutInflater.from(group.context)
                    .inflate(R.layout.item_people_from_ribbon, group, false)
                return PeopleRecyclerVH(view)
            }
        }
        people_recycler.adapter = adapter
        people_recycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        adapter.startListening()
    }
}