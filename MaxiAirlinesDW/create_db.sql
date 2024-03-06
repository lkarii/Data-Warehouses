DROP TABLE IF EXISTS Lot;
DROP TABLE IF EXISTS Awaria;
DROP TABLE IF EXISTS Samolot;
DROP TABLE IF EXISTS SerwisSamolotowy;
DROP TABLE IF EXISTS Trasa;
DROP TABLE IF EXISTS AwariaJunk;
DROP TABLE IF EXISTS LotJunk;
DROP TABLE IF EXISTS Data;
DROP TABLE IF EXISTS Czas;

CREATE TABLE Data(
	id_data INTEGER IDENTITY(1,1) PRIMARY KEY,
	rok INTEGER NOT NULL,
	miesiac VARCHAR(9) NOT NULL,
	dzien INTEGER NOT NULL,
	czy_dzien_roboczy VARCHAR(64) NOT NULL,
	czy_swieto VARCHAR(64) NULL,
	dzien_tygodnia INTEGER NOT NULL 
);

CREATE TABLE Czas(
	id_czas INTEGER IDENTITY(1,1) PRIMARY KEY,
	godzina INTEGER NOT NULL,
	minuta INTEGER NOT NULL,
	sekunda INTEGER NOT NULL,
	pora_dnia varchar(17) NOT NULL
);

CREATE TABLE Samolot(
	id_samolot INTEGER IDENTITY(1,1) PRIMARY KEY,
	numer_seryjny VARCHAR(16) NOT NULL,
	producent VARCHAR(32) NOT NULL,
	model VARCHAR(32) NOT NULL,
	calkowita_waga VARCHAR(24) NOT NULL,
	liczba_miejsc VARCHAR(19) NOT NULL,
	koszt_zakupu VARCHAR(19) NOT NULL,
	czy_aktualny BIT NOT NULL,
	fk_data_produkcji INTEGER NOT NULL,
	FOREIGN KEY (fk_data_produkcji) REFERENCES Data (id_data)
);

CREATE TABLE SerwisSamolotowy(
	id_serwis INTEGER IDENTITY(1,1) PRIMARY KEY,
	nazwa_serwisu VARCHAR(64) NOT NULL UNIQUE,
	miasto VARCHAR(32) NOT NULL,
	ulica VARCHAR(64) NOT NULL,
	numer_budynku VARCHAR(10) NOT NULL,
	kod_pocztowy VARCHAR(16) NOT NULL
);

CREATE TABLE Trasa(
	id_trasy INTEGER IDENTITY(1,1) PRIMARY KEY,
	miasto_wylotu VARCHAR(32) NOT NULL,
	miasto_przylotu VARCHAR(32) NOT NULL
);

CREATE TABLE AwariaJunk(
	id_awaria_junk INTEGER IDENTITY(1,1) PRIMARY KEY,
	przyczyna_awarii VARCHAR(48) NOT NULL,
	opis_naprawy VARCHAR(64) NOT NULL
);

CREATE TABLE LotJunk(
	id_lot_junk INTEGER IDENTITY(1,1) PRIMARY KEY,
	przyczyna_opoznienia  VARCHAR(22) NOT NULL,
	status_lotu VARCHAR(10) NOT NULL,
	srednia_ocena_klienta VARCHAR(36) NOT NULL
);

CREATE TABLE Awaria(
	id_data_awarii INTEGER NOT NULL,
	FOREIGN KEY (id_data_awarii) REFERENCES Data (id_data),
	id_czas_awarii INTEGER NOT NULL,
	FOREIGN KEY (id_czas_awarii) REFERENCES Czas (id_czas),
	id_samolot INTEGER NOT NULL,
	FOREIGN KEY (id_samolot) REFERENCES Samolot (id_samolot),
	id_data_naprawy INTEGER NOT NULL,
	FOREIGN KEY (id_data_naprawy) REFERENCES Data (id_data),
	id_czas_naprawy INTEGER NOT NULL,
	FOREIGN KEY (id_czas_naprawy) REFERENCES Czas (id_czas),
	id_serwis INTEGER NOT NULL,
	FOREIGN KEY (id_serwis) REFERENCES SerwisSamolotowy (id_serwis),
	id_awaria_junk INTEGER NOT NULL,
	FOREIGN KEY (id_awaria_junk) REFERENCES AwariaJunk (id_awaria_junk),
	calkowity_koszt_naprawy INTEGER NOT NULL,
	rzeczywisty_koszt_naprawy FLOAT NOT NULL,
	PRIMARY KEY (id_data_awarii, id_czas_awarii, id_samolot, id_data_naprawy, id_czas_naprawy, id_serwis, id_awaria_junk)
);

CREATE TABLE Lot(
	numer_lotu varchar(16) NOT NULL,
	id_samolot INTEGER NOT NULL,
	FOREIGN KEY (id_samolot) REFERENCES Samolot (id_samolot),
	id_trasy INTEGER NOT NULL,
	FOREIGN KEY (id_trasy) REFERENCES Trasa (id_trasy),
	id_data_wylotu INTEGER NOT NULL,
	FOREIGN KEY (id_data_wylotu) REFERENCES Data (id_data),
	id_czas_wylotu INTEGER NOT NULL,
	FOREIGN KEY (id_czas_wylotu) REFERENCES Czas (id_czas),
	id_data_przylotu INTEGER NOT NULL,
	FOREIGN KEY (id_data_przylotu) REFERENCES Data (id_data),
	id_czas_przylotu INTEGER NOT NULL,
	FOREIGN KEY (id_czas_przylotu) REFERENCES Czas (id_czas),
	id_lot_junk INTEGER NOT NULL,
	FOREIGN KEY (id_lot_junk) REFERENCES LotJunk (id_lot_junk),
	opoznienie_min INTEGER NOT NULL,
	dystans INTEGER NOT NULL,
	PRIMARY KEY (id_samolot, id_trasy, id_data_wylotu, id_czas_wylotu, id_data_przylotu, id_czas_przylotu, id_lot_junk)
);

