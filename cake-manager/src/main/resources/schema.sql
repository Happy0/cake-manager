CREATE TABLE IF NOT EXISTS
    cake (
        id SERIAL PRIMARY KEY,
        title VARCHAR(100),
        description VARCHAR(500),
        image VARCHAR(1000)
    );