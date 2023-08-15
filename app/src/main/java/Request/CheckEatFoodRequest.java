package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckEatFoodRequest extends StringRequest {

    final static private String URL = "http://sans12.ivyro.net/Check_Eat_Food.php"; //음식을 먹은 음식에 추가 하기 전, 이미 먹은 음식에 있는지 확인(user_id, eat_date, food_code, code)
    private Map<String, String> map;

    public CheckEatFoodRequest(String user_id, String food_code, Response.Listener<String> listener){ //선택한 음식을 먹은 음식 테이블에서 제거
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("food_code", food_code);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
