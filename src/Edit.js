import React, { useEffect, useState } from 'react';
import { TextInput, View, Text, Switch } from 'react-native';
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
          onChangeText={v => update([['title', v]])}
          value={alarm.title}
        />
        <TextInput
          onChangeText={v => update([['description', v]])}
          value={alarm.description}
        />
        <DayPicker
          onChange={v => update([['days', v]])}
          activeDays={alarm.days}
        />
        <Switch
          ios_backgroundColor={'black'}
          trackColor={{
            false: 'blue',
            true: 'black',
          }}
          value={alarm.repeating}
          onValueChange={v => update([['repeating', v]])}/>
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
