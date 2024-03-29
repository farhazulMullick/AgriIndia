package com.project.farmingappss.adapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.project.farmingappss.R
import com.project.farmingappss.utilities.CellClickListener
import kotlinx.android.synthetic.main.user_profile_posts_single.view.*

class PostListUserProfileAdapter(val context: Context, var listData: ArrayList<DocumentSnapshot>, private val cellClickListener: CellClickListener<String>) : RecyclerView.Adapter<PostListUserProfileAdapter.PostListUserProfileViewHolder>() {
    class PostListUserProfileViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostListUserProfileViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_profile_posts_single, parent, false)
        return PostListUserProfileAdapter.PostListUserProfileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: PostListUserProfileViewHolder, position: Int) {
        val currentData = listData[position]

        holder.itemView.userPostTitleUserProfileFrag.text = currentData.get("title").toString()
        holder.itemView.userPostUploadTimeUserProfileFrag.text = DateUtils.getRelativeTimeSpanString(currentData.get("timeStamp") as Long)
        holder.itemView.userPostProfileCard.setOnClickListener {
            cellClickListener.onCellClickListener(currentData.id)
        }
        if (!currentData.get("imageUrl").toString().isNullOrEmpty()){
            Glide.with(context).load(currentData.getString("imageUrl")).into(holder.itemView.userPostImageUserProfileFrag)
        }
    }
}