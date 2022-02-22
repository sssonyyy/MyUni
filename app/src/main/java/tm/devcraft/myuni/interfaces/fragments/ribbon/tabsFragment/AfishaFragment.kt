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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import io.paperdb.Paper
import kotlinx.android.synthetic.main.item_announce.view.*
import kotlinx.android.synthetic.main.item_announce.view.service
import kotlinx.android.synthetic.main.item_announce_from_create.view.*
import kotlinx.android.synthetic.main.item_announce_from_create.view.name
import kotlinx.android.synthetic.main.item_announce_from_create.view.price
import kotlinx.android.synthetic.main.item_announce_from_create.view.skills
import kotlinx.android.synthetic.main.tab_fragment_announcement.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.AnnounceUserModel
import tm.devcraft.myuni.data.userData
import tm.devcraft.myuni.interfaces.fragments.ribbon.tabsFragment.AnnounceRecycler.AnnouncentRecyclerVH
import tm.devcraft.myuni.interfaces.singleActivity.AnnounceActivity
import tm.devcraft.myuni.utils.Utils

class PosterFragment : Fragment() {
    private var db = FirebaseFirestore.getInstance()
    private lateinit var adapter: FirestoreRecyclerAdapter<*, *>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var query = db.collection("announces").whereEqualTo("statusPublicate",true).orderBy("created", Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<AnnounceUserModel> = FirestoreRecyclerOptions.Builder<AnnounceUserModel>()
            //.setLifecycleOwner(parentFragment)
            .setQuery(query, AnnounceUserModel::class.java)
            .setLifecycleOwner(this)
            .build()

        adapter = object : FirestoreRecyclerAdapter<AnnounceUserModel, AnnouncentRecyclerVH>(options) {
            override fun onBindViewHolder(
                holder: AnnouncentRecyclerVH,
                position: Int,
                model: AnnounceUserModel
            ): Unit = holder.itemView.run {
                var user : userData

                db.collection("users").document(model.uidCreator).get().addOnSuccessListener {
                        doc ->
                    if(doc != null){
                        user = doc.toObject<userData>()!!
                        service.text = model.skill
                        id2.text = user.nameOfInstitution + ", " + user.course
                        price.text = model.price.toString() + " руб"
                        name.text = user.fullname
                        date.text = user.date?.let { it1 -> Utils.getAge(it1.toDate()) } + " лет"
                        skills.text = user.faculty
                        //created_date.text = ""
                        created_date.text = ""
                        Glide.with(context).load(Uri.parse(model.Photos["0"])).fitCenter().into(photo)
                        //Picasso.get().load(Uri.parse(model.Photos["0"])).into(photo)
                        //created_date.text = Date(model.created.seconds*1000+model.created.nanoseconds)
                        share.setOnClickListener{
                            var intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT,model.dLink)
                            startActivity(Intent.createChooser(intent,"Share Link"))
                            db.collection("announces").whereEqualTo("id",model.id).get().addOnSuccessListener {
                                    docs ->
                                for (doc in docs){
                                    var model = doc.toObject<AnnounceUserModel>()
                                    doc.reference.update("reposted",model.reposted+1)
                                }
                            }
                        }
                        save.setOnClickListener{
                            db.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).update("savedAnn",
                                FieldValue.arrayUnion(model.id))
                            db.collection("announces").whereEqualTo("id",model.id).get().addOnSuccessListener {
                                    docs ->
                                for (doc in docs){
                                    var model = doc.toObject<AnnounceUserModel>()
                                    doc.reference.update("saved",model.saved+1)
                                }
                            }
                        }
                        outlinedButton.setOnClickListener{
                            Paper.book().write("announceSelect",model)
                            db.collection("announces").whereEqualTo("id",model.id).get().addOnSuccessListener {
                                    docs ->
                                for (doc in docs){
                                    var model = doc.toObject<AnnounceUserModel>()
                                    doc.reference.update("views",model.views+1)
                                }
                            }
                            var intent = Intent(context,AnnounceActivity::class.java)
                            startActivity(intent)
                        }
                        send_req.setOnClickListener{
                            db.collection("announces").whereEqualTo("id",model.id).get().addOnSuccessListener {
                                    docs ->
                                for (doc in docs){
                                    var model = doc.toObject<AnnounceUserModel>()
                                    doc.reference.update("request",model.request+1)
                                }
                            }
                            //send_req.visibility = View.GONE
                        }

                    }
                    //Paper.book().write("userData",user)
                }

            }

            override fun onCreateViewHolder(group: ViewGroup, i: Int): AnnouncentRecyclerVH {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                val view: View = LayoutInflater.from(group.context)
                    .inflate(R.layout.item_announce, group, false)
                return AnnouncentRecyclerVH(view)
            }
        }




        return inflater.inflate(R.layout.tab_fragment_announcement, container, false)

    }

    override fun onStart() {
        super.onStart()
        announces_recycler.adapter = adapter
        announces_recycler.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)


    }

    override fun onStop() {
        super.onStop()
       // adapter.stopListening()
    }
}