package com.titan.model;

/**
 * Created by Whs on 2016/12/7 0007.
 */
public class Contributor {
    public final String login;
    public final int contributions;

    public Contributor(String login, int contributions) {
        this.login = login;
        this.contributions = contributions;
    }
}
