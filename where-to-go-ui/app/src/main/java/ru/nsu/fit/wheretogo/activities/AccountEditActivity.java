package ru.nsu.fit.wheretogo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.util.helper.AuthorizationHelper;

public class AccountEditActivity extends AppCompatActivity {

    private String oldUserName;
    private EditText editNameText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ImageButton saveButton;
        ImageButton editPasswordButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        editNameText = findViewById(R.id.name_edit);

        oldUserName = AuthorizationHelper.getUserProfile().getUsername();
        editNameText.setText(oldUserName);

        saveButton = findViewById(R.id.save_profile_btn);
        saveButton.setOnClickListener(this::updateUserSettings);

        editPasswordButton = findViewById(R.id.changePwd_btn);
        editPasswordButton.setOnClickListener(this::openChangePwdEditor);
    }

    public void updateUserSettings(View view) {
        String newUsername = editNameText.getText().toString();


        if (newUsername.isEmpty()) {
            showNotification(getString(R.string.emptyUsernameMsg));
            return;
        }

        if (!newUsername.equals(oldUserName)) {
            AuthorizationHelper.changeUsername(
                    newUsername,
                    successResponse -> {
                        AuthorizationHelper.getUserProfile().setUsername(newUsername);
                        showNotification("Имя успешно изменено");
                    },
                    failResponse ->
                            showNotification("Не удалось изменить имя. Возможно, оно уже занято"),
                    () -> showNotification(getString(R.string.unexpectedErrorMsg))
            );
        }
    }

    public void openChangePwdEditor(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void showNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
