DROP TABLE IF EXISTS hits CASCADE;

CREATE SCHEMA IF NOT EXISTS ewm_stat;

CREATE TABLE IF NOT EXISTS ewm_stat.hits
(
    id          int8         NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    app         varchar(255) NOT NULL,
    ip          varchar(255) NOT NULL,
    "timestamp" timestamp    NOT NULL,
    uri         varchar(255) NOT NULL,
    CONSTRAINT hits_pkey PRIMARY KEY (id)
);
COMMENT ON TABLE ewm_stat.hits IS 'Статистика посещений';
COMMENT ON COLUMN ewm_stat.hits.app IS 'Идентификатор сервиса для которого записывается информация';
COMMENT ON COLUMN ewm_stat.hits.uri IS 'URI для которого был осуществлен запрос';
COMMENT ON COLUMN ewm_stat.hits.ip IS 'IP-адрес пользователя, осуществившего запрос';
COMMENT ON COLUMN ewm_stat.hits."timestamp" IS 'Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")';
