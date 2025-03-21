create schema if not exists analytics_data;

-- создание таблицы instrument_historic_candle
set time zone 'UTC';
create table if not exists analytics_data.instrument_historic_candle
(
    id              uuid primary key,
    instrument_id   varchar(255),
    time            timestamp with time zone,
    candle_interval varchar(50),
    candle_source   varchar(50),
    open_price      numeric(19, 9),
    close_price     numeric(19, 9),
    high_price      numeric(19, 9),
    low_price       numeric(19, 9)
);

