package com.github.florent37.viewtooltip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by florentchampigny on 02/06/2017.
 */

public class ViewTooltip {

    private final View view;
    private final ViewTooltip_view tooltip_view;

    private ViewTooltip(View view) {
        this.view = view;
        this.tooltip_view = new ViewTooltip_view(view.getContext());
    }

    public static ViewTooltip on(final View view) {
        return new ViewTooltip(view);
    }

    public ViewTooltip position(Position position) {
        this.tooltip_view.setPosition(position);
        return this;
    }

    public ViewTooltip align(ALIGN align) {
        this.tooltip_view.setAlign(align);
        return this;
    }

    public void show() {
        final ViewGroup decorView = (ViewGroup) ((Activity) view.getContext()).getWindow().getDecorView();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);

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

    public ViewTooltip duration(long duration) {
        this.tooltip_view.setDuration(duration);
        return this;
    }

    public ViewTooltip color(@ColorInt int color) {
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

    public ViewTooltip textColor(@ColorInt int textColor) {
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

    private static class ViewTooltip_view extends FrameLayout {

        private static final int MARGIN_SCREEN_BORDER_TOOLTIP = 30;
        private final int ARROW_HEIGHT = 15;
        private final int ARROW_WIDTH = 15;
        protected TextView textView;
        private int color = Color.parseColor("#3F51B5");
        private Path bubblePath;
        private Paint bubblePaint;
        private Position position = Position.BOTTOM;
        private ALIGN align = ALIGN.CENTER;
        private boolean clickToHide;
        private boolean autoHide = true;
        private long duration = 4000;

        @Nullable
        private ListenerDisplay listenerDisplay;

        @Nullable
        private ListenerHide listenerHide;

        private TooltipAnimation tooltipAnimation = new FadeTooltipAnimation();

        private int corner = 30;

        public ViewTooltip_view(@NonNull Context context) {
            super(context);
            setWillNotDraw(false);

            this.textView = new TextView(getContext());
            addView(textView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bubblePaint.setColor(color);
            bubblePaint.setStyle(Paint.Style.FILL);

            textView.setTextColor(Color.WHITE);

            int paddingHorizontal = 40 + ARROW_HEIGHT;
            int paddingVertical = 20 + ARROW_HEIGHT;
            setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        }

        public void setColor(int color) {
            this.color = color;
            postInvalidate();
        }

        public void setPosition(@NonNull Position position) {
            this.position = position;
            postInvalidate();
        }

        public void setAlign(ALIGN align) {
            this.align = align;
            postInvalidate();
        }

        public void setText(@NonNull String text) {
            this.textView.setText(text);
            postInvalidate();
        }

        public void setTextColor(@NonNull int textColor) {
            this.textView.setTextColor(textColor);
            postInvalidate();
        }

        public void setTextTypeFace(@NonNull Typeface textTypeFace) {
            this.textView.setTypeface(textTypeFace);
            postInvalidate();
        }

        public void setTextSize(int unit, float size) {
            this.textView.setTextSize(unit, size);
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

        public void setListenerDisplay(@Nullable ListenerDisplay listener) {
            this.listenerDisplay = listener;
        }

        public void setListenerHide(@Nullable ListenerHide listener) {
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
                        listenerDisplay.onDisplay(ViewTooltip_view.this);
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
                        listenerHide.onHide(ViewTooltip_view.this);
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

        private void remove() {
            startExitAnimation(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    final ViewGroup parent = ((ViewGroup) getParent());
                    parent.removeView(ViewTooltip_view.this);
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
                final int myWidth = getWidth();
                final int hisWidth = rect.width();

                final int maxWidth = Math.max(hisWidth, myWidth);
                final int minWidth = Math.min(hisWidth, myWidth);

                int spacingX;
                switch (align) {
                    //case END:
                    //    spacingX = maxWidth - minWidth;
                    //    break;
                    case CENTER:
                        spacingX = (int) (-1f * maxWidth / 2f + minWidth / 2f);
                        break;
                    default:
                        spacingX = 0;
                        break;
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


        private Path drawBubble(RectF rect, float topLeftDiameter, float topRightDiameter, float bottomRightDiameter, float bottomLeftDiameter) {
            final Path path = new Path();

            topLeftDiameter = topLeftDiameter < 0 ? 0 : topLeftDiameter;
            topRightDiameter = topRightDiameter < 0 ? 0 : topRightDiameter;
            bottomLeftDiameter = bottomLeftDiameter < 0 ? 0 : bottomLeftDiameter;
            bottomRightDiameter = bottomRightDiameter < 0 ? 0 : bottomRightDiameter;

            final float spacingLeft = this.position == Position.RIGHT ? ARROW_HEIGHT : 0;
            final float spacingTop = this.position == Position.BOTTOM ? ARROW_HEIGHT : 0;
            final float spacingRight = this.position == Position.LEFT ? ARROW_HEIGHT : 0;
            final float spacingBottom = this.position == Position.TOP ? ARROW_HEIGHT : 0;

            final float left = spacingLeft + rect.left;
            final float top = spacingTop + rect.top;
            final float right = rect.right - spacingRight;
            final float bottom = rect.bottom - spacingBottom;

            path.moveTo(left + topLeftDiameter / 2f, top);
            //LEFT, TOP

            if (position == Position.BOTTOM) {
                path.lineTo((right - topRightDiameter / 2f) / 2f - ARROW_WIDTH, top);
                path.lineTo((right - topRightDiameter / 2f) / 2f, rect.top);
                path.lineTo((right - topRightDiameter / 2f) / 2f + ARROW_WIDTH, top);
            }
            path.lineTo(right - topRightDiameter / 2f, top);

            path.quadTo(right, top, right, top + topRightDiameter / 2);
            //RIGHT, TOP

            if (position == Position.LEFT) {
                path.lineTo(right, bottom / 2f - ARROW_WIDTH);
                path.lineTo(rect.right, bottom / 2f);
                path.lineTo(right, bottom / 2f + ARROW_WIDTH);
            }
            path.lineTo(right, bottom - bottomRightDiameter / 2);

            path.quadTo(right, bottom, right - bottomRightDiameter / 2, bottom);
            //RIGHT, BOTTOM

            if (position == Position.TOP) {
                path.lineTo(right / 2f + ARROW_WIDTH, bottom);
                path.lineTo(right / 2f, rect.bottom);
                path.lineTo(right / 2f - ARROW_WIDTH, bottom);
            }
            path.lineTo(left + bottomLeftDiameter / 2, bottom);

            path.quadTo(left, bottom, left, bottom - bottomLeftDiameter / 2);
            //LEFT, BOTTOM

            if (position == Position.RIGHT) {
                path.lineTo(left, bottom / 2f + ARROW_WIDTH);
                path.lineTo(rect.left, bottom / 2f);
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
            if(position == Position.LEFT && getWidth() > rect.left) {
                layoutParams.width = rect.left - MARGIN_SCREEN_BORDER_TOOLTIP;
                changed = true;
            } else if(position == Position.RIGHT && rect.right + getWidth() > screenWidth){
                layoutParams.width = screenWidth - rect.right - MARGIN_SCREEN_BORDER_TOOLTIP;
                changed = true;
            }
            setLayoutParams(layoutParams);
            postInvalidate();
            return changed;
        }

        private void onSetup(Rect rect){
            setupPosition(rect);

            startEnterAnimation();

            handleAutoRemove();
        }

        public void setup(final Rect rect, int screenWidth) {
            final boolean changed = adjustSize(rect, screenWidth);
            if(!changed) {
                onSetup(rect);
            } else {
                getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        onSetup(rect);
                        getViewTreeObserver().removeOnPreDrawListener(this);
                        return false;
                    }
                });
            }
        }
    }
}
