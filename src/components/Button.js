import { StyleSheet, Text, TouchableOpacity } from 'react-native';
import React from 'react';


export default function ({ onPress, title }) {
  return (
    <TouchableOpacity
      style={styles.container}
      onPress={onPress}
      underlayColor='#fff'>
      <Text style={styles.buttonText}>{title}</Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: 'transparent',
    padding: 10,
    paddingLeft: 20,
    paddingRight: 20,
    borderWidth: 2,
    borderColor: 'black',
    borderRadius: 25
  },
  buttonText: {
    color: 'black',
  },
});
