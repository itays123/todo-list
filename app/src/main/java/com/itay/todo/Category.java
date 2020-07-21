package com.itay.todo;

import java.util.List;

public class Category {
    private String name;
    private int value;

    public Category(){

    }

    public Category(String name, int value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static String[] arrayOfCategories(List<Category> list){
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++){
            strings[i] = list.get(i).getName();
        }
        return strings;
    }

    public static int categoryValue(List<Category> list, String input){
        int value = -1;
        for (int i = 0; i < list.size(); i++){
            if (input.equals(list.get(i).getName())){
                value = list.get(i).getValue();
            }
        }
        return value;
    }

    public int categoryImageResource(){
        if (this.value > 10){
            return R.drawable.star_empty;
        } else if (this.value > 3){
            return R.drawable.star_half;
        } else {
            return R.drawable.star_full;
        }
    }

}
