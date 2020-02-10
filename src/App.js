import React from 'react';
import { StyleSheet, Text, TouchableOpacity } from 'react-native';
import 'react-native-gesture-handler';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

import Home from './Home';
import Settings from './Edit';
import Ring from './Ring';


const Stack = createStackNavigator();

export default function () {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen
          name="Home"
          component={Home}
          options={params => ({
            headerTitle: props => <Text>Alarms</Text>,
            headerRight: () => <AddButton
              title={"ADD"}
              onPress={() => params.navigation.navigate('Edit')}
            />
          })}
        />
        <Stack.Screen
          name="Edit"
          component={Settings}
        />
        <Stack.Screen
          name="Ring"
          component={Ring}
          options={{headerShown: false}}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

function AddButton ({title, onPress}) {
  return (
    <TouchableOpacity
      style={styles.button}
      onPress={onPress}
      underlayColor='#fff'>
      <Text style={styles.buttonText}>{title}</Text>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  button: {
    backgroundColor: 'transparent',
    padding: 10
  },
  buttonText: {
    color: 'black',
  },
});
