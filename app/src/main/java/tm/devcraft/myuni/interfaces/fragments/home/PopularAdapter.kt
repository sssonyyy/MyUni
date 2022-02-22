package tm.devcraft.myuni.interfaces.fragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import kotlinx.android.synthetic.main.item_main_popular_rv.view.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.EventUserModel
import tm.devcraft.myuni.interfaces.singleActivity.EventActivity
import java.text.SimpleDateFormat

class AdapterPopular(var context: Context):  SliderViewAdapter<AdapterPopular.VH>() {
    var mSliderItems = ArrayList<EventUserModel>()

    private var db = FirebaseFirestore.getInstance()
    fun renewItems(sliderItems: ArrayList<EventUserModel>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: EventUserModel) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH {
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.item_main_popular_rv, null)
        return VH(inflate)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        //load image into vie
        Picasso.get().load(mSliderItems[position].photos["0"]).into(viewHolder.imageView)
        viewHolder.imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        viewHolder.title.text = mSliderItems[position].name
        viewHolder.place.text = mSliderItems[position].place

        val format = SimpleDateFormat("dd/MM/yyyy")

        viewHolder.imageView.setOnClickListener {
            Paper.book().write("popularEvent",mSliderItems[position])
            var intent = Intent(context,EventActivity::class.java)
            context.startActivity(intent)
        }

        viewHolder.addBtn.setOnClickListener {
            db.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).update("savedEvents",FieldValue.arrayUnion(mSliderItems[position].id))
        }



        if(mSliderItems[position].dateTo != null ){
            viewHolder.date.text = format.format(mSliderItems[position].dateFrom).toString().replace("/",".") + "-" + format.format(mSliderItems[position].dateTo).toString().replace("/",".")
        }
        else{
            viewHolder.date.text = format.format(mSliderItems[position].dateFrom).toString().replace("/",".")
        }
        if(mSliderItems[position].link != ""){
            viewHolder.btn.visibility = View.VISIBLE
        }
        else{
            viewHolder.btn.visibility = View.GONE
        }
        //viewHolder.imageView.setImageURI(mSliderItems[position])
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

    inner class VH(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.popular)
        var title = itemView.title
        var place = itemView.place
        var date = itemView.date
        var btn = itemView.btn_more
        var addBtn = itemView.btn_add_favorite
    }
}