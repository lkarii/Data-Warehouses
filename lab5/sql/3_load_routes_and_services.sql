USE [MaxiAirlinesDW]

INSERT INTO [MaxiAirlinesDW].dbo.[Trasa] SELECT DISTINCT miasto_wylotu, miasto_przylotu FROM [smao_DB].dbo.[Lot]

INSERT INTO [MaxiAirlinesDW].dbo.[SerwisSamolotowy] SELECT nazwa_serwisu, miasto, ulica, numer_budynku, kod_pocztowy FROM [smao_DB].dbo.[SerwisSamolotowy];