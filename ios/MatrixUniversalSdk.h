
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNMatrixUniversalSdkSpec.h"

@interface MatrixUniversalSdk : NSObject <NativeMatrixUniversalSdkSpec>
#else
#import <React/RCTBridgeModule.h>

@interface MatrixUniversalSdk : NSObject <RCTBridgeModule>
#endif

@end
