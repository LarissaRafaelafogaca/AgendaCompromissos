package Calendario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private List<Compromisso> compromissos;

    public Agenda() {
        compromissos = new ArrayList<>();
    }

    public void adicionarCompromisso(Compromisso compromisso) {
        compromissos.add(compromisso);
    }

    public List<Compromisso> verificarCompromissos(int numeroDias) {
        List<Compromisso> resultado = new ArrayList<>();
        LocalDateTime hoje = LocalDateTime.now();
        
        for (Compromisso c : compromissos) {
            if (c.pertencePeriodo(hoje, numeroDias)) {
                resultado.add(c);
            }
        }
        return resultado;
    }
}