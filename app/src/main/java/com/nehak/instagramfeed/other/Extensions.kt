package com.nehak.instagramfeed.other

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by Neha Kushwah on 2/27/21.
 */
class Extensions {

    companion object {

        /**
         *
         * Returns FirstVisibleItemPosition
         *
         */
        fun RecyclerView.findFirstVisibleItemPosition(): Int {
            if (layoutManager is LinearLayoutManager) {
                return (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
            }
            return if (layoutManager is StaggeredGridLayoutManager) {
                val mItemPositionsHolder =
                    IntArray((layoutManager as StaggeredGridLayoutManager?)!!.spanCount)
                return min(
                    (layoutManager as StaggeredGridLayoutManager?)!!.findFirstVisibleItemPositions(
                        mItemPositionsHolder
                    )
                )
            } else -1
        }


        /**
         *
         * Returns the min value in an array.
         *
         * @param array  an array, must not be null or empty
         * @return the min value in the array
         */
        private fun min(array: IntArray): Int {

            // Finds and returns max
            var min: Int = array[0]
            for (j in 1 until array.size) {
                if (array[j] < min) {
                    min = array[j]
                }
            }
            return min
        }


        /**
         *
         * Returns true if recyclerView isAtTop
         *
         */
        fun RecyclerView.isAtTop(): Boolean {
            val pos: Int = (layoutManager as LinearLayoutManager?)?.findFirstCompletelyVisibleItemPosition()!!
            return pos == 0
        }


        @SuppressLint("DiscouragedApi")
        fun Context.getResource(name:String): Drawable? {
            val resID = this.resources.getIdentifier(name , "drawable", this.packageName)
            return ActivityCompat.getDrawable(this,resID)
        }
    }

}