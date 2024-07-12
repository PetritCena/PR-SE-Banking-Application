CREATE TABLE users (
    vorname VARCHAR(20) NOT NULL,
    nachname VARCHAR(20) NOT NULL,
    email VARCHAR(100) PRIMARY KEY,
    password VARCHAR(10) NOT NULL
);

CREATE TABLE accounts(
    iban VARCHAR2(20) PRIMARY KEY,
    saldo DECIMAL NOT NULL,
    typ VARCHAR2(10) NOT NULL,
    user_email VARCHAR(100) NOT NULL,
    name VARCHAR(20),
    photo BLOB
);

CREATE TABLE cards(
    iban VARCHAR(20) NOT NULL,
    kartenlimit NUMBER NOT NULL,
    kartennummer INTEGER PRIMARY KEY ,
    folgenummer INTEGER,
    typ VARCHAR(10) NOT NULL,
    geheimzahl INTEGER
);

CREATE TABLE transactions(
    transaktionsnummer INTEGER GENERATED ALWAYS as IDENTITY (START WITH 1 INCREMENT BY 1),
    betrag DECIMAL,
    eingangAusgang VARCHAR2(20),
    empfaengerIban VARCHAR2(20),
    senderIban VARCHAR2(20),
    verwendungszweck VARCHAR(100),
    kartennummer INTEGER,
    PRIMARY KEY (transaktionsnummer),
    FOREIGN KEY (empfaengerIban) REFERENCES accounts(iban),
    FOREIGN KEY (senderIban) REFERENCES accounts(iban),
    FOREIGN KEY (kartennummer) REFERENCES cards(kartennummer)
);

-- Trigger, der ein Hauptkonto anlegt, sobald ein neuer User erstellt wird
CREATE OR REPLACE TRIGGER account_trigger
AFTER INSERT ON users FOR EACH ROW
DECLARE
    iban2 VARCHAR2(20);
    saldo2 DECIMAL;
    typ2 VARCHAR2(10);
BEGIN
    iban2 := 'AT';
    FOR cnt IN 1 .. 18 LOOP
        iban2 := iban2 || floor(dbms_random.value(1, 10));
    END LOOP;
    saldo2 := 10.00;
    typ2 := 'Hauptkonto';
    INSERT INTO accounts(iban, saldo, typ, user_email) VALUES (iban2, saldo2, typ2, :NEW.email);
END;

-- Trigger, der eine neue IBAN erzeugt, sobald ein Space erstellt wird
CREATE OR REPLACE TRIGGER space_trigger
BEFORE INSERT ON accounts FOR EACH ROW
DECLARE
    iban2 VARCHAR(20);
BEGIN
    iban2 := 'AT';
    FOR cnt IN 1 .. 18 LOOP
        iban2 := iban2 || floor(dbms_random.value(1, 10));
    END LOOP;
    :NEW.iban := iban2;
END;

-- Trigger, der eine neuen Pin erzeugt, sobald eine Karte erstellt wird
CREATE OR REPLACE TRIGGER card_trigger
BEFORE INSERT ON cards FOR EACH ROW
DECLARE
    kartennummer2 INTEGER;
    pin INTEGER;
BEGIN
    pin := 0;
    kartennummer2 := 0;
    FOR cnt IN 1 .. 4 LOOP
        pin := (pin * 10) + floor(dbms_random.value(1, 10));
    END LOOP;
    FOR cnt IN 1 .. 16 LOOP
        kartennummer2 := (kartennummer2 * 10) + floor(dbms_random.value(1, 10));
    END LOOP;
    :NEW.kartennummer := kartennummer2;
    :NEW.folgenummer := floor(dbms_random.value(1, 10));
    :NEW.geheimzahl := pin;
END;

UPDATE accounts SET saldo = 100 WHERE iban = 'AT287862657226857749';

TRUNCATE TABLE users;
TRUNCATE TABLE accounts;
TRUNCATE TABLE cards;
TRUNCATE TABLE transactions;
DROP TABLE cards;
DROP TABLE transactions;
DROP TABLE accounts;
COMMIT;