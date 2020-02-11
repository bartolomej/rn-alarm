import React from 'react';
import {View, Switch, Text, StyleSheet} from 'react-native';


export default function ({value, onChange, description}) {

  return (
    <View style={styles.container}>
      <View style={styles.innerContainer}>
        <Text>{description}</Text>
      </View>
      <View style={styles.innerContainer}>
        <Switch
          ios_backgroundColor={'black'}
          trackColor={{ false: 'blue', true: 'black', }}
          value={value}
          onValueChange={value => onChange(value)}/>
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between'
  },
  innerContainer: {
    flex: 1
  }
});
