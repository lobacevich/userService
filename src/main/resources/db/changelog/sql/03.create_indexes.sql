CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_name_surname ON users(name, surname);

CREATE INDEX idx_payment_cards_user_id ON payment_cards(user_id);
CREATE INDEX idx_payment_cards_expiration ON payment_cards(expiration_date);