package com.gensagames.sample.adapter.sample;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gensagames.linkedlistview.LinkedListView;
import com.gensagames.linkedlistview.anim.PointMovingController;
import com.gensagames.sample.adapter.helper.SampleLinkedAdapter;
import com.gensagames.sample.util.BaseDrawable;

import java.util.LinkedList;

/**
 * Created by Genka on 10.05.2016.
 * GensaGames
 */
public class SimplePointAdapter extends SampleLinkedAdapter {

    private Context mainContext;
    private LinkedList<View> mainViewList;

    public SimplePointAdapter(Context mainContext) {
        this.mainContext = mainContext;
        mainViewList = new LinkedList<>();
    }

    @Override
    public void addSimpleView() {
        ViewGroup mainView = BaseDrawable.getNumericPoint(mainContext);
        mainViewList.add(mainView);
        notifyDataSetChanged();
    }

    @Override
    public void deleteView(int index) {
        mainViewList.remove(index);
        notifyDataSetChanged();
    }


    @Override
    public LinkedListView.ViewHolder getViewHolder(int position, ViewGroup parentView) {
        if (position < mainViewList.size())
            return new LinkedListView.ViewHolder(mainViewList.get(position));

        return null;
    }

    @Override
    public int getObjectCount() {
        return mainViewList.size();
    }

    @Override
    public void bindView(LinkedListView.ViewHolder v, int position) {

    }

    /**
     * --------------------------------------------
     * Testing another element - AnimationController,
     * that extend from more specific controller
     * -----------------------------------------
     */


    public static class AnimationController extends PointMovingController {

        @Override
        public void updateViewsTranslatedX(ViewGroup mainView, int newTranslationX) {
            super.updateViewsTranslatedX(mainView, newTranslationX);

            ViewGroup viewGroup = getMainViewHolder();
            if (viewGroup == null) {
                return;
            }
            if (getFocusViewText(mainView).getText().toString().equals("")) {
                getFocusViewText(mainView).setText(String.valueOf(viewGroup
                        .indexOfChild(mainView) + 10));
            }
        }

        @Override
        public void animateMovingIn(final ViewGroup mainView) {

        }

        @Override
        public void animateMovingOut(ViewGroup mainView) {

        }

        @Override
        public void onScrollAction() {
            super.onScrollAction();
            updateClipOutside();
        }

        private void updateClipOutside() {
            ViewGroup mainHolder = getMainViewHolder();
            if (mainHolder == null) {
                return;
            }

            int firstVisible = getFirstVisiblePosition();
            int lastVisible = getLastVisiblePosition();
            for (int i = firstVisible; i <= lastVisible; i++) {
                ViewGroup viewGroup = (ViewGroup) mainHolder.getChildAt(i);
                if (viewGroup == null) {
                    continue;
                }
                viewGroup.setVisibility(View.VISIBLE);
            }
            ViewGroup viewGroupPre = (ViewGroup) mainHolder.getChildAt(firstVisible - 1);
            if (viewGroupPre != null) {
                viewGroupPre.setVisibility(View.INVISIBLE);
            }
            ViewGroup viewGroupPost = (ViewGroup) mainHolder.getChildAt(lastVisible + 1);
            if (viewGroupPost != null) {
                viewGroupPost.setVisibility(View.INVISIBLE);
            }
        }

        public TextView getFocusViewText(ViewGroup parentView) {
            for (int i = 0; i < parentView.getChildCount(); i++) {
                if (parentView.getChildAt(i) instanceof TextView) {
                    return (TextView) parentView.getChildAt(i);
                }
            }
            return null;
        }

        @Override
        public void onScrollStop() {

        }

        @Override
        public void onScrollStart() {

        }
    }

}
