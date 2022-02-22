package tm.devcraft.myuni.interfaces.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_home.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.DataConstants
import tm.devcraft.myuni.data.EventUserModel
import tm.devcraft.myuni.interfaces.fragments.ribbon.RibbonFragment

class HomeFragment : Fragment() {

    private val fastNavAdapter: FastNavAdapter = FastNavAdapter(DataConstants.fastNavArr)
    private lateinit var popularAdapterr: AdapterPopular
    private var db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        popularAdapterr = context?.let { AdapterPopular(it) }!!
        db.collection("events").whereEqualTo("statusPublicate",true).orderBy("views", Query.Direction.DESCENDING).limit(4).get()
            .addOnSuccessListener { documents ->
                for(doc in documents){
                    println(documents)
                    println(doc)
                    if(doc != null){
                        val item = doc.toObject<EventUserModel>()
                        Paper.book().write("popularEvent",item)
                        popularAdapterr.addItem(item)
                    }
                }
            }

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (popularAdapterr != null) {
            imageSlider.setSliderAdapter(popularAdapterr)
        }
        //imageSlider.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        rv_fast_navigation_main.adapter = fastNavAdapter
        fastNavAdapter.setOnItemClickListener(object : FastNavAdapter.onItemClickListener{
            override fun OnItemClick(position: Int) {
                println(position)
                when(position){
                    0 -> {

                        activity?.supportFragmentManager?.beginTransaction()?.apply {
                            replace(R.id.nav_host_fragment,RibbonFragment("events"))
                            commit()
                        }
                    }
                    1->{
                        activity?.supportFragmentManager?.beginTransaction()?.apply {
                            replace(R.id.nav_host_fragment,RibbonFragment("people"))
                            commit()
                        }
                    }
                    3->{

                        activity?.supportFragmentManager?.beginTransaction()?.apply {
                            replace(R.id.nav_host_fragment,RibbonFragment("announces"))
                            commit()
                        }
                    }
                }
            }
        })
        rv_fast_navigation_main.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)



    }
}