package kz.aspan.vacancy.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kz.aspan.vacancy.common.extensions.px

class MarginItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceSize + 6.px
            }
            bottom = spaceSize
        }
    }
}