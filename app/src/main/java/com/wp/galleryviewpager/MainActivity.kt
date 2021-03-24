package com.wp.galleryviewpager

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.wp.galleryviewpager.adapter.GalleryDemoAdapter
import com.wp.galleryviewpager.transform.GalleryTransformer
import com.wp.galleryviewpager.view.HandleClickCarouselViewPager

class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<HandleClickCarouselViewPager>(R.id.vpGallery).apply {
            changeScrollDuration(800)
            setPageTransformer(true, GalleryTransformer())
            offscreenPageLimit = 3
            pageMargin = dpToPx(10f).toInt()
            adapter = GalleryDemoAdapter(
                this@MainActivity, arrayListOf(
                    "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1008392441,1794527357&fm=26&gp=0.jpg",
                    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F00imgmini.eastday.com%2Fmobile%2F20181009%2F20181009102746_a664bda6711ff02542c1273b1cb466bb_7.jpeg&refer=http%3A%2F%2F00imgmini.eastday.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1618470220&t=856d47708f92fab0d984d3f3e3f77708",
                    "https://bkimg.cdn.bcebos.com/pic/b3fb43166d224f4af0a425ea0ff790529822d10e?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5/format,f_auto",
                    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201610%2F07%2F20161007101441_2WGHi.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1618470220&t=71e7a44618ca7d0675e1c993c3d29b33"
                )
            )
            currentItem = 400000
            startCarousel()
            onItemClickListener = {
                Toast.makeText(this@MainActivity, "点击了: ${it % 4}", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<FrameLayout>(R.id.flContainer).setOnTouchListener { _, event ->
            findViewById<HandleClickCarouselViewPager>(R.id.vpGallery).dispatchTouchEvent(event)
        }
    }
}