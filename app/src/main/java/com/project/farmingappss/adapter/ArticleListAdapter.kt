package com.project.farmingappss.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.project.farmingappss.R
import com.project.farmingappss.utilities.CellClickListener

class ArticleListAdapter(val context: Context, val articleListData: List<DocumentSnapshot>, private val cellClickListener: CellClickListener<String>): RecyclerView.Adapter<ArticleListAdapter.ArticleListViewholder>() {
    class ArticleListViewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        var articleName = itemView.findViewById<TextView>(R.id.descTextxArticleListFrag)
        var articleImage = itemView.findViewById<ImageView>(R.id.imageArticleListFrag)
        var articleCard = itemView.findViewById<CardView>(R.id.articleListCardArtListFrag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListViewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.article_list_single, parent, false)
        return ArticleListAdapter.ArticleListViewholder(view)
    }

    override fun getItemCount(): Int {
        return articleListData.size
    }

    override fun onBindViewHolder(holder: ArticleListViewholder, position: Int) {
        val singleArticle = articleListData[position]

        holder.articleName.text = singleArticle.data!!.get("title").toString()
        holder.articleCard.setOnClickListener {
            cellClickListener.onCellClickListener(singleArticle.data!!.get("title").toString())
        }
        var list : List<String> = singleArticle.data!!.get("images") as List<String>

        Glide.with(holder.itemView.context)
            .load(list[0])
//            .apply(RequestOptions.bitmapTransform(
//                RoundedCorners(20)
//            ))
            .into(holder.articleImage)

    }
}