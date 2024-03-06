-- przyklad dla 2020

SELECT { [Measures].[Awaria Count] } ON COLUMNS,
{ TopCount(ORDER([Awaria Junk].[Przyczyna Awarii].Children, [Measures].[Awaria Count], DESC), 2) } ON ROWS
FROM [Maxi Airlines DW]
WHERE [Id Data Awarii].[Rok].[2020]