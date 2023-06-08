import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

// Not actually used for now
export interface Spec extends TurboModule {
  multiply(a: number, b: number): Promise<number>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('MatrixUniversalSdk');
