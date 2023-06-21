import { NativeModules, Platform } from 'react-native';
import type { LoginParamsType, RoomType } from './types';

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

export function createClient(url: string): Promise<void> {
  return MatrixUniversalSdk.createClient(url);
}

export function login(params: LoginParamsType): Promise<void> {
  return MatrixUniversalSdk.login(params);
}

export function getRoom(roomId: string): Promise<RoomType> {
  return MatrixUniversalSdk.getRoom(roomId);
}

export function getRooms(): Promise<RoomType[]> {
  return MatrixUniversalSdk.getRooms();
}
