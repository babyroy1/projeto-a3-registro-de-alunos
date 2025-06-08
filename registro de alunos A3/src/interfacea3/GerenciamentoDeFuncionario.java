/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package interfacea3;

import database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Otavio
 */
public class GerenciamentoDeFuncionario extends javax.swing.JFrame {

    public GerenciamentoDeFuncionario() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Funcionário");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblEmail = new JLabel("Email do Funcionário:");
        JTextField txtEmail = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");

        JLabel lblNome = new JLabel("Nome:");
        JTextField txtNome = new JTextField(15);
        JLabel lblSenha = new JLabel("Senha:");
        JTextField txtSenha = new JTextField(15);
        JLabel lblCargo = new JLabel("Cargo:");
        JTextField txtCargo = new JTextField(15);

        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnDeletar = new JButton("Deletar");

        JTable tabela = new JTable(new DefaultTableModel(
            new Object[]{"ID", "Nome", "Email", "Senha", "Cargo"}, 0
        ));
        JScrollPane scroll = new JScrollPane(tabela);

        // Buscar funcionário
        btnBuscar.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite o email do funcionário.");
                return;
            }
            try (Connection con = Database.getConnection()) {
                String sql = "SELECT id, nome, email, senha, cargo FROM funcionario WHERE email = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                DefaultTableModel model = (DefaultTableModel) tabela.getModel();
                model.setRowCount(0);
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("cargo")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        // Selecionar linha da tabela para editar
        tabela.getSelectionModel().addListSelectionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row >= 0) {
                txtNome.setText(tabela.getValueAt(row, 1).toString());
                txtEmail.setText(tabela.getValueAt(row, 2).toString());
                txtSenha.setText(tabela.getValueAt(row, 3).toString());
                txtCargo.setText(tabela.getValueAt(row, 4).toString());
            }
        });

        // Atualizar funcionário
        btnAtualizar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um funcionário na tabela.");
                return;
            }
            int id = (int) tabela.getValueAt(row, 0);
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String senha = txtSenha.getText().trim();
            String cargo = txtCargo.getText().trim();
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cargo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }
            try (Connection con = Database.getConnection()) {
                String sql = "UPDATE funcionario SET nome=?, email=?, senha=?, cargo=? WHERE id=?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, nome);
                stmt.setString(2, email);
                stmt.setString(3, senha);
                stmt.setString(4, cargo);
                stmt.setInt(5, id);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
                    // Atualiza a tabela
                    ((DefaultTableModel) tabela.getModel()).setValueAt(nome, row, 1);
                    ((DefaultTableModel) tabela.getModel()).setValueAt(email, row, 2);
                    ((DefaultTableModel) tabela.getModel()).setValueAt(senha, row, 3);
                    ((DefaultTableModel) tabela.getModel()).setValueAt(cargo, row, 4);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar funcionário.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        // Deletar funcionário
        btnDeletar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um funcionário na tabela.");
                return;
            }
            int id = (int) tabela.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este funcionário?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection con = Database.getConnection()) {
                    String sql = "DELETE FROM funcionario WHERE id=?";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setInt(1, id);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Funcionário deletado com sucesso!");
                        ((DefaultTableModel) tabela.getModel()).removeRow(row);
                        txtNome.setText("");
                        txtEmail.setText("");
                        txtSenha.setText("");
                        txtCargo.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao deletar funcionário.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            }
        });

        JPanel panelBusca = new JPanel();
        panelBusca.add(lblEmail);
        panelBusca.add(txtEmail);
        panelBusca.add(btnBuscar);

        JPanel panelEdita = new JPanel();
        panelEdita.add(lblNome);
        panelEdita.add(txtNome);
        panelEdita.add(lblSenha);
        panelEdita.add(txtSenha);
        panelEdita.add(lblCargo);
        panelEdita.add(txtCargo);
        panelEdita.add(btnAtualizar);
        panelEdita.add(btnDeletar);

        getContentPane().add(panelBusca, "North");
        getContentPane().add(scroll, "Center");
        getContentPane().add(panelEdita, "South");
        setSize(800, 450);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GerenciamentoDeFuncionario().setVisible(true));
    }
}
