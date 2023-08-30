package ru.nsu.fit.wheretogo.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.util.helper.AuthorizationHelper;
import ru.nsu.fit.wheretogo.util.ObscuredSharedPreferences;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordText;
    private EditText newPasswordText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ImageButton saveButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        oldPasswordText = findViewById(R.id.old_pwd);
        newPasswordText = findViewById(R.id.new_pwd);
        saveButton = findViewById(R.id.save_pwd_btn);

        saveButton.setOnClickListener(this::updateUserPassword);
    }

    public void updateUserPassword(View view) {
        String oldPwd = oldPasswordText.getText().toString();
        String newPwd = newPasswordText.getText().toString();

        if (oldPwd.isEmpty() || !oldPwd.equals(AuthorizationHelper.getPassword())) {
            showNotification("Неправильно введен старый пароль!");
            return;
        }

        if (newPwd.isEmpty()) {
            showNotification("Введите новый пароль!");
            return;
        }

        if (newPwd.equals(oldPwd)) {
            showNotification("Новый и старый пароли не должны совпадать!");
        } else {
            AuthorizationHelper.changePassword(
                    newPwd,
                    successResponse -> {
                        AuthorizationHelper.setPassword(newPwd);

                        SharedPreferences sharedPreferences = new ObscuredSharedPreferences(
                                this, this.getSharedPreferences("AUTH_DATA", MODE_PRIVATE)
                        );
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("password", newPwd);
                        editor.apply();

                        showNotification("Пароль успешно изменен");
                        finish();
                    },
                    failResponse -> showNotification("Не удалось изменить имя! Возможно, оно уже занято!"),
                    () -> showNotification(getString(R.string.unexpectedErrorMsg))
            );
        }
    }

    private void showNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
