-- Creazione delle tabelle
CREATE TABLE Studenti (
  Matricola INT PRIMARY KEY,
  Nome VARCHAR(50) NOT NULL,
  Cognome VARCHAR(50) NOT NULL,
  DataNascita DATE NOT NULL
);

CREATE TABLE Docenti (
  CodDocente INT PRIMARY KEY,
  Nome VARCHAR(50) NOT NULL,
  Cognome VARCHAR(50) NOT NULL,
  Email VARCHAR(100) NOT NULL
);

CREATE TABLE Corsi (
  CodCorso INT PRIMARY KEY,
  NomeCorso VARCHAR(100) NOT NULL,
  Crediti TINYINT NOT NULL,
  CodDocente INT NOT NULL,
  FOREIGN KEY (CodDocente) REFERENCES Docenti(CodDocente)
);
CREATE TABLE Esami (
  Matricola INT NOT NULL,
  CodCorso INT NOT NULL,
  Data DATE NOT NULL,
  Voto TINYINT NOT NULL,
  PRIMARY KEY (Matricola, CodCorso),
  FOREIGN KEY (Matricola) REFERENCES Studenti(Matricola),
  FOREIGN KEY (CodCorso) REFERENCES Corsi(CodCorso)
);

-- Inserimento di alcuni dati di esempio
INSERT INTO Studenti VALUES
(1001, 'Mario', 'Rossi', '2000-01-01'),
(1002, 'Luca', 'Verdi', '1999-02-02'),
(1003, 'Anna', 'Bianchi', '2000-03-03'),
(1004, 'Sara', 'Neri', '1999-04-04'),
(1005, 'Giovanni', 'Gialli', '2000-05-05'),
(1006, 'Chiara', 'Rosa', '1999-06-06'),
(1007, 'Alessandro', 'Marroni', '2000-07-07'),
(1008, 'Elisa', 'Viola', '1999-08-08');
INSERT INTO Docenti VALUES
(201, 'Paolo', 'Mazzoni', 'paolo.mazzoni@universita.it'),
(202, 'Laura', 'Ferrari', 'laura.ferrari@universita.it'),
(203, 'Marco', 'Ricci', 'marco.ricci@universita.it'),
(204, 'Elena', 'Galli', 'elena.galli@universita.it');
INSERT INTO Corsi VALUES
(101, 'Matematica', 6, 201),
(102, 'Informatica', 9, 202),
(103, 'Fisica', 6, 203),
(104, 'Chimica', 9, 204),
(105, 'Biologia', 7, 201),
(106, 'Storia dell''arte', 8, 202),
(107, 'Letteratura', 10, 203),
(108, 'Economia', 7, 204);
INSERT INTO Esami VALUES
(1001, 101, '2020-01-10', 28),
(1001, 102, '2020-01-15', 30),
(1002, 101, '2020-01-10', 25),
(1002, 103, '2020-01-20', 27),
(1003, 101, '2020-01-10', 30),
(1003, 104, '2020-01-25', 26),
(1004, 102, '2020-01-15', 24),
(1004, 103, '2020-01-20', 29),
(1005, 101, '2020-01-10', 26),
(1006, 102, '2020-01-15', 28),
(1006, 104, '2020-01-25', 26),
(1006, 101, '2020-01-10', 29),
(1007, 104, '2020-01-20', 28),
(1007, 101, '2020-01-10', 25),
(1008, 103, '2020-01-20', 30),
(1008, 102, '2020-01-15', 27);