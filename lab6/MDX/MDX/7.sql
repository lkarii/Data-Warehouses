-- przyklad dla Listopada i Grudnia 2020

SELECT { [Measures].[CzasTrwaniaOpoznienia] } ON COLUMNS, 
{ [Id Data Przylotu].[HierarchiaDatyPrzylotu].[Rok].[2020].[November], [Id Data Przylotu].[HierarchiaDatyPrzylotu].[Rok].[2020].[December] } ON ROWS
FROM [Maxi Airlines DW]