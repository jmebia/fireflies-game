package com.crackerjacks.game.core.interactions;

public enum Type {

    a("a"),
    b("b"),
    c("c");

    private String id;

    Type (String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
