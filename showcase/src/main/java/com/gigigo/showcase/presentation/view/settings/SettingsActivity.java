package com.gigigo.showcase.presentation.view.settings;

import android.app.Activity;
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
import com.gigigo.showcase.App;
import com.gigigo.showcase.R;
import com.gigigo.showcase.domain.DataManager;
import com.gigigo.showcase.domain.entity.ConfigData;
import com.gigigo.showcase.presentation.presenter.SettingsPresenter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class SettingsActivity extends AppCompatActivity implements SettingsView {

  public static final int SETTINGS_RESULT_CODE = 0x23;
  private EditText apiKeyEditText;
  private EditText apiSecretEditText;
  private EditText businessUnitEditText;
  private SwitchCompat typeSwitch;
  private Spinner levelSpinner;

  private List<DataManager> dataManagerList;
  private boolean doubleTap = false;
  private int currentProject = -1;
  private SettingsPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    initView();
    //dataManagerList = DataManager.Companion.getDefaultDataManagerList();

    App app = (App) getApplication();
    presenter = new SettingsPresenter(app.getDataManager());
    presenter.attachView(this, true);
  }

  private void initView() {
    setTitle("");
    initToolbar();
    apiKeyEditText = findViewById(R.id.apiKeyEditText);
    apiSecretEditText = findViewById(R.id.apiSecretEditText);
    businessUnitEditText = findViewById(R.id.businessUnitEditText);
    typeSwitch = findViewById(R.id.typeSwitch);
    levelSpinner = findViewById(R.id.levelSpinner);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.levels_array,
        android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    levelSpinner.setAdapter(adapter);

    Button startButton = findViewById(R.id.startButton);
    startButton.setOnClickListener(v -> {

      String apiKey = apiKeyEditText.getText().toString();
      String apiSecret = apiSecretEditText.getText().toString();
      String businessUnit = businessUnitEditText.getText().toString();
      ConfigData configData = new ConfigData(apiKey, apiSecret, businessUnit);

      presenter.onStartClick(configData, getCurrentCustomFields());
    });

    View projectsView = findViewById(R.id.projectsView);
    projectsView.setOnClickListener(v -> {
      if (doubleTap) {
        if (currentProject >= dataManagerList.size() - 1) {
          currentProject = 0;
        } else {
          currentProject++;
        }
      }

      doubleTap = true;
      new Handler().postDelayed(() -> doubleTap = false, 500);
    });
  }

  private Map<String, String> getCurrentCustomFields() {

    Map<String, String> customFields = new HashMap<>();

    customFields.put("type", typeSwitch.isChecked() ? "B" : "A");
    customFields.put("level", levelSpinner.getSelectedItem().toString());

    return customFields;
  }

  @Override public void showLoading() {
  }

  private void showError(String title, String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, (dialog, which) -> {

        })
        .setIcon(R.drawable.ic_mistake)
        .show();
  }

  private void initToolbar() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(view -> onBackPressed());

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  public static void openForResult(Activity context) {
    Intent intent = new Intent(context, SettingsActivity.class);
    context.startActivityForResult(intent, SETTINGS_RESULT_CODE);
  }

  @Override public void showConfigData(@NotNull ConfigData configData) {
    apiKeyEditText.setText(configData.getApiKey());
    apiSecretEditText.setText(configData.getApiSecret());
    businessUnitEditText.setText(configData.getBusinessUnit());
  }

  @Override public void showNewProject() {
    Intent returnIntent = new Intent();
    setResult(Activity.RESULT_OK, returnIntent);
    finish();
  }
}