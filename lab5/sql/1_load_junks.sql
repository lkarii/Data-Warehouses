USE [MaxiAirlinesDW]

INSERT INTO [MaxiAirlinesDW].dbo.[LotJunk]
SELECT po, sl, sok
FROM 
	  (
		VALUES 
			  ('Brak opoznienia'),
			  ('Zle warunki pogodowe'),
			  ('Incydent na pokladzie'),
			  ('Awaria samolotu'),
			  ('Inne')
	  ) AS przyczyna_opoznienia(po), 
	  (
		VALUES 
			  ('zakonczony'),
			  ('anulowany')
	  ) AS status_lotu(sl),
	  (
		VALUES 
			  ('Ponizej 75'),
			  ('Rowna lub wyzsza niz 75')
	  ) AS srednia_ocena_klienta(sok);


INSERT INTO [MaxiAirlinesDW].dbo.[AwariaJunk] 
VALUES 
	('Awaria lewego silnika', 'Wymiana turbiny silnika'),
	('Awaria prawego silnika', 'Wymiana silnika'),
	('Niesprawny podwozie', 'Wymiana czesci i serwis podwozia'),
	('Uszkodzone oswietlenie', 'Wymiana oswietlenia'),
	('Zle odczyty czujnikow', 'Wymiana czujnika'),
	('Blad w oprogramowaniu autopilota', 'Aktualizacja oprogramowania'),
	('Usterka systemu manewrowania', 'Wymiana czesci systemu manewrowania'),
	('Niesprawny system komunikacji', 'Wymiana elementow komunikacyjnych'),
	('Zepsuty radar', 'Wymiana radaru'),
	('Zepsuty ster samolotu', 'Wymiana steru samolotu'),
	('Mechaniczne uszkodzenie kadluba samolotu', 'Naprawy w kadlubie samolotu'),
	('Dziura w zbiorniku paliwa samolotu', 'Wymiana zbiornika paliwa'),
	('Uszkodzenie na skrzydle', 'Naprawa skrzydla'),
	('Awaria kierownicy', 'Naprawa kierownicy'),
	('Bledne wskazania sztucznego horyzontu', 'Wymiana kabli'),
	('Problem z dekompresja', 'Naprawa automatycznych systemow')
	
INSERT INTO [MaxiAirlinesDW].dbo.[AwariaJunk]
SELECT pa, onap
FROM 
	  (
		SELECT przyczyna_awarii FROM AwariaJunk
	  ) AS przyczyna_awarii(pa), 
	  (
		VALUES ('Brak naprawy')
	  ) AS opis_naprawy(onap);
