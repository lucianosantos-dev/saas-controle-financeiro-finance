CREATE
EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE usuarios
(
    id               UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    nome             VARCHAR(120) NOT NULL,
    email            VARCHAR(160) NOT NULL UNIQUE,
    senha_hash       VARCHAR(255) NOT NULL,
    email_verificado BOOLEAN      NOT NULL DEFAULT FALSE,
    moeda            VARCHAR(3)   NOT NULL DEFAULT 'BRL',
    fuso_horario     VARCHAR(50)  NOT NULL DEFAULT 'America/Sao_Paulo',
    criado_em        TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE tokens_verificacao
(
    id               UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    usuario_id       UUID         NOT NULL,
    token            VARCHAR(255) NOT NULL UNIQUE,
    tipo_verificacao VARCHAR(30)  NOT NULL CHECK (tipo_verificacao IN ('CONFIRMACAO_EMAIL', 'REDEFINICAO_SENHA')),
    expira_em        TIMESTAMP    NOT NULL,
    usado_em         TIMESTAMP,
    criado_em        TIMESTAMP    NOT NULL DEFAULT NOW(),

    FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE
);

CREATE INDEX idx_token_verificacao_token ON tokens_verificacao (token);

CREATE TABLE categorias
(
    id             UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    usuario_id     UUID,
    nome           VARCHAR(60) NOT NULL,
    tipo_categoria VARCHAR(20) NOT NULL CHECK (tipo_categoria IN ('RECEITA', 'DESPESA')),
    ativo          BOOLEAN     NOT NULL DEFAULT TRUE,
    criado_em      TIMESTAMP   NOT NULL DEFAULT NOW(),

    FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX uq_categoria_global ON categorias (nome, tipo_categoria) WHERE usuario_id IS NULL;
CREATE UNIQUE INDEX uq_categoria_usuario ON categorias (usuario_id, nome, tipo_categoria) WHERE usuario_id IS NOT NULL;

CREATE TABLE cartoes
(
    id             UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    usuario_id     UUID           NOT NULL,
    nome           VARCHAR(60)    NOT NULL,
    limite_credito DECIMAL(12, 2) NOT NULL CHECK (limite_credito > 0),
    dia_fechamento SMALLINT       NOT NULL CHECK (dia_fechamento BETWEEN 1 AND 31),
    dia_vencimento SMALLINT       NOT NULL CHECK (dia_vencimento BETWEEN 1 AND 31),
    ativo          BOOLEAN        NOT NULL DEFAULT TRUE,
    criado_em      TIMESTAMP      NOT NULL DEFAULT NOW(),

    FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE
);

CREATE TABLE transacoes
(
    id              UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    usuario_id      UUID           NOT NULL,
    categoria_id    UUID           NOT NULL,
    cartao_id       UUID,
    descricao       TEXT,
    valor           DECIMAL(12, 2) NOT NULL CHECK (valor > 0),
    data_transacao  DATE           NOT NULL,
    forma_pagamento VARCHAR(20)    NOT NULL CHECK (forma_pagamento IN ('DINHEIRO', 'CONTA', 'CARTAO')),
    situacao        VARCHAR(20)    NOT NULL CHECK (situacao IN ('PENDENTE', 'CONFIRMADA')),
    recorrente      BOOLEAN        NOT NULL DEFAULT FALSE,
    dia_recorrencia SMALLINT CHECK (dia_recorrencia BETWEEN 1 AND 31),
    mes_referencia  DATE,
    criado_em       TIMESTAMP      NOT NULL DEFAULT NOW(),
    atualizado_em   TIMESTAMP      NOT NULL DEFAULT NOW(),

    FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categorias (id) ON DELETE RESTRICT,
    FOREIGN KEY (cartao_id) REFERENCES cartoes (id) ON DELETE RESTRICT
);