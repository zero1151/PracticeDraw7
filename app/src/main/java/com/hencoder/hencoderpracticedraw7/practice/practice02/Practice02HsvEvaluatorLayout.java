package com.hencoder.hencoderpracticedraw7.practice.practice02;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hencoder.hencoderpracticedraw7.R;

public class Practice02HsvEvaluatorLayout extends RelativeLayout {
    Practice02HsvEvaluatorView view;
    Button animateBt;

    public Practice02HsvEvaluatorLayout(Context context) {
        super(context);
    }

    public Practice02HsvEvaluatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice02HsvEvaluatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        view = (Practice02HsvEvaluatorView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xffff0000, 0xff00ff00);
                animator.setEvaluator(new HsvEvaluator()); // 使用自定义的 HsvEvaluator
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(2000);
                animator.start();
            }
        });
    }

    private static class HsvEvaluator implements TypeEvaluator<Integer> {

        private float[] hsvStartValue = new float[3];
        private float[] hsvEndValue = new float[3];
        private float[] hsvCurValue = new float[3];
        // 重写 evaluate() 方法，让颜色按照 HSV 来变化
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            Color.colorToHSV(startValue, hsvStartValue);
            Color.colorToHSV(endValue, hsvEndValue);
            //start[0]、end[0]和cur[0]的范围都是0到360
            //下面这步的作用是找小的那半周？
            if(hsvEndValue[0] - hsvStartValue[0] > 180){
                hsvEndValue[0] -= 360;
            }else if(hsvEndValue[0] - hsvStartValue[0] < -180){
                hsvEndValue[0] += 360;
            }

            hsvCurValue[0] = hsvStartValue[0] + (hsvEndValue[0] - hsvStartValue[0]) * fraction;
            if(hsvCurValue[0] > 360){
                hsvCurValue[0] -= 360;
            }else if(hsvCurValue[0] < 0){
                hsvCurValue[0] += 360;
            }
            hsvCurValue[1] = hsvStartValue[1] + (hsvEndValue[1] - hsvStartValue[1]) * fraction;
            hsvCurValue[2] = hsvStartValue[2] + (hsvEndValue[2] - hsvStartValue[2]) * fraction;
            int startAlpha = startValue >> 24 ;
            int endAlpha = endValue >> 24;
            int curAlpha = (int) (startAlpha + ((endAlpha - startAlpha)  * fraction));
            return Color.HSVToColor(curAlpha,hsvCurValue);
        }
    }
}
