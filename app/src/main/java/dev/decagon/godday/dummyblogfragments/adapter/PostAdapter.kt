package dev.decagon.godday.dummyblogfragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import dev.decagon.godday.dummyblogfragments.R
import dev.decagon.godday.dummyblogfragments.databinding.RecyclerviewItemBinding
import dev.decagon.godday.dummyblogfragments.model.Post

/**
 * A recyclerView adapter class for holding and displaying posts on the screen
 */
class PostAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var data = mutableListOf<Post>()

    class PostViewHolder(private val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(post: Post) {
            val fav = binding.favorite
            val like = binding.like

            fav.setImageResource(R.drawable.ic_outline_favorite_border_24)
            like.setImageResource(R.drawable.ic_outline_thumb_up_alt_24)
            binding.title.text = post.title
            binding.userId.text = binding.root.context.getString(R.string.user_id, post.userId, post.id)
            binding.post.text = post.body

            like.setOnClickListener {
                like.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24)
                Toast.makeText(it.context, "post is liked!", Toast.LENGTH_SHORT).show()
            }

            fav.setOnClickListener {
                fav.setImageResource(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(it.context, "post is loved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val adapterLayout = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context))
        return PostViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = data[position]
        holder.itemView.setOnClickListener { onClickListener.onClick(post) }
        holder.onBind(post)
    }

    override fun getItemCount(): Int = data.size

    fun loadData(post: List<Post>) {
        this.data = post as MutableList<Post>
        notifyDataSetChanged()
    }

    class OnClickListener(val clickListener: (post: Post) -> Unit) {
        fun onClick(post: Post) = clickListener(post)
    }
}