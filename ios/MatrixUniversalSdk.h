#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#import <Foundation/Foundation.h>
#import <React/RCTEventEmitter.h>

// Keys
#define kHomeServerUrl @"homeServerUrl"
#define kDeviceId @"deviceId"
#define kAccessToken @"accessToken"

// Room keys
#define kRoomId @"roomId"
#define kMyUserId @"myUserId"
#define kDisplayName @"displayName"
#define kLastMessage @"lastMessage"

// Authentication keys
#define kUsername @"username"
#define kPassword @"password"

// Error keys
#define kErrorNotInitialized @"not_initialized"
#define kErrorRoomNotFound @"room_not_found"
#define kAuthenticationFailed @"authentication_failed"


@interface MatrixUniversalSdk : RCTEventEmitter <RCTBridgeModule>

@end
