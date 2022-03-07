package com.example.firstgame;
import java.util.Objects;

enum Types {Elementary, KleeneClosure, Chaining, Union}
public class RegularExpression<T> {

    private T element;
    private RegularExpression protasis;
    private RegularExpression apodosis;
    private Types type;

    public RegularExpression(RegularExpression protasis, RegularExpression apodosis, Types type) {
        if(type != Types.Chaining && type != Types.Union) {
            return;
        }
        this.protasis = protasis;
        this.apodosis = apodosis;
        this.type = type;
    }

    public RegularExpression(RegularExpression expression, Types type) {
        if(type == Types.Chaining || type == Types.Union || type == Types.Elementary) {
            return;
        }
        this.protasis = expression;
        this.apodosis = null;
        this.type = type;
    }

    public RegularExpression(T letter, Types type) {
        if(type == Types.Chaining || type == Types.Union || type == Types.KleeneClosure) {
            return;
        }
        this.element = letter;
        this.protasis = null;
        this.apodosis = null;
        this.type = type;
    }

    public RegularExpression(RegularExpression other) {
        this.type = other.type;
        this.element = (T) other.element;
        this.protasis = other.protasis;
        this.apodosis = other.apodosis;
    }

    public T getElement() {
        return element;
    }

    public RegularExpression getProtasis() {
        return protasis;
    }

    public RegularExpression getApodosis() {
        return apodosis;
    }

    public Types getType() {
        return type;
    }

    public String toString() {
        switch (this.type) {
            case Union:
                return "(" + this.protasis.toString() + "+" + this.apodosis.toString() + ")";
            case Chaining:
                return "(" + this.protasis.toString() + "." + this.apodosis.toString() + ")";
            case KleeneClosure:
                return this.protasis.toString() + "*";
            case Elementary:
                return this.element.toString();
            default:
                return "";
        }
    }


}
