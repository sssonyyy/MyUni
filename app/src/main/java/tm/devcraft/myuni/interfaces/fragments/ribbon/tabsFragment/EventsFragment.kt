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
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import io.paperdb.Paper
import kotlinx.android.synthetic.main.item_event.view.*
import kotlinx.android.synthetic.main.tab_fragment_events.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.EventUserModel
import tm.devcraft.myuni.interfaces.fragments.ribbon.tabsFragment.EventsRecycler.EventsRecyclerVH
import tm.devcraft.myuni.interfaces.singleActivity.EventActivity
import java.text.SimpleDateFormat

class EventsFragment : Fragment() {
    private var db = FirebaseFirestore.getInstance()
    private lateinit var adapter: FirestoreRecyclerAdapter<*, *>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tab_fragment_events, container, false)
    }

    override fun onStart() {
        super.onStart()

        var query = db.collection("events").whereEqualTo("statusPublicate",true).whereNotEqualTo("idCreator",FirebaseAuth.getInstance().currentUser?.uid.toString())
        val options: FirestoreRecyclerOptions<EventUserModel> = FirestoreRecyclerOptions.Builder<EventUserModel>()
            //.setLifecycleOwner(parentFragment)
            .setQuery(query, EventUserModel::class.java)
            .build()

        adapter = object : FirestoreRecyclerAdapter<EventUserModel, EventsRecyclerVH>(options) {
            override fun onBindViewHolder(
                holder: EventsRecyclerVH,
                position: Int,
                model: EventUserModel
            ): Unit = holder.itemView.run {
                        val format = SimpleDateFormat("dd/MM/yyyy")

                        type.text = model.type
                        //date.text = model.dateFrom
                        name.text = model.name
                        if(model.dateTo != null ){
                            date.text = format.format(model.dateFrom).toString().replace("/",".") + "-" + format.format(model.dateTo).toString().replace("/",".")
                        }
                        else{
                            date.text = format.format(model.dateFrom).toString().replace("/",".")
                        }
                        Glide.with(context).load(Uri.parse(model.photos["0"])).fitCenter().into(photo)
                        share.setOnClickListener{
                            var intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT,model.dLink)
                            startActivity(Intent.createChooser(intent,"Share Link"))
                            db.collection("events").whereEqualTo("id",model.id).get().addOnSuccessListener {
                                    docs ->
                                for (doc in docs){
                                    var model = doc.toObject<EventUserModel>()
                                    doc.reference.update("reposted",model.reposted+1)
                                }
                            }
                        }
                        save.setOnClickListener{
                            db.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).update("savedEvents",
                                FieldValue.arrayUnion(model.id))
                            db.collection("events").whereEqualTo("id",model.id).get().addOnSuccessListener {
                                    docs ->
                                for (doc in docs){
                                    var model = doc.toObject<EventUserModel>()
                                    doc.reference.update("saved",model.saved+1)
                                }
                            }

                        }
                        outlinedButton.setOnClickListener{
                            Paper.book().write("popularEvent",model)
                            var intent = Intent(context,EventActivity::class.java)
                            startActivity(intent)
                            db.collection("events").whereEqualTo("id",model.id).get().addOnSuccessListener {
                                    docs ->
                                for (doc in docs){
                                    var model = doc.toObject<EventUserModel>()
                                    doc.reference.update("views",model.views+1)
                                }
                            }
                        }
                        //Picasso.get().load(Uri.parse(model.Photos["0"])).into(photo)
                        //created_date.text = Date(model.created.seconds*1000+model.created.nanoseconds)

                    //Paper.book().write("userData",user)


            }

            override fun onCreateViewHolder(group: ViewGroup, i: Int): EventsRecyclerVH {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                val view: View = LayoutInflater.from(group.context)
                    .inflate(R.layout.item_event, group, false)
                return EventsRecyclerVH(view)
            }
        }

        query.addSnapshotListener(EventListener<QuerySnapshot>(){ value, error ->
            if(error == null){
                //Log.e("bug","bug")
                adapter.notifyDataSetChanged()
            }
        })

        events_recycler.adapter = adapter
        events_recycler.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}