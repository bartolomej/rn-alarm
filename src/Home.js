import { Text, View } from 'react-native';
import { getAlarmState, getAllAlarms, disableAlarm, enableAlarm } from './alarm';
import AlarmView from './components/AlarmView';
import React, { useEffect, useState } from 'react';
import { globalStyles } from './global';


export default function ({ navigation }) {
  const [alarms, setAlarms] = useState(null);

  useEffect(() => {
    navigation.addListener('focus', async () => {
      setAlarms(await getAllAlarms());
    });
    (async function () {
      const activeAlarm = await getAlarmState();
      if (activeAlarm) {
        navigation.navigate('Ring', { alarmUid: activeAlarm });
      }
      setAlarms(await getAllAlarms());
    }());
  }, []);

  return (
    <View style={globalStyles.container}>
      <View style={globalStyles.innerContainer}>
        {alarms && alarms.length === 0 && (
          <Text>No alarms</Text>
        )}
        {alarms && alarms.map(a => (
          <AlarmView
            key={a.uid}
            uid={a.uid}
            onChange={active => {
              if (active) enableAlarm(a.uid);
              else disableAlarm(a.uid);
            }}
            onPress={() => navigation.navigate('Edit', { alarm: a })}
            title={a.title}
            hour={a.hour}
            minutes={a.minutes}
            days={a.days}
            active={a.active}
          />
        ))}
      </View>
    </View>
  );
}
