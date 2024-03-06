SELECT { [Measures].[Rzeczywisty koszt naprawy] } ON COLUMNS, 
{ [Id Data Naprawy].[Rok].[2018], [Id Data Naprawy].[Rok].[2018].NextMember } ON ROWS
FROM [Maxi Airlines DW]