package com.sfeng.ontime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirby on 2016-02-05.
 */
public class record {
    private String description;
    private List<Integer> records;


    public record(String desc) {
        description = desc;
        records = new ArrayList<Integer>();
    }

    public void add(int time) {
        records.add(time);
    }

    public void remove(int index) {
        records.remove(index);
    }

    public float average() {
        float sum = 0;
        for (int i=0; i<records.size(); i++) {
            sum = sum + records.get(i);
        }
        return sum / records.size();
    }

    private void updateTimer (float time) {
        long secs, mins, hrs;
        String seconds, minutes, hours, milliseconds;
        secs = (long) (time / 1000);
        mins = (long) ((time / 1000) / 60);
        hrs = (long) (((time / 1000) / 60) / 60);

		/* Convert the seconds to String
		 * and format to ensure it has
		 * a leading zero when required
		 */
        secs = secs % 60;
        seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        }

		/* Convert the minutes to String and format the String */

        mins = mins % 60;
        minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        }

    	/* Convert the hours to String and format the String */

        hours = String.valueOf(hrs);
        if (hrs == 0) {
            hours = "00";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours;
        }

    	/* Although we are not using milliseconds on the timer in this example
    	 * I included the code in the event that you wanted to include it on your own
    	 */
        milliseconds = String.valueOf((long) time);
        if (milliseconds.length() == 2) {
            milliseconds = "0" + milliseconds;
        }
        if (milliseconds.length() <= 1) {
            milliseconds = "00" + milliseconds;
        }
        milliseconds = milliseconds.substring(milliseconds.length() - 3, milliseconds.length() - 2);

		/* Setting the timer text to the elapsed time */
        String ret = hours + ":" + minutes + ":" + seconds + "." + milliseconds;
    }
}
