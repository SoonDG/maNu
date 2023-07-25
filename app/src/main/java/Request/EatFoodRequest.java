package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EatFoodRequest extends StringRequest {
    final static private String URL = "http://sans12.ivyro.net/EatFood.php";

    private Map<String, String> map;

    public EatFoodRequest(String user_id, String eat_date, String food_code, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("eat_date", eat_date);
        map.put("food_code", food_code);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
