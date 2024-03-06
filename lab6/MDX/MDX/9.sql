SELECT [Measures].[Opoznienie Count] ON COLUMNS,
{ [Lot Junk].[Przyczyna Opoznienia].[Zle warunki pogodowe], [Lot Junk].[Przyczyna Opoznienia].[Incydent na pokladzie] } ON ROWS
FROM [Maxi Airlines DW]
WHERE [Id Data Przylotu].[HierarchiaDatyPrzylotu].[Rok].[2020].[May]