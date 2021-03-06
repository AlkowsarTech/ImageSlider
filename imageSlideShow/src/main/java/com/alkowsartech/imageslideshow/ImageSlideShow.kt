package com.alkowsartech.imageslideshow

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.alkowsartech.imageslideshow.adapters.ViewPagerAdapter
import com.alkowsartech.imageslideshow.constants.ScaleTypes
import com.alkowsartech.imageslideshow.interfaces.ItemClickListener
import com.alkowsartech.imageslideshow.models.SlideModel

import java.util.*

/**
 * Created by Adnan Ayoub on 12/08/2020.
 * www.alkowsartech.com
 * Kashmir Shopian-192303
 */
class ImageSlideShow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {

    private var viewPager: ViewPager? = null
    private var pagerDots: LinearLayout? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null

    private var dots: Array<ImageView?>? = null

    private var currentPage = 0
    private var imageCount = 0

    private var cornerRadius: Int = 0
    private var period: Long = 0
    private var delay: Long = 0
    private var autoCycle = false

    private var selectedDot = 0
    private var unselectedDot = 0
    private var errorImage = 0
    private var placeholder = 0
    private var titleBackground = 0
    private var textAlign = "LEFT"
    private var swipeTimer = Timer()

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.image_slide_show, this, true)
        viewPager = findViewById(R.id.view_pager)
        pagerDots = findViewById(R.id.pager_dots)

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ImageSlideShow,
            defStyleAttr,
            defStyleAttr
        )

        cornerRadius = typedArray.getInt(R.styleable.ImageSlideShow_iss_corner_radius, 1)
        period = typedArray.getInt(R.styleable.ImageSlideShow_iss_period, 1000).toLong()
        delay = typedArray.getInt(R.styleable.ImageSlideShow_iss_delay, 1000).toLong()
        autoCycle = typedArray.getBoolean(R.styleable.ImageSlideShow_iss_auto_cycle, false)
        placeholder =
            typedArray.getResourceId(R.styleable.ImageSlideShow_iss_placeholder, R.drawable.loading)
        errorImage =
            typedArray.getResourceId(R.styleable.ImageSlideShow_iss_error_image, R.drawable.error)
        selectedDot = typedArray.getResourceId(
            R.styleable.ImageSlideShow_iss_selected_dot,
            R.drawable.default_selected_dot
        )
        unselectedDot = typedArray.getResourceId(
            R.styleable.ImageSlideShow_iss_unselected_dot,
            R.drawable.default_unselected_dot
        )
        titleBackground = typedArray.getResourceId(
            R.styleable.ImageSlideShow_iss_title_background,
            R.drawable.gradient
        )
        if (typedArray.getString(R.styleable.ImageSlideShow_iss_text_align) != null) {
            textAlign = typedArray.getString(R.styleable.ImageSlideShow_iss_text_align)!!
        }
    }

    /**
     * Set image list to adapter.
     *
     * @param  imageList  the image list by user
     * @param  scaleType  scale type for all image
     */
    fun setImageList(imageList: List<SlideModel>, scaleType: ScaleTypes? = null) {
        viewPagerAdapter = ViewPagerAdapter(
            context,
            imageList,
            cornerRadius,
            errorImage,
            placeholder,
            titleBackground,
            scaleType,
            textAlign
        )
        viewPager!!.adapter = viewPagerAdapter
        imageCount = imageList.size
        if (imageList.size > 1) {
            setupDots(imageList.size)
            if (autoCycle) {
                startSliding()
            }
        }
    }

    private fun setupDots(size: Int) {
        pagerDots!!.removeAllViews()
        dots = arrayOfNulls(size)

        for (i in 0 until size) {
            dots!![i] = ImageView(context)
            dots!![i]!!.setImageDrawable(ContextCompat.getDrawable(context, unselectedDot))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            pagerDots!!.addView(dots!![i], params)
        }
        dots!![0]!!.setImageDrawable(ContextCompat.getDrawable(context, selectedDot))

        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.d("Tag", position.toString())
            }

            override fun onPageSelected(position: Int) {
                currentPage = position
                for (dot in dots!!) {
                    dot!!.setImageDrawable(ContextCompat.getDrawable(context, unselectedDot))
                }
                dots!![position]!!.setImageDrawable(ContextCompat.getDrawable(context, selectedDot))
            }

            override fun onPageScrollStateChanged(state: Int) {
                Log.d("Tag", state.toString())
            }
        })
    }

    /**
     * Start image sliding.
     *
     * @param  changeablePeriod  optional period value
     */
    fun startSliding(changeablePeriod: Long = period) {
        scheduleTimer(changeablePeriod)
    }

    /**
     * Stop image sliding.
     *
     */
    fun stopSliding() {
        swipeTimer.cancel()
        swipeTimer.purge()
    }

    private fun scheduleTimer(period: Long) {
        val handler = Handler()
        val update = Runnable {
            if (currentPage == imageCount) {
                currentPage = 0
            }
            viewPager!!.setCurrentItem(currentPage++, true)
        }
        swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, delay, period)
    }

    /**
     * Set item click listener for listen to image click
     *
     * @param  itemClickListener  interface callback
     */
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        viewPagerAdapter?.setItemClickListener(itemClickListener)
    }

}



