INSERT INTO tools
    (code, type, brand)
VALUES
    ('CHNS', 'CHAINSAW', 'STIHL'),
    ('LADW', 'LADDER', 'WERNER'),
    ('JAKD', 'JACKHAMMER', 'DEWALT'),
    ('JAKR', 'JACKHAMMER', 'RIDGID')
ON CONFLICT DO NOTHING;
