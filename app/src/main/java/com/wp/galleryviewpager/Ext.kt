package com.wp.galleryviewpager

import android.content.res.Resources
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager
import java.lang.reflect.Field

fun dpToPx(dp: Float): Float {
    return dp * Resources.getSystem().displayMetrics.density
}

fun ViewPager.changeScrollDuration(newDuration: Int) {
    try {
        val mScroller: Field = ViewPager::class.java.getDeclaredField("mScroller")
        mScroller.isAccessible = true
        mScroller.set(this, object : Scroller(context) {
            override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
                super.startScroll(startX, startY, dx, dy, newDuration)
            }

            override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
                super.startScroll(startX, startY, dx, dy, newDuration)
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}