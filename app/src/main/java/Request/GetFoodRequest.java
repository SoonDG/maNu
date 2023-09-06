package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetFoodRequest extends StringRequest{
    final static private String URL = "http://sans12.ivyro.net/Get_Food.php"; //입력한 검색어에 해당하는 음식 데이터를 가져오는 php의 주소
    private Map<String, String> map;

    public GetFoodRequest(String Search_String, int index, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        if(!Search_String.isEmpty()) map.put("Search_String", Search_String);
        map.put("index", index + "");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
