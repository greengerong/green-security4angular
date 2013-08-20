package com.github.greengerong;

public enum TokenFrom {
    Header(1), Param(2), All(1 | 2);
    private int value;

    TokenFrom(int value) {

        this.value = value;
    }

    public boolean include(TokenFrom tokenFrom) {
        return (tokenFrom.value & this.value) == this.value;
    }
}
