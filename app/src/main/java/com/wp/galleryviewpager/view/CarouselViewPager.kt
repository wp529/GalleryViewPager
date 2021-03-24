package com.wp.galleryviewpager.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager.widget.ViewPager
import com.wp.galleryviewpager.R

open class CarouselViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {
    //轮询间隔，默认3秒切换一次
    private val interval: Long

    //轮询中
    private var inCarousel: Boolean = false

    //是否调用过startCarousel()方法
    private var calledStartCarousel = false

    //轮询的handler
    private val carouselHandler: Handler

    //轮询任务
    private val carouselRunnable: Runnable

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.CarouselViewPager)
        interval = attr.getInt(R.styleable.CarouselViewPager_carouselInterval, 3 * 1000).toLong()
        attr.recycle()
        carouselHandler = Handler()
        carouselRunnable = object : Runnable {
            override fun run() {
                adapter ?: return
                carouselHandler.postDelayed(this, interval)
                val nextItemIndex = if (currentItem == adapter!!.count - 1) {
                    //已经滚动到最后一个了,那就轮询到第一条去
                    0
                } else {
                    currentItem + 1
                }
                currentItem = nextItemIndex
            }
        }
        (context as? LifecycleOwner)?.lifecycle?.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                startCarouselInner()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                stopCarousel()
            }
        })
    }

    /**
     * 开始轮播
     */
    fun startCarousel() {
        calledStartCarousel = true
        startCarouselInner()
    }

    /**
     * 停止轮播
     */
    fun stopCarousel() {
        if (!inCarousel) {
            return
        }
        inCarousel = false
        carouselHandler.removeCallbacks(carouselRunnable)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        ev ?: return super.onTouchEvent(ev)
        //触摸停止,离开恢复轮询
        when (ev.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                stopCarousel()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                startCarouselInner()
            }
        }
        return super.onTouchEvent(ev)
    }

    //内部启动轮播
    private fun startCarouselInner() {
        if (!calledStartCarousel) {
            return
        }
        if (inCarousel) {
            return
        }
        adapter ?: throw IllegalStateException("开始轮播必须设置数据")
        if (adapter!!.count <= 1) {
            //数量不足一个不需要轮播
            return
        }
        inCarousel = true
        carouselHandler.postDelayed(carouselRunnable, interval)
    }
}