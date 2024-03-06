USE [temp1_DB];

DROP TABLE IF EXISTS [temp1_DB].dbo.[TempSamolotCsv];
CREATE TABLE TempSamolotCsv(
	model VARCHAR(32),
	numer_seryjny VARCHAR(16) PRIMARY KEY,
	liczba_miejsc INT,
	producent VARCHAR(32),
	data_produkcji DATE,
	data_zakupu DATE,
	koszt_zakupu INT,
	calkowita_waga INT
);

BULK INSERT [temp1_DB].dbo.[TempSamolotCsv]
FROM 'C:\Samoloty_t1.csv'
WITH
(
    FIRSTROW = 2,
    FIELDTERMINATOR = ',', 
    ROWTERMINATOR = '\n'
)

INSERT INTO [MaxiAirlinesDW].dbo.[Samolot]
SELECT 
	numer_seryjny, 
	producent, 
	model, 
	CASE
		WHEN p.calkowita_waga < 50001 THEN 'Ponizej 50 ton'
		WHEN p.calkowita_waga < 100001 THEN 'Miedzy 51 a 100 ton'
		WHEN p.calkowita_waga < 150001 THEN 'Miedzy 101 a 150 ton'
		WHEN p.calkowita_waga < 200001 THEN 'Miedzy 151 a 200 ton'
		ELSE 'Powyzej 200 ton'
	END AS [waga], 
	CASE
		WHEN p.liczba_miejsc < 76 THEN 'Ponizej 76'
		WHEN p.liczba_miejsc < 126 THEN 'Miedzy 76 a 125'
		WHEN p.liczba_miejsc < 176 THEN 'Miedzy 126 a 175'
		WHEN p.liczba_miejsc < 226 THEN 'Miedzy 176 a 225'
		ELSE 'Powyzej 225'
	END AS [miejsca],
	CASE
		WHEN p.koszt_zakupu < 35000001 THEN 'Ponizej 35'
		WHEN p.koszt_zakupu < 60000001 THEN 'Miedzy 35 a 60'
		WHEN p.koszt_zakupu < 95000001 THEN 'Miedzy 60 a 95'
		WHEN p.koszt_zakupu < 120000001 THEN 'Miedzy 95 a 120'
		WHEN p.koszt_zakupu < 145000001 THEN 'Miedzy 120 a 145'
		ELSE 'Powyzej 145'
	END AS [koszt_zakupu],
	'1' AS [czy_aktualny],
	(SELECT id_data FROM [MaxiAirlinesDW].dbo.[Data] AS d WHERE YEAR(p.data_produkcji) = d.rok AND DATENAME(MONTH, p.data_produkcji) = d.miesiac 
		AND DAY(p.data_produkcji) = d.dzien) AS [fk_data_produkcji]
	FROM [temp1_DB].dbo.[TempSamolotCsv] AS p
	ORDER BY data_zakupu