package com.layerinfinity.matrixuniversalsdk;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MatrixUniversalSdkModule extends ReactContextBaseJavaModule {
  public static final String NAME = "MatrixUniversalSdk";
  public static final String TAG = MatrixUniversalSdkModule.class.getSimpleName();

  public static final String E_MATRIX_ERROR = "E_MATRIX_ERROR";
  public static final String E_NETWORK_ERROR = "E_NETWORK_ERROR";
  public static final String E_UNEXCPECTED_ERROR = "E_UNKNOWN_ERROR";


  /**
   * Used when loading old messages
   */
  private HashMap<String, String> roomPaginationTokens = new HashMap<>();

  MatrixUniversalSdkModule(ReactApplicationContext context) {
    super(context);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


}
