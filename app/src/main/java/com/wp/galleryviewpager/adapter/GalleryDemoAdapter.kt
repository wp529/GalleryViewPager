package com.wp.galleryviewpager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.wp.galleryviewpager.R
import com.wp.galleryviewpager.dpToPx

class GalleryDemoAdapter(
    private val context: Context,
    private val imageUrlList: List<String>
) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getCount(): Int = Int.MAX_VALUE

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageUrl = imageUrlList[position % imageUrlList.size]
        return LayoutInflater.from(context)
            .inflate(R.layout.item_demo, null)
            .apply {
                Glide.with(context)
                    .load(imageUrl)
                    .apply(RequestOptions().apply {
                        transform(
                            MultiTransformation(
                                CenterCrop(),
                                RoundedCorners(dpToPx(10f).toInt())
                            )
                        )
                    }).into(findViewById(R.id.ivImage))
                setTag(R.id.view_pager_click_item_position_id, position)
                container.addView(this)
            }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as? View)
    }
}