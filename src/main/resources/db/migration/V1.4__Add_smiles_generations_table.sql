CREATE TABLE smiles_generation (
  id              BIGINT(20)   NOT NULL AUTO_INCREMENT PRIMARY KEY,
  generation_date TIMESTAMP    NOT NULL,
  smile_id        BIGINT(20)   NULL,
  error           VARCHAR(255) NULL,
  FOREIGN KEY (smile_id) REFERENCES smiles(id)
);