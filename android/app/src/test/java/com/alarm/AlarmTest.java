package com.alarm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Helper.class})
public class AlarmTest {

    // Tue Jan 28 21:06:47 CET 2020
    private long mockTimestamp = 1580242007891L;
    private Calendar mockDate;

    @Before
    public void before () {
        Calendar mockCalendar = Calendar.getInstance();
        mockCalendar.setTimeInMillis(mockTimestamp);
        mockDate = mockCalendar;
        PowerMockito.mockStatic(Calendar.class);
        Mockito.when(Calendar.getInstance()).thenReturn(mockCalendar);
    }

    @Test
    public void testAlarmInitialisation() throws CloneNotSupportedException {
        ArrayList<Integer> days = getDays(new int[]{1,3});

        Alarm a1 = new Alarm("1", days, 1, 0, 1,"Test", "Test description", false, true);
        Alarm a2 = a1.clone();

        assertEquals(a1, a2);
    }

    @Test
    public void testAlarmDates () {
        ArrayList<Integer> days = getDays(new int[]{1,3});

        Alarm alarm = new Alarm("1", days, 1, 0, 1, "Test", "Test description", false, true);
        Date[] dates = alarm.getDates();

        // TODO: fix tests returning invalid mocked date
        assertEquals(addDays(getDate(26,0, 1,0), 7).getTime(), dates[0]);
        assertEquals(getDate(28,0, 1,0).getTime(), dates[1]);
    }

    @Test
    public void testNonRepeatingAlarmDates () {
        ArrayList<Integer> days = getDays(new int[]{1});
        Alarm alarm = new Alarm("1", days, 1, 0, 1, "Test", "Test description", false, true);

        assertEquals(1, alarm.getDates().length);
        assertEquals(Helper.getDate(1, 1, 0).getTime(), alarm.getDates()[0]);
    }

    private static ArrayList<Integer> getDays (int[] days) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int d : days) arrayList.add(d);
        return arrayList;
    }

    private static Calendar getDate (int dayOfMonth, int month, int hour, int minute) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        date.set(Calendar.SECOND, 0);
        return date;
    }

    private static Calendar addDays (Calendar c, int n) {
        Calendar calendar = (Calendar)c.clone();
        calendar.add(Calendar.DATE, n);
        return calendar;
    }

}
