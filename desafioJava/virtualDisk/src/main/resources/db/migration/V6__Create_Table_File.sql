CREATE TABLE files (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  type VARCHAR(255) NOT NULL,
  size BIGINT NOT NULL,
  folder_id INT,
  FOREIGN KEY (folder_id) REFERENCES folders (id)
);
