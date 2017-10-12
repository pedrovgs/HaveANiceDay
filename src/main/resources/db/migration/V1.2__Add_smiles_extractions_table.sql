CREATE TABLE smiles_extractions (
  id                         BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  extraction_date            TIMESTAMP  NOT NULL,
  number_of_smiles_extracted INT
);