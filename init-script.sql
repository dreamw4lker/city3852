create table stations (
	id serial,
	station_id int,
	station_name text,
	station_type int,
	latitude varchar(20),
	longtitude varchar(20),
	
	constraint stations_id primary key(id)
) with (OIDS = false);

create table cookie_entity (
	id serial,
	cookie_key varchar not null,
	cookie_value varchar null,
	cookie_date timestamp,

	constraint cookie_entity_id primary key(id)
) with (OIDS = false);