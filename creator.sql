CREATE TABLE Battery(
precent INTEGER,
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE Buttons(
Button1 BOOLEAN,
Button2 BOOLEAN,
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE Color(
r INTEGER,
g INTEGER,
b INTEGER,
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE Ledlights(
Led INTEGER NOT NULL,
CONSTRAINT CON1 FOREIGN KEY (Led) REFERENCES Color(p_key),
Led1Working BOOLEAN,
Led2Working BOOLEAN,
Led3Working BOOLEAN,
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE MessageMonitor(
ActualMessage CHARACTER(20),
BackgorundColor INTEGER NOT NULL,
CONSTRAINT CON2 FOREIGN KEY (BackgorundColor) REFERENCES Color(p_key),
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE TemperatureType
(
enums CHARACTER(20),
p_key INTEGER NOT NULL,
PRIMARY KEY (p_key)
);
INSERT INTO TemperatureType (enums,p_key)
VALUES
('Fahrenheit' , 0),
('Celsius' , 1);

CREATE TABLE TemperatureSensor(
ActualTemperature FLOAT,
UnitType INTEGER NOT NULL,
CONSTRAINT CON3 FOREIGN KEY (UnitType) REFERENCES TemperatureType(p_key),
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE State
(
enums CHARACTER(20),
p_key INTEGER NOT NULL,
PRIMARY KEY (p_key)
);
INSERT INTO State (enums,p_key)
VALUES
('Left' , 0),
('Right' , 1),
('Up' , 2),
('Down' , 3);

CREATE TABLE MultiStateSwitch(
state INTEGER NOT NULL,
CONSTRAINT CON4 FOREIGN KEY (state) REFERENCES State(p_key),
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE Switcher(
SwitchState INTEGER NOT NULL,
CONSTRAINT CON5 FOREIGN KEY (SwitchState) REFERENCES MultiStateSwitch(p_key),
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE Calculation(
deltaSpeed DOUBLE PRECISION,
t DOUBLE PRECISION,
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE Acceleration(
acceleration DOUBLE PRECISION,
calc INTEGER NOT NULL,
CONSTRAINT CON12 FOREIGN KEY (calc) REFERENCES Calculation(p_key),
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE SpeedUnit
(
enums VARCHAR(20),
p_key INTEGER NOT NULL,
PRIMARY KEY (p_key)
);
INSERT INTO SpeedUnit (enums,p_key)
VALUES
('kmph' , 0),
('mph' , 1)
;

CREATE TABLE Accelerometer(
currentAcc INTEGER NOT NULL,
CONSTRAINT CON13 FOREIGN KEY (currentAcc) REFERENCES Acceleration(p_key),
speedUnit INTEGER NOT NULL,
CONSTRAINT CON14 FOREIGN KEY (speedUnit) REFERENCES SpeedUnit(p_key),
p_key INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
PRIMARY KEY (p_key))
;

CREATE TABLE MyFictiveSensor(
battery INTEGER NOT NULL ,
CONSTRAINT CON6 FOREIGN KEY(battery) REFERENCES Battery(p_key),
buttons INTEGER NOT NULL ,
CONSTRAINT CON7 FOREIGN KEY(buttons) REFERENCES Buttons(p_key),
ledlights INTEGER NOT NULL ,
CONSTRAINT CON8 FOREIGN KEY(ledlights) REFERENCES Ledlights(p_key),
messagemonitor INTEGER NOT NULL ,
CONSTRAINT CON9 FOREIGN KEY(messagemonitor) REFERENCES MessageMonitor(p_key),
temperaturesensor INTEGER NOT NULL,
CONSTRAINT CON10 FOREIGN KEY(temperaturesensor) REFERENCES TemperatureSensor(p_key),
switcher INTEGER NOT NULL ,
CONSTRAINT CON11 FOREIGN KEY(switcher) REFERENCES Switcher(p_key),
accelerometer INTEGER NOT NULL ,
CONSTRAINT CON15 FOREIGN KEY(accelerometer) REFERENCES accelerometer(p_key),
measure_time TIMESTAMP
);