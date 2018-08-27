package com.gigigo.showcase.presentation.view.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.gigigo.showcase.R;
import com.gigigo.showcase.Utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

  private static final String TAG = "SettingsActivity";
  public static final int RESULT_CODE = 0x23;
  private EditText apiKeyEditText;
  private EditText apiSecretEditText;
  private SwitchCompat typeSwitch;
  private Spinner levelSpinner;
  private List<ProjectData> projectDataList;
  private boolean doubleTap = false;
  private int currentProject = -1;
  Boolean isFinihsed = true;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    initView();
    projectDataList = ProjectData.getDefaultProjectDataList();
    isFinihsed = false;
  }

  private void initView() {
    setTitle("");
    initToolbar();
    apiKeyEditText = findViewById(R.id.apiKeyEditText);
    apiSecretEditText = findViewById(R.id.apiSecretEditText);
    typeSwitch = findViewById(R.id.typeSwitch);
    levelSpinner = findViewById(R.id.levelSpinner);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.levels_array,
        android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    levelSpinner.setAdapter(adapter);

    Button startButton = findViewById(R.id.startButton);
    startButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startOrchextra();
      }
    });

    View projectsView = findViewById(R.id.projectsView);
    projectsView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (doubleTap) {

          if (currentProject >= projectDataList.size() - 1) {
            currentProject = 0;
          } else {
            currentProject++;
          }

          loadProjectData();
          return;
        }

        doubleTap = true;
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            doubleTap = false;
          }
        }, 500);
      }
    });
  }

  void loadProjectData() {

    Toast.makeText(this, projectDataList.get(currentProject).getName(), Toast.LENGTH_SHORT).show();
    apiKeyEditText.setText(projectDataList.get(currentProject).getApiKey());
    apiSecretEditText.setText(projectDataList.get(currentProject).getApiSecret());
  }

  private void startOrchextra() {
    String apiKey = apiKeyEditText.getText().toString();
    String apiSecret = apiSecretEditText.getText().toString();

    if (apiSecret.isEmpty() || apiKey.isEmpty()) {
      showError("Credentials empty", "Apikey and Apisecret are mandatory to start orchextra.");
      return;
    }

    getApiToken(apiKey, apiSecret);
  }

  private void getApiToken(String apiKey, String apiSecret) {

    if (!Utils.isOnline(this)) {
      showError("Connection error", "You should have internet connection");
      return;
    }

    showLoading();
  }

  private Map<String, String> getCurrentCustomFields() {

    Map<String, String> customFields = new HashMap<>();

    customFields.put("type", typeSwitch.isChecked() ? "B" : "A");
    customFields.put("level", levelSpinner.getSelectedItem().toString());

    return customFields;
  }

  private void showLoading() {
  }

  private void hideLoading() {
  }

  private void showError(String title, String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {

          }
        })
        .setIcon(R.drawable.ic_mistake)
        .show();
  }

  private void initToolbar() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onBackPressed();
      }
    });

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  public static void openForResult(Activity context) {
    Intent intent = new Intent(context, SettingsActivity.class);
    context.startActivityForResult(intent, RESULT_CODE);
  }
}