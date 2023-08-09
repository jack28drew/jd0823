package com.tool_rental.time;

import com.tool_rental.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DateClassificationService {

    public ClassifiedDates classifyDatesInRange(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate.plusDays(1))  // add a day to the end date since we need that date and datesUntil() is end-date exclusive
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(this::getDateClassification, Collectors.toSet()),  // first group the dates by their classification
                        classifiedDateMap -> new ClassifiedDates(    // then unpack the map and insert the dates into a record
                                classifiedDateMap.getOrDefault(DateClassification.HOLIDAY, Collections.emptySet()),
                                classifiedDateMap.getOrDefault(DateClassification.WEEKDAY, Collections.emptySet()),
                                classifiedDateMap.getOrDefault(DateClassification.WEEKEND, Collections.emptySet())
                        )
                ));
    }

    private DateClassification getDateClassification(LocalDate date) {
        if (Holiday.isHoliday(date)) {
            return DateClassification.HOLIDAY;
        } else if (DateUtils.isWeekday(date)) {
            return DateClassification.WEEKDAY;
        } else {
            return DateClassification.WEEKEND;
        }
    }

    private enum DateClassification {
        HOLIDAY,
        WEEKDAY,
        WEEKEND
    }

    // This helps us avoid passing around a map; this helps mitigate some of the safety concerns that come with them
    // while making it clearer what the data contained within represents
    public record ClassifiedDates(Set<LocalDate> holidays,
                                  Set<LocalDate> weekdays,
                                  Set<LocalDate> weekends) {
        public ClassifiedDates {
            Objects.requireNonNull(holidays);
            Objects.requireNonNull(weekdays);
            Objects.requireNonNull(weekends);
        }
    }
}
