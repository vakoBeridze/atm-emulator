INSERT INTO auth_method (id, description)
VALUES (1, 'PIN');
INSERT INTO auth_method (id, description)
VALUES (2, 'FINGERPRINT');

INSERT INTO card (id, card_number, preferred_auth_id, balance)
VALUES (1, '1111222233334444', 1, 11.22);
INSERT INTO card (id, card_number, preferred_auth_id, balance)
VALUES (3, '4444333322221111', 2, 0.00);
INSERT INTO card (id, card_number, preferred_auth_id, balance)
VALUES (2, '1234567812345678', 1, 1001.01);

INSERT INTO card_credential (id, card_id, secret, credential_type_id)
VALUES (1, 1, '1111', 1);
INSERT INTO card_credential (id, card_id, secret, credential_type_id)
VALUES (2, 1, '12345678', 2);
INSERT INTO card_credential (id, card_id, secret, credential_type_id)
VALUES (3, 2, '1234', 1);