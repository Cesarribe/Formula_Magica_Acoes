package org.example.app;


import org.example.dto.AcaoIndicadoresDTO;
import org.example.mapper.AcaoIndicadoresMapper;
import org.example.model.AcaoIndicadoresModel;
import org.example.services.ExportarCsvService;
import org.example.services.ExtracaoDeExclusoesService;
import org.example.services.ExtracaoFundamentusService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


public class Application {

    private static ExtracaoFundamentusService extracaoService = new ExtracaoFundamentusService();
    private static ExtracaoDeExclusoesService exclusaoService = new ExtracaoDeExclusoesService();
    private static ExportarCsvService csvService = new ExportarCsvService();


    public static void main(String[] args) throws IOException {

        List<AcaoIndicadoresModel> listaAcoesModel = extracaoService.extract();

        listaAcoesModel = listaAcoesModel.stream()
                .filter(x -> x.getLiq2meses().compareTo(new BigDecimal("100000")) >= 0)
                .collect(Collectors.toList());
        listaAcoesModel = listaAcoesModel.stream() // EvEbit negativa
                .filter(x -> x.getEvEbit().compareTo(BigDecimal.ZERO) >= 0)
                .collect(Collectors.toList());

        // Remover seguro, banco e Energia
        Set<String> acoesSeguro = exclusaoService.extract("31");
        Set<String> acoesBanco = exclusaoService.extract("20");
        Set<String> acoesEnergia = exclusaoService.extract("14");

        Set <String> exclusoes = acoesSeguro;
        exclusoes.addAll(acoesEnergia);
        exclusoes.addAll(acoesBanco);

        listaAcoesModel = listaAcoesModel.stream().
                filter(x->!exclusoes.contains(x.getPapel())).collect(Collectors.toList());


        // filtrar acoes repetidas
        Map<String, AcaoIndicadoresModel> papeisJaLidos = new HashMap<>();
        for (var acao: listaAcoesModel) {
            String papel = acao.getPapel().replaceAll("0-9", "null");
            if (papeisJaLidos.keySet().contains(papel)) {
                if (acao.getLiq2meses().compareTo(papeisJaLidos.get(papel).getLiq2meses()) >= 0) {
                papeisJaLidos.put(papel, acao);
                }
            } else {
                papeisJaLidos.put(papel, acao);
            }
        }
        listaAcoesModel = papeisJaLidos.values().stream().collect(Collectors.toList());


        // Ranking EvEbit
        listaAcoesModel.sort(new Comparator <AcaoIndicadoresModel>() {
            @Override
            public int compare(AcaoIndicadoresModel a1, AcaoIndicadoresModel a2) {
                return a1.getEvEbit().compareTo(a2.getEvEbit());
            }
        });
        int tamanho = listaAcoesModel.size();
        for (int i = 0; i< tamanho; i++) {
            listaAcoesModel.get(i).setPosicaoEvEbit(i+1);
        }

        // Rank Roic
        listaAcoesModel.sort(new Comparator <AcaoIndicadoresModel>() {
            @Override
            public int compare(AcaoIndicadoresModel a1, AcaoIndicadoresModel a2) {
                return a2.getRoic().compareTo(a1.getRoic());
            }
        });

        for (int i = 0; i< tamanho; i++) {
            listaAcoesModel.get(i).setPosicaoRoic(i+1);
            listaAcoesModel.get(i).setPosicaoFinal(listaAcoesModel.get(i).getPosicaoEvEbit() +
                    listaAcoesModel.get(i).getPosicaoRoic());
        }

        // Ordenaçào por posição final.
        listaAcoesModel.sort(new Comparator <AcaoIndicadoresModel>() {
            @Override
            public int compare(AcaoIndicadoresModel a1, AcaoIndicadoresModel a2) {
                return a1.getPosicaoFinal() - a2.getPosicaoFinal();
            }
        });


        for (int i = 0; i< tamanho; i++) {
            listaAcoesModel.get(i).setPosicaoFinal(i+1);
        }
        for (var acao: listaAcoesModel){
            System.out.println(acao);
        }
        csvService.exportarCsv(listaAcoesModel);
    }
}