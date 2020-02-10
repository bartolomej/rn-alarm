import { StyleSheet, Text, View } from 'react-native';
import { getAllAlarms } from './alarm';
import AlarmView from './components/AlarmView';
import React, { useEffect, useState } from 'react';


export default function ({ navigation }) {
  const [alarms, setAlarms] = useState(null);

  useEffect(() => {
    (async function () {
      const alarms = await getAllAlarms();
      setAlarms(alarms);
    }());
  }, []);

  return (
    <View style={styles.container}>
      <View style={styles.innerContainer}>
        {alarms && alarms.length === 0 && (
          <Text>No alarms</Text>
        )}
        {alarms && alarms.map(a => (
          <AlarmView
            key={a.uid}
            uid={a.uid}
            onPress={() => navigation.navigate('Edit', { alarm: a })}
            title={a.title}
            hour={a.hour}
            minutes={a.minutes}
            days={a.days}
            enabled={a.enabled}
          />
        ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    height: '100%',
    width: '100%',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center'
  },
  innerContainer: {
    width: '90%',
    height: '90%',
    display: 'flex',
    alignItems: 'center',
  }
});
