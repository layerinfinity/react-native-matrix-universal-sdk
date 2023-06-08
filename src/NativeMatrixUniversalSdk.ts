import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
import type { HomeServerConfigType } from './types';

// Not actually used for now
export interface Spec extends TurboModule {
  createClient(params: HomeServerConfigType): Promise<void>;
  loginWithToken(token: string): Promise<void>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('MatrixUniversalSdk');
