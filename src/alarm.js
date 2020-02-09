import { NativeModules } from 'react-native';
import uuid from 'uuid/v4';


const AlarmService = NativeModules.AlarmModule;

export function set (alarm) {
  AlarmService.set(alarm);
}

export function stop () {
  AlarmService.stop();
}

export function snooze () {
  AlarmService.snooze();
}

export function removeAll () {
  AlarmService.removeAll();
}

export async function getAll () {
  return AlarmService.getAll();
}

export default class Alarm {

  constructor (title, description, days, hour, minutes, repeating) {
    this.uid = uuid();
    this.title = title;
    this.description = description;
    this.days = days ? days.map(getAndroidDay) : [];
    this.hour = hour;
    this.minutes = minutes;
    this.repeating = repeating;
  }

  default () {
    this.title = "Alarm";
    this.description = "Wake up";
    this.days = [getAndroidDay(new Date().getDay() + 1)];
    this.hour = new Date().getHours();
    this.minutes = new Date().getMinutes();
    this.repeating = false;
  }

}

export function getAndroidDay (day) {
  return (day + 1) % 7;
}
