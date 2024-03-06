SELECT { [Measures].[Opoznienie Min] } ON COLUMNS,
{ CrossJoin([Samolot].[Producent].Children, { [Id Data Przylotu].[Miesiac].[June], [Id Data Przylotu].[Miesiac].[July] }) } ON ROWS
FROM [Maxi Airlines DW]
WHERE [Id Data Przylotu].[Rok].[2020]