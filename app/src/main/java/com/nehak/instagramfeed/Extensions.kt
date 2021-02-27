package com.nehak.instagramfeed

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
         * Returns the maximum value in an array.
         *
         * @param array  an array, must not be null or empty
         * @return the maximum value in the array
         */
        fun max(array: IntArray): Int {

            // Finds and returns max
            var max: Int = array[0]
            for (j in 1 until array.size) {
                if (array[j] > max) {
                    max = array[j]
                }
            }
            return max
        }


        /**
         *
         * Returns the min value in an array.
         *
         * @param array  an array, must not be null or empty
         * @return the min value in the array
         */
        fun min(array: IntArray): Int {

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
         * Returns LastVisibleItemPosition
         *
         */
        fun RecyclerView.findLastVisibleItemPosition(): Int {
            if (getLayoutManager() is LinearLayoutManager) {
                return (getLayoutManager() as LinearLayoutManager).findLastVisibleItemPosition()
            }
            return if (getLayoutManager() is StaggeredGridLayoutManager) {
                findLastVisibleItemPosition(getLayoutManager() as StaggeredGridLayoutManager)
            } else -1
        }

        /**
         *
         * Returns LastVisibleItemPosition
         *
         */
        fun findLastVisibleItemPosition(
            staggeredGridLayoutManager: StaggeredGridLayoutManager
        ): Int {
            val mItemPositionsHolder = IntArray(staggeredGridLayoutManager.spanCount)
            return max(
                staggeredGridLayoutManager.findLastVisibleItemPositions(mItemPositionsHolder)
            )
        }


        /**
         *
         * Returns true if recyclerView isAtTop
         *
         */
        fun RecyclerView.isAtTop(): Boolean {
            val pos: Int = (layoutManager as LinearLayoutManager?)?.findFirstCompletelyVisibleItemPosition()!!
            return if (pos == 0) { //&& linearLayoutmanager.findViewByPosition(pos)!=null && linearLayoutmanager.findViewByPosition(pos).getTop()==0){
                true
            } else false
        }
    }

}