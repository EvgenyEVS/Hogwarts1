ALTER TABLE student ADD CONSTRAINT age_constraint CHECK ( AGE > 15 );

ALTER TABLE student ADD CONSTRAINT name_unique UNIQUE (name);

ALTER TABLE student ALTER COLUMN name SET  NOT NULL;

ALTER TABLE student ALTER COLUMN age SET DEFAULT 20;

ALTER TABLE faculty
    ADD CONSTRAINT unique_name_and_color UNIQUE (name, color);

