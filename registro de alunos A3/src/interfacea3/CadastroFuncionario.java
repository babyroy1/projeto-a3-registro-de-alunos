package interfacea3;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import database.Database;

public class CadastroFuncionario extends JFrame {
    private JTextField txtNome;
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JTextField txtCargo;
    private JButton btnCadastrar;

    public CadastroFuncionario() {
        setTitle("Cadastro de Funcionário");
        setSize(350, 260);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblNome = new JLabel("Nome:");
        JLabel lblUsuario = new JLabel("Email:");
        JLabel lblSenha = new JLabel("Senha:");
        JLabel lblCargo = new JLabel("Cargo:");
        txtNome = new JTextField(15);
        txtUsuario = new JTextField(15);
        txtSenha = new JPasswordField(15);
        txtCargo = new JTextField(15);
        btnCadastrar = new JButton("Cadastrar");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel linha1 = new JPanel();
        linha1.add(lblNome);
        linha1.add(txtNome);
        JPanel linha2 = new JPanel();
        linha2.add(lblUsuario);
        linha2.add(txtUsuario);
        JPanel linha3 = new JPanel();
        linha3.add(lblSenha);
        linha3.add(txtSenha);
        JPanel linha4 = new JPanel();
        linha4.add(lblCargo);
        linha4.add(txtCargo);
        JPanel linha5 = new JPanel();
        linha5.add(btnCadastrar);

        panel.add(linha1);
        panel.add(linha2);
        panel.add(linha3);
        panel.add(linha4);
        panel.add(linha5);

        add(panel);

        btnCadastrar.addActionListener(e -> {
            String nome = txtNome.getText();
            String email = txtUsuario.getText();
            String senha = new String(txtSenha.getPassword());
            String cargo = txtCargo.getText();
            if(nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cargo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            } else {
                try (Connection con = Database.getConnection()) {
                    String sql = "INSERT INTO funcionario (nome, email, senha, cargo) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setString(1, nome);
                    stmt.setString(2, email);
                    stmt.setString(3, senha);
                    stmt.setString(4, cargo);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastroFuncionario().setVisible(true));
    }
}