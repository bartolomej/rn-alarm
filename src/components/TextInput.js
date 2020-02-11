import React from 'react';
import { TextInput, View, StyleSheet, Text } from 'react-native';


export default function ({onChangeText, value, description}) {

  return (
    <View style={styles.container}>
      <Text>{description}</Text>
      <TextInput
        style={styles.textInput}
        onChangeText={onChangeText}
        value={value}
      />
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    width: '100%',
  },
  textInput: {
    borderWidth: 1,
    borderRadius: 12,
    padding: 10,
    paddingTop: 5,
    paddingBottom: 5,
    borderColor: 'black',
  },
});
