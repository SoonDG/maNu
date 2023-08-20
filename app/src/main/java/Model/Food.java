package Model;

import java.io.Serializable;

public class Food implements Serializable {

    private int serving = 1;

    private String food_code;
    private String food_name; //이름
    private double food_kcal; //칼로리
    private int food_size; //용량
    private double food_carbs; //탄수화물
    private double food_protein; //단백질
    private double food_fat; //지방
    private double food_sugars; //당류
    private double food_sodium; //나트륨
    private double food_CH; //콜레스테롤
    private double food_Sat_fat; //포화지방산
    private double food_trans_fat; //트랜스지방

    //일반 음식
    public Food(String food_code, String food_name, double food_kcal, int food_size, double food_carbs, double food_protein, double food_fat, double food_sugars, double food_sodium, double food_CH, double food_Sat_fat, double food_trans_fat) {
        this.food_code = food_code;
        this.food_name = food_name;
        this.food_kcal = food_kcal;
        this.food_size = food_size;
        this.food_carbs = food_carbs;
        this.food_protein = food_protein;
        this.food_fat = food_fat;
        this.food_sugars = food_sugars;
        this.food_sodium = food_sodium;
        this.food_CH = food_CH;
        this.food_Sat_fat = food_Sat_fat;
        this.food_trans_fat = food_trans_fat;
    }

    //먹은 음식
    public Food(int serving, String food_code, String food_name, double food_kcal, int food_size, double food_carbs, double food_protein, double food_fat, double food_sugars, double food_sodium, double food_CH, double food_Sat_fat, double food_trans_fat) {
        this.serving = serving;
        this.food_code = food_code;
        this.food_name = food_name;
        this.food_kcal = food_kcal;
        this.food_size = food_size;
        this.food_carbs = food_carbs;
        this.food_protein = food_protein;
        this.food_fat = food_fat;
        this.food_sugars = food_sugars;
        this.food_sodium = food_sodium;
        this.food_CH = food_CH;
        this.food_Sat_fat = food_Sat_fat;
        this.food_trans_fat = food_trans_fat;
    }

    //먹은 음식 영양분
    public Food(double food_kcal, double food_carbs, double food_protein, double food_fat, double food_sugars, double food_sodium, double food_CH, double food_Sat_fat, double food_trans_fat) {
        this.food_kcal = food_kcal;
        this.food_carbs = food_carbs;
        this.food_protein = food_protein;
        this.food_fat = food_fat;
        this.food_sugars = food_sugars;
        this.food_sodium = food_sodium;
        this.food_CH = food_CH;
        this.food_Sat_fat = food_Sat_fat;
        this.food_trans_fat = food_trans_fat;
    }

    public Food(){

    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    public String getFood_code() {
        return food_code;
    }

    public void setFood_code(String food_code) {
        this.food_name = food_code;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public double getFood_kcal() {
        return food_kcal;
    }

    public void setFood_kcal(double food_kcal) {
        this.food_kcal = food_kcal;
    }

    public int getFood_size() {
        return food_size;
    }

    public void setFood_size(int food_size) {
        this.food_size = food_size;
    }

    public double getFood_carbs() {
        return food_carbs;
    }

    public void setFood_carbs(double food_carbs) {
        this.food_carbs = food_carbs;
    }

    public double getFood_protein() {
        return food_protein;
    }

    public void setFood_protein(double food_protein) {
        this.food_protein = food_protein;
    }

    public double getFood_fat() {
        return food_fat;
    }

    public void setFood_fat(double food_fat) {
        this.food_fat = food_fat;
    }

    public double getFood_sugars() {
        return food_sugars;
    }

    public void setFood_sugars(double food_sugars) {
        this.food_sugars = food_sugars;
    }

    public double getFood_sodium() {
        return food_sodium;
    }

    public void setFood_sodium(double food_sodium) {
        this.food_sodium = food_sodium;
    }

    public double getFood_CH() {
        return food_CH;
    }

    public void setFood_CH(double food_CH) {
        this.food_CH = food_CH;
    }

    public double getFood_Sat_fat() {
        return food_Sat_fat;
    }

    public void setFood_Sat_fat(double food_Sat_fat) {
        this.food_Sat_fat = food_Sat_fat;
    }

    public double getFood_trans_fat() {
        return food_trans_fat;
    }

    public void setFood_trans_fat(double food_trans_fat) {
        this.food_trans_fat = food_trans_fat;
    }
}
