DROP TABLE membre;
DROP TABLE statut;
DROP SEQUENCE membre_id_seq;

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

