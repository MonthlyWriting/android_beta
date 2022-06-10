package com.monthlywriting.android.beta.adapter

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.monthlywriting.android.beta.R

class HorizontalMarginDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        if (parent.getChildAdapterPosition(view) != parent.childCount) {
            outRect.right =
                context.resources.getDimension(R.dimen.horizontal_recycler_view_margin).toInt()
        }
    }
}