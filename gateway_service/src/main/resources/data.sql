INSERT INTO users(id, account_non_expired, account_non_locked, credentials_non_expired, email,
                  enabled, firstname, lastname, password, register_date, username)
VALUES (1, true, true, true, 'admin@admin.com', true, 'Admin', 'Admin', '0000', current_timestamp, 'Administrator')