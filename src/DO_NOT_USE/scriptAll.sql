DROP SEQUENCE membre_id_seq;
DROP TRIGGER trigger_membre_id;
DROP PROCEDURE TULIP_UPDATE;

DROP TABLE membre;
DROP TABLE statut;

CREATE SEQUENCE membre_id_seq
                INCREMENT BY 1
                START WITH 10
                MAXVALUE 9999
                NOCACHE
                NOCYCLE;

CREATE TABLE statut 
(
    id NUMBER(3) CONSTRAINT pk_statut_id PRIMARY KEY,
    statut_name VARCHAR2(255) CONSTRAINT nn_statut_name NOT NULL CONSTRAINT u_statut_name UNIQUE  CONSTRAINT ck_statut_name CHECK (statut_name IN ('gold','silver','bronze'))
);


CREATE TABLE membre
(
    id_membre NUMBER(3) CONSTRAINT pk_membre_id PRIMARY KEY,
    first_name VARCHAR2(255) NOT NULL,
    last_name VARCHAR2(255) NOT NULL,
    address VARCHAR2(255) NOT NULL,
    id_statut  NUMBER(3) NOT NULL,
    CONSTRAINT fk_membre_id_statut FOREIGN KEY (id_statut) REFERENCES statut (id)
);

CREATE OR REPLACE TRIGGER trigger_membre_id
      BEFORE INSERT ON membre
      FOR EACH ROW
    BEGIN
        IF :NEW.id_membre IS NULL THEN
          SELECT  membre_id_seq.NEXTVAL INTO :NEW.id_membre FROM dual;
        END IF;
    END;
/


CREATE OR REPLACE PROCEDURE TULIP_UPDATE 
(
  IDMEMBRE IN MEMBRE.ID_MEMBRE%TYPE 
) AS 
stat STATUT.STATUT_NAME%TYPE;
BEGIN
  SELECT S.STATUT_NAME INTO stat 
  FROM STATUT S JOIN MEMBRE MB ON (S.ID = MB.ID_STATUT) WHERE MB.ID_MEMBRE = IDMEMBRE ;
  IF(stat <> 'bronze') THEN
    UPDATE membre 
    SET ID_STATUT = (SELECT ID FROM STATUT WHERE STATUT_NAME = 'bronze')
    WHERE ID_MEMBRE = IDMEMBRE;
  END IF;
EXCEPTION 
WHEN NO_DATA_FOUND THEN
  RAISE_APPLICATION_ERROR (-20222, 'WRONG MEMBER ID');
COMMIT;
END TULIP_UPDATE;
/


INSERT INTO statut VALUES(100,'gold');
INSERT INTO statut VALUES(200,'silver');
INSERT INTO statut VALUES(300,'bronze');

INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Bugs','Bunny','Address one',100);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Mickey','Mouse','Address two',100);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Fred','Flintstone','Address three',200);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Popeye','The Sailor','Address four',200);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Daffy','Duck','Address five',300);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Porky','Pig','Address six',300);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Pink','Panther','Address seven',300);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Winnie','The Pooh','Address eight',100);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Yogi','Bear','Address nine',200);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Boo','Boo','Address ten',300);

INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Bugs','Bunny 2','Address one',100);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Mickey','Mouse 2','Address two',100);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Fred','Flintstone 2','Address three',200);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Popeye','The Sailor 2','Address four',200);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Daffy','Duck 2','Address five',300);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Porky','Pig 2','Address six',300);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Pink','Panther 2','Address seven',300);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Winnie','The Pooh 2','Address eight',100);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Yogi','Bear 2','Address nine',200);
INSERT INTO membre(first_name,last_name,address,id_statut) VALUES('Boo','Boo 2','Address ten',300);


COMMIT;