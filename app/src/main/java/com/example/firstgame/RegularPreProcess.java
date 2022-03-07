package com.example.firstgame;

import java.util.Stack;

public class RegularPreProcess {

    private static String addChar(String str, char ch, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }

    private static String addBracketsAndOperates(String regExp) {
        regExp = '(' + regExp + ')';
        Stack<Integer> bracketsIndexes = new Stack<Integer>();
        for(int i = 0; i<regExp.length() - 1; i++) {
            if(regExp.charAt(i) == '(') {
                bracketsIndexes.push(i);
            }

            if(Character.isDigit(regExp.charAt(i)) && Character.isDigit(regExp.charAt(i+1))) {
                regExp = addChar(regExp, '@', i+1);
                regExp = addChar(regExp, ')', i+3);
                regExp = addChar(regExp, '(', i);
                bracketsIndexes.push(i);
                i+=1;
            }
            if(regExp.charAt(i) == ')' && regExp.charAt(i+1) == '(') {
                regExp = addChar(regExp, '@', i+1);
            }
            if(regExp.charAt(i) == ')' && Character.isDigit(regExp.charAt(i+1))) {
                regExp = addChar(regExp, '@', i+1);
                regExp = addChar(regExp, ')', i+3);
                int firstBracket = bracketsIndexes.peek();
                regExp = addChar(regExp, '(', firstBracket);
                bracketsIndexes.push(firstBracket);
                i+=1;
            }
            if(Character.isDigit(regExp.charAt(i)) && regExp.charAt(i+1) == '(') {
                regExp = addChar(regExp, '@', i+1);
                regExp = addChar(regExp, '(', i);
                for(int j = i; j<regExp.length(); j++) {
                    if(regExp.charAt(j) == ')') {
                        regExp = addChar(regExp, ')', j);
                    }
                }
            }
            if(regExp.charAt(i) == ')') {
                bracketsIndexes.pop();
            }
        }
        return regExp;
    }

    private static String deleteExcessBrackets(String regExp) {
        String regCopy = new String(regExp);
        boolean flag, flag2 = false;
        flag = regCopy.charAt(0) == '(' && regCopy.charAt(1) == '(';
        flag2 = regCopy.charAt(regCopy.length() - 1) == ')' && (regCopy.charAt(regCopy.length() - 2) == ')' || regCopy.charAt(regCopy.length() - 2) == '*');
        while(flag2 && flag && balancedBrackets(regCopy)) {
            regCopy = regCopy.substring(1, regCopy.length() - 1);
            flag = regCopy.charAt(0) == '(' && regCopy.charAt(1) == '(';
            flag2 = regCopy.charAt(regCopy.length() - 1) == ')' && (regCopy.charAt(regCopy.length() - 2) == ')' || regCopy.charAt(regCopy.length() - 2) == '*');
        }
        if(!balancedBrackets(regCopy)) {
            regCopy = '(' + regCopy + ')';
        }
        return regCopy;
    }

    private static boolean balancedBrackets(String regExp) {
        int bracketsCounter = 0;
        for(int i = 0; i<regExp.length(); i++) {
            if (regExp.charAt(i) == '(') {
                bracketsCounter++;
            }
            if (regExp.charAt(i) == ')') {
                bracketsCounter--;
            }
            if(bracketsCounter < 0 || (bracketsCounter == 0 && regExp.length() - i > 1)) {
                return false;
            }
        }
        return (bracketsCounter==0);
    }

    public static String preProcess(String regExp) {
        regExp = addBracketsAndOperates(regExp);
        String reg2 = deleteExcessBrackets(regExp);
        if(balancedBrackets(reg2)) {
            return reg2;
        }
        else {
            return regExp;
        }
    }

    public static int mainOperatorIndex(String regExp) {
        if(regExp.length() == 5 || regExp.length() == 4) {
            return 2;
        }
        boolean flag = false;
        int bracketsCounter = 0;
        for(int i = 0; i<regExp.length()-1; i++) {
            if(regExp.charAt(i) == '(') {
                bracketsCounter++;
            }
            if(regExp.charAt(i) == ')') {
                bracketsCounter--;
                flag = true;
            }
            if(bracketsCounter == 1 && flag) {
                return i+1;
            }
        }
        return -1;
    }

    public static boolean valid(String regExp) {
        if(regExp.length() == 1) {
            return Character.isDigit(regExp.charAt(0));
        }
        if(regExp.charAt(mainOperatorIndex(regExp)) == '*') {
            return valid(regExp.substring(1, regExp.length()-2));
        }
        String protasis = regExp.substring(1, mainOperatorIndex(regExp));
        //protasis = preProcess(protasis);
        String apodosis = regExp.substring(mainOperatorIndex(regExp) + 1, regExp.length() - 1);
        //apodosis = preProcess(apodosis);
        return valid(protasis) && valid(protasis);
    }

}
