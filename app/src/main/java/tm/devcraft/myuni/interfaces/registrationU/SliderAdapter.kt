package tm.devcraft.myuni.interfaces.registrationU

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.slider_item.view.*
import tm.devcraft.myuni.R

class SliderAdapter(
    private val list: ArrayList<Uri>
) : RecyclerView.Adapter<PagerVH>() {

    private val limit: Int = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.slider_item, parent, false))

    override fun getItemCount(): Int {
        return if (list.size > limit) {
            limit
        } else {
            list.size
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        imageView.setImageURI(list[position])
        val count = position + 1
        counter_photo.text = "$count/5"
        btn_delete_photo.setOnClickListener {
            list.removeAt(position-1)
            notifyItemRangeChanged(position, list.size)
        }
    }
}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)
