package Interface;

public interface EatFoodDelete { //먹은 음식을 지웠을 때 MainFragment가 Adapter로 부터 삭제된 음식 데이터를 받아 영양분 정보를 즉시 수정하도록 할 인터페이스
    public void EatFoodDelete(int serving, double food_kcal, double food_carbs, double food_protein, double food_fat, double food_sugars, double food_sodium, double food_CH, double food_Sat_fat, double food_trans_fat);
}
