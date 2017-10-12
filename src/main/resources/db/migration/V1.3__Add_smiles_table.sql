CREATE TABLE smiles (
  id                BIGINT(20)    NOT NULL AUTO_INCREMENT PRIMARY KEY,
  creation_date     TIMESTAMP     NOT NULL,
  photo_url         VARCHAR(2083) NULL,
  description       VARCHAR(280)  NULL,
  source            VARCHAR(255)  NOT NULL,
  source_url        VARCHAR(2083) NOT NULL,
  sent              BOOL          NOT NULL,
  sent_date         DATETIME      NULL,
  smile_number      INT           NULL
);