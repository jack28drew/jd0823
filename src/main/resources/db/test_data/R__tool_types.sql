INSERT INTO tool_types
    (type, daily_charge_cents, weekday_charge, weekend_charge, holiday_charge)
VALUES
    ('LADDER', 199, true, true, false),
    ('CHAINSAW', 149, true, false, true),
    ('JACKHAMMER', 299, true, false, false)
ON CONFLICT DO NOTHING;