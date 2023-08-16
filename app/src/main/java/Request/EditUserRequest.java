package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditUserRequest extends StringRequest {
    final static private String URL = "http://sans12.ivyro.net/Edit_User.php"; //회원 정보 수정 php
    private Map<String ,String> map;

    public EditUserRequest(String ID, int Age, String Gender, Response.Listener<String> listener){ //계정 정보 수정
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("ID", ID);
        map.put("Age",Age + "");
        map.put("Gender", Gender);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
