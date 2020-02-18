import React from 'react';
import { StyleSheet, Text, TouchableOpacity } from 'react-native';
import 'react-native-gesture-handler';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

import Home from './screens/Alarms';
import Settings from './screens/Edit';
import Ring from './screens/Ring';


const Stack = createStackNavigator();

export default function () {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen
          name="Alarms"
          component={Home}
          options={params => ({
            ...headerStyles,
            title: 'Alarms',
            headerRight: () => (
              <AddButton
                title={"+ "}
                onPress={() => params.navigation.navigate('Edit')}
              />
            )
          })}
        />
        <Stack.Screen
          name="Edit"
          component={Settings}
          options={{...headerStyles, title: 'Alarm'}}
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

export const headerStyles = {
  headerStyle: {
    elevation: 0,
  },
  headerTintColor: '#000',
  headerTitleStyle: {
    fontWeight: 'bold',
  },
};

const styles = StyleSheet.create({
  button: {
    backgroundColor: 'transparent',
    padding: 10
  },
  buttonText: {
    color: 'black',
    fontWeight: 'bold',
    fontSize: 25
  },
});
