import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import React, { useState } from 'react';
import {colors} from '../global';


export default function ({ activeDays = [], description, onChange = () => null }) {
  // NOTICE: days doesn't change if prop activeDays changes
  const [days, setDays] = useState(activeDays);

  function onDayChange (dayIndex) {
    let selectedBtn = getSelected(days);
    selectedBtn[dayIndex] = !selectedBtn[dayIndex];
    const newDays = getDays(selectedBtn);
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
    <View style={{flex: 1}}>
      <TouchableOpacity
        style={[isActive ? styles.selectedBtn : styles.unselectedBtn, styles.btnContainer]}
        onPress={() => onUpdate(dayIndex)}>
        <Text style={styles.dayText}>
          {getDay(dayIndex)}
        </Text>
      </TouchableOpacity>
    </View>
  );
}

export function getSelected (activeDays) {
  let selectedBtn = new Array(7).fill(false);
  for (let i = 0; i < activeDays.length; i++) {
    selectedBtn[activeDays[i]] = true;
  }
  return selectedBtn;
}

export function getDays (selectedBtn) {
  let activeDays = [];
  for (let i = 0; i < selectedBtn.length; i++) {
    if (selectedBtn[i]) activeDays.push(i);
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
    alignItems: 'flex-start',
  },
  innerContainer: {
    display: 'flex',
    flexDirection: 'row'
  },
  description: {
    marginLeft: 10,
    textAlign: 'left',
    fontWeight: '200',
    fontSize: 13,
    color: 'black',
  },
  btnContainer: {
    width: 40,
    height: 40,
    borderRadius: 20,
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center'
  },
  selectedBtn: {
    color: 'black',
    fontWeight: 'bold',
    backgroundColor: colors.BLUE
  },
  unselectedBtn: {
    color: 'black',
    borderWidth: 1,
    borderColor: colors.GREY
  },
  dayText: {
    color: 'black'
  }
});
