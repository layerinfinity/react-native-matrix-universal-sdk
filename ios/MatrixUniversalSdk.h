#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#import "MatrixUniversalSdk.h"


// Will expose a RN_MatrixSdk native module to JS
@interface RCT_EXTERN_REMAP_MODULE(RN_MatrixSdk, MatrixUniversalSdk, NSObject)

+ (BOOL)requiresMainQueueSetup
{
    return false;
}

RCT_EXTERN_METHOD(supportedEvents)

RCT_EXTERN_METHOD(constantsToExport)

RCT_EXTERN_METHOD(setAdditionalEventTypes:(NSArray *)types)

RCT_EXTERN_METHOD(createClient:(NSString *)url)

RCT_EXTERN_METHOD(setCredentials:(nonnull NSString *)accessToken deviceId:(nonnull NSString *)deviceId userId:(nonnull NSString *)userId homeServer:(nonnull NSString *)homeServer refreshToken:(NSString *)refreshToken)

RCT_EXTERN_METHOD(login:(nonnull NSString *)username password:(nonnull NSString *)password resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(startSession:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(createRoom:(NSArray *)userIds isDirect:(nonnull BOOL *)isDirect isTrustedPrivateChat:(nonnull BOOL *)isTrustedPrivateChat name:(NSString *)name resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
@end
