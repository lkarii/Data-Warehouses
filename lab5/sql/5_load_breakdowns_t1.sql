USE [temp1_DB]

DROP TABLE IF EXISTS [temp1_DB].dbo.[TempAwaria];
CREATE TABLE TempAwaria(
	id INT IDENTITY(1,1) PRIMARY KEY,
	data_awarii DATETIME,
	przyczyna VARCHAR(64),
	samolot_numer_seryjny VARCHAR(64),
	data_naprawy DATETIME,
	opis VARCHAR(254),
	koszt_laczny INTEGER,
	nazwa_serwisu VARCHAR(64)
);

INSERT INTO [temp1_DB].dbo.[TempAwaria]
SELECT 
	b.data_awarii,
	b.przyczyna,
	(SELECT numer_seryjny FROM [smao_db].dbo.[Samolot] AS p WHERE b.fk_samolot = p.id_samolot),
	r.data_naprawy,
	r.opis,
	r.koszt_laczny,
	(SELECT s.nazwa_serwisu FROM [smao_db].dbo.SerwisSamolotowy AS s WHERE r.fk_serwis = s.id_serwis)
	FROM [smao_db].dbo.[Awaria] AS b
	LEFT JOIN [smao_db].dbo.[Naprawa] AS r ON r.fk_awaria = b.id_awaria

UPDATE [temp1_DB].dbo.[TempAwaria] SET opis = 'Brak naprawy', koszt_laczny = 0, nazwa_serwisu = 'AAR Corporation', data_naprawy = data_awarii WHERE data_naprawy IS NULL;

INSERT INTO [MaxiAirlinesDW].dbo.[Awaria]
SELECT 
	(SELECT id_data FROM [MaxiAirlinesDW].dbo.[Data] AS d WHERE YEAR(b.data_awarii) = d.rok AND DATENAME(MONTH, b.data_awarii) = d.miesiac 
		AND DAY(b.data_awarii) = d.dzien) AS [id_data_awarii], 
	(SELECT id_czas FROM [MaxiAirlinesDW].dbo.[Czas] AS t WHERE DATEPART(hh, b.data_awarii) = t.godzina AND DATEPART(mi, b.data_awarii) = t.minuta 
		AND DATEPART(ss, b.data_awarii) = t.sekunda) AS [id_data_awarii],
	(SELECT id_samolot FROM [MaxiAirlinesDW].dbo.[Samolot] AS p WHERE b.samolot_numer_seryjny = p.numer_seryjny) AS [id_samolot],
	(SELECT id_data FROM [MaxiAirlinesDW].dbo.[Data] AS d WHERE YEAR(b.data_naprawy) = d.rok AND DATENAME(MONTH, b.data_naprawy) = d.miesiac 
		AND DAY(b.data_naprawy) = d.dzien) AS [id_data_naprawy], 
	(SELECT id_czas FROM [MaxiAirlinesDW].dbo.[Czas] AS t WHERE DATEPART(hh, b.data_naprawy) = t.godzina AND DATEPART(mi, b.data_naprawy) = t.minuta 
		AND DATEPART(ss, b.data_naprawy) = t.sekunda) AS [id_data_naprawy],
	(SELECT id_serwis FROM [MaxiAirlinesDW].dbo.[SerwisSamolotowy] AS a WHERE b.nazwa_serwisu = a.nazwa_serwisu) AS [id_serwis],
	(SELECT id_awaria_junk FROM [MaxiAirlinesDW].dbo.[AwariaJunk] AS j WHERE b.przyczyna = j.przyczyna_awarii AND b.opis = j.opis_naprawy) AS [id_awaria_junk],
	b.koszt_laczny AS [calkowity_koszt_naprawy],
	0.6 * b.koszt_laczny AS [rzeczywisty_koszt_naprawy]
	FROM [temp1_DB].dbo.[TempAwaria] AS b