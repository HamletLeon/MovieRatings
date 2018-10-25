package com.hamletleon.moviewratings.adapter.movies

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hamletleon.moviewratings.R
import com.hamletleon.moviewratings.utils.load
import com.hamletleon.moviewratings.utils.setColorTint
import com.hamletleon.moviewratings.utils.setDateToTextView
import com.hamletleon.moviewratings.utils.setTextView
import kotlinx.android.synthetic.main.movie_item_list.view.*

class MovieViewHolder(view: View): RecyclerView.ViewHolder(view) {
    @SuppressLint("SetTextI18n") // Not enough time to do it in the right way with resources
    fun bind(item: MovieItemModel?, onItemClicked: (View, MovieItemModel) -> Unit): View? {
        if (item == null) return null

        // Top Layout
        itemView.topImage.load(item.imageUrl, if (item.big) "w700" else "w185")
        itemView.popularityAverage.text = item.popularityAverage.toString() + "%"
        itemView.popularityIndicator.progress = item.popularityAverage
        itemView.language.text = item.language

        // Details Layout
        fun setAddToFavoritesColor(act: Boolean) = itemView.addToFavorites.setColorTint(if (act) R.color.colorAccent else android.R.color.darker_gray)
        item.title.setTextView(itemView.title, R.string.title_not_specified)
        item.overview.setTextView(itemView.overview, R.string.overview_not_specified)
        item.releasedDate.setDateToTextView(itemView.releaseDate, R.string.release_date_not_specified, "dd/MM/yyyy")
        setAddToFavoritesColor(item.addedToFavourite)
        itemView.addToFavorites.setOnClickListener {
            setAddToFavoritesColor(!item.addedToFavourite)
            onItemClicked(it, item)
        }

        return itemView
    }
}