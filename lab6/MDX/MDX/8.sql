SELECT ORDER([Measures].[Opoznienie Count], ASC) ON COLUMNS,
{ [Id Data Przylotu].[Miesiac].Children } ON ROWS
FROM [Maxi Airlines DW]
WHERE -{[Lot Junk].[Przyczyna Opoznienia].&[brak opoznienia]}