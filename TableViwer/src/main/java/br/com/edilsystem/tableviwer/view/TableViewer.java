package br.com.edilsystem.tableviwer.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import br.com.edilsystem.tableviwer.model.ProdutoStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewer implements Initializable {

	@FXML
	private TableView<ProdutoStatus> tblMain;
	
	@FXML
	private TableColumn<ProdutoStatus, CheckBox> tblColCheck;

	@FXML
	private TableColumn<ProdutoStatus, Long> tblColId;

	@FXML
	private TableColumn<ProdutoStatus, String> tblColCod;

	@FXML
	private TableColumn<ProdutoStatus, String> tblColDesc;

	@FXML
	private TableColumn<ProdutoStatus, String> tblColGrade;

	@FXML
	private TableColumn<ProdutoStatus, String> tblColQuant;

	@FXML
	private TableColumn<ProdutoStatus, String> tblColPrec;

	@FXML
	private TableColumn<ProdutoStatus, String> tblColProm;

	private ArrayList<ProdutoStatus> proStsList = new ArrayList<ProdutoStatus>();
	private ObservableList<ProdutoStatus> obsProStsList;

	public void feedTable() {

		tblColId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tblColCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		tblColDesc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tblColGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
		tblColQuant.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tblColPrec.setCellValueFactory(new PropertyValueFactory<>("preco"));
		tblColProm.setCellValueFactory(new PropertyValueFactory<>("promocao"));
		tblColCheck.setCellValueFactory(new PropertyValueFactory<>("checked"));


		tblMain.setRowFactory(tv -> new TableRow<ProdutoStatus>() {
			@Override
			public void updateItem(ProdutoStatus item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null) {
					setStyle("");
				} else {
					
					setStyle("");

					if (!item.getPrecoEnviado().equals(item.getPrecoRecebido())) {
						setStyle("-fx-background-color:lightcoral");
					}

					if (!item.getPromocaoEnviado().equals(item.getPromocaoRecebido())) {
						setStyle("-fx-background-color:lightcoral");
					}

					if (!item.getQuantidadeEnviada().equals(item.getQuantidadeRecebida())) {
						setStyle("-fx-background-color:lightcoral");
					}
				}
			}
		});

		proStsList.add(new ProdutoStatus(1L, "00001", "Prod 1", "18", 1, 600.900, 300.900, 1, 600.900, 300.900, new CheckBox()));
		proStsList.add(new ProdutoStatus(2L, "00002", "Prod 2", "10", 2, 300.900, 150.900, 1, 300.900, 100.900, new CheckBox()));

		obsProStsList = FXCollections.observableArrayList(proStsList);
		tblMain.setItems(obsProStsList);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		feedTable();
	}
}
