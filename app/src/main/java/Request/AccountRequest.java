package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class AccountRequest extends StringRequest {
    final static private String Edit_URL = "http://sans12.ivyro.net/Edit_User.php"; //회원 정보 수정 php
    final static private String Withdrawal_URL = "http://sans12.ivyro.net/Withdrawal.php"; //회원 탈퇴 php
    private Map<String, String> map;
    public AccountRequest(String ID, String Password, int Age, String Gender, Response.Listener<String> listener){ //계정 정보 수정
        super(Method.POST, Edit_URL, listener, null);
        map = new HashMap<>();
        map.put("ID", ID);
        map.put("Password", Password);
        map.put("Age",Age + "");
        map.put("Gender", Gender);
    }

    public AccountRequest(String ID, Response.Listener<String> listener){ //계정 삭제
        super(Method.POST, Withdrawal_URL, listener, null);
        map = new HashMap<>();
        map.put("ID", ID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
