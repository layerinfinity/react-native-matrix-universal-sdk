package com.layerinfinity.matrixuniversalsdk

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

class MatrixUniversalSdkPackage : TurboReactPackage() {

  override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
    val eventEmitter = RNEventEmitter(reactContext)
    val observers = MatrixTimelineObservers(eventEmitter)

    return if (name == MatrixUniversalSdkModule.NAME) {
      MatrixUniversalSdkModule(reactContext, eventEmitter, observers)
    } else {
      null
    }
  }

  override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
    return ReactModuleInfoProvider {
      val moduleInfos: MutableMap<String, ReactModuleInfo> = HashMap()
      val isTurboModule = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
      moduleInfos[MatrixUniversalSdkModule.NAME] = ReactModuleInfo(
        MatrixUniversalSdkModule.NAME,
        MatrixUniversalSdkModule.NAME,
        false,  // canOverrideExistingModule
        false,  // needsEagerInit
        true,  // hasConstants
        false,  // isCxxModule
        isTurboModule // isTurboModule
      )
      moduleInfos
    }
  }
}
