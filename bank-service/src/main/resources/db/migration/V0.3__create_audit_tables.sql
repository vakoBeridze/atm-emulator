create table revinfo
(
    rev      integer not null primary key,
    revtstmp bigint
);

create table auth_method_aud
(
    id          bigint  not null,
    rev         integer not null
        constraint fksmb0fbm5iw86l9swyqwm7w3gx
            references revinfo,
    revtype     smallint,
    description varchar(255),
    primary key (id, rev)
);

create table card_aud
(
    id                bigint  not null,
    rev               integer not null
        constraint fka3m9sqpy9cfw8y74egtvlixbu
            references revinfo,
    revtype           smallint,
    balance           numeric(19, 2),
    card_number       varchar(255),
    preferred_auth_id bigint,
    primary key (id, rev)
);


create table card_credential_aud
(
    id                 bigint  not null,
    rev                integer not null
        constraint fkqxbq1jc5n6il3hxth3howo7y
            references revinfo,
    revtype            smallint,
    secret             varchar(255),
    card_id            bigint,
    credential_type_id bigint,
    primary key (id, rev)
);
