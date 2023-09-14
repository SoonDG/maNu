package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ResetEatFoodRequest extends StringRequest {
    final static private String URL = "http://sans12.ivyro.net/Reset_Eat_Food.php"; //회원 탈퇴 php
    private Map<String, String> map;

    public ResetEatFoodRequest(String ID, Response.Listener<String> listener){ //계정 삭제
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("ID", ID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
