--------------------------
-- Auxiliary types
--------------------------

  CREATE TYPE STATE AS ENUM ('Uncompleted', 'Completed');

--------------------------
-- Create tables
--------------------------

  CREATE TABLE checklist_template(
    checklist_template_id SERIAL,
    checklist_template_name VARCHAR(50),
    checklist_template_description VARCHAR(100),
    PRIMARY KEY (checklist_template_id)
  );

  CREATE TABLE item_template(
    item_template_id SERIAL,
    template_id INTEGER REFERENCES checklist_template,
    item_template_name VARCHAR(50),
    item_template_description VARCHAR(100),
    item_template_state STATE DEFAULT 'Uncompleted',
    PRIMARY KEY (item_template_id, template_id)
  );

  CREATE TABLE checklist(
    checklist_id SERIAL,
    checklist_name VARCHAR(50),
    checklist_completion_date date,
    template_id INTEGER REFERENCES checklist_template,
    PRIMARY KEY (checklist_id)
  );

  CREATE TABLE item(
    item_id SERIAL,
    checklist_id INTEGER REFERENCES checklist,
    item_name VARCHAR(50),
    item_description VARCHAR(100),
    item_state STATE,
    PRIMARY KEY (item_id, checklist_id)
  );