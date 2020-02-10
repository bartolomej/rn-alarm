import Alarm from '../src/alarm';


it('should convert to Android object', function () {
  const alarm = new Alarm();
  alarm.days = [0,1,2,3,4,5,6];
  const androidAlarm = alarm.toAndroid();
  expect(androidAlarm.days).toEqual([1,2,3,4,5,6,0]);
});
