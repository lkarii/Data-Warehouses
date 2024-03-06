-- od 2018

WITH
MEMBER [Liczba samolotow] AS 'Count(Descendants([Samolot].[Numer Seryjny].[Numer Seryjny]))'
SELECT { [Samolot].[Producent].Children } ON COLUMNS,
{ [Measures].[Awaria Count], [Liczba samolotow] } ON ROWS
FROM [Maxi Airlines DW]
WHERE { [Id Data Awarii].[Rok].[2018] : NULL }