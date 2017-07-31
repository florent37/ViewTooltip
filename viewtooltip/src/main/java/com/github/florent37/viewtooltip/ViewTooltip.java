package com.github.florent37.viewtooltip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by florentchampigny on 02/06/2017.
 */

public class ViewTooltip {

    private final View view;
    private final TooltipView tooltip_view;

    private ViewTooltip(View view) {
        this.view = view;
        this.tooltip_view = new TooltipView(getActivityContext(view.getContext()));
    }

    public static ViewTooltip on(final View view) {
        return new ViewTooltip(view);
    }

    public Activity getActivityContext(Context context) {
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

    public ViewTooltip customView(View customView) {
        this.tooltip_view.setCustomView(customView);
        return this;
    }

    public ViewTooltip customView(int viewId) {
        this.tooltip_view.setCustomView(((Activity) view.getContext()).findViewById(viewId));
        return this;
    }

    public ViewTooltip align(ALIGN align) {
        this.tooltip_view.setAlign(align);
        return this;
    }

    public TooltipView show() {
        final Context activityContext = tooltip_view.getContext();
        if (activityContext != null && activityContext instanceof Activity) {
            final ViewGroup decorView = (ViewGroup) ((Activity) activityContext).getWindow().getDecorView();
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Rect rect = new Rect();
                    view.getGlobalVisibleRect(rect);

                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    rect.left = location[0];
                    //rect.left = location[0];

                    decorView.addView(tooltip_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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

    public ViewTooltip clickToHide(boolean clickToHide) {
        this.tooltip_view.setClickToHide(clickToHide);
        return this;
    }

    public ViewTooltip autoHide(boolean autoHide, long duration) {
        this.tooltip_view.setAutoHide(autoHide);
        this.tooltip_view.setDuration(duration);
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

    public static class TooltipView extends FrameLayout {

        private static final int MARGIN_SCREEN_BORDER_TOOLTIP = 30;
        private final int ARROW_HEIGHT = 15;
        private final int ARROW_WIDTH = 15;
        protected View childView;
        private int color = Color.parseColor("#1F7C82");
        private Path bubblePath;
        private Paint bubblePaint;
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

        private Rect viewRect;

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

        public void setPosition(Position position) {
            this.position = position;
            switch (position){
                case TOP:
                    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + ARROW_HEIGHT);
                    break;
                case BOTTOM:
                    setPadding(paddingLeft, paddingTop + ARROW_HEIGHT, paddingRight, paddingBottom);
                    break;
                case LEFT:
                    setPadding(paddingLeft, paddingTop, paddingRight + ARROW_HEIGHT, paddingBottom);
                    break;
                case RIGHT:
                    setPadding(paddingLeft + ARROW_HEIGHT, paddingTop, paddingRight, paddingBottom);
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

        public void setTextColor(int textColor) {
            if (childView instanceof TextView) {
                ((TextView) this.childView).setTextColor(textColor);
            }
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

        public void setClickToHide(boolean clickToHide) {
            this.clickToHide = clickToHide;
        }

        public void setCorner(int corner) {
            this.corner = corner;
        }

        @Override
        protected void onSizeChanged(int width, int height, int oldw, int oldh) {
            super.onSizeChanged(width, height, oldw, oldh);

            bubblePath = drawBubble(new RectF(0, 0, width, height), corner, corner, corner, corner);

            /*
            float x = 0;
            float y = 0;

            switch (position) {
                case TOP: {
                    bubblePath.moveTo(x, y);
                    bubblePath.lineTo((x = width), y);
                    bubblePath.lineTo(x, (y = height - ARROW_HEIGHT));
                    bubblePath.lineTo((x = width / 2f + ARROW_WIDTH), y);
                    bubblePath.lineTo((x = width / 2f), (y = height));
                    bubblePath.lineTo((x = width / 2f - ARROW_WIDTH), (y = height - ARROW_HEIGHT));
                    bubblePath.lineTo((x = 0), y);
                    bubblePath.lineTo((x = 0), y);
                    bubblePath.close();
                }
                break;
                case BOTTOM: {
                    bubblePath.moveTo(x, (y = ARROW_HEIGHT));
                    bubblePath.lineTo((x = width / 2f - ARROW_WIDTH), y);
                    bubblePath.lineTo((x = width / 2f), (y = 0));
                    bubblePath.lineTo((x = width / 2f + ARROW_WIDTH), (y = ARROW_HEIGHT));
                    bubblePath.lineTo((x = width), y);
                    bubblePath.lineTo(x, (y = height));
                    bubblePath.lineTo((x = 0), y);
                    bubblePath.close();
                }
                break;
                case LEFT: {
                    bubblePath.moveTo(x, y);
                    bubblePath.lineTo((x = width - ARROW_HEIGHT), y);
                    bubblePath.lineTo(x, (y = height / 2 - ARROW_WIDTH));
                    bubblePath.lineTo((x = width - ARROW_HEIGHT), (y = height / 2 - ARROW_WIDTH));
                    bubblePath.lineTo((x = width), (y = height / 2));
                    bubblePath.lineTo((x = width - ARROW_HEIGHT), (y = height / 2 + ARROW_WIDTH));
                    bubblePath.lineTo(x, (y = height));
                    bubblePath.lineTo((x = 0), y);
                    bubblePath.close();
                }
                break;
                case RIGHT: {
                    bubblePath.moveTo((x = ARROW_HEIGHT), y);
                    bubblePath.lineTo((x = width), y);
                    bubblePath.lineTo(x, (y = height));
                    bubblePath.lineTo((x = ARROW_HEIGHT), y);
                    bubblePath.lineTo(x, (y = height / 2 + ARROW_WIDTH));
                    bubblePath.lineTo((x = 0), (y = height / 2));
                    bubblePath.lineTo((x = ARROW_HEIGHT), (y = height / 2 - ARROW_WIDTH));
                    bubblePath.close();
                }
                break;
            }*/
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (bubblePath != null) {
                canvas.drawPath(bubblePath, bubblePaint);
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
            tooltipAnimation.animateEnter(this, new AnimatorListenerAdapter() {
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
            tooltipAnimation.animateExit(this, new AnimatorListenerAdapter() {
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
            if (clickToHide) {
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickToHide) {
                            remove();
                        }
                    }
                });
            }

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
                    if (getParent() != null) {
                        final ViewGroup parent = ((ViewGroup) getParent());
                        parent.removeView(TooltipView.this);
                    }
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

            if (position == Position.LEFT || position == Position.RIGHT) {

                final int myHeight = getHeight();
                final int hisHeight = rect.height();

                final int maxHeight = Math.max(hisHeight, myHeight);
                final int minHeight = Math.min(hisHeight, myHeight);

                int spacingY = 0;
                switch (align) {
                    case START:
                        spacingY = 0;
                        break;
                    //case END:
                    //    spacingY = maxHeight - minHeight;
                    //    break;
                    case CENTER:
                        spacingY = (int) (-1f * maxHeight / 2f + minHeight / 2f);
                        break;
                }

                if (position == Position.LEFT) {
                    setTranslationY(rect.top + spacingY);
                    setTranslationX(rect.left - getWidth());
                } else {
                    setTranslationY(rect.top + spacingY);
                    setTranslationX(rect.right);
                }

            } else {
                int spacingX = 0;

                final int myWidth = getWidth();
                final int hisWidth = rect.width();

                if (align == ALIGN.CENTER) {
                    spacingX = (int) (hisWidth / 2f - 1f * myWidth / 2f);
                }

                if (position == Position.BOTTOM) {
                    setTranslationY(rect.bottom);
                    setTranslationX(rect.left + spacingX);
                } else {
                    setTranslationY(rect.top - getHeight());
                    setTranslationX(rect.left + spacingX);
                }
            }
        }


        private Path drawBubble(RectF myRect, float topLeftDiameter, float topRightDiameter, float bottomRightDiameter, float bottomLeftDiameter) {
            final Path path = new Path();

            if(viewRect == null)
                return path;

            topLeftDiameter = topLeftDiameter < 0 ? 0 : topLeftDiameter;
            topRightDiameter = topRightDiameter < 0 ? 0 : topRightDiameter;
            bottomLeftDiameter = bottomLeftDiameter < 0 ? 0 : bottomLeftDiameter;
            bottomRightDiameter = bottomRightDiameter < 0 ? 0 : bottomRightDiameter;

            final float spacingLeft = this.position == Position.RIGHT ? ARROW_HEIGHT : 0;
            final float spacingTop = this.position == Position.BOTTOM ? ARROW_HEIGHT : 0;
            final float spacingRight = this.position == Position.LEFT ? ARROW_HEIGHT : 0;
            final float spacingBottom = this.position == Position.TOP ? ARROW_HEIGHT : 0;

            final float left = spacingLeft + myRect.left;
            final float top = spacingTop + myRect.top;
            final float right = myRect.right - spacingRight;
            final float bottom = myRect.bottom - spacingBottom;
            final float centerX = viewRect.centerX() - getX();

            path.moveTo(left + topLeftDiameter / 2f, top);
            //LEFT, TOP

            if (position == Position.BOTTOM) {
                path.lineTo(centerX - ARROW_WIDTH, top);
                path.lineTo(centerX, myRect.top);
                path.lineTo(centerX + ARROW_WIDTH, top);
            }
            path.lineTo(right - topRightDiameter / 2f, top);

            path.quadTo(right, top, right, top + topRightDiameter / 2);
            //RIGHT, TOP

            if (position == Position.LEFT) {
                path.lineTo(right, bottom / 2f - ARROW_WIDTH);
                path.lineTo(myRect.right, bottom / 2f);
                path.lineTo(right, bottom / 2f + ARROW_WIDTH);
            }
            path.lineTo(right, bottom - bottomRightDiameter / 2);

            path.quadTo(right, bottom, right - bottomRightDiameter / 2, bottom);
            //RIGHT, BOTTOM

            if (position == Position.TOP) {
                path.lineTo(centerX + ARROW_WIDTH, bottom);
                path.lineTo(centerX, myRect.bottom);
                path.lineTo(centerX - ARROW_WIDTH, bottom);
            }
            path.lineTo(left + bottomLeftDiameter / 2, bottom);

            path.quadTo(left, bottom, left, bottom - bottomLeftDiameter / 2);
            //LEFT, BOTTOM

            if (position == Position.RIGHT) {
                path.lineTo(left, bottom / 2f + ARROW_WIDTH);
                path.lineTo(myRect.left, bottom / 2f);
                path.lineTo(left, bottom / 2f - ARROW_WIDTH);
            }
            path.lineTo(left, top + topLeftDiameter / 2);

            path.quadTo(left, top, left + topLeftDiameter / 2, top);

            path.close();

            return path;
        }

        public boolean adjustSize(Rect rect, int screenWidth) {
            boolean changed = false;
            final ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (position == Position.LEFT && getWidth() > rect.left) {
                layoutParams.width = rect.left - MARGIN_SCREEN_BORDER_TOOLTIP;
                changed = true;
            } else if (position == Position.RIGHT && rect.right + getWidth() > screenWidth) {
                layoutParams.width = screenWidth - rect.right - MARGIN_SCREEN_BORDER_TOOLTIP;
                changed = true;
            } else if (position == Position.TOP || position == Position.BOTTOM) {
                float widthRight = (getWidth() - rect.width()) / 2f;
                if(rect.right + widthRight > screenWidth) {
                    final float movinX = (rect.right + widthRight) - screenWidth + 30;
                    rect.left -= movinX;
                    rect.right -= movinX;
                    changed = true;
                }
                else if(rect.left - widthRight < 0) {
                    final float movinX = 0 - (rect.left - widthRight) + 30;
                    rect.left += movinX;
                    rect.right += movinX;
                    changed = true;
                }
            }
            setLayoutParams(layoutParams);
            postInvalidate();
            return changed;
        }

        private void onSetup(Rect myRect) {
            setupPosition(myRect);

            bubblePath = drawBubble(new RectF(0, 0, getWidth(), getHeight()), corner, corner, corner, corner);

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
    }
}
