package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.CategoriaRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.CategoriaUpdateRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CategoriaDetalheResponse;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CategoriaListaResponse;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CategoriaResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.Categoria;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoCategoria;
import com.lucianodev.saascontrolefinanceirofinance.exception.CategoriaDuplicadaException;
import com.lucianodev.saascontrolefinanceirofinance.exception.CategoriaNaoEncontradaException;
import com.lucianodev.saascontrolefinanceirofinance.exception.OperacaoInvalidaException;
import com.lucianodev.saascontrolefinanceirofinance.mapper.CategoriaMapper;
import com.lucianodev.saascontrolefinanceirofinance.repository.CategoriaRepository;
import com.lucianodev.saascontrolefinanceirofinance.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaRepository repository, UsuarioRepository usuarioRepository, CategoriaMapper categoriaMapper) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Transactional
    public CategoriaResponse create(UUID idUsuario, CategoriaRequest request) {

        Usuario usuario = usuarioRepository.getReferenceById(idUsuario);

        if (repository.existsByNomeAndTipoCategoriaAndUsuario(request.nome(), request.tipo(), usuario)) {
            throw new CategoriaDuplicadaException("Você já possui uma categoria cadastrada com esse nome. Escolha um nome diferente.");
        }

        Categoria categoria = categoriaMapper.toEntity(request);

        categoria.setUsuario(usuario);

        Categoria criada = repository.save(categoria);
        return categoriaMapper.toResponse(criada);
    }

    @Transactional
    public CategoriaResponse update(UUID idCateg, UUID idUsuario, CategoriaUpdateRequest request) {
        Categoria cat = repository.buscarPorIdSeguro(idCateg, idUsuario)
                .orElseThrow(() -> new CategoriaNaoEncontradaException("Categoria não encontrada"));

        if (cat.getUsuario() == null) {
            throw new OperacaoInvalidaException("Categorias do Sistema não podem ser alteradas ou excluídas");
        }

        categoriaMapper.updateCategoria(request, cat);

        Categoria atualizada = repository.save(cat);
        return categoriaMapper.toResponse(atualizada);
    }

    @Transactional(readOnly = true)
    public Page<CategoriaListaResponse> findAll(UUID idUsuario, Pageable pageable) {
        Page<Categoria> page = repository.listarCategoriasUsuario(idUsuario, pageable);
        return page.map(categoriaMapper::toListaResponse);
    }

    @Transactional(readOnly = true)
    public List<CategoriaDetalheResponse> listarPorTipo(UUID idUsuario, TipoCategoria tipo) {
        List<Categoria> list = repository.listarCategoriasUsuarioPeloTipo(idUsuario, tipo);
        return list.stream().map(categoriaMapper::toCategoriaDetalheResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse findById(UUID idCateg, UUID idUsuario) {
        Categoria cat = repository.buscarPorIdSeguro(idCateg, idUsuario)
                .orElseThrow(() -> new CategoriaNaoEncontradaException("Categoria não encontrada"));
        return categoriaMapper.toResponse(cat);
    }
}
