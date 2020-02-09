import { NativeModules } from 'react-native';
const AlarmService = NativeModules.AlarmModule;

export function toast (message) {
  AlarmService.toast(message);
}
