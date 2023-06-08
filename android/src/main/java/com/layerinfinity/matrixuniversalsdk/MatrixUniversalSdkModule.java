package com.layerinfinity.matrixuniversalsdk;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.layerinfinity.matrixuniversalsdk.key.HomeServerKey;

import org.matrix.android.sdk.api.Matrix;
import org.matrix.android.sdk.api.MatrixConfiguration;
import org.matrix.android.sdk.api.SyncConfig;
import org.matrix.android.sdk.api.auth.data.HomeServerConnectionConfig;
import org.matrix.android.sdk.api.crypto.MXCryptoConfig;
import org.matrix.android.sdk.api.session.Session;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import okhttp3.ConnectionSpec;


public class MatrixUniversalSdkModule extends ReactContextBaseJavaModule {
  public static final String NAME = "MatrixUniversalSdk";
  public static final String TAG = MatrixUniversalSdkModule.class.getSimpleName();

  public static final String E_MATRIX_ERROR = "E_MATRIX_ERROR";
  public static final String E_NETWORK_ERROR = "E_NETWORK_ERROR";
  public static final String E_UNEXCPECTED_ERROR = "E_UNKNOWN_ERROR";

  ReactApplicationContext ctx;

  MatrixUniversalSdkModule(ReactApplicationContext context) {
    super(context);
    ctx = context;
  }

  private Matrix matrix;

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  public Session getLastSession() {
    Session lastSession = matrix.authenticationService().getLastAuthenticatedSession();
    if (lastSession != null) {
      SessionHolder.currentSession = lastSession;
      // Don't forget to open the session and start syncing.

      lastSession.open();
      lastSession.syncService().startSync(true);
    }

    return lastSession;
  }

  // TODO: Maybe use this? https://developer.android.com/topic/libraries/app-startup
  // Must be used first
  @ReactMethod
  public void createClient(ReadableMap params) {
    try {
      MatrixConfiguration configuration = new MatrixConfiguration(
        "Default-application-flavor", // applicationFlavor
        new MXCryptoConfig(),
        "https://scalar.vector.im/",
        "https://scalar.vector.im/api",
        // integrationWidgetUrls
        Arrays.asList("https://scalar.vector.im/_matrix/integrations/v1",
          "https://scalar.vector.im/api",
          "https://scalar-staging.vector.im/_matrix/integrations/v1",
          "https://scalar-staging.vector.im/api",
          "https://scalar-staging.riot.im/scalar/api"),
        null, // clientPermalinkBaseUrl
        null, // proxy
        ConnectionSpec.RESTRICTED_TLS,
        true,
        null,
        new RoomDisplayNameFallbackProviderImpl(),
        true,
        Collections.emptyList(),
        new SyncConfig(), // syncConfig
        Collections.emptyList(), // metricPlugins
        null
      );

      matrix = new Matrix(ctx, configuration);
      HomeServerConnectionConfig config = new HomeServerConnectionConfig.Builder()
        .withHomeServerUri(Uri.parse(params.getString(HomeServerKey.BASE_URL)))
        .build();
    } catch (Exception e) {
      // Catch here
    }
  }

  @ReactMethod
  public void loginWithToken(String token) {
    // Try this.
    matrix.authenticationService().getLoginWizard().loginWithToken(token, null);
  }
}
