package interfacea3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class AtualizarNota extends JFrame {
    private JTextField txtIdAluno;
    private JTextField txtNovaNota;
    private JButton btnBuscar;
    private JButton btnAtualizar;
    private JButton btnDeletar;
    private JTable tabelaNotas;
    private int idNotaSelecionada = -1;

    public AtualizarNota() {
        setTitle("Atualizar Nota");
        setSize(500, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblIdAluno = new JLabel("ID do Aluno:");
        txtIdAluno = new JTextField(10);
        btnBuscar = new JButton("Buscar Notas");

        JLabel lblNovaNota = new JLabel("Nova Nota:");
        txtNovaNota = new JTextField(10);
        btnAtualizar = new JButton("Atualizar");
        btnDeletar = new JButton("Deletar Nota");

        tabelaNotas = new JTable(new DefaultTableModel(
            new Object[]{"ID Nota", "ID Curso", "Nota", "Data Lançada"}, 0
        ));
        JScrollPane scroll = new JScrollPane(tabelaNotas);

        JPanel painelBusca = new JPanel();
        painelBusca.add(lblIdAluno);
        painelBusca.add(txtIdAluno);
        painelBusca.add(btnBuscar);

        JPanel painelAtualiza = new JPanel();
        painelAtualiza.add(lblNovaNota);
        painelAtualiza.add(txtNovaNota);
        painelAtualiza.add(btnAtualizar);
        painelAtualiza.add(btnDeletar);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(painelBusca);
        add(scroll);
        add(painelAtualiza);

        btnBuscar.addActionListener(e -> buscarNotas());
        tabelaNotas.getSelectionModel().addListSelectionListener(e -> {
            int row = tabelaNotas.getSelectedRow();
            if (row >= 0) {
                idNotaSelecionada = (int) tabelaNotas.getValueAt(row, 0);
            }
        });

        btnAtualizar.addActionListener(e -> {
            if (idNotaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma nota na tabela.");
                return;
            }
            String novaNotaStr = txtNovaNota.getText().trim();
            if (novaNotaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite a nova nota.");
                return;
            }
            try {
                double novaNota = Double.parseDouble(novaNotaStr);
                try (Connection con = database.Database.getConnection()) {
                    String sql = "UPDATE notas SET nota = ? WHERE id = ?";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setDouble(1, novaNota);
                    stmt.setInt(2, idNotaSelecionada);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Nota atualizada com sucesso!");
                        buscarNotas(); // Atualiza a tabela
                        txtNovaNota.setText("");
                        idNotaSelecionada = -1;
                    } else {
                        JOptionPane.showMessageDialog(this, "ID da nota não encontrado.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nota inválida.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar nota: " + ex.getMessage());
            }
        });

        btnDeletar.addActionListener(e -> {
            if (idNotaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma nota na tabela para deletar.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar esta nota?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection con = database.Database.getConnection()) {
                    String sql = "DELETE FROM notas WHERE id = ?";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setInt(1, idNotaSelecionada);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Nota deletada com sucesso!");
                        buscarNotas(); // Atualiza a tabela
                        txtNovaNota.setText("");
                        idNotaSelecionada = -1;
                    } else {
                        JOptionPane.showMessageDialog(this, "ID da nota não encontrado.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao deletar nota: " + ex.getMessage());
                }
            }
        });
    }

    private void buscarNotas() {
        String idAlunoStr = txtIdAluno.getText().trim();
        if (idAlunoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o ID do aluno.");
            return;
        }
        try {
            int idAluno = Integer.parseInt(idAlunoStr);
            try (Connection con = database.Database.getConnection()) {
                String sql = "SELECT id, id_curso, nota, data_lançada FROM notas WHERE id_aluno = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setInt(1, idAluno);
                ResultSet rs = stmt.executeQuery();
                DefaultTableModel model = (DefaultTableModel) tabelaNotas.getModel();
                model.setRowCount(0);
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("id_curso"),
                        rs.getDouble("nota"),
                        rs.getDate("data_lançada")
                    });
                }
                idNotaSelecionada = -1;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID do aluno inválido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar notas: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AtualizarNota().setVisible(true));
    }
}
