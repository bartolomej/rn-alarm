import React, { useState } from 'react';
import { TouchableOpacity, Platform, View, Text, StyleSheet, StatusBar } from 'react-native';
import DateTimePicker from '@react-native-community/datetimepicker';
import {toast} from './alarm';


const App = () => {
  const [date, setDate] = useState(null);
  const [showPicker, setShowPicker] = useState(false);

  const onChange = (event, selectedDate) => {
    const currentDate = selectedDate || date;

    setDate(currentDate);
    setShowPicker(Platform.OS === 'ios');
  };

  const onShowPicker = () => {
    if (!date) setDate(new Date());
    setShowPicker(true);
  };

  return (
    <View style={styles.container}>
      <StatusBar barStyle="light-content"/>
      <View>
        <Text>{date ? date.toLocaleString() : 'No alarm!'}</Text>
        <Button onPress={onShowPicker} title="SET ALARM"/>
        <Button onPress={() => toast("HELLO WORLD")} title="TOAST"/>
      </View>
      {showPicker && (
        <DateTimePicker
          testID="dateTimePicker"
          timeZoneOffsetInMinutes={0}
          value={date}
          mode={'time'}
          is24Hour={true}
          display="default"
          onChange={onChange}
        />
      )}
    </View>
  );
};

function Button ({onPress, title}) {
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
  container: {
    height: '100%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center'
  },
  button: {
    backgroundColor: 'transparent',
  },
  buttonText: {
    color: 'black',
  }
});

export default App;
