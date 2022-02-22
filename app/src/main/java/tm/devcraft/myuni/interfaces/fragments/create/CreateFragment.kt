package tm.devcraft.myuni.interfaces.fragments.create

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_create.*
import kotlinx.android.synthetic.main.item_announce_from_create.view.*
import kotlinx.android.synthetic.main.item_announce_from_create.view.name
import kotlinx.android.synthetic.main.item_event_from_create.view.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.AnnounceUserModel
import tm.devcraft.myuni.data.EventUserModel
import tm.devcraft.myuni.data.userData
import tm.devcraft.myuni.interfaces.fragments.create.createActivity.CreateAnnounce
import tm.devcraft.myuni.interfaces.fragments.create.createActivity.CreateEvent
import tm.devcraft.myuni.interfaces.singleActivity.AnalyticAnnActivity
import tm.devcraft.myuni.utils.Utils

class CreateFragment : Fragment() {
    private var db = FirebaseFirestore.getInstance()
    private var user = Paper.book().read<userData>("userData")
    var queryAnn = db.collection("announces").whereEqualTo("uidCreator",FirebaseAuth.getInstance().currentUser?.uid.toString()).orderBy("created", Query.Direction.DESCENDING)


    val optionsAnn: FirestoreRecyclerOptions<AnnounceUserModel> = FirestoreRecyclerOptions.Builder<AnnounceUserModel>()
        .setQuery(queryAnn, AnnounceUserModel::class.java)
        .build()

    var queryEvents = db.collection("events").whereEqualTo("uidCreator",FirebaseAuth.getInstance().currentUser?.uid.toString()).orderBy("created", Query.Direction.DESCENDING)


    val optionsEvents: FirestoreRecyclerOptions<EventUserModel> = FirestoreRecyclerOptions.Builder<EventUserModel>()
        .setQuery(queryEvents, EventUserModel::class.java)
        .build()

    val snapHelper = PagerSnapHelper()


    private lateinit var adapter: FirestoreRecyclerAdapter<*, *>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return LayoutInflater
            .from(inflater.context)
            .inflate(R.layout.fragment_create, container, false)
    }

    override fun onStart() {
        super.onStart()
        btn_create_announce.setBackgroundColor(Color.parseColor("#A300BC"))
        btn_create_announce.setTextColor(Color.parseColor("#FFFFFF"))

        btn_create_event.setBackgroundColor(Color.parseColor("#FFFFFF"))
        btn_create_event.setTextColor(Color.parseColor("#000000"))

        create_what.text = "Новый анонс"
        my_annevent.text = "Мои анонсы"
        snapHelper.attachToRecyclerView(user_data)
        adapter =
            object : FirestoreRecyclerAdapter<AnnounceUserModel, AnnouncentVH>(optionsAnn) {
                override fun onBindViewHolder(
                    holder: AnnouncentVH,
                    position: Int,
                    model: AnnounceUserModel
                ) = holder.itemView.run {
                    Picasso.get().load(Uri.parse(model.Photos["0"])).into(photo_announce)

                    service.text = model.skill
                    vuz_course.text = user.nameOfInstitution + ", " + user.course
                    skills.text = user.faculty
                    age.text = user.date?.let { it1 -> Utils.getAge(it1.toDate()) }
                    name.text = user.fullname
                    share_ann.setOnClickListener{
                        var intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT,model.dLink)
                        startActivity(Intent.createChooser(intent,"Share Link"))
                    }
                    analytic_ann.setOnClickListener {
                        Paper.book().write("announceSelect",model)
                        var intent = Intent(context,AnalyticAnnActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onCreateViewHolder(group: ViewGroup, i: Int): AnnouncentVH {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    val view: View = LayoutInflater.from(group.context)
                        .inflate(R.layout.item_announce_from_create, group, false)
                    return AnnouncentVH(view)
                }


            }
        user_data.adapter = adapter
        user_data.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        adapter.startListening()
        initListeners()
    }

    private fun initListeners() {
        btn_create_announce.setOnClickListener {
            btn_create_announce.setBackgroundColor(Color.parseColor("#A300BC"))
            btn_create_announce.setTextColor(Color.parseColor("#FFFFFF"))

            btn_create_event.setBackgroundColor(Color.parseColor("#FFFFFF"))
            btn_create_event.setTextColor(Color.parseColor("#000000"))

            create_what.text = "Новый анонс"
            my_annevent.text = "Мои анонсы"



            adapter =
                object : FirestoreRecyclerAdapter<AnnounceUserModel, AnnouncentVH>(optionsAnn) {
                    override fun onBindViewHolder(
                        holder: AnnouncentVH,
                        position: Int,
                        model: AnnounceUserModel
                    ) = holder.itemView.run {
                        Picasso.get().load(Uri.parse(model.Photos["0"])).into(photo_announce)
                        //Glide.with(context).load(model.Photos["main"]).into(photo_announce)
                        //photo_announce.setImageURI(Uri.parse(model.Photos["main"]))
                        service.text = model.skill

                        vuz_course.text = user.nameOfInstitution + ", " + user.course
                        skills.text = user.faculty
                        age.text = user.date?.let { it1 -> Utils.getAge(it1.toDate()) }
                        name.text = user.fullname
                        share_ann.setOnClickListener{
                            var intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT,model.dLink)
                            startActivity(Intent.createChooser(intent,"Share Link"))
                        }
                        analytic_ann.setOnClickListener {
                            Paper.book().write("announceSelect",model)
                            var intent = Intent(context,AnalyticAnnActivity::class.java)
                            startActivity(intent)
                        }

                    }

                    override fun onCreateViewHolder(group: ViewGroup, i: Int): AnnouncentVH {
                        // Create a new instance of the ViewHolder, in this case we are using a custom
                        // layout called R.layout.message for each item
                        val view: View = LayoutInflater.from(group.context)
                            .inflate(R.layout.item_announce_from_create, group, false)
                        return AnnouncentVH(view)
                    }


                }

            user_data.adapter = adapter
            user_data.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter.startListening()

        }
        btn_create_event.setOnClickListener {
            btn_create_event.setBackgroundColor(Color.parseColor("#A300BC"))
            btn_create_event.setTextColor(Color.parseColor("#FFFFFF"))

            btn_create_announce.setBackgroundColor(Color.parseColor("#FFFFFF"))
            btn_create_announce.setTextColor(Color.parseColor("#000000"))

            create_what.text = "Новое событие"
            my_annevent.text = "Мои события"

            adapter =
                object : FirestoreRecyclerAdapter<EventUserModel, EventVH>(optionsEvents) {
                    override fun onBindViewHolder(
                        holder: EventVH,
                        position: Int,
                        model: EventUserModel
                    ) = holder.itemView.run {
                        Picasso.get().load(Uri.parse(model.photos["0"])).into(photo_event)
                        //Glide.with(context).load(model.Photos["main"]).into(photo_announce)
                        //photo_announce.setImageURI(Uri.parse(model.Photos["main"]))
                        name.text = model.name
                        share.setOnClickListener{
                            var intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT,model.dLink)
                            startActivity(Intent.createChooser(intent,"Share Link"))
                        }

                    }

                    override fun onCreateViewHolder(group: ViewGroup, i: Int): EventVH {
                        // Create a new instance of the ViewHolder, in this case we are using a custom
                        // layout called R.layout.message for each item
                        val view: View = LayoutInflater.from(group.context)
                            .inflate(R.layout.item_event_from_create, group, false)

                        return EventVH(view)
                    }


                }

            user_data.adapter = adapter
            user_data.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter.startListening()
        }

        create_what.setOnClickListener {
            if (create_what.text == "Новый анонс") {
                startActivity(Intent(context, CreateAnnounce::class.java))
            }
            else{
                startActivity(Intent(context, CreateEvent::class.java))
            }
        }
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}