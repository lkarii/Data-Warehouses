DROP TABLE IF EXISTS Naprawa;
DROP TABLE IF EXISTS SerwisSamolotowy;
DROP TABLE IF EXISTS Opoznienie;
DROP TABLE IF EXISTS Lot;
DROP TABLE IF EXISTS Awaria;
DROP TABLE IF EXISTS Samolot;

CREATE TABLE Samolot(
	id_samolot INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	numer_seryjny VARCHAR(64) UNIQUE NOT NULL
);

CREATE TABLE SerwisSamolotowy(
	id_serwis INTEGER IDENTITY(1,1) PRIMARY KEY,
	nazwa_serwisu VARCHAR(64) NOT NULL UNIQUE,
	opis VARCHAR(254),
	miasto VARCHAR(32) NOT NULL,
	ulica VARCHAR(64) NOT NULL,
	numer_budynku VARCHAR(10) NOT NULL,
	kod_pocztowy VARCHAR(16) NOT NULL
);

CREATE TABLE Lot(
	id_lot INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	fk_samolot INT NOT NULL,
	numer_lotu VARCHAR(32) NOT NULL,
	data_wylotu DATETIME NOT NULL,
	data_przylotu DATETIME NOT NULL,
	miasto_wylotu VARCHAR(32) NOT NULL,
	miasto_przylotu VARCHAR(32) NOT NULL,
	odwolany BIT NOT NULL,
	FOREIGN KEY (fk_samolot) REFERENCES Samolot (id_samolot)
);

CREATE TABLE Awaria(
	id_awaria INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	fk_samolot INT NOT NULL,
	data_awarii DATETIME NOT NULL,
	przyczyna VARCHAR(254) NOT NULL,
	status VARCHAR(16) NOT NULL,
	FOREIGN KEY (fk_samolot) REFERENCES Samolot (id_samolot)
);

CREATE TABLE Opoznienie(
	id_opoznienie INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	fk_lot INT NOT NULL,
	fk_awaria INT,
	przyczyna VARCHAR(254) NOT NULL,
	opis_przyczyny VARCHAR(254),
	czas_opoznienia INT NOT NULL,
	FOREIGN KEY (fk_lot) REFERENCES Lot (id_lot),
	FOREIGN KEY (fk_awaria) REFERENCES Awaria (id_awaria)
);

CREATE TABLE Naprawa(
	id_naprawa INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	fk_awaria INT NOT NULL,
	fk_serwis INT NOT NULL,
	data_naprawy DATETIME NOT NULL,
	opis VARCHAR(254) NOT NULL,
	koszt_laczny INT NOT NULL,
	FOREIGN KEY (fk_awaria) REFERENCES Awaria (id_awaria),
	FOREIGN KEY (fk_serwis) REFERENCES SerwisSamolotowy (id_serwis)
);