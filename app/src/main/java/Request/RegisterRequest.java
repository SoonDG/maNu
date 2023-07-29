package Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest{

    final static private String URL = "http://sans12.ivyro.net/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String ID, String Password, int Age, String Gender, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("ID", ID);
        map.put("Password", Password);
        map.put("Age", Age + "");
        map.put("Gender", Gender);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
       return map;
    }
}