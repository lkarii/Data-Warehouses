USE [MaxiAirlinesDW]


-- wstaw wszystkie daty od 1970 do 2023 roku.
DECLARE @StartDate DATE = '1970-01-01',
	@EndDate DATE = '2023-12-31';

DECLARE @DateInProcess DATETIME = @StartDate;
DECLARE @CurrentWeekDayNo INT = CAST(DATEPART(dw, @DateInProcess) AS INT);


WHILE @DateInProcess <= @EndDate
	BEGIN
		INSERT INTO Data 
		VALUES 
		(
			CAST(YEAR(@DateInProcess) AS INT),					    -- rok
			CAST(DATENAME(MONTH, @DateInProcess) AS VARCHAR(9)),	-- miesiac
			CAST(DAY(@DateInProcess) AS INT),					    -- dzien
			CASE
				WHEN @CurrentWeekDayNo > 5 THEN 'dzien roboczy'
				ELSE 'dzien wolny'
			END,													-- czy_dzien_roboczy
			CASE
				WHEN @CurrentWeekDayNo > 5 THEN 'nie'
				ELSE 'swieto'
			END,													-- czy_swięto
			@CurrentWeekDayNo									    -- dzien_tygodnia
		);  

		SET @DateInProcess = DATEADD(d, 1, @DateInProcess);
		SET @CurrentWeekDayNo = CAST(DATEPART(dw, @DateInProcess) AS INT);
	END


-- wstaw wszystkie możliwe kombinacje czasu
DECLARE @CurrentHour INT = 0,
		@CurrentMinute INT = 0,
		@CurrentSecond INT = 0;

WHILE @CurrentHour < 24
	BEGIN
		WHILE @CurrentMinute < 60
			BEGIN
				WHILE @CurrentSecond < 60
					BEGIN
						INSERT INTO Czas 
						VALUES
						(
							@CurrentHour,											-- godzina
							@CurrentMinute,									        -- minuta
							@CurrentSecond,			                                -- sekunda
							CASE
								WHEN @CurrentHour < 9 THEN 'miedzy 0 a 8'
								WHEN @CurrentHour < 13 THEN 'miedzy 9 a 12'
								WHEN @CurrentHour < 16 THEN 'miedzy 13 a 15'
								WHEN @CurrentHour < 21 THEN 'miedzy 16 a 20'
								ELSE 'miedzy 21 a 23'
							END													    -- pora_dnia
						);


						SET @CurrentSecond = @CurrentSecond + 1;
					END
				SET @CurrentMinute = @CurrentMinute + 1;
				SET @CurrentSecond = 0;
			END
		SET @CurrentHour = @CurrentHour + 1;
		SET @CurrentMinute = 0;
		SET @CurrentSecond = 0;
	END
