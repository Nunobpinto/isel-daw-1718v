
DROP TABLE IF EXISTS template;
DROP TABLE IF EXISTS checklist;
DROP TABLE IF EXISTS item;

DROP TYPE IF EXISTS STATE;

CREATE TABLE template(
    template_id SERIAL PRIMARY KEY,
    template_name VARCHAR(50),
    template_description VARCHAR(50),
    item_name VARCHAR(50),
    item_desc VARCHAR(50)
  );

  CREATE TABLE checklist(
    checklist_id SERIAL PRIMARY KEY,
    checklist_name VARCHAR(50),
    completion_date date,
    template INTEGER REFERENCES template
  );

  CREATE TYPE STATE AS ENUM ('Uncompleted', 'Completed');

  CREATE TABLE item (
    item_id SERIAL,
    checklist_id INTEGER REFERENCES checklist,
    item_name VARCHAR(50),
    item_description VARCHAR(50),
    state STATE,
    PRIMARY KEY (item_id, checklist_id)
  )


