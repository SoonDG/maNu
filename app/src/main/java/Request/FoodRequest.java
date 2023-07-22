package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FoodRequest extends StringRequest{
    final static private String URL_ALL = "http://sans12.ivyro.net/Select_ALL_Food.php"; //모든 음식 데이터를 가져오는 php의 주소
    final static private String URL = "http://sans12.ivyro.net/Select_Food.php"; //입력한 검색어에 해당하는 음식 데이터를 가져오는 php의 주소
    private Map<String, String> map;

    public FoodRequest(Response.Listener<String> listener){
        super(Method.POST, URL_ALL, listener, null);
    }

    public FoodRequest(String Search_String, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("Search_String", Search_String);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
