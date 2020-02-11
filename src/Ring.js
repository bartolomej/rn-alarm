import React, { useEffect, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { getAlarm, snoozeAlarm, stopAlarm } from './alarm';
import Button from './components/Button';
import { globalStyles } from './global';


export default function ({ route, navigation }) {
  const [alarm, setAlarm] = useState(null);

  useEffect(() => {
    const alarmUid = route.params.alarmUid;
    (async function () {
      const alarm = await getAlarm(alarmUid);
      setAlarm(alarm);
    })();
  }, []);

  if (!alarm) {
    return <Text>Loading...</Text>;
  }

  return (
    <View style={globalStyles.container}>
      <View style={[globalStyles.innerContainer, styles.container]}>
        <Text style={styles.clockText}>
          {alarm.getTimeString().hour} : {alarm.getTimeString().minutes}
        </Text>
        <Text>{alarm.title}</Text>
        <View style={styles.buttonContainer}>
          <Button
            title={'Snooze'}
            onPress={() => {
              snoozeAlarm();
              navigation.goBack();
            }}
          />
          <Button
            title={'Stop'}
            onPress={() => {
              stopAlarm();
              navigation.goBack();
            }}
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
  },
  clockText: {
    color: 'black',
    fontWeight: 'bold',
    fontSize: 50,
  },
  buttonContainer: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around'
  },
});