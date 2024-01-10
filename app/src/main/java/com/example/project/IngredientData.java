package com.example.project;

public class IngredientData {
    private int iImg;
    private String iName;
    private String iDate;

    public IngredientData(int iImg, String iName,  String iDate){
        this.iImg = iImg;
        this.iName = iName;
        this.iDate = iDate;
    }
    public int getIngredient(){
        return this.iImg;
    }

    public String getName(){
        return this.iName;
    }

    public String getDate(){
        return this.iDate;
    }
}
