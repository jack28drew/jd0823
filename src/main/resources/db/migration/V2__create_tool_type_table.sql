CREATE TABLE tool_types (
    type               TEXT UNIQUE NOT NULL,
    daily_charge_cents INTEGER     NOT NULL,
    weekday_charge     BOOLEAN     NOT NULL,
    weekend_charge     BOOLEAN     NOT NULL,
    holiday_charge     BOOLEAN     NOT NULL
)