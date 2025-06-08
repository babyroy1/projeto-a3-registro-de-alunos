package interfacea3;

import javax.swing.*;

public class LoginFuncionario extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public LoginFuncionario() {
        setTitle("Login de Funcionário");
        setSize(350, 180);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblUsuario = new JLabel("Email:");
        JLabel lblSenha = new JLabel("Senha:");
        txtUsuario = new JTextField(15);
        txtSenha = new JPasswordField(15);
        btnEntrar = new JButton("Entrar");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel linha1 = new JPanel();
        linha1.add(lblUsuario);
        linha1.add(txtUsuario);
        JPanel linha2 = new JPanel();
        linha2.add(lblSenha);
        linha2.add(txtSenha);
        JPanel linha3 = new JPanel();
        linha3.add(btnEntrar);

        panel.add(linha1);
        panel.add(linha2);
        panel.add(linha3);

        add(panel);

        btnEntrar.addActionListener(e -> {
            String email = txtUsuario.getText();
            String senha = new String(txtSenha.getPassword());
            if(email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }
            // Aqui você pode colocar a validação no banco
            JOptionPane.showMessageDialog(this, "Login de Funcionário clicado!");
            // Abre o JFrame de escolha após login de sucesso
            new EscolhaNota().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFuncionario().setVisible(true));
    }
}