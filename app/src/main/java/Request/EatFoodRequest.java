package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EatFoodRequest extends StringRequest {
    final static private String URL = "http://sans12.ivyro.net/Eat_Food.php"; //선택한 음식을 먹은 음식 테이블로 저장(user_id, eat_date, serving, food_Code)

    private Map<String, String> map;

    public EatFoodRequest(String user_id, String eat_date, int serving, String food_code, Response.Listener<String> listener){ //선택한 음식을 먹은 음식 테이블에 저장
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("eat_date", eat_date);
        map.put("serving", serving + "");
        map.put("food_code", food_code);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
