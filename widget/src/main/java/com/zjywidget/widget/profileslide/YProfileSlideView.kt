package com.zjywidget.widget.profileslide

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.zjywidget.widget.R
import java.lang.StringBuilder


/**
 * <b>Project:</b> https://github.com/GitHubZJY/ZJYWidget <br>
 * <b>Create Date:</b> 2020/6/2 <br>
 * <b>@author:</b> Yang <br>
 * <b>Description:</b> 侧滑解锁自定义View <br>
 */
class YProfileSlideView : View {

    /**
     * 侧滑条圆角度数
     */
    private var mCorner: Float = 0f

    /**
     * 滑块宽度
     */
    private var mSliderWidth: Float = 0f

    /**
     * 提示文案字体大小
     */
    private var mTextSize: Float = 0f

    /**
     * 滑块自动归位动画时长
     */
    private var mAutoSlideTime: Int = 0

    /**
     * 背景色
     */
    private var mBackgroundColor: Int = 0

    /**
     * 已滑过的区域颜色
     */
    private var mProgressColor: Int = 0

    /**
     * 滑块颜色
     */
    private var mSliderColor: Int = 0

    /**
     * 提示文案
     */
    private lateinit var mTipText: String

    /**
     * 滑块与背景的内间距
     */
    private var mPadding: Float = 0f

    private lateinit var mBgPaint: Paint
    private lateinit var mSliderPaint: Paint
    private lateinit var mTextPaint: Paint
    private lateinit var mBgRectF: RectF
    private lateinit var mProgressRectF: RectF
    private lateinit var mSliderRectF: RectF
    private lateinit var mSlideArrowPath: Path

    private var mLastTouchX = 0F
    private var mAlphaProgress = 0F
    private lateinit var mAutoSlideAnimator: ValueAnimator
    private lateinit var mArrowAlphaAnimator: ValueAnimator
    private lateinit var mTextAnimator: ValueAnimator

    private lateinit var mLinearGradient: LinearGradient
    private lateinit var mGradientMatrix: Matrix
    private var mGradientProgress = 0F

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.YProfileSlideView, defStyle, 0)

        mCorner = typedArray.getDimension(R.styleable.YProfileSlideView_profile_slide_corner, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22f, resources.displayMetrics))
        mSliderWidth = typedArray.getDimension(R.styleable.YProfileSlideView_profile_slide_width, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, resources.displayMetrics))
        mTextSize = typedArray.getDimension(R.styleable.YProfileSlideView_profile_slide_text_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics))
        mAutoSlideTime = typedArray.getInt(R.styleable.YProfileSlideView_profile_slide_auto_slide_time, 500)
        mBackgroundColor = typedArray.getColor(R.styleable.YProfileSlideView_profile_slide_background_color, Color.parseColor("#E6E6E6"))
        mProgressColor = typedArray.getColor(R.styleable.YProfileSlideView_profile_slide_progress_color, Color.parseColor("#30C28B"))
        mSliderColor = typedArray.getColor(R.styleable.YProfileSlideView_profile_slide_slider_color, Color.parseColor("#E04E3E"))
        mTipText = typedArray.getString(R.styleable.YProfileSlideView_profile_slide_tip_text)
                ?: "滑动通过验证"
        mPadding = typedArray.getDimension(R.styleable.YProfileSlideView_profile_slide_padding, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics))

        typedArray.recycle()

        mBgRectF = RectF()
        mProgressRectF = RectF()
        mSliderRectF = RectF()
        mSlideArrowPath = Path()
        mBgPaint = Paint()
        mBgPaint.color = mBackgroundColor
        mBgPaint.isAntiAlias = true

        mSliderPaint = Paint()
        mSliderPaint.color = mSliderColor
        mSliderPaint.isAntiAlias = true

        mTextPaint = Paint()
        mTextPaint.color = Color.GRAY
        mTextPaint.textSize = mTextSize
        mTextPaint.isAntiAlias = true

        mGradientMatrix = Matrix()

        initAnim()
    }

    private fun initAnim() {
        mAutoSlideAnimator = ValueAnimator()
        mAutoSlideAnimator.duration = mAutoSlideTime.toLong()
        mAutoSlideAnimator.addUpdateListener {
            mSliderRectF.left = it.animatedValue as Float
            mSliderRectF.right = mSliderRectF.left + mSliderWidth
            mProgressRectF.right = mSliderRectF.right
            if (mSliderRectF.right == mBgRectF.width() - mPadding) {
                mListener?.slideEnd()
            }
            invalidate()
        }

        mArrowAlphaAnimator = ValueAnimator()
        mArrowAlphaAnimator.setFloatValues(1F, 3F)
        mArrowAlphaAnimator.repeatCount = -1
        mArrowAlphaAnimator.duration = 800
        mArrowAlphaAnimator.addUpdateListener {
            mAlphaProgress = it.animatedValue as Float
            invalidate()
        }

        mTextAnimator = ValueAnimator()
        mTextAnimator.setFloatValues(0F, 1F)
        mTextAnimator.repeatCount = -1
        mTextAnimator.duration = 2000
        mTextAnimator.addUpdateListener {
            mGradientProgress = it.animatedValue as Float
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val viewWidth = right - left
        val viewHeight = bottom - top

        mBgRectF.left = 0f
        mBgRectF.top = 0f
        mBgRectF.right = viewWidth * 1.0f
        mBgRectF.bottom = viewHeight * 1.0f

        mSliderRectF.left = mPadding
        mSliderRectF.top = mPadding
        mSliderRectF.right = mSliderWidth
        mSliderRectF.bottom = viewHeight * 1.0f - mPadding

        mProgressRectF.left = mPadding
        mProgressRectF.top = mPadding
        mProgressRectF.right = mSliderWidth / 2
        mProgressRectF.bottom = viewHeight * 1.0f - mPadding

        mLinearGradient = LinearGradient(0F, 0F, viewWidth * 1.0F, 0F, intArrayOf(Color.GRAY, Color.WHITE, Color.GRAY), null, Shader.TileMode.CLAMP)
        mTextPaint.shader = mLinearGradient
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mArrowAlphaAnimator.start()
        mTextAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制背景
        drawBackground(canvas)
        //绘制有效进度
        drawProgress(canvas)
        //绘制提示文字
        drawTipText(canvas)
        //绘制滑块
        drawSlider(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRoundRect(mBgRectF, mCorner, mCorner, mBgPaint)
    }

    private fun drawProgress(canvas: Canvas) {
        mSliderPaint.style = Paint.Style.FILL
        mSliderPaint.color = mProgressColor
        canvas.drawRoundRect(mProgressRectF, mCorner, mCorner, mSliderPaint)
    }

    private fun drawTipText(canvas: Canvas) {
        mGradientMatrix.setTranslate(mGradientProgress * mBgRectF.width(), 0F)
        mLinearGradient.setLocalMatrix(mGradientMatrix)
        val textWidth = mTextPaint.measureText(mTipText)
        val fontMetrics = mTextPaint.fontMetrics
        val baseLine: Float = mBgRectF.height() * 0.5f - (fontMetrics.ascent + fontMetrics.descent) / 2
        canvas.drawText(mTipText, ((mBgRectF.width() - mSliderWidth - textWidth) / 2f) + mSliderWidth, baseLine, mTextPaint)
    }

    private fun drawSlider(canvas: Canvas) {
        mSliderPaint.style = Paint.Style.FILL
        mSliderPaint.color = mSliderColor
        canvas.drawRoundRect(mSliderRectF, mCorner, mCorner, mSliderPaint)

        mSliderPaint.style = Paint.Style.STROKE
        mSliderPaint.strokeWidth = 2f
        //箭头宽度为滑块的1/16
        val arrowWidth = mSliderWidth / 16f
        //遍历下标1到3，依次绘制3个箭头
        for (index in 1..3) {
            val alphaValue = if (index + mAlphaProgress >= 4) {
                index + mAlphaProgress - 3
            } else {
                (index + mAlphaProgress).coerceAtMost(3F)
            }
            mSlideArrowPath.reset()
            val colorBuilder = StringBuilder()
            colorBuilder.clear()
            colorBuilder.append("#")
            colorBuilder.append(Integer.toHexString((85 * alphaValue).toInt()))
            colorBuilder.append("FFFFFF")
            mSliderPaint.color = Color.parseColor(colorBuilder.toString())
            //这里使得3个箭头的横坐标依次在滑块的1/3,1/2,2/3处
            val arrowPos = (index + 1) / 6f
            mSlideArrowPath.moveTo(mSliderRectF.left + mSliderWidth * arrowPos - arrowWidth / 2, mSliderRectF.bottom / 3f)
            mSlideArrowPath.lineTo(mSliderRectF.left + mSliderWidth * arrowPos + arrowWidth, mSliderRectF.bottom / 2f)
            mSlideArrowPath.lineTo(mSliderRectF.left + mSliderWidth * arrowPos - arrowWidth / 2, mSliderRectF.bottom * 2 / 3f)
            canvas.drawPath(mSlideArrowPath, mSliderPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastTouchX = 0f
                if (!checkTouchSlide(x, y)) {
                    return false
                } else {
                    mAutoSlideAnimator?.cancel()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mLastTouchX > 0f) {
                    when {
                        mSliderRectF.left + x - mLastTouchX + mSliderWidth > mBgRectF.width() - mPadding -> {
                            mSliderRectF.left = mBgRectF.width() - mPadding - mSliderWidth
                            mSliderRectF.right = mSliderRectF.left + mSliderWidth
                            mProgressRectF.right = mSliderRectF.right
                        }
                        mSliderRectF.left + x - mLastTouchX < mPadding -> {
                            mSliderRectF.left = mPadding
                            mSliderRectF.right = mSliderRectF.left + mSliderWidth
                            mProgressRectF.right = mSliderRectF.right
                        }
                        else -> {
                            mSliderRectF.left += x - mLastTouchX
                            mSliderRectF.right = mSliderRectF.left + mSliderWidth
                            mProgressRectF.right = mSliderRectF.right
                        }
                    }
                    invalidate()
                }
                mLastTouchX = x
            }
            MotionEvent.ACTION_UP -> {
                if (mSliderRectF.centerX() > mBgRectF.centerX()) {
                    mAutoSlideAnimator.setFloatValues(mSliderRectF.left, mBgRectF.right - mPadding - mSliderWidth)
                    mAutoSlideAnimator.start()
                } else {
                    mAutoSlideAnimator.setFloatValues(mSliderRectF.left, mPadding)
                    mAutoSlideAnimator.start()
                }
            }
        }
        return true
    }

    private fun checkTouchSlide(x: Float, y: Float): Boolean {
        return mSliderRectF.contains(x, y)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mArrowAlphaAnimator?.apply {
            cancel()
        }
        mTextAnimator?.apply {
            cancel()
        }
        mAutoSlideAnimator?.apply {
            cancel()
        }
    }

    var mListener: SlideListener? = null

    public fun addSlideListener(listener: SlideListener?) {
        mListener = listener
    }

    interface SlideListener {
        fun slideEnd()
    }
}