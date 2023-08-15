package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetEatFoodRequest extends StringRequest {

    final static private String URL = "http://sans12.ivyro.net/Get_Eat_Food.php"; //먹은 음식 정보들을 가져오기(user_id, eat_date)
    private Map<String, String> map;

    public GetEatFoodRequest(String user_id, String eat_date, Response.Listener<String> listener){ //먹은 음식 정보들을 전부 가져오기
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("user_id", user_id);
        if(!eat_date.isEmpty()) map.put("eat_date", eat_date);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
