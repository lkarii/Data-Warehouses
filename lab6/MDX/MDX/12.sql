WITH 
MEMBER [Opoznienie w minutach] AS '[Measures].[Opoznienie Min]'
MEMBER [Ilosc lotow] AS '[Measures].[Lot Count]'
MEMBER [Opoznienie w minutach / na lot] AS '[Opoznienie Min]/[Lot Count]'
SELECT { [Opoznienie w minutach], [Ilosc lotow], [Opoznienie w minutach / na lot] } ON COLUMNS,
{ [Lot Junk].[Srednia Ocena Klienta].Children } ON ROWS
FROM [Maxi Airlines DW]
