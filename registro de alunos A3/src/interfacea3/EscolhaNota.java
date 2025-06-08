package interfacea3;

import javax.swing.*;

public class EscolhaNota extends JFrame {
    private JButton btnNovaNota;
    private JButton btnAlterarNota;

    public EscolhaNota() {
        setTitle("Escolha de Ação");
        setSize(300, 150);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        btnNovaNota = new JButton("Enviar nova nota");
        btnAlterarNota = new JButton("Alterar nota enviada");

        btnNovaNota.addActionListener(e -> {
            new MudarNotas().setVisible(true);
            dispose();
        });

        btnAlterarNota.addActionListener(e -> {
            new AtualizarNota().setVisible(true);
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnNovaNota);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnAlterarNota);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EscolhaNota().setVisible(true));
    }
}