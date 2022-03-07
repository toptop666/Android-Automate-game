package com.example.firstgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.firstgame.RegularExpression;
import com.example.firstgame.Word;


import java.util.*;

public class Automate<T> {

    private boolean receiver;
    private Hashtable<T, Automate> transitions;

    public Automate(boolean receiver) {
        this.receiver = receiver;
        this.transitions = new Hashtable<T, Automate>();
    }

    public Automate(RegularExpression<T> expression) {
        Automate<RegularExpression<T>> workSpace = new Automate<RegularExpression<T>>(false);
        if(expression.getType() == Types.KleeneClosure) {
            workSpace.receiver = true;
            workSpace.addLoopToState(expression.getProtasis());
            construct(expression.getProtasis(), workSpace);
        }
        else {
            workSpace.createState(expression, true);
            workSpace = construct(expression, workSpace);
        }
        this.receiver = receiver;
        this.transitions = new Hashtable<T, Automate>();
        this.RegularExpressionDFAtoDFA(workSpace);
    }

    private Automate construct(RegularExpression<T> expression, Automate<RegularExpression<T>> automate) {
        if (!automate.transitions.containsKey(expression)) {
            return null;
        }
        if (expression.getType() == Types.Elementary) {
            return automate;
        }
        boolean undone = true;
        if (expression.getType() == Types.Union && undone) {
            automate.addInputToState(automate.getState(expression), expression.getProtasis());
            automate.transitions.remove(expression);
            automate.addInputToState(automate.getState(expression.getProtasis()), expression.getApodosis());
            automate = construct(expression.getProtasis(), automate);
            automate = construct(expression.getApodosis(), automate);
            undone = false;
        }
        if (expression.getType() == Types.Chaining && undone) {
            Automate newState = new Automate(false);
            automate.addInputToState(newState, expression.getProtasis());
            newState.addInputToState(automate.getState(expression), expression.getApodosis());
            automate.transitions.remove(expression);
            automate.transitions.put(expression.getProtasis(), construct(expression.getApodosis(), automate.getState(expression.getProtasis())));
            automate = construct(expression.getProtasis(), automate);
            undone = false;
        }
        if(expression.getType() == Types.KleeneClosure && undone) {
            automate.getState(expression).transitions.putAll(automate.transitions);
            automate.receiver = automate.getState(expression).receiver;
            automate = automate.getState(expression);
            automate.transitions.remove(expression);
            automate.addLoopToState(expression.getProtasis());
            automate = construct(expression.getProtasis(), automate);
            undone = false;
        }
        return automate;
    }

    private void RegularExpressionDFAtoDFA(Automate<RegularExpression<T>> regAutomate) {
        Hashtable visited = new Hashtable<Automate, Boolean>();
        Hashtable added = new Hashtable<Automate, Boolean>();
        Queue<Automate> qReg = new LinkedList<>();
        Queue<Automate> qAutomate = new LinkedList<>();
        qReg.add(regAutomate);
        qAutomate.add(this);
        visited.put(regAutomate, true);
        added.put(this, true);
        this.receiver = regAutomate.receiver;
        while (!qReg.isEmpty()) {
            Automate current = qReg.remove();
            Automate pointer = qAutomate.remove();
            Enumeration<RegularExpression> expressionEnumeration = current.transitions.keys();
            while (expressionEnumeration.hasMoreElements()) {
                RegularExpression transition = expressionEnumeration.nextElement();
                if(current.getState(transition) == current) {
                    pointer.addLoopToState(transition.getElement());
                    pointer.receiver = current.receiver;
                }
                else {
                    if(!visited.containsKey(current.getState(transition))) {
                        visited.put(current.getState(transition), true);
                        pointer.createState(transition.getElement(), current.getState(transition).receiver);
                        qReg.add(current.getState(transition));
                        qAutomate.add(pointer.getState(transition.getElement()));
                    }
                    else {
                        pointer.addInputToState(qAutomate.peek(), transition.getElement());
                    }
                }
            }
        }
    }

    public Hashtable<T, Automate> getTransitions() {
        return this.transitions;
    }
    public boolean isReceiver() {
        return receiver;
    }
    public void setReceiver(boolean receiver) {
        this.receiver = receiver;
    }
    public Automate getState(T transition) {
        return this.transitions.get(transition);
    }
    public Automate createState(T input, boolean receiver) {
        if(this.transitions.contains(input)) {
            return null;
        }
        Automate automate = new Automate(receiver);
        this.transitions.put(input, automate);
        return automate;
    }
    public Automate addInputToState(Automate state, T extraInput) {
        if(this.transitions.containsKey(extraInput)) {
            return null;
        }
        this.transitions.put(extraInput, state);
        return this.transitions.get(extraInput);
    }
    public Automate addLoopToState(T input) {
        if(this.transitions.contains(input)) {
            return null;
        }
        this.transitions.put(input, this);
        return this;
    }

    private void drawState(Paint paint, Canvas canvas, int x, int y, int radius) {
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, radius, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, radius-15, paint);
        if(this.isReceiver()) {
            paint.setColor(Color.BLACK);
            canvas.drawCircle(x, y, radius-30, paint);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(x, y, radius-45, paint);
        }
    }

    public void fillArrow(Paint paint, Canvas canvas, Point origin, Point target, Bitmap arrowHead) {
        int width = 5;
        for(int i = 0; i<width; i++) {
            paint.setColor(Color.BLACK);
            canvas.drawLine(origin.x, origin.y, target.x+i-30, target.y-30, paint);
            paint.setColor(Color.RED);
            canvas.drawLine(target.x+i-30, target.y-30, target.x+i, target.y, paint);
        }
    }

    public void fillArrowSelfReference(Paint paint, Canvas canvas, Point center) {
        int radius = 60;
        int x = center.x;
        int y = center.y-130;
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, radius, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, radius-5, paint);

    }

    public void drawAutomate(Paint paint, Canvas canvas, Bitmap arrowHead) {
        int radius = 80;
        int x = 200;
        int y = 200;

        Hashtable drawn = new Hashtable<Automate, Boolean>();
        Queue<Automate> q = new LinkedList<>();
        Queue<Point> qLoc = new LinkedList<>();
        q.add(this);
        qLoc.add(new Point(x,y));
        drawn.put(this, true);
        this.drawState(paint, canvas, x, y, radius);
        y+=200;

        while (!q.isEmpty()) {
            Automate current = q.remove();
            Point originalState = qLoc.remove();
            Enumeration<Integer> enumeration = current.getTransitions().keys();
            while (enumeration.hasMoreElements()) {
                int transition = enumeration.nextElement();
                if(current.getState(transition) == current) {
                    fillArrowSelfReference(paint, canvas, originalState);
                }
                else {
                    if (!drawn.containsKey(current.getState(transition))) {
                        drawn.put(current.getState(transition), true);
                        current.getState(transition).drawState(paint, canvas, x, y, radius);
                        Point stateLoc = new Point(x,y);
                        current.fillArrow(paint, canvas, originalState, stateLoc, arrowHead);
                        q.add(current.getState(transition));
                        qLoc.add(stateLoc);
                        x += 200;
                    }
                    else {
                        Point stateLoc = qLoc.remove();
                        current.fillArrow(paint, canvas, originalState, stateLoc, arrowHead);
                    }
                }
            }
            x=200;
            y += 200;
        }
    }
}

