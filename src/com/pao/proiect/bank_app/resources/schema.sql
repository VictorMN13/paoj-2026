-- schema aplicatie in postgre
DROP TABLE IF EXISTS tranzactie;
DROP TABLE IF EXISTS card;
DROP TABLE IF EXISTS cont_bancar;
DROP TABLE IF EXISTS angajat;
DROP TABLE IF EXISTS client;

-- tabela clienti
CREATE TABLE client (
    id VARCHAR(6) PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    prenume VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    parola VARCHAR(255) NOT NULL,
    adresa VARCHAR(255),
    numar_telefon VARCHAR(20)
);

-- tabela angajati si manager
CREATE TABLE angajat (
    id VARCHAR(6) PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    prenume VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    parola VARCHAR(255) NOT NULL,
    salariu NUMERIC(10, 2) NOT NULL,
    sucursala VARCHAR(100) NOT NULL,

    is_manager BOOLEAN DEFAULT FALSE,
    -- campuri nule pentru angajati
    bonus_conducere NUMERIC(10, 2),
    departament VARCHAR(100)
);

-- tabela conturi curent + economii
CREATE TABLE cont_bancar (
    iban VARCHAR(34) PRIMARY KEY,
    client_id VARCHAR(6) NOT NULL,
    sold NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    moneda VARCHAR(3) NOT NULL,
    tip_cont VARCHAR(20) NOT NULL,

    -- camp cont curent
    comision_administrare NUMERIC(10, 2),

    -- camp cont economii
    rata_dobanda NUMERIC(5, 2),

    CONSTRAINT fk_cont_client FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

-- tabela carduri
CREATE TABLE card (
    numar_card VARCHAR(16) PRIMARY KEY,
    iban_asociat VARCHAR(34) NOT NULL,
    cvv VARCHAR(3) NOT NULL,
    data_expirare DATE NOT NULL,
    pin VARCHAR(4) NOT NULL,
    blocat BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_card_cont FOREIGN KEY (iban_asociat) REFERENCES cont_bancar(iban) ON DELETE CASCADE
);

-- tabela tranzactii
CREATE TABLE tranzactie (
    id_tranzactie VARCHAR(36) PRIMARY KEY,
    data_executie TIMESTAMP NOT NULL,
    suma NUMERIC(15, 2) NOT NULL,
    tip_tranzactie VARCHAR(20) NOT NULL,
    iban_sursa VARCHAR(34),
    iban_destinatie VARCHAR(34),

    CONSTRAINT fk_tranzactie_sursa FOREIGN KEY (iban_sursa) REFERENCES cont_bancar(iban) ON DELETE SET NULL,
    CONSTRAINT fk_tranzactie_destinatie FOREIGN KEY (iban_destinatie) REFERENCES cont_bancar(iban) ON DELETE SET NULL
);