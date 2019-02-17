package com.zjywidget.widget.arcmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zjywidget.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YANG on 2019/2/16.
 * 卫星式弧形导航菜单
 */

public class YArcMenuView extends FrameLayout implements View.OnClickListener {

    public static final String TAG = "ArcMenuView";

    private Context mContext;

    /**
     * 主菜单按钮
     */
    private ImageView mMenuIv;

    /**
     * 主菜单按钮资源ID
     */
    private int mMenuResId;

    /**
     * 所有菜单子项View
     */
    private List<ImageView> mImgViews;

    /**
     * 所有菜单子项资源ID
     */
    private List<Integer> mMenuItemResIds;

    /**
     * View宽高
     */
    private int mWidth, mHeight;

    /**
     * 展开半径
     */
    private int mRadius;

    /**
     * 主菜单按钮宽高
     */
    private int mMenuWidth;

    /**
     * 菜单子项宽高
     */
    private int mMenuItemWidth;

    /**
     * 展开/收缩 动画时长
     */
    private long mDuration;

    /**
     * 是否支持在展开的同时旋转
     */
    private boolean mCanRotate;

    /**
     * 是否处于展开状态
     */
    boolean mIsOpen;

    public YArcMenuView(@NonNull Context context) {
        this(context, null);
    }

    public YArcMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YArcMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        mImgViews = new ArrayList<>();
        mMenuItemResIds = new ArrayList<>();
        handleStyleable(context, attrs, defStyleAttr);
        initMenuItemViews(context);
        initMenuView(context);
    }


    private void handleStyleable(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenuView, defStyle, 0);
        try {
            mRadius = ta.getDimensionPixelSize(R.styleable.ArcMenuView_spread_radius, dip2px(context, 150));
            mMenuWidth = ta.getDimensionPixelSize(R.styleable.ArcMenuView_menu_width, dip2px(context, 64));
            mMenuItemWidth = ta.getDimensionPixelSize(R.styleable.ArcMenuView_menu_item_width, dip2px(context, 64));
            mDuration = ta.getInteger(R.styleable.ArcMenuView_duration, 1000);
            mCanRotate = ta.getBoolean(R.styleable.ArcMenuView_can_rotate, true);
            mMenuResId = ta.getResourceId(R.styleable.ArcMenuView_menu_drawable, R.drawable.ic_arc_menu);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }
    }

    /**
     * 初始化主按钮
     * @param context
     */
    private void initMenuView(Context context) {
        mMenuIv = new ImageView(context);
        mMenuIv.setImageResource(mMenuResId);
        FrameLayout.LayoutParams params = new LayoutParams(mMenuWidth, mMenuWidth);
        params.bottomMargin = mMenuItemWidth / 2;
        params.rightMargin = mMenuItemWidth / 2;
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        addView(mMenuIv, params);
        mMenuIv.setOnClickListener(this);
    }

    /**
     * 初始化菜单子项按钮
     * @param context
     */
    private void initMenuItemViews(Context context) {
        mImgViews.clear();
        for (int index = 0; index < mMenuItemResIds.size(); index++) {
            ImageView menuItem = new ImageView(context);
            menuItem.setImageResource(mMenuItemResIds.get(index));
            FrameLayout.LayoutParams params = new LayoutParams(mMenuItemWidth, mMenuItemWidth);
            params.bottomMargin = mMenuItemWidth / 2;
            params.rightMargin = mMenuItemWidth / 2;
            params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            menuItem.setTag(index);
            menuItem.setOnClickListener(this);
            addView(menuItem, params);
            menuItem.setScaleX(0f);
            menuItem.setScaleY(0f);

            mImgViews.add(menuItem);
        }
    }

    /**
     * 设置菜单子项Icon资源
     *
     * @param resIds 资源Id列表
     */
    public void setMenuItems(List<Integer> resIds) {
        removeAllViews();
        mMenuItemResIds = resIds;
        initMenuItemViews(mContext);
        initMenuView(mContext);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = mMenuItemWidth * 2 + mRadius;
        mHeight = mMenuItemWidth * 2 + mRadius;
        setMeasuredDimension(mWidth, mHeight);

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == mMenuIv) {
            if (mMenuItemResIds == null || mMenuItemResIds.size() == 0) {
                Log.e(TAG, "当前未设置菜单子项！");
                return;
            }
            if (mIsOpen) {
                startCloseAnim();
                mIsOpen = false;
                return;
            }
            startOpenAnim();
            mIsOpen = true;
        } else {
            rotateMenu(90, 0);
            int index = (int) view.getTag();
            startClickItemAnim(index);
            if (mItemListener != null && index < mMenuItemResIds.size()) {
                mItemListener.clickMenuItem(mMenuItemResIds.get(index));
            }
        }
    }

    /**
     * 菜单展开动画
     */
    private void startOpenAnim() {
        int count = mMenuItemResIds.size();
        List<Animator> animators = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int tranX = -(int) (mRadius * Math.sin(Math.toRadians(90 * i / (count - 1))));
            int tranY = -(int) (mRadius * Math.cos(Math.toRadians(90 * i / (count - 1))));
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(mImgViews.get(i), "translationX", 0f, tranX);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(mImgViews.get(i), "translationY", 0f, tranY);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mImgViews.get(i), "alpha", 0, 1);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mImgViews.get(i), "scaleX", 0.1f, 1);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mImgViews.get(i), "scaleY", 0.1f, 1);

            animators.add(animatorX);
            animators.add(animatorY);
            animators.add(alpha);
            animators.add(scaleX);
            animators.add(scaleY);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mDuration);
        animatorSet.playTogether(animators);
        animatorSet.start();
        rotateMenu(0, 90);
    }

    /**
     * 菜单收回动画
     */
    private void startCloseAnim() {
        int count = mMenuItemResIds.size();
        List<Animator> animators = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int tranX = -(int) (mRadius * Math.sin(Math.toRadians(90 * i / (count - 1))));
            int tranY = -(int) (mRadius * Math.cos(Math.toRadians(90 * i / (count - 1))));
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(mImgViews.get(i), "translationX", tranX, 0f);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(mImgViews.get(i), "translationY", tranY, 0f);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mImgViews.get(i), "alpha", 1, 0);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mImgViews.get(i), "scaleX", 1, 0.3f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mImgViews.get(i), "scaleY", 1, 0.3f);

            animators.add(animatorX);
            animators.add(animatorY);
            animators.add(alpha);
            animators.add(scaleX);
            animators.add(scaleY);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mDuration);
        animatorSet.playTogether(animators);
        animatorSet.start();
        rotateMenu(90, 0);
    }

    /**
     * 菜单子项点击动画
     *
     * @param index 子项下标
     */
    private void startClickItemAnim(int index) {
        int count = mMenuItemResIds.size();
        List<Animator> animators = new ArrayList<>();
        //当前被点击按钮放大且逐渐变透明，造成消散效果
        ObjectAnimator clickItemAlpha = ObjectAnimator.ofFloat(mImgViews.get(index), "alpha", 1, 0);
        ObjectAnimator clickItemScaleX = ObjectAnimator.ofFloat(mImgViews.get(index), "scaleX", 1, 2);
        ObjectAnimator clickItemScaleY = ObjectAnimator.ofFloat(mImgViews.get(index), "scaleY", 1, 2);
        animators.add(clickItemAlpha);
        animators.add(clickItemScaleX);
        animators.add(clickItemScaleY);

        for (int i = 0; i < count; i++) {
            if (index == i) {
                //过滤当前被点击的子项
                continue;
            }
            //其他选项缩小且变透明
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mImgViews.get(i), "alpha", 1, 0);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mImgViews.get(i), "scaleX", 1, 0.1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mImgViews.get(i), "scaleY", 1, 0.1f);
            animators.add(alpha);
            animators.add(scaleX);
            animators.add(scaleY);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(animators);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //点击动画结束之后要将所有子项归位
                resetItems();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    /**
     * 旋转主菜单按钮
     *
     * @param startAngel 起始角度
     * @param endAngel   结束角度
     */
    private void rotateMenu(int startAngel, int endAngel) {
        if (!mCanRotate) {
            return;
        }
        ObjectAnimator clickItemAlpha = ObjectAnimator.ofFloat(mMenuIv, "rotation", startAngel, endAngel);
        clickItemAlpha.setDuration(mDuration);
        clickItemAlpha.start();
    }

    /**
     * 重置所有子项位置
     */
    private void resetItems() {
        int count = mImgViews.size();
        for (int i = 0; i < mImgViews.size(); i++) {
            int tranX = (int) (mRadius * Math.sin(Math.toRadians(90 * i / (count - 1))));
            int tranY = (int) (mRadius * Math.cos(Math.toRadians(90 * i / (count - 1))));
            mImgViews.get(i).setTranslationX(tranX);
            mImgViews.get(i).setTranslationY(tranY);
        }
        mIsOpen = false;
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    ClickMenuListener mItemListener;

    public void setClickItemListener(ClickMenuListener mItemListener) {
        this.mItemListener = mItemListener;
    }

    public interface ClickMenuListener {
        void clickMenuItem(int resId);
    }

}
