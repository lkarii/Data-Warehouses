WITH
MEMBER [Odleglosc na awarie] AS 'Divide([Measures].[Dystans],[Measures].[Awaria Count])', Format_String = '0.#'
MEMBER [Sredni dystans na awarie] AS 'AVG([Samolot].[Numer Seryjny].Members, [Odleglosc na awarie])', Format_String = '0.#'
MEMBER [Procent sredniego dystansu na awarie] AS 'Divide([Odleglosc na awarie],[Sredni dystans na awarie]) * 100', Format_String = '0.#'
SELECT { [Measures].[Dystans], [Measures].[Awaria Count], [Odleglosc na awarie], 
     [Sredni dystans na awarie], [Procent sredniego dystansu na awarie] } ON COLUMNS,
ORDER([Samolot].[Numer Seryjny].Children, [Measures].[Dystans], DESC) ON ROWS
FROM [Maxi Airlines DW]

-- jak widac samoloty, ktore przelecialy sporo, maja czestsze awarie (potrzebuja < 50% sredniego dystansu na awarie)