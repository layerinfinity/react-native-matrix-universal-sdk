package com.layerinfinity.matrixuniversalsdk

import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.layerinfinity.matrixuniversalsdk.key.AuthenticationKey
import com.layerinfinity.matrixuniversalsdk.key.HomeServerKey
import com.layerinfinity.matrixuniversalsdk.key.RoomKey
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.Matrix
import org.matrix.android.sdk.api.MatrixConfiguration
import org.matrix.android.sdk.api.auth.data.HomeServerConnectionConfig
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.events.model.toModel
import org.matrix.android.sdk.api.session.room.RoomSortOrder
import org.matrix.android.sdk.api.session.room.RoomSummaryQueryParams
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.model.message.MessageContent
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams

class MatrixUniversalSdkModule internal constructor(var ctx: ReactApplicationContext) :
  ReactContextBaseJavaModule(
    ctx
  ) {
  private lateinit var matrix: Matrix


  override fun getName(): String {
    return NAME
  }

  // Don't forget to open the session and start syncing.
  val lastSession: Session?
    get() {
      val lastSession = matrix.authenticationService().getLastAuthenticatedSession()
      if (lastSession != null) {
        SessionHolder.currentSession = lastSession
        // Don't forget to open the session and start syncing.
        lastSession.open()
        lastSession.syncService().startSync(true)
      }
      return lastSession
    }

  // TODO: Maybe use this? https://developer.android.com/topic/libraries/app-startup
  // Must be used first
  @ReactMethod
  fun createClient(params: ReadableMap) {
    try {
      val configuration = MatrixConfiguration(
        roomDisplayNameFallbackProvider = RoomDisplayNameFallbackProviderImpl()
      )
      matrix = Matrix(ctx, configuration)
    } catch (e: Exception) {
      // Catch here
    }
  }

  @ReactMethod
  fun login(params: ReadableMap) {
    // Try this.
    val homeServerUri = params.getString(HomeServerKey.HOME_SERVER_URL)
    val username = params.getString(AuthenticationKey.USERNAME_KEY)!!.trim()
    val password = params.getString(AuthenticationKey.PASSWORD_KEY)!!.trim()
    val homeServerConnectionConfig = try {
      HomeServerConnectionConfig
        .Builder()
        .withHomeServerUri(Uri.parse(homeServerUri))
        .build()
    } catch (failure: Throwable) {
      return
    }

    (ctx.currentActivity as AppCompatActivity).lifecycleScope.launch {
      try {
        matrix.authenticationService().directAuthentication(
          homeServerConnectionConfig,
          username,
          password,
          "chatgm-matrix",
        )
      } catch (failure: Throwable) {
        Toast.makeText(ctx, "Failure: $failure", Toast.LENGTH_SHORT).show()
        null
      }?.let { session ->
        SessionHolder.currentSession = session
        session.open()
        session.syncService().startSync(true)
      }
    }
  }

  @ReactMethod
  fun getRoom(roomId: String, promise: Promise) {
    val room = SessionHolder.currentSession?.roomService()?.getRoom(roomId)
    if (room !== null) {
      val messageContent = room.roomSummary()?.latestPreviewableEvent?.root?.getClearContent()
        .toModel<MessageContent>()
      val lastMsgContent = messageContent?.body ?: ""

      val throwback = Arguments.createMap().apply {
        putString(RoomKey.ROOM_ID, room.roomId)
        putString(RoomKey.DISPLAY_NAME, room.roomSummary()?.displayName)
        putString(RoomKey.LAST_MESSAGE, lastMsgContent)
      }

      promise.resolve(throwback)
    }
  }

  fun getRooms(promise: Promise) {
    val roomSummariesQuery = roomSummaryQueryParams {
      memberships = Membership.activeMemberships()
    }
    val defaultRoomSortOrder = RoomSortOrder.ACTIVITY

    val rooms = SessionHolder.currentSession?.roomService()?.getRoomSummaries(
      roomSummariesQuery,
      defaultRoomSortOrder
    )

    if (rooms !== null) {
      rooms.map
      val throwback = Arguments.createArray().apply {
//        putString("id", "aa")
      }
      promise.resolve(throwback)
    }
  }

  companion object {
    const val NAME = "MatrixUniversalSdk"
    val TAG = MatrixUniversalSdkModule::class.java.simpleName
    const val E_MATRIX_ERROR = "E_MATRIX_ERROR"
    const val E_NETWORK_ERROR = "E_NETWORK_ERROR"
    const val E_UNEXCPECTED_ERROR = "E_UNKNOWN_ERROR"
  }
}
