package Calendario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class Compromisso {
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String descricao;

    public Compromisso(String descricao, LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public boolean pertencePeriodo(LocalDate data) {
        return !dataInicio.toLocalDate().isAfter(data) && !dataFim.toLocalDate().isBefore(data);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Compromisso: " + descricao + " | Início: " + dataInicio.format(formatter) + " | Fim: " + dataFim.format(formatter);
    }
}

class Agenda {
    private List<Compromisso> compromissos;

    public Agenda() {
        compromissos = new ArrayList<>();
    }

    public void adicionarCompromisso(Compromisso compromisso) {
        compromissos.add(compromisso);
    }

    public List<Compromisso> verificarCompromissos(LocalDate data) {
        List<Compromisso> resultado = new ArrayList<>();
        for (Compromisso c : compromissos) {
            if (c.pertencePeriodo(data)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Compromisso> getCompromissos() {
        return compromissos;
    }
}

public class AgendaCompromissos extends JFrame {
    private Agenda agenda;
    private DefaultListModel<String> listaModel;
    private JList<String> listaCompromissos;
    private JTextField campoDescricao;
    private JTextField campoDataInicio;
    private JTextField campoHoraInicio;
    private JTextField campoDataFim;
    private JTextField campoHoraFim;
    private JTextField campoDataVerificar;

    public AgendaCompromissos() {
        setTitle("Agenda de Compromissos");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        agenda = new Agenda();
        listaModel = new DefaultListModel<>();
        listaCompromissos = new JList<>(listaModel);

        campoDescricao = new JTextField();
        campoDataInicio = new JTextField();
        campoHoraInicio = new JTextField();
        campoDataFim = new JTextField();
        campoHoraFim = new JTextField();
        campoDataVerificar = new JTextField();

        JButton botaoAdicionar = new JButton("Adicionar");
        JButton botaoVerificar = new JButton("Verificar");

        JPanel painelEntrada = new JPanel(new GridLayout(6, 2));
        painelEntrada.add(new JLabel("Descrição:"));
        painelEntrada.add(campoDescricao);
        painelEntrada.add(new JLabel("Data Início (dd/MM/yyyy):"));
        painelEntrada.add(campoDataInicio);
        painelEntrada.add(new JLabel("Hora Início (HH:mm):"));
        painelEntrada.add(campoHoraInicio);
        painelEntrada.add(new JLabel("Data Fim (dd/MM/yyyy):"));
        painelEntrada.add(campoDataFim);
        painelEntrada.add(new JLabel("Hora Fim (HH:mm):"));
        painelEntrada.add(campoHoraFim);

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(botaoAdicionar);

        JPanel painelVerificar = new JPanel(new GridLayout(2, 2));
        painelVerificar.add(new JLabel("Verificar compromissos para o dia (dd/MM/yyyy):"));
        painelVerificar.add(campoDataVerificar);
        painelVerificar.add(botaoVerificar);

        add(painelEntrada, BorderLayout.NORTH);
        add(new JScrollPane(listaCompromissos), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.WEST);
        add(painelVerificar, BorderLayout.SOUTH);

        botaoAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarCompromisso();
            }
        });

        botaoVerificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarCompromissos();
            }
        });

        setVisible(true);
    }

    private void adicionarCompromisso() {
        try {
            String descricao = campoDescricao.getText().trim();
            LocalDate dataInicio = LocalDate.parse(campoDataInicio.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime horaInicio = LocalTime.parse(campoHoraInicio.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalDate dataFim = LocalDate.parse(campoDataFim.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime horaFim = LocalTime.parse(campoHoraFim.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));

            if (descricao.isEmpty()) throw new IllegalArgumentException("Descrição não pode estar vazia.");
            if (dataFim.isBefore(dataInicio) || (dataFim.isEqual(dataInicio) && horaFim.isBefore(horaInicio))) {
                throw new IllegalArgumentException("Data/Fim deve ser após Data/Início.");
            }

            Compromisso compromisso = new Compromisso(descricao, LocalDateTime.of(dataInicio, horaInicio), LocalDateTime.of(dataFim, horaFim));
            agenda.adicionarCompromisso(compromisso);
            
            listaModel.addElement(compromisso.toString());

            // Limpa campos após adicionar
            campoDescricao.setText("");
            campoDataInicio.setText("");
            campoHoraInicio.setText("");
            campoDataFim.setText("");
            campoHoraFim.setText("");

            JOptionPane.showMessageDialog(this, "Compromisso adicionado com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verificarCompromissos() {
        listaModel.clear();

        try {
            LocalDate dataVerificada = LocalDate.parse(campoDataVerificar.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            List<Compromisso> compromissos = agenda.verificarCompromissos(dataVerificada);

            if (compromissos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum compromisso para " + dataVerificada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Compromisso c : compromissos) {
                    listaModel.addElement(c.toString());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AgendaCompromissos();
    }
}
