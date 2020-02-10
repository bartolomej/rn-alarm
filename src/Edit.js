import React, { useEffect, useState } from 'react';
import { StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import Alarm, { removeAlarm, scheduleAlarm, updateAlarm } from './alarm';
import DayPicker from './components/DayPicker';
import TimePicker from './components/TimePicker';


export default function ({ route, navigation }) {
  const [alarm, setAlarm] = useState(Alarm.getEmpty);
  const [mode, setMode] = useState(null);

  useEffect(() => {
    if (route.params && route.params.alarm) {
      setAlarm(new Alarm(route.params.alarm));
      setMode('EDIT');
    } else {
      setAlarm(new Alarm());
      setMode('CREATE');
    }
  }, []);

  function update (updates) {
    const a = Object.assign({}, alarm);
    for (let u of updates) {
      a[u[0]] = u[1];
    }
    setAlarm(a);
  }

  return (
    <View style={styles.container}>
      <View style={styles.innerContainer}>
        <TimePicker
          onChange={(h, m) => update([['hour', h], ['minutes', m]])}
          hour={alarm.hour}
          minutes={alarm.minutes}
        />
        <TextInput
          onChangeText={value => update([['title', value]])}
          value={alarm.title}
        />
        <TextInput
          onChangeText={value => update([['description', value]])}
          value={alarm.description}
        />
        <DayPicker
          onChange={value => update([['days', value]])}
          activeDays={alarm.days}
        />
        <Button
          onPress={() => {
            if (mode === 'EDIT') {
              updateAlarm(alarm);
            }
            if (mode === 'CREATE') {
              scheduleAlarm(alarm);
            }
            navigation.goBack();
          }}
          title={'Save'}
        />
        {mode === 'EDIT' && (
          <Button
            onPress={() => {
              removeAlarm(alarm.uid);
              navigation.goBack();
            }}
            title={'Delete'}
          />
        )}
      </View>
    </View>
  );
}

function Button ({ onPress, title }) {
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
  container: {
    height: '100%',
    width: '100%',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  innerContainer: {
    width: '90%',
    height: '90%',
    display: 'flex',
    alignItems: 'center',
  },
  button: {
    backgroundColor: 'transparent',
  },
  buttonText: {
    color: 'black',
  },
});
