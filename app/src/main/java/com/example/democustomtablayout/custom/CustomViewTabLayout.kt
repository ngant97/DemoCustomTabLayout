package com.example.democustomtablayout.custom

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.drm.DrmStore
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.democustomtablayout.R
import com.google.android.material.math.MathUtils
import java.util.*


class CustomViewTabLayout(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    var textPaint: Paint //cọ để vẽ text màu đen
    var bgPaint: Paint //cọ để vẽ back ground đen
    var xferPaint: Paint // cọ để vẽ back ground mask
    var maskPaint: Paint  //cọ để vẽ text màu trắng
    var sourceImage: Bitmap? = null
    var bitmapBg: Bitmap? = null
    var bitmapMask: Bitmap? = null

    var titles: ArrayList<String> = ArrayList()
    var titlews: ArrayList<Float> = ArrayList()
    var titleps: ArrayList<Float> = ArrayList()
    var textPading: Int = 0
    var radius: Int = 0
    var bgPaddingV: Int = 0
    var bgHeight = 0
    var paddingHorizontalText: Int = 0
    var bgWidth = 0
    //for background mask process
    var startX = 0f
    var saveX: Float = 0f
    var currentFocus = 0f
    var pressedDownTime = 0L
    var witdhScreen = 0
    var horizontalScrollView: CustomMyTabLayout? = null
    var mListener:OnChangeTabIO?=null

    init {
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = context.resources.getDimension(R.dimen._16sp)
        textPaint.color = context.getColor(R.color.color_back)

        bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bgPaint.color = context.getColor(R.color.color_back)

        xferPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        xferPaint.color = context.getColor(R.color.color_white)
        xferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        maskPaint.color = Color.WHITE

        radius = context.resources.getDimensionPixelSize(R.dimen._30dp) //Bo viền cho background
        bgPaddingV = context.resources.getDimensionPixelSize(R.dimen._4dp) //padding 2 bên cho text
        bgHeight =
            context.resources.getDimensionPixelSize(R.dimen._50dp) //chiều cao của back ground
        paddingHorizontalText =
            context.resources.getDimensionPixelSize(R.dimen._8dp) //1/2 độ rộng của text

        isClickable = true
        isFocusable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        titlews.clear()

        textPading = context.resources.getDimensionPixelSize(R.dimen._14dp)
        var totalWidthSize = 0f
        for (i in titles.indices) {
            val title = titles[i]
            val w = textPaint.measureText(title)
            titlews.add(w)
            totalWidthSize += w
        }
        val width = totalWidthSize.toInt() + (titlews.size + 1) * textPading
        val height = context.resources.getDimensionPixelSize(R.dimen._50dp)

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)
        witdhScreen = displayMetrics.widthPixels

        sourceImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        sourceImage?.let { sourceImage ->
            val srcCanvas = Canvas(sourceImage)
            var drawingX = textPading.toFloat()
            for (i in titles.indices) {

                val posX = drawingX
                titleps.add(posX)

                srcCanvas.drawText(
                    titles[i],
                    posX,
                    (bgHeight / 2).toFloat() + paddingHorizontalText,
                    textPaint
                )

                drawingX = posX + titlews[i] + textPading.toFloat()
            }

            //MUST CALL THIS
            setMeasuredDimension(width, height)
        }

        processMask()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val ex = event.x
        if (event.action == MotionEvent.ACTION_DOWN) {
            pressedDownTime = System.currentTimeMillis()
            saveX = ex
            for (i in titles.indices) {
                if (saveX < titleps[i]) {
                    break
                }
                if (saveX > 0) {
                    //Do nothing
                }
            }
        }

        if (event.action == MotionEvent.ACTION_UP
            && System.currentTimeMillis() - pressedDownTime < 200
            && saveX > ex - 15 && saveX < ex + 15) {
            for (i in titles.indices) {
                if (ex > titleps[i] && ex < titleps[i] + titlews[i]) {
                    mListener?.tabSelected(i)
                    startAnimation(i)
                }

            }
        }

        return super.onTouchEvent(event)
    }

    private fun createBackground() {
        bitmapBg = Bitmap.createBitmap(
            bgWidth,
            bgHeight,
            Bitmap.Config.ARGB_8888
        )
        bitmapMask =
            Bitmap.createBitmap(
                bgWidth,
                bgHeight,
                Bitmap.Config.ARGB_8888
            )
        val bgRect = RectF(
            bgPaddingV.toFloat(),
            bgPaddingV.toFloat(),
            bitmapBg!!.width.toFloat(),
            (bitmapBg!!.height).toFloat()
        )

        bitmapBg?.let { bitmapBg ->
            val canvasBg = Canvas(bitmapBg)
            canvasBg.drawRoundRect(bgRect, radius.toFloat(), radius.toFloat(), bgPaint)
        }

        bitmapMask?.let { bitmapMask ->
            val canvasMask = Canvas(bitmapMask)
            canvasMask.drawRoundRect(bgRect, radius.toFloat(), radius.toFloat(), maskPaint)
        }

    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        sourceImage?.let { sourceImage ->
            canvas.drawBitmap(sourceImage, 0f, 0f, null)

            bitmapBg?.let { bitmapBg ->
                canvas.drawBitmap(bitmapBg, startX, 0f, bgPaint)
            }

            val bitmapTextOut =
                Bitmap.createBitmap(sourceImage.width, sourceImage.height, Bitmap.Config.ARGB_8888)
            val canvas2 = Canvas(bitmapTextOut)
            canvas2.drawBitmap(sourceImage, 0f, 0f, null)

            bitmapMask?.let { bitmapMask ->
                canvas2.drawBitmap(bitmapMask, startX, 0f, xferPaint)
            }
            canvas.drawBitmap(bitmapTextOut, 0f, 0f, null)
        }
    }

    private fun startAnimation(pos: Int) {
        val animator = ValueAnimator.ofFloat(currentFocus, pos.toFloat())
        animator.duration = 500
        animator.addUpdateListener { animation ->
            updatePosition(animation.animatedValue as Float)
        }
        animator.start()
    }

    fun updatePosition(position: Float) {
        if (titles.isNotEmpty()) {
            currentFocus = position
            processMask()
            invalidate()
        }
    }

    private fun processMask() {
        val spos = currentFocus.toInt()
        val epos = spos + 1
        val amount = currentFocus - spos

        if (epos < titles.size) {
            bgWidth = MathUtils.lerp(getWidthOf(spos).toFloat(), getWidthOf(epos).toFloat(), amount)
                .toInt()
            startX = MathUtils.lerp(getPosXOf(spos), getPosXOf(epos), amount)

            var sScrollPosition = titleps[spos].toInt() - (witdhScreen - titlews[spos]).toInt() / 2
            var eScrollPosition = titleps[epos].toInt() - (witdhScreen - titlews[epos]).toInt() / 2
            var scrollPosition =
                MathUtils.lerp(sScrollPosition.toFloat(), eScrollPosition.toFloat(), amount)
            horizontalScrollView?.smoothScrollTo(scrollPosition.toInt(), 0)

        } else {
            bgWidth = getWidthOf(spos)
            startX = getPosXOf(spos)

            var scrollPosition = titleps[spos].toInt() - (witdhScreen - titlews[spos]).toInt() / 2
            horizontalScrollView?.smoothScrollTo(scrollPosition, 0)
        }

        createBackground()
    }

    private fun getWidthOf(pos: Int): Int {
        return titlews[pos].toInt() + textPading + bgPaddingV * 2
    }

    private fun getPosXOf(pos: Int): Float {
        return titleps[pos] - textPading
    }

    fun setTitle(titles: ArrayList<String>) {
        this.titles.clear()
        this.titles.addAll(titles)
        invalidate()
    }
    fun setListenter(listener :OnChangeTabIO){
        mListener =listener
    }
    interface OnChangeTabIO {
        fun tabSelected(pos: Int)
    }
}