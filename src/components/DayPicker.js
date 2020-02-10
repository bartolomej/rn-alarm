import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import React, { useState } from 'react';

export default function ({ activeDays = [], description, onChange = () => null }) {
  // NOTICE: days doesn't change if prop activeDays changes
  const [days, setDays] = useState(activeDays);

  function onDayChange (dayIndex) {
    let selected = getSelected(days);
    selected[dayIndex] = !selected[dayIndex];
    const newDays = getDays(selected);
    setDays(newDays);
    onChange(newDays);
  }

  return (
    <View style={styles.container}>
      <Text style={styles.description}>{description}</Text>
      <View style={styles.innerContainer}>
        {getSelected(days).map((isSelected, index) => (
          <Day
            key={index}
            isActive={isSelected}
            dayIndex={index}
            onUpdate={onDayChange}
          />
        ))}
      </View>
    </View>
  );
}

function Day ({ isActive, dayIndex, onUpdate }) {
  return (
    <TouchableOpacity onPress={() => onUpdate(dayIndex)}>
      <Text style={isActive ? styles.selected : styles.unselected}>
        {getDay(dayIndex)}
      </Text>
    </TouchableOpacity>
  );
}

export function getSelected (activeDays) {
  let selected = new Array(7).fill(false);
  for (let i = 0; i < activeDays.length; i++) {
    selected[activeDays[i]] = true;
  }
  return selected;
}

export function getDays (selected) {
  let activeDays = [];
  for (let i = 0; i < selected.length; i++) {
    if (selected[i]) activeDays.push(i);
  }
  return activeDays;
}

function getDay (number) {
  let weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  return weekdays[number];
}

const styles = StyleSheet.create({
  container: {
    display: 'flex',
    alignItems: 'center'
  },
  innerContainer: {
    flexDirection: 'row',
  },
  description: {
    marginLeft: 10,
    textAlign: 'left',
    fontWeight: '200',
    fontSize: 13,
    color: 'black',
  },
  selected: {
    color: 'black',
    fontWeight: 'bold',
    margin: 12,
  },
  unselected: {
    color: 'grey',
    margin: 12,
  },
});
