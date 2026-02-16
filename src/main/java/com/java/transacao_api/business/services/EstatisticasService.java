package com.java.transacao_api.business.services;

import java.util.DoubleSummaryStatistics;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.java.transacao_api.controller.dtos.EstatisticasResponseDTO;
import com.java.transacao_api.controller.dtos.TransacaoRequestDTO;

@Service
public class EstatisticasService {

    private static final Logger log =
            LoggerFactory.getLogger(EstatisticasService.class);

    private final TransacaoService transacaoService;

    public EstatisticasService(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    public EstatisticasResponseDTO calcularEstatisticasTransacoes(Integer intervaloBusca) {

        log.info("Iniciada busca de estatísticas pelo período de {} segundos", intervaloBusca);

        long start = System.currentTimeMillis();

        List<TransacaoRequestDTO> transacoes =
                transacaoService.buscarTransacoes(intervaloBusca);

        if (transacoes.isEmpty()) {
            return new EstatisticasResponseDTO(0L, 0.0, 0.0, 0.0, 0.0);
        }

        DoubleSummaryStatistics estatisticas =
                transacoes.stream()
                        .mapToDouble(TransacaoRequestDTO::valor)
                        .summaryStatistics();

        long tempoRequisicao = System.currentTimeMillis() - start;

        System.out.println("Tempo de requisição: " + tempoRequisicao + "ms");

        log.info("Estatisticas retornadas com sucesso");

        return new EstatisticasResponseDTO(
                estatisticas.getCount(),
                estatisticas.getSum(),
                estatisticas.getAverage(),
                estatisticas.getMin(),
                estatisticas.getMax()
        );
    }
}