package com.wp.galleryviewpager.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wp.galleryviewpager.R
import kotlin.math.abs

class HandleClickCarouselViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CarouselViewPager(context, attrs) {
    var onItemClickListener: ((clickPosition: Int) -> Unit)? = null

    private var downX = 0f
    private var downY = 0f
    private var lastDownTime = 0L

    private companion object {
        //一次点击、抬起间隔在300毫秒内才能被认为是点击事件
        const val CLICK_EVENT_INTERVAL_DURATION = 100

        //一次点击、抬起X,Y偏移量在20像素内才能被认为是点击事件
        const val CLICK_EVENT_OFFSET_PIXEL = 20
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null || onItemClickListener == null) {
            return super.onTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.rawX
                downY = ev.rawY
                lastDownTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_UP -> {
                checkClickEvent(ev.rawX, ev.rawY)
            }
        }
        return super.onTouchEvent(ev)
    }

    //点击事件判定
    private fun checkClickEvent(upX: Float, upY: Float) {
        if (System.currentTimeMillis() - lastDownTime < CLICK_EVENT_INTERVAL_DURATION
            && abs(upX - downX) < CLICK_EVENT_OFFSET_PIXEL
            && abs(upY - downY) < CLICK_EVENT_OFFSET_PIXEL
        ) {
            //发生了点击事件
            val clickView = viewOfClick(upX, upY) ?: return
            val clickPosition = clickView.getTag(R.id.view_pager_click_item_position_id)
            if (clickPosition !is Int) {
                throw IllegalStateException(
                    "想通过HandleClickCarouselViewPager来处理点击事件,必须给VP的每一个item设置position做为tag"
                )
            }
            onItemClickListener?.invoke(clickPosition)
        }
    }

    //被点击的View
    private fun viewOfClick(upX: Float, upY: Float): View? {
        //满足点击事件判定标准
        viewIsVisible().forEach {
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            //横向、纵向需要考虑缩放,不然宽高不是真实展示宽高
            if (upX >= location[0] && upX <= location[0] + it.width * it.scaleX && upY >= location[1] && upY <= location[1] + it.height * it.scaleY) {
                return it
            }
        }
        return null
    }

    //可见的itemView
    private fun viewIsVisible(): List<View> {
        val visibleView = ArrayList<View>()
        repeat(childCount) {
            val child = getChildAt(it)
            if (child.getGlobalVisibleRect(Rect())) {
                visibleView.add(child)
            }
        }
        return visibleView
    }
}