INSERT INTO voucher (code, redemption_limit, redeemed_count, expiry_date)
VALUES ('SINGLE', 1, 0, '2025-12-31T23:59:59');

INSERT INTO voucher (code, redemption_limit, redeemed_count, expiry_date)
VALUES ('MULTI', 0, 0, '2025-12-31T23:59:59');

INSERT INTO voucher (code, redemption_limit, redeemed_count, expiry_date)
VALUES ('3TIMES', 3, 0, '2025-12-31T23:59:59');

INSERT INTO voucher (code, redemption_limit, redeemed_count, expiry_date)
VALUES ('EXPIRED', 1, 0, '2024-01-01T00:00:00');

INSERT INTO voucher (code, redemption_limit, redeemed_count, expiry_date)
VALUES ('PARTIAL', 5, 2, '2025-12-31T23:59:59');