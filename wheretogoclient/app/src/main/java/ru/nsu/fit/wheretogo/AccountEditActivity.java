package ru.nsu.fit.wheretogo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.nsu.fit.wheretogo.util.AuthorizationHelper;
import ru.nsu.fit.wheretogo.util.ObscuredSharedPreferences;

public class AccountEditActivity extends AppCompatActivity {
    private String oldUserName;

    private EditText editNameText;
    private ImageButton editPasswordButton;
    private ImageButton saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        editNameText = (EditText) findViewById(R.id.name_edit);

        oldUserName = AuthorizationHelper.getUserProfile().getUsername();
        editNameText.setText(oldUserName);

        saveButton = (ImageButton) findViewById(R.id.save_profile_btn);
        saveButton.setOnClickListener(this::updateUserSettings);

        editPasswordButton =  (ImageButton) findViewById(R.id.changePwd_btn);
        editPasswordButton.setOnClickListener(this::openChangePwdEditor);
    }

    public void updateUserSettings(View view){
        String newUsername = editNameText.getText().toString();


        if(newUsername.isEmpty()){
            showNotification(getString(R.string.emptyUsernameMsg));
            return;
        }

        if(!newUsername.equals(oldUserName)){
            AuthorizationHelper.changeUsername(
                    newUsername,
                    successResponse -> {
                        AuthorizationHelper.getUserProfile().setUsername(newUsername);
                        showNotification("Имя успешно изменено");
                    },
                    failResponse -> showNotification("Не удалось изменить имя. Возможно оно уже занято"),
                    () -> showNotification(getString(R.string.unexpectedErrorMsg))
            );
        }

    }

    public void openChangePwdEditor(View view){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void showNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
