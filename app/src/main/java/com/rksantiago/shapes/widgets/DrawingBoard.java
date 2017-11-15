package com.rksantiago.shapes.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;
import java.util.Stack;

/**
 *  Created by rk-santiago on 11/15/17.
 *
 *  Copyright 2017 Ralph Kristofelle A. Santiago
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 **/

public class DrawingBoard extends View {

    private static final int ALPHA_BEFORE = 0x66;

    private static final int ALPHA_AFTER = 0xAA;

    private static final int MAX_COLORS = 0xFFFFFF;

    private Stack<ShapeDrawable> mShapes;

    private ShapeDrawable mCurrentShape;

    private int mDownX;

    private int mDownY;

    private Random mRandom;

    public DrawingBoard(Context context) {
        super(context);
        init();
    }

    public DrawingBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mShapes = new Stack<>();
        mRandom = new Random();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (ShapeDrawable shape : mShapes) {
            shape.draw(canvas);
        }

        if (null != mCurrentShape) {
            mCurrentShape.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();

                mCurrentShape = createRandomShape(mDownX, mDownY, ALPHA_BEFORE);
                break;

            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getX();
                int currentY = (int) event.getY();
                mCurrentShape.setBounds(Math.min(mDownX, currentX), Math.min(mDownY, currentY),
                        Math.max(mDownX, currentX), Math.max(mDownY, currentY));
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mCurrentShape.getPaint().setAlpha(ALPHA_AFTER);
                mShapes.push(mCurrentShape);
                mCurrentShape = null;
                invalidate();
                break;

            default:
                return super.onTouchEvent(event);
        }

        return true;
    }

    private ShapeDrawable createRandomShape(int x, int y, int alpha) {
        int randomColor = mRandom.nextInt(MAX_COLORS) + 1;
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.setBounds(x, y, x, y);
        shape.getPaint().setColor(randomColor);
        shape.getPaint().setAlpha(alpha);
        return shape;
    }

    // TODO : Create getters and setters to control properties of a shape (color, alpha, shape to draw, etc)

}
