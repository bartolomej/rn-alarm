import React, { useState } from 'react';
import { TouchableOpacity, Platform, View, Text, StyleSheet, StatusBar } from 'react-native';
import DateTimePicker from '@react-native-community/datetimepicker';
import Alarm, {set, getAll, stop, snooze, removeAll, getAndroidDay} from './alarm';


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
        <Button onPress={() => {
          const alarm = new Alarm();
          alarm.default();
          alarm.days = [getAndroidDay(new Date().getDay())];
          alarm.minutes = new Date().getMinutes() + 1;
          set(alarm)
        }} title="SET ALARM"/>
        <Button onPress={stop} title="STOP ALARM"/>
        <Button onPress={snooze} title="SNOOZE ALARM"/>
        <Button onPress={removeAll} title="REMOVE ALARMS"/>
        <Button onPress={async () => {
          console.log(await getAll())
        }} title="GET ALARMS"/>
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
