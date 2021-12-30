create unique index card_card_number_uindex
    on card (card_number);

create unique index card_credentials_card_id_credential_type_uindex
    on card_credential (card_id, credential_type_id);
