package com.github.florent37.viewtooltip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

/**
 * Created by florentchampigny on 02/06/2017.
 */

public class ViewTooltip {
    public static final int POSITION_OVERLAY = 1;
    public static final int POSITION_TOOLTIP = 2;
    public static final int POSITION_TARGET = 3;
    private View rootView;
    private final View view;
    private final TooltipView tooltip_view;
    private final FrameLayout overlay;
    private OnClickListener onClickListener;

    @IntDef({POSITION_OVERLAY, POSITION_TOOLTIP, POSITION_TARGET})
    @Retention(RetentionPolicy.SOURCE)
    private @interface ViwPosition {}

    private ViewTooltip(View view) {
        this(view.getContext(), view);
    }

    private ViewTooltip(Context context, View view) {
        this(context, null, view);
    }

    private ViewTooltip(Context context, View rootView, View view) {
        this.rootView = rootView;
        this.view = view;
        this.tooltip_view = new TooltipView(context);
        this.overlay = new FrameLayout(context);
        final NestedScrollView scrollParent = findScrollParent(view);
        if (scrollParent != null) {
            scrollParent.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    tooltip_view.setTranslationY(tooltip_view.getTranslationY() - (scrollY - oldScrollY));
                }
            });
        }

        tooltip_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null) onClickListener.onClick(POSITION_TOOLTIP, ViewTooltip.this);
                if (tooltip_view.clickToHide) {
                    tooltip_view.remove();
                }
            }
        });
    }

    private void initTargetClone(){
        TargetGhostView targetGhostView = new TargetGhostView(view.getContext());
        targetGhostView.setTarget(view);
        overlay.addView(targetGhostView);
        targetGhostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null) onClickListener.onClick(POSITION_TARGET, ViewTooltip.this);
            }
        });
    }

    public static ViewTooltip on(final View view) {
        return new ViewTooltip(view);
    }

    public static ViewTooltip on(Context context, final View view) {
        return new ViewTooltip(context, view);
    }

    public static ViewTooltip on(Context context, final View rootView, final View view) {
        return new ViewTooltip(context, rootView, view);
    }

    private NestedScrollView findScrollParent(View view) {
        if (view.getParent() == null || !(view.getParent() instanceof View)) {
            return null;
        } else if (view.getParent() instanceof NestedScrollView) {
            return ((NestedScrollView) view.getParent());
        } else {
            return findScrollParent(((View) view.getParent()));
        }
    }

    private static Activity getActivityContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public ViewTooltip position(Position position) {
        this.tooltip_view.setPosition(position);
        return this;
    }

    public ViewTooltip withShadow(boolean withShadow) {
        this.tooltip_view.setWithShadow(withShadow);
        return this;
    }

    public ViewTooltip shadowColor(@ColorInt int shadowColor) {
        this.tooltip_view.setShadowColor(shadowColor);
        return this;
    }

    public ViewTooltip customView(View customView) {
        this.tooltip_view.setCustomView(customView);
        return this;
    }

    public ViewTooltip customView(int viewId) {
        this.tooltip_view.setCustomView(((Activity) view.getContext()).findViewById(viewId));
        return this;
    }

    public ViewTooltip arrowWidth(int arrowWidth) {
        this.tooltip_view.setArrowWidth(arrowWidth);
        return this;
    }

    public ViewTooltip arrowHeight(int arrowHeight) {
        this.tooltip_view.setArrowHeight(arrowHeight);
        return this;
    }

    public ViewTooltip arrowSourceMargin(int arrowSourceMargin) {
        this.tooltip_view.setArrowSourceMargin(arrowSourceMargin);
        return this;
    }

    public ViewTooltip arrowTargetMargin(int arrowTargetMargin) {
        this.tooltip_view.setArrowTargetMargin(arrowTargetMargin);
        return this;
    }

    public ViewTooltip align(ALIGN align) {
        this.tooltip_view.setAlign(align);
        return this;
    }

    public ViewTooltip overlayColor(@ColorInt int color){
        overlay.setBackgroundColor(color);
        initTargetClone();
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null) onClickListener.onClick(POSITION_OVERLAY, ViewTooltip.this);
            }
        });
        return this;
    }

    public ViewTooltip onClick(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
        return this;
    }

    public void hide(){
        tooltip_view.remove();
    }

    public TooltipView show() {
        final Context activityContext = tooltip_view.getContext();
        if (activityContext != null && activityContext instanceof Activity) {
            final ViewGroup decorView = rootView != null ?
                    (ViewGroup) rootView :
                    (ViewGroup) ((Activity) activityContext).getWindow().getDecorView();

            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Rect rect = new Rect();
                    view.getGlobalVisibleRect(rect);

                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    rect.left = location[0];
                    //rect.left = location[0];

                    overlay.addView(tooltip_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    decorView.addView(overlay, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    tooltip_view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {

                            tooltip_view.setup(rect, decorView.getWidth());

                            tooltip_view.getViewTreeObserver().removeOnPreDrawListener(this);

                            return false;
                        }
                    });
                }
            }, 100);
        }
        return tooltip_view;
    }

    public void close(){
        tooltip_view.close();
    }

    public ViewTooltip duration(long duration) {
        this.tooltip_view.setDuration(duration);
        return this;
    }

    public ViewTooltip color(int color) {
        this.tooltip_view.setColor(color);
        return this;
    }

    public ViewTooltip color(Paint paint) {
        this.tooltip_view.setPaint(paint);
        return this;
    }

    public ViewTooltip onDisplay(ListenerDisplay listener) {
        this.tooltip_view.setListenerDisplay(listener);
        return this;
    }

    public ViewTooltip onHide(ListenerHide listener) {
        this.tooltip_view.setListenerHide(listener);
        return this;
    }

    public ViewTooltip padding(int left, int top, int right, int bottom) {
        this.tooltip_view.paddingTop = top;
        this.tooltip_view.paddingBottom = bottom;
        this.tooltip_view.paddingLeft = left;
        this.tooltip_view.paddingRight = right;
        return this;
    }

    public ViewTooltip animation(TooltipAnimation tooltipAnimation) {
        this.tooltip_view.setTooltipAnimation(tooltipAnimation);
        return this;
    }

    public ViewTooltip text(String text) {
        this.tooltip_view.setText(text);
        return this;
    }

    public ViewTooltip text(@StringRes int text) {
        this.tooltip_view.setText(text);
        return this;
    }

    public ViewTooltip corner(int corner) {
        this.tooltip_view.setCorner(corner);
        return this;
    }

    public ViewTooltip textColor(int textColor) {
        this.tooltip_view.setTextColor(textColor);
        return this;
    }

    public ViewTooltip textTypeFace(Typeface typeface) {
        this.tooltip_view.setTextTypeFace(typeface);
        return this;
    }

    public ViewTooltip textSize(int unit, float textSize) {
        this.tooltip_view.setTextSize(unit, textSize);
        return this;
    }

    public ViewTooltip setTextGravity (int textGravity) {
        this.tooltip_view.setTextGravity(textGravity);
        return this;
    }

    public ViewTooltip clickToHide(boolean clickToHide) {
        this.tooltip_view.setClickToHide(clickToHide);
        return this;
    }

    public ViewTooltip autoHide(boolean autoHide, long duration) {
        this.tooltip_view.setAutoHide(autoHide);
        this.tooltip_view.setDuration(duration);
        return this;
    }

    public ViewTooltip distanceWithView(int distance) {
        this.tooltip_view.setDistanceWithView(distance);
        return this;
    }

    public ViewTooltip border(int color,float width){
        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(color);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(width);
        this.tooltip_view.setBorderPaint(borderPaint);
        return this;
    }

    public enum Position {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
    }

    public enum ALIGN {
        START,
        CENTER,
        END
    }

    public interface TooltipAnimation {
        void animateEnter(View view, Animator.AnimatorListener animatorListener);

        void animateExit(View view, Animator.AnimatorListener animatorListener);
    }

    public interface ListenerDisplay {
        void onDisplay(View view);
    }

    public interface ListenerHide {
        void onHide(View view);
    }

    public static class FadeTooltipAnimation implements TooltipAnimation {

        private long fadeDuration = 400;

        public FadeTooltipAnimation() {
        }

        public FadeTooltipAnimation(long fadeDuration) {
            this.fadeDuration = fadeDuration;
        }

        @Override
        public void animateEnter(View view, Animator.AnimatorListener animatorListener) {
            view.setAlpha(0);
            view.animate().alpha(1).setDuration(fadeDuration).setListener(animatorListener);
        }

        @Override
        public void animateExit(View view, Animator.AnimatorListener animatorListener) {
            view.animate().alpha(0).setDuration(fadeDuration).setListener(animatorListener);
        }
    }

    public interface OnClickListener{
        void onClick(@ViwPosition int position, ViewTooltip view);
    }

    /**
     * Target Image View
     *
     */
    private static class TargetGhostView extends AppCompatImageView {
        private int[] position = new int[4];
        private int[] size = new int[2];

        public TargetGhostView(Context context) {
            super(context);
        }

        private void setLayoutLikeAsView(View target){
            target.getLocationOnScreen(position);
            position[2] = position[0] + target.getWidth();
            position[3] = position[1] + target.getHeight();
        }

        @Override
        public void layout(int l, int t, int r, int b) {
            super.layout(position[0], position[1], position[2], position[3]);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(size[0], size[1]);
        }

        public void setTarget(View target){
            if(target == null) return;
            setLayoutLikeAsView(target);
            Bitmap bitmap = getBitmapFromView(target);
            setImageBitmap(bitmap);
            size[0] = bitmap.getWidth();
            size[1] = bitmap.getHeight();
        }

        public static Bitmap createDrawableFromView(Activity activity, View view) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
            view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
            view.buildDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);

            return bitmap;
        }

        public static Bitmap loadBitmapFromView(View v) {
            if (v.getMeasuredHeight() <= 0) {
                int specWidth = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
                v.measure(specWidth, specWidth);
                int questionWidth = v.getMeasuredWidth();

                int specHeight = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
                v.measure(specHeight, specHeight);
                int questionHeight = v.getMeasuredHeight();

                v.measure(questionWidth, questionHeight);

                Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(b);
                v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
                v.draw(c);
                return b;
            }

            Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            v.draw(c);


            return b;
        }

        public static Bitmap getBitmapFromView(View view) {
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            Drawable bgDrawable =view.getBackground();
            if (bgDrawable!=null)
                bgDrawable.draw(canvas);
            else
                canvas.drawColor(Color.WHITE);
            view.draw(canvas);
            return returnedBitmap;
        }
    }

    public static class TooltipView extends FrameLayout {

        private static final int MARGIN_SCREEN_BORDER_TOOLTIP = 30;
        private int arrowHeight = 15;
        private int arrowWidth = 15;
        private int arrowSourceMargin = 0;
        private int arrowTargetMargin = 0;
        protected View childView;
        private int color = 0xff1F7C82;
        private Path bubblePath;
        private Paint bubblePaint;
        private Paint borderPaint;
        private Position position = Position.BOTTOM;
        private ALIGN align = ALIGN.CENTER;
        private boolean clickToHide;
        private boolean autoHide = true;
        private long duration = 4000;

        private ListenerDisplay listenerDisplay;

        private ListenerHide listenerHide;

        private TooltipAnimation tooltipAnimation = new FadeTooltipAnimation();

        private int corner = 30;

        private int paddingTop = 20;
        private int paddingBottom = 30;
        private int paddingRight = 30;
        private int paddingLeft = 30;

        int shadowPadding = 4;
        int shadowWidth = 8;

        private Rect viewRect;
        private int distanceWithView = 0;
        private int shadowColor = 0xffaaaaaa;

        public TooltipView(Context context) {
            super(context);
            setWillNotDraw(false);

            this.childView = new TextView(context);
            ((TextView) childView).setTextColor(Color.WHITE);
            addView(childView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            childView.setPadding(0, 0, 0, 0);

            bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bubblePaint.setColor(color);
            bubblePaint.setStyle(Paint.Style.FILL);

            borderPaint = null;

            setLayerType(LAYER_TYPE_SOFTWARE, bubblePaint);

            setWithShadow(true);

        }

        public void setCustomView(View customView) {
            this.removeView(childView);
            this.childView = customView;
            addView(childView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        public void setColor(int color) {
            this.color = color;
            bubblePaint.setColor(color);
            postInvalidate();
        }

        public void setShadowColor(int color) {
            this.shadowColor = color;
            postInvalidate();
        }

        public void setPaint(Paint paint) {
            bubblePaint = paint;
            setLayerType(LAYER_TYPE_SOFTWARE, paint);
            postInvalidate();
        }

        public void setPosition(Position position) {
            this.position = position;
            switch (position){
                case TOP:
                    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + arrowHeight);
                    break;
                case BOTTOM:
                    setPadding(paddingLeft, paddingTop + arrowHeight, paddingRight, paddingBottom);
                    break;
                case LEFT:
                    setPadding(paddingLeft, paddingTop, paddingRight + arrowHeight, paddingBottom);
                    break;
                case RIGHT:
                    setPadding(paddingLeft + arrowHeight, paddingTop, paddingRight, paddingBottom);
                    break;
            }
            postInvalidate();
        }

        public void setAlign(ALIGN align) {
            this.align = align;
            postInvalidate();
        }

        public void setText(String text) {
            if (childView instanceof TextView) {
                ((TextView) this.childView).setText(Html.fromHtml(text));
            }
            postInvalidate();
        }

        public void setText(int text) {
            if (childView instanceof TextView) {
                ((TextView) this.childView).setText(text);
            }
            postInvalidate();
        }

        public void setTextColor(int textColor) {
            if (childView instanceof TextView) {
                ((TextView) this.childView).setTextColor(textColor);
            }
            postInvalidate();
        }

        public int getArrowHeight() {
            return arrowHeight;
        }

        public void setArrowHeight(int arrowHeight) {
            this.arrowHeight = arrowHeight;
            postInvalidate();
        }

        public int getArrowWidth() {
            return arrowWidth;
        }

        public void setArrowWidth(int arrowWidth) {
            this.arrowWidth = arrowWidth;
            postInvalidate();
        }

        public int getArrowSourceMargin() {
            return arrowSourceMargin;
        }

        public void setArrowSourceMargin(int arrowSourceMargin) {
            this.arrowSourceMargin = arrowSourceMargin;
            postInvalidate();
        }

        public int getArrowTargetMargin() {
            return arrowTargetMargin;
        }

        public void setArrowTargetMargin(int arrowTargetMargin) {
            this.arrowTargetMargin = arrowTargetMargin;
            postInvalidate();
        }

        public void setTextTypeFace(Typeface textTypeFace) {
            if (childView instanceof TextView) {
                ((TextView) this.childView).setTypeface(textTypeFace);
            }
            postInvalidate();
        }

        public void setTextSize(int unit, float size) {
            if (childView instanceof TextView) {
                ((TextView) this.childView).setTextSize(unit, size);
            }
            postInvalidate();
        }

        public void setTextGravity(int textGravity) {
            if (childView instanceof TextView) {
                ((TextView) this.childView).setGravity(textGravity);
            }
            postInvalidate();
        }

        public void setClickToHide(boolean clickToHide) {
            this.clickToHide = clickToHide;
        }

        public void setCorner(int corner) {
            this.corner = corner;
        }

        @Override
        protected void onSizeChanged(int width, int height, int oldw, int oldh) {
            super.onSizeChanged(width, height, oldw, oldh);

            bubblePath = drawBubble(new RectF(shadowPadding, shadowPadding, width - shadowPadding * 2, height - shadowPadding * 2), corner, corner, corner, corner);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (bubblePath != null) {
                canvas.drawPath(bubblePath, bubblePaint);
                if(borderPaint != null){
                    canvas.drawPath(bubblePath,borderPaint);
                }
            }
        }

        public void setListenerDisplay(ListenerDisplay listener) {
            this.listenerDisplay = listener;
        }

        public void setListenerHide(ListenerHide listener) {
            this.listenerHide = listener;
        }

        public void setTooltipAnimation(TooltipAnimation tooltipAnimation) {
            this.tooltipAnimation = tooltipAnimation;
        }

        protected void startEnterAnimation() {
            tooltipAnimation.animateEnter((View) this.getParent(), new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (listenerDisplay != null) {
                        listenerDisplay.onDisplay(TooltipView.this);
                    }
                }
            });
        }

        protected void startExitAnimation(final Animator.AnimatorListener animatorListener) {
            tooltipAnimation.animateExit((View) this.getParent(), new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animatorListener.onAnimationEnd(animation);
                    if (listenerHide != null) {
                        listenerHide.onHide(TooltipView.this);
                    }
                }
            });
        }

        protected void handleAutoRemove() {
            if (autoHide) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        remove();
                    }
                }, duration);
            }
        }

        public void remove() {
            startExitAnimation(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    removeNow();
                }
            });
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public void setAutoHide(boolean autoHide) {
            this.autoHide = autoHide;
        }

        public void setupPosition(Rect rect) {

            int x, y;

            if (position == Position.LEFT || position == Position.RIGHT) {
                if (position == Position.LEFT) {
                    x = rect.left - getWidth() - distanceWithView;
                } else {
                    x = rect.right + distanceWithView;
                }
                y = rect.top + getAlignOffset(getHeight(), rect.height());
            } else {
                if (position == Position.BOTTOM) {
                    y = rect.bottom + distanceWithView;
                } else { // top
                    y = rect.top - getHeight() - distanceWithView;
                }
                x = rect.left + getAlignOffset(getWidth(), rect.width());
            }

            setTranslationX(x);
            setTranslationY(y);
        }

        private int getAlignOffset(int myLength, int hisLength) {
            switch (align) {
                case END:    return hisLength - myLength;
                case CENTER: return (hisLength - myLength) / 2;
            }
            return 0;
        }

        private Path drawBubble(RectF myRect, float topLeftDiameter, float topRightDiameter, float bottomRightDiameter, float bottomLeftDiameter) {
            final Path path = new Path();

            if(viewRect == null)
                return path;

            topLeftDiameter = topLeftDiameter < 0 ? 0 : topLeftDiameter;
            topRightDiameter = topRightDiameter < 0 ? 0 : topRightDiameter;
            bottomLeftDiameter = bottomLeftDiameter < 0 ? 0 : bottomLeftDiameter;
            bottomRightDiameter = bottomRightDiameter < 0 ? 0 : bottomRightDiameter;

            final float spacingLeft = this.position == Position.RIGHT ? arrowHeight : 0;
            final float spacingTop = this.position == Position.BOTTOM ? arrowHeight : 0;
            final float spacingRight = this.position == Position.LEFT ? arrowHeight : 0;
            final float spacingBottom = this.position == Position.TOP ? arrowHeight : 0;

            final float left = spacingLeft + myRect.left;
            final float top = spacingTop + myRect.top;
            final float right = myRect.right - spacingRight;
            final float bottom = myRect.bottom - spacingBottom;
            final float centerX = viewRect.centerX() - getX();

            final float arrowSourceX = (Arrays.asList(Position.TOP, Position.BOTTOM).contains(this.position))
                    ? centerX + arrowSourceMargin
                    : centerX;
            final float arrowTargetX = (Arrays.asList(Position.TOP, Position.BOTTOM).contains(this.position))
                    ? centerX + arrowTargetMargin
                    : centerX;
            final float arrowSourceY = (Arrays.asList(Position.RIGHT, Position.LEFT).contains(this.position))
                    ? bottom / 2f - arrowSourceMargin
                    : bottom / 2f;
            final float arrowTargetY = (Arrays.asList(Position.RIGHT, Position.LEFT).contains(this.position))
                    ? bottom / 2f - arrowTargetMargin
                    : bottom / 2f;

            path.moveTo(left + topLeftDiameter / 2f, top);
            //LEFT, TOP

            if (position == Position.BOTTOM) {
                path.lineTo(arrowSourceX - arrowWidth, top);
                path.lineTo(arrowTargetX, myRect.top);
                path.lineTo(arrowSourceX + arrowWidth, top);
            }
            path.lineTo(right - topRightDiameter / 2f, top);

            path.quadTo(right, top, right, top + topRightDiameter / 2);
            //RIGHT, TOP

            if (position == Position.LEFT) {
                path.lineTo(right, arrowSourceY - arrowWidth);
                path.lineTo(myRect.right, arrowTargetY);
                path.lineTo(right, arrowSourceY + arrowWidth);
            }
            path.lineTo(right, bottom - bottomRightDiameter / 2);

            path.quadTo(right, bottom, right - bottomRightDiameter / 2, bottom);
            //RIGHT, BOTTOM

            if (position == Position.TOP) {
                path.lineTo(arrowSourceX + arrowWidth, bottom);
                path.lineTo(arrowTargetX, myRect.bottom);
                path.lineTo(arrowSourceX - arrowWidth, bottom);
            }
            path.lineTo(left + bottomLeftDiameter / 2, bottom);

            path.quadTo(left, bottom, left, bottom - bottomLeftDiameter / 2);
            //LEFT, BOTTOM

            if (position == Position.RIGHT) {
                path.lineTo(left, arrowSourceY + arrowWidth);
                path.lineTo(myRect.left, arrowTargetY);
                path.lineTo(left, arrowSourceY - arrowWidth);
            }
            path.lineTo(left, top + topLeftDiameter / 2);

            path.quadTo(left, top, left + topLeftDiameter / 2, top);

            path.close();

            return path;
        }

        public boolean adjustSize(Rect rect, int screenWidth) {

            final Rect r = new Rect();
            getGlobalVisibleRect(r);

            boolean changed = false;
            final ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (position == Position.LEFT && getWidth() > rect.left) {
                layoutParams.width = rect.left - MARGIN_SCREEN_BORDER_TOOLTIP - distanceWithView;
                changed = true;
            } else if (position == Position.RIGHT && rect.right + getWidth() > screenWidth) {
                layoutParams.width = screenWidth - rect.right - MARGIN_SCREEN_BORDER_TOOLTIP - distanceWithView;
                changed = true;
            } else if (position == Position.TOP || position == Position.BOTTOM) {
                int adjustedLeft = rect.left;
                int adjustedRight = rect.right;

                if((rect.centerX() + getWidth() / 2f) > screenWidth){
                    float diff = (rect.centerX() + getWidth() / 2f) - screenWidth;

                    adjustedLeft -=  diff;
                    adjustedRight -=  diff;

                    setAlign(ALIGN.CENTER);
                    changed = true;
                }else if((rect.centerX() - getWidth() / 2f) < 0){
                    float diff = -(rect.centerX() - getWidth() / 2f);

                    adjustedLeft +=  diff;
                    adjustedRight +=  diff;

                    setAlign(ALIGN.CENTER);
                    changed = true;
                }

                if(adjustedLeft < 0){
                    adjustedLeft = 0;
                }

                if(adjustedRight > screenWidth){
                    adjustedRight = screenWidth;
                }

                rect.left = adjustedLeft;
                rect.right = adjustedRight;
            }

            setLayoutParams(layoutParams);
            postInvalidate();
            return changed;
        }

        private void onSetup(Rect myRect) {
            setupPosition(myRect);

            bubblePath = drawBubble(new RectF(shadowPadding, shadowPadding, getWidth() - shadowPadding * 2f, getHeight() - shadowPadding * 2f), corner, corner, corner, corner);
            startEnterAnimation();

            handleAutoRemove();
        }

        public void setup(final Rect viewRect, int screenWidth) {
            this.viewRect = new Rect(viewRect);
            final Rect myRect = new Rect(viewRect);

            final boolean changed = adjustSize(myRect, screenWidth);
            if (!changed) {
                onSetup(myRect);
            } else {
                getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        onSetup(myRect);
                        getViewTreeObserver().removeOnPreDrawListener(this);
                        return false;
                    }
                });
            }
        }

        public void close() {
            remove();
        }

        public void removeNow() {
            if (getParent() != null) {
                final ViewGroup parent = ((ViewGroup) getParent().getParent());
                parent.removeView((ViewGroup) TooltipView.this.getParent());
            }
        }

        public void closeNow() {
            removeNow();
        }

        public void setWithShadow(boolean withShadow) {
            if(withShadow){
                bubblePaint.setShadowLayer(shadowWidth, 0, 0, shadowColor);
            } else {
                bubblePaint.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
            }
        }

        public void setDistanceWithView(int distanceWithView) {
            this.distanceWithView = distanceWithView;
        }

        public void setBorderPaint(Paint borderPaint) {
            this.borderPaint = borderPaint;
            postInvalidate();
        }
    }
}
