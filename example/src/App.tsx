import * as React from 'react';

import { StyleSheet, View, Text, Button, NativeModules } from 'react-native';
import { createClient, loginWithJwt } from '../../';

export default function App() {
  const [result] = React.useState<number | undefined>();

  async function loggg() {
    const resp = await createClient('https://matrix.tauhu.cloud/');
    console.log(resp);
  }

  async function loginWithPw() {
    const resp = await loginWithJwt({
      homeServerUrl: 'https://matrix.tauhu.cloud/',
      jwtToken: process.env.GM_JWT_TOKEN || '',
    });
    console.log(resp);
  }

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>

      <Button
        title="Check Native Modules"
        onPress={() => {
          console.log(
            NativeModules.RN_MatrixSdk,
            NativeModules.MatrixUniversalSdk
          );
        }}
      />

      <Button title="Log" onPress={loggg} />

      <Button title="Log2" onPress={loginWithPw} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    gap: 22,
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
