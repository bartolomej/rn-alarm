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
@PrepareForTest({Utils.class})
public class AlarmTest {

    // Tue Jan 28 21:06:47 CET 2020
    private long mockDate = 1580242007891L;

    @Before
    public void before () {
        Calendar mockCalendar = Calendar.getInstance();
        mockCalendar.setTime(new Date(mockDate));
        PowerMockito.mockStatic(Calendar.class);
        Mockito.when(Calendar.getInstance()).thenReturn(mockCalendar);
    }

    @Test
    public void testAlarmInitialisation() throws CloneNotSupportedException {
        ArrayList<Integer> days = getDays(new int[]{1,3});

        Alarm a1 = new Alarm("1", days, 1, 0, "Test", "Test description", false);
        Alarm a2 = a1.clone();

        assertEquals(a1, a2);
    }

    @Test
    public void testAlarmDates () {
        ArrayList<Integer> days = getDays(new int[]{1,3});

        Alarm alarm = new Alarm("1", days, 1, 0, "Test", "Test description", false);
        Date[] dates = alarm.getDates();

        assertEquals(alarm.getRepeating(), AlarmType.REPEATING);
        assertEquals(Utils.getDate(1,1,0).getTime(), dates[0]);
        assertEquals(Utils.getDate(3,1,0).getTime(), dates[1]);
    }

    @Test
    public void testNonRepeatingAlarmDates () {
        ArrayList<Integer> days = getDays(new int[]{1});
        Alarm alarm = new Alarm("1", days, 1, 0, "Test", "Test description", false);

        assertEquals(AlarmType.SINGLE, alarm.getRepeating());
        assertEquals(1, alarm.getDates().length);
        assertEquals(Utils.getDate(1, 1, 0).getTime(), alarm.getDates()[0]);
    }

    private static ArrayList<Integer> getDays (int[] days) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int d : days) arrayList.add(d);
        return arrayList;
    }

}
