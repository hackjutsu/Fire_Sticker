package com.gogocosmo.cosmoqiu.memoryaccelerator.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.memoryaccelerator.R;


public class CardFragment extends Fragment {

    private final String TAG = "MEMORY-ACC";
    private TextView _card;
    private int _color;
    private String _text;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card,
                container, false);

        _card = (TextView) view.findViewById(R.id.card);
        _card.setBackgroundColor(_color);
        _card.setText(_text);


//        Random r = new Random();
//        int red = r.nextInt(255 - 0 + 1) + 0;
//        int green = r.nextInt(255 - 0 + 1) + 0;
//        int blue = r.nextInt(255 - 0 + 1) + 0;
//
//        _color = Color.rgb(red, green, blue);
//
//        Log.d(TAG,
//                String.valueOf(red) + " ," +
//                        String.valueOf(green) + " ," +
//                        String.valueOf(blue)
//        );

        return view;
    }

    public void swipeDownEvent(String text) {

//        Integer colorFrom = Color.rgb(43, 94, 125);
//        Integer colorTo = _color;
//        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
//        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animator) {
//                _card.setBackgroundColor((Integer)animator.getAnimatedValue());
//            }
//
//        });
//        colorAnimation.start();
//        _card.setBackgroundColor(_color);
        _card.setText(text);
    }

    public void swipeUpEvent(String text) {

//        Integer colorFrom = _color;
//        Integer colorTo = Targetcolor;
//        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
//        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animator) {
//                _card.setBackgroundColor((Integer)animator.getAnimatedValue());
//            }
//
//        });
//        colorAnimation.start();

        _card.setText(text);
    }

    public void setCardColor(int color) {
        _color = color;
    }

    public void setCardText(String text) {
        _text = text;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
//        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
//        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }


    @Override
    public void onPause() {
//        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
//        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

    @Override
    public void onDetach() {
//        Log.i(TAG, getClass().getSimpleName() + ":entered onDetach()");
        super.onDetach();
    }


    @Override
    public void onDestroy() {
//        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
//        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroyView()");
        super.onDestroyView();
    }


}
