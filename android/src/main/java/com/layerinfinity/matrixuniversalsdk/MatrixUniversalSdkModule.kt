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
import com.facebook.react.bridge.WritableArray
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
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.model.message.MessageContent
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
import org.matrix.android.sdk.api.util.JsonDict

class MatrixUniversalSdkModule internal constructor(
  private val ctx: ReactApplicationContext,
  private val eventEmitter: RNEventEmitter,
  private val matrixTimelineObservers: MatrixTimelineObservers
) :
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
  fun createClient(url: String, promise: Promise) {
    try {
      val configuration = MatrixConfiguration(
        roomDisplayNameFallbackProvider = RoomDisplayNameFallbackProviderImpl()
      )
      if (!this::matrix.isInitialized) {
        matrix = Matrix(ctx, configuration)
      }
      promise.resolve(true)
    } catch (e: Exception) {
      // Catch here
      promise.reject(Error(e))
    }
  }

  // Use this to login
  @ReactMethod
  fun loginWithJwt(params: ReadableMap, promise: Promise) {
    // Try this.
    val homeServerUri = params.getString(HomeServerKey.HOME_SERVER_URL)
    val gmToken = params.getString(AuthenticationKey.GM_JWT_TOKEN)!!.trim()
    val homeServerConnectionConfig = try {
      HomeServerConnectionConfig
        .Builder()
        .withHomeServerUri(Uri.parse(homeServerUri))
        .build()
    } catch (failure: Throwable) {
      promise.reject(Error(failure))
      return
    }

    (ctx.currentActivity as AppCompatActivity).lifecycleScope.launch {
      try {
        val params: JsonDict = mapOf("type" to "org.matrix.login.jwt", "token" to gmToken)
        matrix.authenticationService().getLoginFlow(homeServerConnectionConfig)
        matrix.authenticationService().getLoginWizard().loginCustom(params)
        // Old method
        //        matrix.authenticationService().directAuthentication(
        //          homeServerConnectionConfig,
        //          username,
        //          password,
        //          "chatgm-matrix",
        //        )
      } catch (failure: Throwable) {
        promise.reject(Error(failure))
        Toast.makeText(ctx, "Failure: $failure", Toast.LENGTH_SHORT).show()
        null
      }?.let { session ->
        Toast.makeText(ctx, "Success: ${session.myUserId} ${session.sessionId}", Toast.LENGTH_SHORT).show()
        SessionHolder.currentSession = session
        session.open()
        session.syncService().startSync(true)
        promise.resolve("connected")
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

  @ReactMethod
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
      val throwback = rooms.map { it ->
        Arguments.createMap().apply {
          val messageContent = it.latestPreviewableEvent?.root?.getClearContent()
            .toModel<MessageContent>()
          val lastMsgContent = messageContent?.body ?: ""
          putString(RoomKey.ROOM_ID, it.roomId)
          putString(RoomKey.DISPLAY_NAME, it.displayName)
          putString(RoomKey.LAST_MESSAGE, lastMsgContent)
        }
      }
      promise.resolve(throwback)
    } else {
      promise.reject(Error("Room object is null"))
    }
  }

  fun createRoom(roomName: String, participants: WritableArray) {

  }

  companion object {
    const val NAME = "MatrixUniversalSdk"
    val TAG = MatrixUniversalSdkModule::class.java.simpleName
    const val E_MATRIX_ERROR = "E_MATRIX_ERROR"
    const val E_NETWORK_ERROR = "E_NETWORK_ERROR"
    const val E_UNEXCPECTED_ERROR = "E_UNKNOWN_ERROR"
  }
}
