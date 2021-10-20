create table T_TOPIC (
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(100)
);

create table T_TAG (
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(100)
);

create table T_SUMMARY (
    id            VARCHAR(100) PRIMARY KEY,
    topic_id      VARCHAR(100),
    title         VARCHAR(4000),
    text          MEDIUMTEXT,
    creation_date TIMESTAMP    NOT NULL,
    foreign key(topic_id) references T_TOPIC(id)
);

create table T_INDEX_CARD (
     id            VARCHAR(100) PRIMARY KEY,
     topic_id      VARCHAR(100),
     front_content VARCHAR(4000),
     back_content  VARCHAR(4000),
     creation_date TIMESTAMP    NOT NULL,
     foreign key(topic_id) references T_TOPIC(id)
);

create table T_ENTITY_TAG (
     entity_id VARCHAR(100),
     tag_id VARCHAR(100),
     PRIMARY KEY(entity_id,tag_id),
     FOREIGN KEY(tag_id) REFERENCES T_TAG(id)
);