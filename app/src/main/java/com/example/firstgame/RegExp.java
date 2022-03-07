package com.example.firstgame;

import static com.example.firstgame.RegularPreProcess.*;

import java.util.Stack;

public class RegExp {

    private String regularExpression;

    public RegExp(String regularExpression) {
        regularExpression = preProcess(regularExpression);
        if(this.valid()) {
            this.regularExpression = regularExpression;
        }
        this.regularExpression = null;
    }

    public String getRegularExpression() {
        return regularExpression;
    }

    public void joinRegularExpression(RegExp other, char operator) {
        if(operator != '@' && operator != '+') {
            return;
        }
        this.regularExpression = "(" + this.regularExpression + operator + other + ")";
    }

    public int mainOperatorIndex() {
        if(this.regularExpression.length() == 5 || this.regularExpression.length() == 4) {
            return 2;
        }
        boolean flag = false;
        int bracketsCounter = 0;
        for(int i = 0; i<this.regularExpression.length()-1; i++) {
            if(this.regularExpression.charAt(i) == '(') {
                bracketsCounter++;
            }
            if(this.regularExpression.charAt(i) == ')') {
                bracketsCounter--;
                flag = true;
            }
            if(bracketsCounter == 1 && flag) {
                return i+1;
            }
        }
        return -1;
    }

    public boolean valid() {
        if(this.regularExpression.length() == 1) {
            return Character.isDigit(this.regularExpression.charAt(0));
        }
        if(this.regularExpression.charAt(this.mainOperatorIndex()) == '*') {
            RegExp subReg = new RegExp(this.regularExpression.substring(1, this.regularExpression.length()-2));
            return subReg.valid();
        }
        RegExp protasis = new RegExp(this.regularExpression.substring(1, this.mainOperatorIndex()));
        RegExp apodosis = new RegExp(this.regularExpression.substring(this.mainOperatorIndex() + 1, this.regularExpression.length() - 1));
        return protasis.valid()&& apodosis.valid();
    }

}
