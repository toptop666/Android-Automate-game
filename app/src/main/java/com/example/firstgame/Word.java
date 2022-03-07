package com.example.firstgame;

import java.util.*;

public class Word<T extends Comparable> implements Iterable<T>{

    private final char SEPARATOR = '>';

    private T letter;
    private int length;
    private Word next;

    public Word(T letter) {
        if(letter == null) {
            this.letter = null;
            this.length = 0;
            this.next = null;
        }
        else {
            this.letter = letter;
            this.length = 1;
            this.next = null;
        }
    }

    public T getLetter() {
        return letter;
    }

    public int length() {
        return length;
    }

    public Word getNext() {
        return next;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Word<T> word = (Word<T>) other;
        for(int i = 0; i<this.length; i++) {
            if((word.next == null && this.next != null) || (word.length != this.length) || !(word.letter.equals(this.letter))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        if(this == null) {
            return "{}";
        }
        String result = "";
        Word current = this;
        while(current != null){
            result += current.letter.toString();
            if(current.getNext() != null){
                result += SEPARATOR;
            }
            current = current.getNext();
        }
        return result;
    }

    public void addLetter(T letter) {
        if(this == null) {
            this.letter = letter;
            this.length = 1;
            return;
        }
        Word pointer = this;
        while (pointer.next != null) {
            pointer.length++;
            pointer = pointer.getNext();
        }
        pointer.length++;
        Word tail = new Word(letter);
        pointer.next = tail;

    }

    public Word subWord(int start, int finish) {
        if(start>finish || start<0 || finish>=this.length) {
            return null;
        }
        Word current = this;
        Word result = new Word(null);
        int counter = 0;
        boolean startInserting = false;
        while(current != null){
            if(counter-1 == finish) {
                break;
            }
            if(counter == start) {
                startInserting = true;
            }
            if(startInserting) {
                result.addLetter(current.getLetter());
            }
            current = current.getNext();
            counter++;
        }
        return result;
    }


    @Override
    public Iterator<T> iterator() {
        return new myIterator();
    }

    class myIterator implements Iterator<T> {

        Word<T> current = null;

        @Override
        public boolean hasNext() {
            if(current == null && Word.this != null) {
                return true;
            }
            else if(current != null) {
                return current.getNext() != null;
            }
            return false;
        }

        @Override
        public T next() {
            if(current == null && Word.this != null) {
                current = Word.this;
                return Word.this.getLetter();
            }
            else if (current != null) {
                current = current.getNext();
                return current.getLetter();
            }
            throw new NoSuchElementException();
        }
    }


}
