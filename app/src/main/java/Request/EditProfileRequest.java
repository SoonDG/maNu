package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditProfileRequest extends StringRequest {

    final static private String URL = "http://sans12.ivyro.net/Edit_Profile.php"; //유저의 프로필 이미지 변경
    private Map<String, String> map;

    public EditProfileRequest(String ID, String Profile, Response.Listener<String> listener){ //선택한 음식을 먹은 음식 테이블에서 제거
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("ID", ID);
        map.put("Profile", Profile);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
