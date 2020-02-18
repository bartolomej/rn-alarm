import React, { useEffect, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { getAlarm, snoozeAlarm, stopAlarm } from '../alarm';
import Button from '../components/Button';
import { colors, globalStyles } from '../global';


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
    return <View/>;
  }

  return (
    <View style={globalStyles.container}>
      <View style={[globalStyles.innerContainer, styles.container]}>
        <View style={styles.textContainer}>
          <Text style={styles.clockText}>
            {alarm.getTimeString().hour} : {alarm.getTimeString().minutes}
          </Text>
          <Text style={styles.title}>{alarm.title}</Text>
        </View>
        <View style={styles.buttonContainer}>
          <Button
            title={'Snooze'}
            onPress={async () => {
              await snoozeAlarm();
              navigation.goBack();
            }}
          />
          <Button
            title={'Stop'}
            onPress={async () => {
              await stopAlarm();
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
  textContainer: {
    display: 'flex',
    alignItems: 'center'
  },
  buttonContainer: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around'
  },
  title: {
    fontWeight: 'bold',
    fontSize: 20,
    color: colors.GREY
  }
});
