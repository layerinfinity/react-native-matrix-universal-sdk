package com.layerinfinity.matrixuniversalsdk;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import org.matrix.android.sdk.api.Matrix;
import org.matrix.android.sdk.api.MatrixConfiguration;
import org.matrix.android.sdk.api.session.Session;


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
  public Matrix initMatrix() {
    MatrixConfiguration configuration = new MatrixConfiguration(
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      true,
      null,
      new RoomDisplayNameFallbackProviderImpl(),
      true,
      null,
      null,
      null,
      null
    );

    matrix = new Matrix(ctx, configuration);

    return matrix;
  }
}
