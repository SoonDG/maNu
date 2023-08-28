package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupEatFoodEditBinding;

public class PopupEatFoodEditActivity extends AppCompatActivity {
    private ActivityPopupEatFoodEditBinding popupEatFoodEditBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupEatFoodEditBinding = ActivityPopupEatFoodEditBinding.inflate(getLayoutInflater());
        View view = popupEatFoodEditBinding.getRoot();
        setContentView(view);
    }
}