package org.example.services;

import org.example.dto.AcaoIndicadoresDTO;
import org.example.mapper.AcaoIndicadoresMapper;
import org.example.model.AcaoIndicadoresModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExtracaoFundamentusService {
    public List<AcaoIndicadoresModel> extract() throws IOException {

        Document doc = Jsoup.connect("https://www.fundamentus.com.br/resultado.php").get();
        Element table = doc.select("table#resultado").first();

        boolean header = true;
        List<AcaoIndicadoresDTO> listaDeAcoes = new ArrayList<>();
        for (Element row : table.select("tr")) {
            if (header) {
                header = false;
                continue;
            }
            Elements column = row.select("td");
            AcaoIndicadoresDTO dto = new AcaoIndicadoresDTO();
            dto.setPapel(limparTextoExtraido(column.get(0).select("a").text()));
            dto.setCotacao(limparTextoExtraido(column.get(1).text()));
            dto.setPl(limparTextoExtraido(column.get(2).text()));
            dto.setEvEbit(limparTextoExtraido(column.get(10).text()));
            dto.setRoic(limparTextoExtraido(limparPorcentagem(column.get(15).text())));
            dto.setLiq2meses(limparTextoExtraido(column.get(17).text()));
            listaDeAcoes.add(dto);
        }
        AcaoIndicadoresMapper mapper = new AcaoIndicadoresMapper();
        List <AcaoIndicadoresModel> listaAcoesTratadas = listaDeAcoes.stream()
                .map(x -> mapper.toModel(x)).collect(Collectors.toList());

        return listaAcoesTratadas;
        }


    private static String limparTextoExtraido(String texto){
        return texto.replace(".","").replace("," , ".");
    }
    private static String limparPorcentagem (String texto){
        return texto.replace("%","");

    }

}

