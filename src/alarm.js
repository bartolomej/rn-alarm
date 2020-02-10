import { NativeModules } from 'react-native';
import uuid from 'uuid/v4';


const AlarmService = NativeModules.AlarmModule;

export function scheduleAlarm (alarm) {
  AlarmService.set(alarm);
}

export function stopAlarm () {
  AlarmService.stop();
}

export function snoozeAlarm () {
  AlarmService.snooze();
}

export function removeAlarm (uid) {
  AlarmService.remove(uid);
}

export function updateAlarm (alarm) {
  AlarmService.update(alarm);
}

export function removeAllAlarms () {
  AlarmService.removeAll();
}

export async function getAllAlarms () {
  return AlarmService.getAll();
}

export default class Alarm {

  constructor (params = null) {
    this.uid = getParam(params, 'uid', uuid());
    this.enabled = getParam(params, 'enabled', true);
    this.title = getParam(params, 'title', 'Alarm');
    this.description = getParam(params, 'description', 'Wake up');
    this.hour = getParam(params, 'hour', new Date().getHours());
    this.minutes = getParam(params, 'minutes', new Date().getMinutes());
    this.repeating = getParam(params, 'repeating', false);
    this.days = toAndroidDays(getParam(params, 'days', [new Date().getDay()]));
  }

  static getEmpty () {
    return new Alarm({
      title: '',
      description: '',
      hour: 0,
      minutes: 0,
      repeating: false,
      days: []
    })
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
    if (param && param[key]) return param[key];
    else return defaultValue;
  } catch (e) {
    return defaultValue;
  }
}

export function toAndroidDays (daysArray) {
  return daysArray.map(day => (day + 1) % 7);
}
