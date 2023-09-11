#import <Foundation/Foundation.h>
#import <React/RCTUIManager.h>
#import <MatrixSDK/MatrixSDK.h>

#import "MatrixUniversalSdk.h"

@implementation MatrixUniversalSdk
{
    MXRestClient *mxRestClient;
    MXSession *mxSession;
    MXCredentials *mxCredentials;
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

// TODO
RCT_EXPORT_METHOD(createClient:(NSString *) url
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    if (mxSession == nil) {
        reject(kErrorNotInitialized, @"MXSession not initialized", nil);
    }
}

// TODO
RCT_EXPORT_METHOD(login:(NSDictionary *) params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    if (mxSession == nil) {
        reject(kErrorNotInitialized, @"MXSession not initialized", nil);
    }
    
    NSString *homeServerUrl = params[kHomeServerUrl];
    
    mxCredentials = [[MXCredentials alloc]
                     initWithHomeServer:homeServerUrl
                     userId:nil
                     accessToken:nil];
    
    mxRestClient = [[MXRestClient alloc]
                    initWithHomeServer:homeServerUrl
                    andOnUnrecognizedCertificateBlock:^BOOL(NSData *certificate) {
        // TODO: Reject properly
        return NO;
    }];
    
    mxSession = [[MXSession alloc] initWithMatrixRestClient:mxRestClient];
    [
        mxSession.matrixRestClient login:params success:^(NSDictionary *JSONResponse) {
            resolve(@(true));
        } failure:^(NSError *error) {
            reject(kAuthenticationFailed, @"Cannot login, check your credentials", nil);
        }
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
