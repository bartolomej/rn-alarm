import { StyleSheet, Text, TouchableOpacity } from 'react-native';
import React from 'react';


export default function ({ onPress, title }) {
  return (
    <TouchableOpacity
      style={styles.button}
      onPress={onPress}
      underlayColor='#fff'>
      <Text style={styles.buttonText}>{title}</Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  button: {
    backgroundColor: 'transparent',
  },
  buttonText: {
    color: 'black',
  },
});
