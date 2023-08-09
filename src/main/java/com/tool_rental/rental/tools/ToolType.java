package com.tool_rental.rental.tools;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tool_types")
public class ToolType {

    @Id
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "daily_charge_cents", nullable = false)
    private int dailyChargeCents;
    @Column(name = "weekday_charge", nullable = false)
    private boolean weekdayCharge;
    @Column(name = "weekend_charge", nullable = false)
    private boolean weekendCharge;
    @Column(name = "holiday_charge", nullable = false)
    private boolean holidayCharge;

    public ToolType(String type,
                    int dailyChargeCents,
                    boolean weekdayCharge,
                    boolean weekendCharge,
                    boolean holidayCharge) {
        this.type = type;
        this.dailyChargeCents = dailyChargeCents;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }

    public ToolType() {
        // for hibernate
    }

    public String getTypeName() {
        return type;
    }

    public int getDailyChargeCents() {
        return dailyChargeCents;
    }

    public boolean hasWeekdayCharge() {
        return weekdayCharge;
    }

    public boolean hasWeekendCharge() {
        return weekendCharge;
    }

    public boolean hasHolidayCharge() {
        return holidayCharge;
    }
}

