SELECT { [Measures].[Opoznienie Count] } ON COLUMNS,
{ TopCount(([Samolot].[Id Samolot].[Id Samolot], [Samolot].[Numer Seryjny].Children), 10) } ON ROWS
FROM [Maxi Airlines DW]