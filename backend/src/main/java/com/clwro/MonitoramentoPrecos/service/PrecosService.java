package com.clwro.MonitoramentoPrecos.service;

import com.clwro.MonitoramentoPrecos.model.Precos;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.clwro.MonitoramentoPrecos.repository.PrecosRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrecosService {

    private final WebClient webClient;
    private final PrecosRepository precosRepository;

    private static final String DOWNLOAD_URL = "https://www.ccee.org.br/documents/80415/919464/Historico_do_Preco_Medio_Mensal_-_janeiro_de_2001_a_julho_de_2025.xls/ef315202-326b-6ced-368e-0f306c9fe1f3?version=1.32&t=1675341728056&download=true";

    public PrecosService(WebClient.Builder webClientBuilder, PrecosRepository precosRepository) {
        this.webClient = webClientBuilder.build();
        this.precosRepository = precosRepository;
    }

    public byte[] downloadFile(){
        return webClient.get().uri(DOWNLOAD_URL).retrieve().bodyToMono(byte[].class).block();
    }

    public void downloadAndProcess() throws IOException{
        byte[] fileContent = downloadFile();
        List<Precos> precos = parsePrecos(new ByteArrayInputStream(fileContent));
        if(!precos.isEmpty()){
            precosRepository.deleteAll();
            precosRepository.saveAll(precos);
        }
    };

    private List<Precos> parsePrecos(InputStream inputStream) throws IOException{
        List<Precos> precos = new ArrayList<>();

        LocalDate anoPassado = LocalDate.now().withDayOfMonth(1).minusMonths(12);

        try(Workbook workbook = new HSSFWorkbook(inputStream)){
            Sheet sheet = workbook.getSheetAt(0);
            final int dateCol = 0;
            final int sudesteCol = 1;
            final int sulCol = 2;
            final int nordesteCol = 3;
            final int norteCol = 4;

            for(Row row : sheet) {
                if(row.getRowNum() < 3) continue;

                Cell dateCell = row.getCell(dateCol);
                if(dateCell == null) continue;

                LocalDate rowDate = parseDate(dateCell);

                if(rowDate != null & !rowDate.isBefore(anoPassado)){
                    addPreco(precos, row, "Sudeste", sudesteCol, rowDate);
                    addPreco(precos, row, "Sul", sulCol, rowDate);
                    addPreco(precos, row, "Nordeste", nordesteCol, rowDate);
                    addPreco(precos, row, "Norte", norteCol, rowDate);
                };
            }

        }
        return precos;
    }

    private void addPreco(List<Precos> precos, Row row, String region, int regionIndex, LocalDate date){
        Cell priceCell = row.getCell(regionIndex);

        if(priceCell != null && priceCell.getCellType() == CellType.NUMERIC){
            double price = priceCell.getNumericCellValue();
            precos.add(new Precos(region, date, price));
        }
    }

    private LocalDate parseDate(Cell dateCell){
        if(dateCell.getCellType() == CellType.STRING){
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDate.parse(dateCell.getStringCellValue(), formatter);
            } catch(DateTimeParseException e){}
                return null;
        }else if(dateCell.getCellType() == CellType.NUMERIC){
            return dateCell.getLocalDateTimeCellValue().toLocalDate();
        }
        return null;
    }
}
