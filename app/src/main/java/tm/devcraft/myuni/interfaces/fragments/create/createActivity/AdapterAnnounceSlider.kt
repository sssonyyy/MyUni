package tm.devcraft.myuni.interfaces.fragments.create.createActivity

import android.app.ActionBar
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso
import tm.devcraft.myuni.R

class AdapterAnnounceSlider(): SliderViewAdapter<AdapterAnnounceSlider.VH>() {
    private var mSliderItems = ArrayList<Uri>()

    fun renewItems(sliderItems: ArrayList<Uri>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: Uri) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH {
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.image_item, null)
        return VH(inflate)
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        //load image into vie
        Picasso.get().load(mSliderItems[position]).into(viewHolder.imageView)
        viewHolder.imageView.layoutParams.height = ActionBar.LayoutParams.WRAP_CONTENT
        viewHolder.imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        //viewHolder.imageView.setImageURI(mSliderItems[position])
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

    inner class VH(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageSlider)

    }
}