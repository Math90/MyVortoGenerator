CREATE TABLE Battery;
ALTER TABLE battery
ADD precent INTEGER
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE Buttons;
ALTER TABLE buttons
ADD Button1 BOOLEAN
ADD Button2 BOOLEAN
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE Color;
ALTER TABLE Color
ADD r INTEGER
ADD g INTEGER
ADD b INTEGER
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE Ledlights;
ALTER TABLE ledlights
ADD Led INTEGER NOT NULL
ADD FOREIGN KEY (Led)
REFERENCES Color(p_key)
ADD Led1Working BOOLEAN
ADD Led2Working BOOLEAN
ADD Led3Working BOOLEAN
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE MessageMonitor;
ALTER TABLE messagemonitor
ADD ActualMessage CHARACTER(20)
ADD BackgorundColor INTEGER NOT NULL
ADD FOREIGN KEY (BackgorundColor)
REFERENCES Color(p_key)
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE TemperatureType
(
enums VARCHAR(20),
p_key INTEGER NOT NULL,
PRIMARY KEY (p_key)
);
INSERT INTO TemperatureType (enums,p_key)
VALUES (
(Fahrenheit , 0),
(Celsius , 1),
);

CREATE TABLE TemperatureSensor;
ALTER TABLE temperaturesensor
ADD ActualTemperature FLOAT
ADD UnitType INTEGER NOT NULL
ADD FOREIGN KEY (UnitType)
REFERENCES TemperatureType(p_key)
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE MultiStateSwitch;
ALTER TABLE MultiStateSwitch
ADD state INTEGER NOT NULL
ADD FOREIGN KEY (state)
REFERENCES State(p_key)
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE State
(
enums VARCHAR(20),
p_key INTEGER NOT NULL,
PRIMARY KEY (p_key)
);
INSERT INTO State (enums,p_key)
VALUES (
(Left , 0),
(Right , 1),
(Up , 2),
(Down , 3),
);

CREATE TABLE Switcher;
ALTER TABLE switcher
ADD SwitchState INTEGER NOT NULL
ADD FOREIGN KEY (SwitchState)
REFERENCES MultiStateSwitch(p_key)
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE Acceleration;
ALTER TABLE Acceleration
ADD acceleration DOUBLE PRECISION
ADD calc INTEGER NOT NULL
ADD FOREIGN KEY (calc)
REFERENCES Calculation(p_key)
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE Calculation;
ALTER TABLE Calculation
ADD deltaSpeed DOUBLE PRECISION
ADD time DOUBLE PRECISION
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE SpeedUnit
(
enums VARCHAR(20),
p_key INTEGER NOT NULL,
PRIMARY KEY (p_key)
);
INSERT INTO SpeedUnit (enums,p_key)
VALUES (
(kmph , 0),
(mph , 1),
);

CREATE TABLE Accelerometer;
ALTER TABLE accelerometer
ADD currentAcc INTEGER NOT NULL
ADD FOREIGN KEY (currentAcc)
REFERENCES Acceleration(p_key)
ADD speedUnit INTEGER NOT NULL
ADD FOREIGN KEY (speedUnit)
REFERENCES SpeedUnit(p_key)
ADD p_key INTEGER NOT NULL AUTO_INCREMENT
ADD PRIMARY KEY (p_key)
;

CREATE TABLE MyFictiveSensor;
ALTER TABLE MyFictiveSensor
ADD battery INTEGER NOT NULL 
ADD FOREIGN KEY(battery)
REFERENCES Battery(p_key)
ADD buttons INTEGER NOT NULL 
ADD FOREIGN KEY(buttons)
REFERENCES Buttons(p_key)
ADD ledlights INTEGER NOT NULL 
ADD FOREIGN KEY(ledlights)
REFERENCES Ledlights(p_key)
ADD messagemonitor INTEGER NOT NULL 
ADD FOREIGN KEY(messagemonitor)
REFERENCES MessageMonitor(p_key)
ADD temperaturesensor INTEGER NOT NULL 
ADD FOREIGN KEY(temperaturesensor)
REFERENCES TemperatureSensor(p_key)
ADD switcher INTEGER NOT NULL 
ADD FOREIGN KEY(switcher)
REFERENCES Switcher(p_key)
ADD accelerometer INTEGER NOT NULL 
ADD FOREIGN KEY(accelerometer)
REFERENCES Accelerometer(p_key)
ADD measure_time TIME;