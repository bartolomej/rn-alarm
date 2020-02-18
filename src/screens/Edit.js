import React, { useEffect, useState } from 'react';
import { StyleSheet, View } from 'react-native';
import Alarm, { removeAlarm, scheduleAlarm, updateAlarm } from '../alarm';
import TextInput from '../components/TextInput';
import DayPicker from '../components/DayPicker';
import TimePicker from '../components/TimePicker';
import Button from '../components/Button';
import { globalStyles } from '../global';
import SwitcherInput from '../components/SwitcherInput';


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

  async function onSave () {
    if (mode === 'EDIT') {
      alarm.active = true;
      await updateAlarm(alarm);
    }
    if (mode === 'CREATE') {
      await scheduleAlarm(alarm);
    }
    navigation.goBack();
  }

  async function onDelete () {
    await removeAlarm(alarm.uid);
    navigation.goBack();
  }

  if (!alarm) {
    return <View/>;
  }

  return (
    <View style={globalStyles.container}>
      <View style={[globalStyles.innerContainer, styles.container]}>
        <View styles={styles.inputsContainer}>
          <TimePicker
            onChange={(h, m) => update([['hour', h], ['minutes', m]])}
            hour={alarm.hour}
            minutes={alarm.minutes}
          />
          <TextInput
            description={'Title'}
            style={styles.textInput}
            onChangeText={v => update([['title', v]])}
            value={alarm.title}
          />
          <TextInput
            description={'Description'}
            style={styles.textInput}
            onChangeText={v => update([['description', v]])}
            value={alarm.description}
          />
          <SwitcherInput
            description={'Repeat'}
            value={alarm.repeating}
            onChange={v => update([['repeating', v]])}
          />
          {alarm.repeating && (
            <DayPicker
              onChange={v => update([['days', v]])}
              activeDays={alarm.days}
            />
          )}
        </View>
        <View style={styles.buttonContainer}>
          {mode === 'EDIT' && (
            <Button
              onPress={onDelete}
              title={'Delete'}
            />
          )}
          <Button
            fill={true}
            onPress={onSave}
            title={'Save'}
          />
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    justifyContent: 'space-around',
    alignItems: 'center',
    height: '100%',
  },
  inputsContainer: {
    width: '100%',
  },
  buttonContainer: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-around',
  },
});
