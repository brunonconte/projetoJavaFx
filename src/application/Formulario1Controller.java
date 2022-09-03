package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Formulario1Controller {
	@FXML
	private TextField inputId;

	@FXML
	private TextField inputNome;
	@FXML
	private TextField inputSalario;
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnSalvarEdicao;
	
	@FXML
	private TableView tableFuncionarios;
	@FXML
	private TableColumn colId;
	@FXML
	private TableColumn colNome;
	@FXML
	private TableColumn colSalario;
	
	
	public void initialize() {
		
			colId.setCellValueFactory(new PropertyValueFactory<>("id"));
			colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
			colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
			
			tableFuncionarios.getItems().setAll( getListaFuncionarios() );
			
			btnSalvarEdicao.setVisible(false);
		
		
		
	}
	
	private List<Funcionario> getListaFuncionarios() {

		try {
			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://localhost/banco1";
			String username = "root";
			String password = "admin";

			Connection connection = DriverManager.getConnection(url, username, password);
			System.out.println("banco conectado");

			// Codigo para select
			PreparedStatement prepStmt = connection
					.prepareStatement("select id, nome, salario from banco1.funcionario");
			ResultSet rs = prepStmt.executeQuery();

			List<Funcionario> listaFuncionario = new ArrayList<Funcionario>();
			
			while (rs.next()) {

				Funcionario func = new Funcionario();

				func.setId(rs.getInt("id"));
				func.setNome(rs.getString("nome"));
				func.setSalario(rs.getFloat("salario"));

				listaFuncionario.add(func);

			}

			rs.close();
			prepStmt.close();
			connection.close();

			return listaFuncionario;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	@FXML
	public void deletarRegistro(ActionEvent event) {
		
		Funcionario c = (Funcionario) tableFuncionarios.getSelectionModel().getSelectedItem();
		
		int id = c.getId();
		
		try {

			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://localhost/banco1";
			String username = "root";
			String password = "admin";

			Connection connection = DriverManager.getConnection(url, username, password);
			System.out.println("banco conectado");

			Statement stmt = connection.createStatement();

			stmt.executeUpdate("delete from banco1.funcionario where id =" + id);

			stmt.close();
			connection.close();
			
			tableFuncionarios.getItems().setAll( getListaFuncionarios() );

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML
	public void carregarEdicao(ActionEvent event) {
		
		Funcionario c = (Funcionario) tableFuncionarios.getSelectionModel().getSelectedItem();
		
		
		inputId.setText( String.valueOf(c.getId())  );
		inputNome.setText(c.getNome() );
		inputSalario.setText( String.valueOf(c.getSalario())  );
		
		btnSalvar.setVisible(false);
		
		btnSalvarEdicao.setVisible(true);
		
		
		
	}
	
	@FXML
	public void salvarEdicao(ActionEvent event) {
	
		try {

			int id = Integer.parseInt(inputId.getText());
			String nome = inputNome.getText();
			float salario = Float.parseFloat(inputSalario.getText());

			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://localhost/banco1";
			String username = "root";
			String password = "admin";

			Connection connection = DriverManager.getConnection(url, username, password);
			System.out.println("banco conectado");

			Statement stmt = connection.createStatement();

			stmt.executeUpdate("update banco1.funcionario set nome ='"+nome+"', salario ="+ salario+" where id ="+ id );

			stmt.close();
			connection.close();
			
			tableFuncionarios.getItems().setAll( getListaFuncionarios() );
			
			btnSalvarEdicao.setVisible(false);
			btnSalvar.setVisible(true);
			
			inputId.setText( "" );
			inputNome.setText("");
			inputSalario.setText("");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	// Event Listener on Button[#btnSalvar].onAction
	@FXML
	public void salvarDados(ActionEvent event) {

		int id = Integer.parseInt(inputId.getText());
		String nome = inputNome.getText();
		float salario = Float.parseFloat(inputSalario.getText());
		try {

			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://localhost/banco1";
			String username = "root";
			String password = "admin";

			Connection connection = DriverManager.getConnection(url, username, password);
			System.out.println("banco conectado");

			Statement stmt = connection.createStatement();

			stmt.executeUpdate("insert into banco1.funcionario(id, nome, salario) values (" + id + ",'" + nome + "',"
					+ salario + ")");

			stmt.close();
			connection.close();
			
			tableFuncionarios.getItems().setAll( getListaFuncionarios() );
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
