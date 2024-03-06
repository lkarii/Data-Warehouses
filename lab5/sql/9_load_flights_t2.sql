USE [temp1_DB]

DROP TABLE IF EXISTS [temp1_DB].dbo.[TempLot2];
CREATE TABLE TempLot2(
	numer_lotu VARCHAR(32) PRIMARY KEY,
	samolot_numer_seryjny VARCHAR(16),
	miasto_wylotu VARCHAR(32),
	miasto_przylotu VARCHAR(32),
	data_wylotu DATETIME,
	data_przylotu DATETIME,
	status VARCHAR(32),
	opoznienie_min INT,
	przyczyna_opoznienia VARCHAR(256),
	dystans INT,
	ocena_klienta Varchar(32)
);

INSERT INTO [temp1_DB].dbo.[TempLot2]
SELECT
	f.numer_lotu,
	(SELECT numer_seryjny FROM [smao_DB].dbo.[Samolot] AS p WHERE f.fk_samolot = p.id_samolot),
	f.miasto_wylotu,
	f.miasto_przylotu,
	f.data_wylotu,
	f.data_przylotu,
	CASE 
		WHEN f.odwolany = 1 THEN 'anulowany'
		ELSE 'zakonczony'
	END AS [status],
	d.czas_opoznienia,
	d.przyczyna,
	(SELECT dystans FROM [temp1_DB].dbo.[TempDystans] AS d WHERE f.miasto_wylotu = d.pierwsze_lotnisko AND f.miasto_przylotu = d.drugie_lotnisko),
	CASE
		WHEN (SELECT ocena FROM [temp1_DB].dbo.[TempAvgOceny] AS r WHERE f.numer_lotu = r.lot) < 75 THEN 'Ponizej 75'
		ELSE 'Rowna lub wyzsza niz 75'
	END
	FROM [smao_DB].dbo.[Lot] AS f
	LEFT JOIN [smao_DB].dbo.[Opoznienie] AS d ON f.id_lot = d.fk_lot;

UPDATE [temp1_DB].dbo.[TempLot2] SET przyczyna_opoznienia = 'Brak opoznienia', opoznienie_min = 0 WHERE opoznienie_min IS NULL;

INSERT INTO [MaxiAirlinesDW].dbo.[Lot]
SELECT 
	f.numer_lotu,
	(SELECT id_samolot FROM [MaxiAirlinesDW].dbo.[Samolot] AS p WHERE f.samolot_numer_seryjny = p.numer_seryjny AND p.czy_aktualny = 1) AS [id_samolot],
	(SELECT id_trasy FROM [MaxiAirlinesDW].dbo.[Trasa] AS r WHERE f.miasto_wylotu = r.miasto_wylotu AND f.miasto_przylotu = r.miasto_przylotu) AS [id_trasy],
	(SELECT id_data FROM [MaxiAirlinesDW].dbo.[Data] AS d WHERE YEAR(f.data_wylotu) = d.rok AND DATENAME(MONTH,f.data_wylotu) = d.miesiac 
		AND DAY(f.data_wylotu) = d.dzien) AS [id_data_wylotu],
	(SELECT id_czas FROM [MaxiAirlinesDW].dbo.[Czas] AS t WHERE DATEPART(hh, f.data_wylotu) = t.godzina AND DATEPART(mi, f.data_wylotu) = t.minuta 
		AND DATEPART(ss, f.data_wylotu) = t.sekunda) AS [id_czas_wylotu],
	(SELECT id_data FROM [MaxiAirlinesDW].dbo.[Data] AS d WHERE YEAR(f.data_przylotu) = d.rok AND DATENAME(MONTH, f.data_przylotu) = d.miesiac 
		AND DAY(f.data_przylotu) = d.dzien) AS [id_data_przylotu],
	(SELECT id_czas FROM [MaxiAirlinesDW].dbo.[Czas] AS t WHERE DATEPART(hh, f.data_przylotu) = t.godzina AND DATEPART(mi, f.data_przylotu) = t.minuta 
		AND DATEPART(ss, f.data_przylotu) = t.sekunda) AS [id_czas_przylotu],
	(SELECT id_lot_junk FROM [MaxiAirlinesDW].dbo.[LotJunk] AS j WHERE f.przyczyna_opoznienia = j.przyczyna_opoznienia AND f.status = j.status_lotu 
		AND f.ocena_klienta = j.srednia_ocena_klienta) AS [id_lot_junk],
	f.opoznienie_min,
	f.dystans
	FROM (SELECT f2.numer_lotu, f2.samolot_numer_seryjny, f2.miasto_wylotu, f2.miasto_przylotu, f2.data_wylotu, f2.data_przylotu, f2.status, f2.opoznienie_min, f2.przyczyna_opoznienia, f2.dystans, f2.ocena_klienta FROM TempLot AS f1 
		RIGHT JOIN TempLot2 AS f2 ON f1.numer_lotu = f2.numer_lotu WHERE f1.numer_lotu IS NULL) AS f