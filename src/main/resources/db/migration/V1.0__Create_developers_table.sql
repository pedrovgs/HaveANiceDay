CREATE TABLE developers(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  email VARCHAR(255)
);

INSERT INTO developers (username, email) VALUES ('pedrovgs', 'pedrovicente.gomez@gmail.com');