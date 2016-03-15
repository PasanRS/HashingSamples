package org.tharindue.sample.hashing.app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HashingApp extends JFrame {

	private JFrame frmHashingApp;
	private JTextField txtPlainTextPassword;
	private JTextField txtSaltValue;
	private JPanel panelSalt;
	private JCheckBox chkSaltedPassword;
	private JLabel lblPlainTextPassword;
	private JComboBox cmbHashingAlgorithm;
	private JTextPane txtOutput;
	private JLabel lblHashingAlgorithm;
	private JLabel lblSaltValue_1;
	private JButton btnHash;
	private JLabel lblError;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HashingApp window = new HashingApp();
					window.frmHashingApp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HashingApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHashingApp = new JFrame();
		frmHashingApp.setResizable(false);
		frmHashingApp.setTitle("Hashing App");
		frmHashingApp.setBounds(100, 100, 1044, 397);
		frmHashingApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmHashingApp.getContentPane().setLayout(null);

		lblPlainTextPassword = new JLabel("Plain Text Password");
		lblPlainTextPassword.setBounds(34, 30, 143, 15);
		frmHashingApp.getContentPane().add(lblPlainTextPassword);

		txtPlainTextPassword = new JTextField();
		txtPlainTextPassword.setBounds(195, 22, 301, 32);
		frmHashingApp.getContentPane().add(txtPlainTextPassword);
		txtPlainTextPassword.setColumns(10);

		chkSaltedPassword = new JCheckBox("Salted Password");
		chkSaltedPassword.setBounds(512, 22, 145, 23);
		frmHashingApp.getContentPane().add(chkSaltedPassword);

		lblHashingAlgorithm = new JLabel("Hashing Algorithm");
		lblHashingAlgorithm.setBounds(34, 83, 147, 15);
		frmHashingApp.getContentPane().add(lblHashingAlgorithm);

		cmbHashingAlgorithm = new JComboBox();
		cmbHashingAlgorithm.setBounds(195, 78, 301, 24);
		frmHashingApp.getContentPane().add(cmbHashingAlgorithm);
		cmbHashingAlgorithm.setModel(
				new DefaultComboBoxModel(new String[] { "MD2", "MD5", "SHA-1", "SHA-256", "SHA-384", "SHA-512" }));

		panelSalt = new JPanel();
		panelSalt.setBounds(513, 51, 437, 77);
		frmHashingApp.getContentPane().add(panelSalt);
		panelSalt.setLayout(null);
		panelSalt.setVisible(false);

		txtSaltValue = new JTextField();
		txtSaltValue.setBounds(128, 12, 292, 26);
		panelSalt.add(txtSaltValue);
		txtSaltValue.setColumns(10);

		lblSaltValue_1 = new JLabel("Salt Value");
		lblSaltValue_1.setBounds(28, 21, 140, 15);
		panelSalt.add(lblSaltValue_1);

		final JCheckBox chkAutoGenerateSalt = new JCheckBox("Auto Generate Salt");
		chkAutoGenerateSalt.setBounds(128, 46, 189, 23);
		panelSalt.add(chkAutoGenerateSalt);

		btnHash = new JButton("Hash");
		btnHash.setBounds(195, 118, 94, 23);
		frmHashingApp.getContentPane().add(btnHash);

		txtOutput = new JTextPane();
		txtOutput.setFont(new Font("Dialog", Font.BOLD, 14));
		txtOutput.setBounds(50, 186, 948, 161);
		frmHashingApp.getContentPane().add(txtOutput);

		lblError = new JLabel("Error Text");
		lblError.setForeground(Color.RED);
		lblError.setBounds(195, 151, 755, 23);
		lblError.setText("");

		frmHashingApp.getContentPane().add(lblError);
		btnHash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				lblError.setText("");
				String plainTextPassword = txtPlainTextPassword.getText();

				if (plainTextPassword == null || "".equals(plainTextPassword)) {
					lblError.setText("Password cannot be empty !");
					txtPlainTextPassword.requestFocus();
					return;
				}

				String hashingAlgorithm = cmbHashingAlgorithm.getSelectedItem().toString();
				boolean isSaltedPassword = chkSaltedPassword.isSelected();
				String hashedPassword;
				String message;

				if (isSaltedPassword) {

					boolean isAutoGenerateSaltEnabled = chkAutoGenerateSalt.isSelected();
					String saltValue = null;

					if (isAutoGenerateSaltEnabled) {
						txtSaltValue.setText("");
						txtSaltValue.setEnabled(false);
						saltValue = HashingUtils.generateSaltValue();
					} else {
						saltValue = txtSaltValue.getText();

						if (saltValue == null || "".equals(saltValue)) {
							lblError.setText("Salt Value cannot be empty when auto generating salt is not enabled !");

							txtSaltValue.requestFocus();
							txtSaltValue.setEnabled(true);

							return;
						}

					}

					hashedPassword = HashingUtils.preparePassword(plainTextPassword, hashingAlgorithm, saltValue);

					message  = "Plaint Text Password : " + plainTextPassword + "\n\n";
					message += "Salt                 : " + saltValue + "\n\n";
					message += "Hashed Password      : " + hashedPassword;
				} else {
					txtSaltValue.setText("");

					hashedPassword = HashingUtils.preparePassword(plainTextPassword, hashingAlgorithm, null);
					message =  "Plaint Text Password : " + plainTextPassword + "\n\n";
					message += "Hashed Password      : " + hashedPassword;

				}

				txtOutput.setText(message);

			}
		});
		chkAutoGenerateSalt.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				if (chkAutoGenerateSalt.isSelected()) {
					txtSaltValue.setText("");
					txtSaltValue.setEnabled(false);
				} else {
					txtSaltValue.setEnabled(true);
					txtSaltValue.requestFocus();
				}

			}
		});
		chkSaltedPassword.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				if (chkSaltedPassword.isSelected()) {

					panelSalt.setVisible(true);

				} else {
					panelSalt.setVisible(false);

				}

			}
		});
	}
}
