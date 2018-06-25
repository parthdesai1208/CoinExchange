package com.p1208.sample;

import ir.mirrajabi.searchdialog.core.Searchable;

public class user implements Searchable {
    public String name;
    public String password;
    public int balance;
    public String id;
    public String image;
    public int age;

    public user() {
        //required
        //don't erase it.
    }

    public user(String title) {
        name = title;
    }

    public user setname(String title) {
        name = title;
        return this;
    }

    public user(String id, String name, String password, int balance, String image, int age) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.balance = balance;
        this.image = image;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getBalance() {
        return balance;
    }

    public String getImage() {
        return image;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String getTitle() {
        return name;
    }


}
