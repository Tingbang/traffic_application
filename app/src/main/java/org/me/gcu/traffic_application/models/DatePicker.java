package org.me.gcu.traffic_application.models;

import com.google.android.material.picker.CalendarConstraints;
import com.google.android.material.picker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DatePicker {

    private TextInputEditText DateInput;
    private long StartDate;
    private long EndDate;
    private Calendar calendar;
    private final String dateFormat = "MMM, dd, yyyy";

    public DatePicker(TextInputEditText dateInput){
        dateInput = this.DateInput;
    }

    //Build android date picker

    public MaterialDatePicker build(){
        calendar =Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.StartDate = calendar.getTimeInMillis();
        this.calendar.roll(Calendar.MONTH,1);
        this.EndDate = calendar.getTimeInMillis();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        //constraintBuilder.setStart(StartDate);
        //constraintBuilder.setEnd(EndDate);

        //initialise

        MaterialDatePicker.Builder builds = MaterialDatePicker.Builder.datePicker();
        //builds.setTitleText("Please enter a valid Date");
        builds.setCalendarConstraints(constraintBuilder.build());
        //builds.setTheme()
       // builds.setSelection(this.StartDate);


        //Build the picker
        final MaterialDatePicker materialDatePicker = builds.build();
        return materialDatePicker;

    }


    public String Today_View_Roadworks(){
        //Set date format
        SimpleDateFormat sdfFormat = new SimpleDateFormat(this.dateFormat);
        sdfFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date today = Calendar.getInstance().getTime();
        return sdfFormat.format(today);

    }

    //Ensure the user can't select any dates that have been.
    public boolean before_date(long dateValidation){
        if(dateValidation > this.StartDate){
            return true;
        }else{
            //Throw warning at user.
            return false;
        }
    }

}
