-- przyklad dla wszystkich samolotow w 2020

SELECT { [Measures].[Awaria Count] } ON COLUMNS, 
{ ORDER([Samolot].[Numer Seryjny].Children, [Measures].[Awaria Count], DESC) } ON ROWS
FROM [Maxi Airlines DW]
WHERE [Id Data Awarii].[Rok].[2020]