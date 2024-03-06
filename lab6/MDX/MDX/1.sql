WITH 
SET [Wiek] AS 'NonEmpty([Id data awarii].[Rok].Children * [Samolot - Data produkcji].[Rok].Children)'
SELECT { [Measures].[Awaria Count] } ON COLUMNS,
{ [Wiek] } ON ROWS
FROM [Maxi Airlines DW]


-- trzeba odjac pierwszyr_rok - drugi_rok i zrzutowac na przedzialy np. above 30 years itd.
-- i potem to wyswietlic zamiast { [Age] } ON ROWS