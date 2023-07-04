CREATE TABLE folders (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  user_id INT,
  parent_folder_id INT,
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (parent_folder_id) REFERENCES folders (id)
);

