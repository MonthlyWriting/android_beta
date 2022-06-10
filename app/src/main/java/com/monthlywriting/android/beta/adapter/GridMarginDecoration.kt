package com.monthlywriting.android.beta.adapter

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridMarginDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view) // item position
        val column: Int = position % 4
        val margin: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            12f,
            context.resources.displayMetrics).toInt()

        outRect.left = column * margin / 4
        outRect.right = margin - (column + 1) * margin / 4
        outRect.bottom = margin / 3 * 2
    }
}