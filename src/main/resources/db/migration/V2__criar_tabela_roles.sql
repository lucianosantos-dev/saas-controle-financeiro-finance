CREATE TABLE roles(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    nome VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE usuarios_roles(
    usuario_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY(usuario_id, role_id),

    FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE CASCADE
    );

INSERT INTO roles(nome) VALUES('ROLE_ADMIN');
INSERT INTO roles(nome) VALUES('ROLE_USER');