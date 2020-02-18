import { NativeModules } from 'react-native';
import uuid from 'uuid/v4';
import React from 'react';


const AlarmService = NativeModules.AlarmModule;

export async  function scheduleAlarm (alarm) {
  if (!(alarm instanceof Alarm)) {
    alarm = new Alarm(alarm);
  }
  await AlarmService.set(alarm.toAndroid());
  console.log('scheduling alarm: ', JSON.stringify(alarm))
}

export async function enableAlarm (uid) {
  await AlarmService.enable(uid);
}

export async function disableAlarm (uid) {
  await AlarmService.disable(uid);
}

export async function stopAlarm () {
  await AlarmService.stop();
}

export async function snoozeAlarm () {
  await AlarmService.snooze();
}

export async function removeAlarm (uid) {
  await AlarmService.remove(uid);
}

export async function updateAlarm (alarm) {
  if (!(alarm instanceof Alarm)) {
    alarm = new Alarm(alarm);
  }
  await AlarmService.update(alarm.toAndroid());
}

export async function removeAllAlarms () {
  await AlarmService.removeAll();
}

export async function getAllAlarms () {
  const alarms = await AlarmService.getAll();
  return alarms.map(a => Alarm.fromAndroid(a));
}

export async function getAlarm (uid) {
  const alarm = await AlarmService.get(uid);
  return Alarm.fromAndroid(alarm)
}

export async function getAlarmState () {
  return AlarmService.getState();
}

export default class Alarm {

  constructor (params = null) {
    this.uid = getParam(params, 'uid', uuid());
    this.enabled = getParam(params, 'enabled', true);
    this.title = getParam(params, 'title', 'Alarm');
    this.description = getParam(params, 'description', 'Wake up');
    this.hour = getParam(params, 'hour', new Date().getHours());
    this.minutes = getParam(params, 'minutes', new Date().getMinutes() + 1);
    this.snoozeInterval = getParam(params, 'snoozeInterval', 1);
    this.repeating = getParam(params, 'repeating', false);
    this.active = getParam(params, 'active', true);
    this.days = getParam(params, 'days', [new Date().getDay()]);
  }

  static getEmpty () {
    return new Alarm({
      title: '',
      description: '',
      hour: 0,
      minutes: 0,
      repeating: false,
      days: [],
    });
  }

  toAndroid () {
    return {
      ...this,
      days: toAndroidDays(this.days)
    }
  }

  static fromAndroid (alarm) {
    alarm.days = fromAndroidDays(alarm.days);
    return new Alarm(alarm);
  }

  getTimeString () {
    const hour = this.hour < 10 ? '0' + this.hour : this.hour;
    const minutes = this.minutes < 10 ? '0' + this.minutes : this.minutes;
    return { hour, minutes };
  }

  getTime () {
    const timeDate = new Date();
    timeDate.setMinutes(this.minutes);
    timeDate.setHours(this.hour);
    return timeDate;
  }

}

function getParam (param, key, defaultValue) {
  try {
    if (param && (param[key] !== null || param[key] !== undefined)) {
      return param[key];
    } else {
      return defaultValue;
    }
  } catch (e) {
    return defaultValue;
  }
}

export function toAndroidDays (daysArray) {
  return daysArray.map(day => (day + 1) % 7);
}

export function fromAndroidDays (daysArray) {
  return daysArray.map(d => d === 0 ? 6 : d - 1);
}
