package com.layerinfinity.matrixuniversalsdk

import com.facebook.react.bridge.ReactApplicationContext

class RNEventEmitter(private val reactContext: ReactApplicationContext) {
  companion object {
    // Event types
    const val RN_EVENT_ERROR = "OnError"
  }
}
