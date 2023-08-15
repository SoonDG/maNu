package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteEatFoodRequest extends StringRequest {

    final static private String URL = "http://sans12.ivyro.net/Delete_Eat_Food.php"; //먹은 음식 정보를 제거(user_id, eat_date, food_code)
    private Map<String, String> map;

    public DeleteEatFoodRequest(String user_id, String food_code, Response.Listener<String> listener){ //선택한 음식을 먹은 음식 테이블에서 제거
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
