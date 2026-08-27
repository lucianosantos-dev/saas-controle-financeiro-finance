INSERT INTO categorias(id, nome, tipo_categoria, ativo, criado_em, usuario_id) VALUES
  (gen_random_uuid(), 'Moradia', 'DESPESA', true, now(), null),
  (gen_random_uuid(), 'Alimentação', 'DESPESA', true, now(), null),
  (gen_random_uuid(), 'Transporte', 'DESPESA', true, now(), null),
  (gen_random_uuid(), 'Saúde', 'DESPESA', true, now(), null),
  (gen_random_uuid(), 'Lazer', 'DESPESA', true, now(), null),
  (gen_random_uuid(), 'Educação', 'DESPESA', true, now(), null),
  (gen_random_uuid(), 'Outros', 'DESPESA', true, now(), null),

  (gen_random_uuid(), 'Salário', 'RECEITA', true, now(), null),
  (gen_random_uuid(), 'Freelancer/Extra', 'RECEITA', true, now(), null),
  (gen_random_uuid(), 'Outros', 'RECEITA', true, now(), null)