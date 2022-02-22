package tm.devcraft.myuni.interfaces.fragments.chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_chat_dialog.view.*
import tm.devcraft.myuni.R


class MessageList(private val arr: ArrayList<Any>) :
    RecyclerView.Adapter<MessageList.MessageVH>() {

    private lateinit var listener: OnClickListener

    interface OnClickListener {
        fun onClick(pos: Int)
    }

    fun setOnItemClickListener(listener: OnClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageVH {
        return MessageVH(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_chat_dialog, parent, false), listener)
    }

    override fun getItemCount() = arr.size

    override fun onBindViewHolder(holder: MessageVH, position: Int) {
        holder.bind(arr)

    }

    class MessageVH(itemView: View, itemClick: OnClickListener) : RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener {
                itemClick.onClick(adapterPosition)
            }
        }
        fun bind(arr: ArrayList<Any>) {
            itemView.run {
                name.text = arr[0].toString()
                photo_u.setImageResource(arr[1] as Int)
            }
        }
    }
}
