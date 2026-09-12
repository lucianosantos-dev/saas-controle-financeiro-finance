package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.CartaoRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.CartaoUpdateRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CartaoResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.Cartao;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import com.lucianodev.saascontrolefinanceirofinance.exception.OperacaoInvalidaException;
import com.lucianodev.saascontrolefinanceirofinance.exception.ResourceNotFoundException;
import com.lucianodev.saascontrolefinanceirofinance.mapper.CartaoMapper;
import com.lucianodev.saascontrolefinanceirofinance.repository.CartaoRepository;
import com.lucianodev.saascontrolefinanceirofinance.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CartaoService {

    private final CartaoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CartaoMapper cartaoMapper;

    public CartaoService(CartaoRepository repository, UsuarioRepository usuarioRepository, CartaoMapper cartaoMapper) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.cartaoMapper = cartaoMapper;
    }


    @Transactional
    public CartaoResponse create(CartaoRequest request, UUID idUsuario) {
        Usuario usuario = usuarioRepository.getReferenceById(idUsuario);

        if (repository.existsByNomeAndUsuarioAndAtivoTrue(request.nome(), usuario)) {
            throw new OperacaoInvalidaException("Você já possui um cartão ativo com esse nome.");
        }
        if (request.diaFechamento().equals(request.diaVencimento())) {
            throw new OperacaoInvalidaException("O dia do fechamento não pode ser igual ao dia do vencimento.");
        }

        Cartao cartao = cartaoMapper.toEntity(request);
        cartao.setUsuario(usuario);

        Cartao cartaoSalvo = repository.save(cartao);
        return cartaoMapper.toResponse(cartaoSalvo);
    }

    @Transactional
    public CartaoResponse update(CartaoUpdateRequest request, UUID id, UUID idUsuario) {
        Cartao cartao = repository.findByIdAndUsuarioId(id, idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString()));

        if (repository.existsByNomeAndUsuarioAndAtivoTrueAndIdNot(request.nome(), cartao.getUsuario(), id)) {
            throw new OperacaoInvalidaException("Você já possui um cartão ativo com esse nome.");
        }
        if (request.diaFechamento().equals(request.diaVencimento())) {
            throw new OperacaoInvalidaException("O dia do fechamento não pode ser igual ao dia do vencimento.");
        }

        cartaoMapper.updateCartao(request, cartao);

        Cartao atualizado = repository.save(cartao);

        return cartaoMapper.toResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public CartaoResponse findById(UUID id, UUID idUsuario) {
        Cartao cartao = repository.findByIdAndUsuarioIdAndAtivoTrue(id, idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString()));

        return cartaoMapper.toResponse(cartao);
    }

    @Transactional(readOnly = true)
    public List<CartaoResponse> findAll(UUID idUsuario) {
        List<Cartao> list = repository.findByUsuarioIdAndAtivoTrue(idUsuario);

        return list
                .stream()
                .map(cartaoMapper::toResponse)
                .toList();
    }

    @Transactional
    public void ativarCartao(UUID id, UUID idUsuario) {
        Cartao cartao = repository.findByIdAndUsuarioId(id, idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString()));

        cartao.ativar();
    }

    @Transactional
    public void desativarCartao(UUID id, UUID idUsuario) {
        Cartao cartao = repository.findByIdAndUsuarioId(id, idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString()));

        cartao.desativar();
    }
}















