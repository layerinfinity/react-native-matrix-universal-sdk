import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import { createClient } from '../../';

export default function App() {
  const [result] = React.useState<number | undefined>();

  async function login() {
    const resp = await createClient({
      homeServerUrl: 'https://matrix.tauhu.cloud/',
    });
    console.log(resp);
  }

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>

      <Button title="Log" onPress={login} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
