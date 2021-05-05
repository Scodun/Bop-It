package com.se2.bopit.domain;

public class ActionModel extends ResponseModel {
    public final String action;

    public ActionModel(String action) {
        this.action = action;
    }

    @Override
    public ActionModel clone() {
        return new ActionModel(action);
    }
}
