package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EatFoodRequest extends StringRequest {
    final static private String Save_URL = "http://sans12.ivyro.net/Eat_Food.php"; //선택한 음식을 먹은 음식 테이블로 저장
    final static private String Delete_URL = "http://sans12.ivyro.net/Delete_Eat_Food.php"; //먹은 음식 정보를 제거
    final static private String Get_URL = "http://sans12.ivyro.net/Get_Eat_Food.php"; //먹은 음식 정보들을 가져오기
    final static private String Edit_URL = "http://sans12.ivyro.net/Edit_Eat_Food.php"; //먹은 음식 정보를 수정

    private Map<String, String> map;

    public EatFoodRequest(String user_id, String eat_date, int serving, String food_code, Response.Listener<String> listener){ //선택한 음식을 먹은 음식 테이블에 저장
        super(Method.POST, Save_URL, listener, null);
        map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("eat_date", eat_date);
        map.put("serving", serving + "");
        map.put("food_code", food_code);
    }

    public EatFoodRequest(String user_id, String eat_date, String food_code, Response.Listener<String> listener){ //선택한 음식을 먹은 음식 테이블에서 제거
        super(Method.POST, Delete_URL, listener, null);
        map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("eat_date", eat_date);
        map.put("food_code", food_code);
    }

    public EatFoodRequest(String user_id, String eat_date, Response.Listener<String> listener){ //먹은 음식 정보들을 전부 가져오기
        super(Method.POST, Get_URL, listener, null);
        map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("eat_date", eat_date);
    }

    public EatFoodRequest(int serving, String user_id, String eat_date, String food_code, Response.Listener<String> listener){ //선택한 음식을 먹은 음식 테이블에서 제거
        super(Method.POST, Edit_URL, listener, null);
        map = new HashMap<>();
        map.put("serving", serving + "");
        map.put("user_id", user_id);
        map.put("eat_date", eat_date);
        map.put("food_code", food_code);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
