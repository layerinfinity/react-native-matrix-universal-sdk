import * as React from 'react';

import { StyleSheet, View, Text, Button, NativeModules } from 'react-native';
import { createClient, loginWithJwt, getRooms } from '../../';

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

  async function getRoomsRN() {
    const resp = await getRooms();
    console.log(resp);
  }

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>

      <Button
        title="Check Native Modules"
        onPress={() => {
          console.log(
            // NativeModules.RN_MatrixSdk,
            NativeModules.MatrixUniversalSdk
          );
        }}
      />

      <Button title="Create Client (run first)" onPress={loggg} />

      <Button title="Login with PW" onPress={loginWithPw} />

      <Button title="Get Rooms" onPress={getRoomsRN} />
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
