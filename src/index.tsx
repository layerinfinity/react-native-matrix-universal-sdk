import { NativeModules, Platform } from 'react-native';
import type { HomeServerConfigType } from './types';

const LINKING_ERROR =
  `The package 'react-native-matrix-universal-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const MatrixUniversalSdkModule = isTurboModuleEnabled
  ? require('./NativeMatrixUniversalSdk').default
  : NativeModules.MatrixUniversalSdk;

const MatrixUniversalSdk = MatrixUniversalSdkModule
  ? MatrixUniversalSdkModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function createClient(params: HomeServerConfigType): Promise<void> {
  return MatrixUniversalSdk.createClient(params);
}

export function loginWithToken(token: string): Promise<void> {
  return MatrixUniversalSdk.loginWithToken(token);
}
