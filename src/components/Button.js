import { StyleSheet, Text, TouchableOpacity } from 'react-native';
import React from 'react';
import { colors } from '../global';


export default function ({ onPress, title, fill = false }) {
  return (
    <TouchableOpacity
      style={[styles.container, fill ? styles.fillContainer : styles.normalContainer]}
      onPress={onPress}
      underlayColor='#fff'>
      <Text
        style={[styles.buttonText, fill ? styles.fillText : styles.normalText]}>
        {title}
      </Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 10,
    margin: 10,
    paddingLeft: 20,
    paddingRight: 20,
    borderWidth: 2,
    borderColor: colors.BLUE,
    borderRadius: 25
  },
  fillContainer: {
    backgroundColor: colors.BLUE,
  },
  normalContainer: {
    backgroundColor: 'transparent',
  },
  buttonText: {
    fontWeight: 'bold'
  },
  fillText: {
    color: 'white',
  },
  normalText: {
    color: colors.BLUE
  }
});
