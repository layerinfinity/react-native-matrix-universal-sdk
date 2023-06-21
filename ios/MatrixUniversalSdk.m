#import <Foundation/Foundation.h>
#import <React/RCTUIManager.h>
#import <MatrixSDK/MatrixSDK.h>

#import "MatrixUniversalSdk.h"

@implementation MatrixUniversalSdk
{
    MXRestClient *mxRestClient;
    MXSession *mxSession;
}

static NSUUID * _uuid;

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents {
    return
    @[
        kHomeServerUrl,
        kDeviceId,
        kAccessToken,
    ];
}

RCT_EXPORT_METHOD(getRoom:(NSString *) roomId
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    if (mxSession == nil) {
        reject(kErrorNotInitialized, @"MXSession not initialized", nil);
    }
    
    MXRoom *room = [mxSession roomWithRoomId:roomId];
    if (room == nil) {
        reject(kErrorRoomNotFound, @"Specified room is not found", nil);
    }
}

+ (BOOL)requiresMainQueueSetup {
    return NO;
}

@end
