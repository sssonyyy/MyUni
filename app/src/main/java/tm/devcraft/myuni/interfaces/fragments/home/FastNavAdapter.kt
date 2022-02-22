package tm.devcraft.myuni.interfaces.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_main_fast_navigation.view.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.models.fastNavData.FastNavData

class FastNavAdapter(private val navDataArr: ArrayList<FastNavData>) :
    RecyclerView.Adapter<NavVH>() {

    private lateinit var listener: onItemClickListener
    interface onItemClickListener{
        fun OnItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavVH {
        return NavVH(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_main_fast_navigation, parent, false),
            listener
        )
    }

    override fun getItemCount() = navDataArr.size

    override fun onBindViewHolder(holder: NavVH, position: Int) {
        holder.bind(navDataArr[position])
    }
}

class NavVH(itemView: View, listener: FastNavAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener {
            listener.OnItemClick(adapterPosition)
        }
    }
    fun bind(item: FastNavData) {
        itemView.run {
            title.text = item.title
            img_hint.setImageResource(item.img)
            img_hint.scaleType = ImageView.ScaleType.CENTER_CROP
            description.text = item.description


        }
    }

}
