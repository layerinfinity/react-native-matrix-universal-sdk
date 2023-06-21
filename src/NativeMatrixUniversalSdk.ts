import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
import type { LoginParamsType } from './types';

// Not actually used for now
export interface Spec extends TurboModule {
  createClient(url: string): Promise<void>;
  login(params: LoginParamsType): Promise<void>;
  getRoom(roomId: string): Promise<void>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('MatrixUniversalSdk');
