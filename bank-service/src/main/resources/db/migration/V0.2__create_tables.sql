create table auth_method
(
    id          bigint  not null
        constraint auth_method_pk
            primary key,
    description varchar not null
);

create table card
(
    id                 bigint                not null
        constraint card_pk
            primary key,
    card_number        varchar(16)           not null,
    preferred_auth_id  bigint                not null
        constraint card_auth_method_id_fk
            references auth_method,
    balance            numeric(19, 2)        not null,
    incorrect_attempts integer default 0,
    blocked            boolean default false not null,
    version            bigint                not null
);

create table card_credential
(
    id                 bigint  not null
        constraint card_credentials_pk
            primary key,
    card_id            bigint  not null
        constraint card_credentials_card_card_id_fk
            references card,
    secret             varchar not null,
    credential_type_id bigint  not null
        constraint card_credentials_auth_method_id_fk
            references auth_method
);
