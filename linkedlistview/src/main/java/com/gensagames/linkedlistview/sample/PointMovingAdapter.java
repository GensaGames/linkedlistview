package com.gensagames.linkedlistview.sample;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gensagames.linkedlistview.LinkedListView;
import com.gensagames.linkedlistview.anim.PointMovingController;
import com.gensagames.linkedlistview.sample.utils.BaseDrawable;
import com.gensagames.linkedlistview.sample.utils.DefaultSize;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;


/**
 * Created by Genka on 13.04.2016.
 * GensaGames
 */

/**
 * --------------------------------------------
 * Sample using Adapter for views list,
 * but with link for all views!
 * -----------------------------------------
 */

public class PointMovingAdapter extends LinkedListView.Adapter {

    private Context mainContext;
    private LinkedList<View> mainViewList;

    public PointMovingAdapter(Context mainContext) {
        this.mainContext = mainContext;
        mainViewList = new LinkedList<>();
    }

    public void addSimpleView() {
        ViewGroup mainView = BaseDrawable.getPointView(mainContext);
        mainViewList.add(mainView);
        notifyDataSetChanged();
    }

    public void deleteView(int index) {
        mainViewList.remove(index);
        notifyDataSetChanged();
    }

    public void addSimpleView(int index) {
        ViewGroup mainView = BaseDrawable.getPointView(mainContext);
        mainViewList.add(index, mainView);
        notifyDataSetChanged();
    }


    @Override
    public View getObjectView(int position) {
        return mainViewList.get(position);
    }

    @Override
    public int getObjectCount() {
        return mainViewList.size();
    }

    /**
     * --------------------------------------------
     * Testing another element - AnimationController,
     * that extend from more specific controller
     * -----------------------------------------
     */


    public static class AnimationController extends PointMovingController {

        Map<View, AnimatorSet> animatorMovingInMap = new HashMap<>();
        Map<View, AnimatorSet> animatorMovingOutMap = new HashMap<>();
        private  Random random = new Random();

        public  int getRandomInt(int min, int max){
            return random.nextInt(max - min + 1) + min;
        }
        @Override
        public void onScrollAction() {
            super.onScrollAction();
        }

        @Override
        public void animateMovingIn(final ViewGroup mainView) {

            if (animatorMovingInMap.get(mainView) != null
                    && animatorMovingInMap.get(mainView).isStarted()) {
                return;
            }

            final float scale = getMainViewHolder().getContext().getResources().getDisplayMetrics().density;
            int newPositionY = (int) ((getRandomInt(DefaultSize.POINT_ANIM_NIM, DefaultSize.POINT_ANIM_MAX)) * scale + 0.5f);
            AnimatorSet animatorSet = new AnimatorSet();
            PropertyValuesHolder movingUp = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,
                    getFocusView(mainView).getTranslationY(), newPositionY);
            ObjectAnimator animatorView = ObjectAnimator.ofPropertyValuesHolder(getFocusView(mainView), movingUp);
            animatorView.setDuration(DefaultSize.POINT_ANIM_TIME);
            animatorSet.play(animatorView);
            animatorSet.start();

            animatorMovingInMap.put(mainView, animatorSet);
        }

        @Override
        public void animateMovingOut(ViewGroup mainView) {
            if (animatorMovingOutMap.get(mainView) != null
                    && animatorMovingOutMap.get(mainView).isStarted()) {
                return;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            PropertyValuesHolder movingUp = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,
                    getFocusView(mainView).getTranslationY(), 0);

            ObjectAnimator animatorView = ObjectAnimator.ofPropertyValuesHolder(getFocusView(mainView), movingUp);
            animatorView.setDuration(DefaultSize.POINT_ANIM_TIME);
            animatorSet.play(animatorView);
            animatorSet.start();
            animatorMovingOutMap.put(mainView, animatorSet);
        }

        @Override
        public View getFocusView(ViewGroup parentView) {
            for (int i = 0; i < parentView.getChildCount(); i++) {
                if (parentView.getChildAt(i) instanceof ImageView) {
                    return parentView.getChildAt(i);
                }
            }
            return parentView;
        }
    }

}