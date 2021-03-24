package com.wp.galleryviewpager.transform

import android.view.View
import androidx.viewpager.widget.ViewPager

class GalleryTransformer : ViewPager.PageTransformer {
    private companion object {
        private const val MIN_SCALE = 0.5f
        private const val MAX_SCALE = 1.0f
        private const val MIN_ALPHA = 0.5f
        private const val MAX_ALPHA = 1.0f
    }

    override fun transformPage(page: View, position: Float) {
        if (position < 0) {
            //左边的page缩放基点在右下部分
            page.pivotX = page.width.toFloat()
            page.pivotY = page.height / 13f * 8f
        } else {
            //右边的page缩放基点在左下部分
            page.pivotX = 0f
            page.pivotY = page.height / 13f * 8f
        }
        val tempScale = if (position < 0) {
            1 + position
        } else {
            1 - position
        }
        val scaleValue = MIN_SCALE + tempScale * ((MAX_SCALE - MIN_SCALE) / 1)
        page.scaleX = scaleValue
        page.scaleY = scaleValue
        val tempAlpha = if (position < 0) {
            1 + position
        } else {
            1 - position
        }
        page.alpha = MIN_ALPHA + tempAlpha * ((MAX_ALPHA - MIN_ALPHA) / 1)
    }
}