CREATE TABLE todos (
    id UUID DEFAULT GEN_RANDOM_UUID() NOT NULL,
    owner text,
    text text,
    done BOOLEAN,
    PRIMARY KEY (id)
);
