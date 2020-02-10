import React, { useEffect, useState } from 'react';
import { TextInput, View, Text } from 'react-native';
import Alarm, { removeAlarm, scheduleAlarm, updateAlarm } from './alarm';
import DayPicker from './components/DayPicker';
import TimePicker from './components/TimePicker';
import Button from './components/Button';
import { globalStyles } from './global';


export default function ({ route, navigation }) {
  const [alarm, setAlarm] = useState(null);
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

  if (!alarm) return <Text>Loading...</Text>;

  return (
    <View style={globalStyles.container}>
      <View style={globalStyles.innerContainer}>
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
