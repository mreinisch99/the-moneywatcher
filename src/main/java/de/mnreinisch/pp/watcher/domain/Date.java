package de.mnreinisch.pp.watcher.domain;

import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Embeddable
public class Date implements Cloneable, Serializable, Comparable<Date> {
    public static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy";
    public static final String DATE_FORMAT_PATTERN_SQL = "yyyy-MM-dd";

    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private java.util.Date date;

    public Date(LocalDate date){
        if(date == null) throw new NullPointerException("Please provide a date");
        this.date = date.toDateTimeAtStartOfDay().toDate();
    }

    public Date(String date){
        if(date == null) throw new NullPointerException("Please provide a date");
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
            this.date = sdf.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(MessageFormat.format("{0} isn't a valid date!", date));
        }
    }

    @Override
    public int compareTo(Date o) {
        return this.date.compareTo(o.date);
    }

    public String getDateAsString() {
        return convertDateToString(this.date);
    }

    public LocalDate getDateAsLD() {
        return this.date == null ? null : new LocalDate(this.date);
    }

    public java.util.Date getDateAsDate() {
        return this.date;
    }

    public static String convertDateToString(java.util.Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        return sdf.format(date);
    }
}
