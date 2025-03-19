package Calendario;

import java.time.LocalDateTime;

public class Compromisso {
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String descricao;

    public Compromisso(String descricao, LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public boolean pertencePeriodo(LocalDateTime inicioPeriodo, int numeroDias) {
        LocalDateTime fimPeriodo = inicioPeriodo.plusDays(numeroDias);
        return (dataInicio.isAfter(inicioPeriodo) || dataInicio.isEqual(inicioPeriodo)) && 
               (dataFim.isBefore(fimPeriodo) || dataFim.isEqual(fimPeriodo));
    }

    @Override
    public String toString() {
        return "Compromisso: " + descricao + " | Início: " + dataInicio + " | Fim: " + dataFim;
    }
}