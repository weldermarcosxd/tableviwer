package br.com.edilsystem.tableviwer.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import br.com.edilsystem.tableviwer.model.ProdutoStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
	
    @FXML
    private Button btnReSend;

    @FXML
    private Button btnSelctAll;

    @FXML
    private Button btnSortByStatus;
    
	private ArrayList<ProdutoStatus> proStsList = new ArrayList<ProdutoStatus>();
	private ObservableList<ProdutoStatus> obsProStsList;
	
    @FXML
    void reSend(ActionEvent event) {

    }

    @FXML
    void selectAllNotOk(ActionEvent event) {
    	for (ProdutoStatus produtoStatus : obsProStsList) {
    		if(!produtoStatus.getValid()) {
    			produtoStatus.getChecked().selectedProperty().set(Boolean.TRUE);
    		}
		}
    }
    
    @FXML
    void sortByStatus(ActionEvent event) {
    	ObservableList<ProdutoStatus> obsProStsListSorted = FXCollections.observableArrayList(new ArrayList<ProdutoStatus>());
    	for (ProdutoStatus produtoStatus : obsProStsList) {
			if(produtoStatus.getValid()) {
				obsProStsListSorted.add(produtoStatus);
			}else {
				obsProStsListSorted.add(0, produtoStatus);
			}
		}
    	obsProStsList = obsProStsListSorted;
    	tblMain.setItems(obsProStsList);
    }

	public void feedTable() {
		
		tblColId.prefWidthProperty().bind(tblMain.widthProperty().divide(4)); // w * 1/4
		tblColDesc.prefWidthProperty().bind(tblMain.widthProperty().divide(2)); // w * 1/2
		tblColCod.prefWidthProperty().bind(tblMain.widthProperty().divide(4)); // w * 1/4

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

		Random random = new Random();
		
		for (int i = 0; i < 100; i++) {
			proStsList.add(new ProdutoStatus(new Long(i + 1), "000" + i, "Prod " + i, String.valueOf(random.nextInt(32)), random.nextInt(10), 600.900, 300.900, random.nextInt(10), 600.900, 300.900, new CheckBox()));
		}

		obsProStsList = FXCollections.observableArrayList(proStsList);
		tblMain.setItems(obsProStsList);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		feedTable();
	}
}
