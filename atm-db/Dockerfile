FROM postgres:9.6

COPY docker-entrypoint-initdb.d /docker-entrypoint-initdb.d

RUN chmod 755 docker-entrypoint-initdb.d/*.sh
