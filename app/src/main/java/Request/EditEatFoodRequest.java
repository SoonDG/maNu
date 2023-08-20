package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditEatFoodRequest extends StringRequest {

    final static private String URL = "http://sans12.ivyro.net/Edit_Eat_Food.php"; //먹은 음식 정보를 수정(serving, user_id, eat_date, food_code)
    private Map<String, String> map;

    public EditEatFoodRequest(int serving, String user_id, String eat_date, String food_code, Response.Listener<String> listener){ //선택한 음식을 먹은 음식 테이블에서 제거
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("serving", serving + "");
        map.put("user_id", user_id);
        if(!eat_date.isEmpty()) map.put("eat_date", eat_date);
        map.put("food_code", food_code);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
