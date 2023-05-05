#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#import <React/RCTUIManager.h>
#import <MatrixSDK/MatrixSDK.h>
#import "MatrixUniversalSdk.h"

@implementation MatrixUniversalSdk

static NSUUID * _uuid;

NSURL* mxHomeServer;
MXCredentials* mxCredentials;

RCT_EXPORT_MODULE()

// Example method
// See // https://reactnative.dev/docs/native-modules-ios
RCT_REMAP_METHOD(multiply,
                 multiplyWithA:(double)a withB:(double)b
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
    NSNumber *result = @(a * b);
    resolve(result);
}

- (NSArray<NSString *> *)supportedEvents
{
  return
  @[
    kUserSession
  ];
}

RCT_EXPORT_METHOD(configure:(NSString*) url) {
    mxHomeServer = [NSURL URLWithString:url];
}

RCT_EXPORT_METHOD(
            login:(NSString*)username
            password:(NSString*)password
            resolver:(RCTPromiseResolveBlock)resolve
            rejecter:(RCTPromiseRejectBlock)reject)
{
    if (mxCredentials != nil) {
        resolve(@{
            @"home_server": [mxCredentials homeServer],
            @"user_id": [mxCredentials userId]
        });
    }
}

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeMatrixUniversalSdkSpecJSI>(params);
}
#endif


@end
