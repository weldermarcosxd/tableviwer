package br.com.edilsystem.tableviwer.view;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import br.com.edilsystem.retrancon.exceptions.RetranException;
import br.com.edilsystem.retrancon.models.ProdutoStatus;
import br.com.edilsystem.retrancon.modelsMenut.ProdutoMenut;
import br.com.edilsystem.retrancon.repository.EstoqueRepository;
import br.com.edilsystem.retrancon.repository.PrecoRepository;
import br.com.edilsystem.retrancon.repository.ProdutoRepository;
import br.com.edilsystem.tableviwer.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
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
	private MenuItem menuItemReOpen;

	@FXML
	private MenuItem menuItemSortByStatus;

	@FXML
	private MenuItem menuItemSelectAllNotOk;

	@FXML
	private MenuItem menuItemReSend;

	@FXML
	private ProgressIndicator pgrProgresso;

	private ArrayList<ProdutoStatus> proStsList = new ArrayList<ProdutoStatus>();
	private ObservableList<ProdutoStatus> obsProStsList;

	@FXML
	void reSend(ActionEvent event) {

		if (!pgrProgresso.isVisible()) {
			pgrProgresso.setVisible(true);
		}

		Task<List<String>> listLoader = new Task<List<String>>() {
			{
				setOnSucceeded(workerStateEvent -> {
					pgrProgresso.setVisible(false); // stop displaying the loading indicator
				});
				setOnFailed(workerStateEvent -> getException().printStackTrace());
			}

			@Override
			protected List<String> call() throws Exception {

				ProdutoRepository produtoRepository = new ProdutoRepository();
				ArrayList<ProdutoMenut> produtoList = new ArrayList<ProdutoMenut>();
				Map<Integer, ProdutoStatus> produtoStatusList = new HashMap<Integer, ProdutoStatus>();

				for (ProdutoStatus produto : obsProStsList) {
					if (produto.getChecked().selectedProperty().getValue()) {
						produtoStatusList.put(Integer.valueOf(produto.getCodigo()), produto);
					}
				}

				for (ProdutoStatus produtoStatus : produtoStatusList.values()) {
					produtoList.add(new ProdutoMenut(produtoStatus));
				}

				try {
					produtoList = produtoRepository.feedIdProduto(produtoList, null, produtoStatusList);
				} catch (RetranException e) {
					e.printStackTrace();
				}

				EstoqueRepository estoqueRepository = new EstoqueRepository();
				try {
					estoqueRepository.updateEstoqueList(produtoList, null);
				} catch (RetranException e) {
					e.printStackTrace();
				}

				PrecoRepository precoRepository = new PrecoRepository();
				try {
					precoRepository.updatePrecoList(produtoList, null);
				} catch (RetranException e) {
					e.printStackTrace();
				}

				ArrayList<ProdutoStatus> values = new ArrayList<ProdutoStatus>(produtoStatusList.values());
				estoqueRepository.getEstoque(values);
				precoRepository.getPreco(values);
				
				for (ProdutoStatus produtoStatus : values) {
					produtoStatus.setChecked(new CheckBox());
				}
				
				obsProStsList = FXCollections.observableArrayList(values);
				tblMain.setItems(obsProStsList);
				return null;
			}
		};

		Thread loadingThread = new Thread(listLoader, "list-loader");
		loadingThread.setDaemon(true);
		loadingThread.start();

	}

	static void writeCSVFile(String csvFileName, List<ProdutoStatus> produtoStatusList) {
		ICsvBeanWriter beanWriter = null;
		CellProcessor[] processors = new CellProcessor[] { new Optional(), // id
				new NotNull(), // codigo
				new NotNull(), // descricao
				new NotNull(), // quantidadeEnviada
				new NotNull(), // precoEnviado
				new NotNull(), // promocaoEnviado
				new Optional(), // grade
				new Optional(), // quantidadeRetornada
				new Optional(), // precoRetornado
				new Optional() // promocaoRetornado
		};

		try {
			beanWriter = new CsvBeanWriter(new FileWriter(csvFileName), CsvPreference.STANDARD_PREFERENCE);
			String[] header = { "id", "codigo", "descricao", "quantidadeEnviada", "precoEnviado", "promocaoEnviado",
					"grade", "quantidadeRecebida", "precoRecebido", "promocaoRecebido" };
			beanWriter.writeHeader(header);

			for (ProdutoStatus aBook : produtoStatusList) {
				beanWriter.write(aBook, header, processors);
			}

		} catch (IOException ex) {
			System.err.println("Error writing the CSV file: " + ex);
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
				} catch (IOException ex) {
					System.err.println("Error closing the writer: " + ex);
				}
			}
		}
	}

	@FXML
	void selectAllNotOk(ActionEvent event) {
		for (ProdutoStatus produtoStatus : obsProStsList) {
			if (!produtoStatus.getValid()) {
				produtoStatus.getChecked().selectedProperty().set(Boolean.TRUE);
			}
		}
	}

	@FXML
	void sortByStatus(ActionEvent event) {
		ObservableList<ProdutoStatus> obsProStsListSorted = FXCollections
				.observableArrayList(new ArrayList<ProdutoStatus>());
		for (ProdutoStatus produtoStatus : obsProStsList) {
			if (produtoStatus.getValid()) {
				obsProStsListSorted.add(produtoStatus);
			} else {
				obsProStsListSorted.add(0, produtoStatus);
			}
		}
		obsProStsList = obsProStsListSorted;
		tblMain.setItems(obsProStsList);
	}

	@FXML
	void reOpen(ActionEvent event) {

		if (!pgrProgresso.isVisible()) {
			pgrProgresso.setVisible(true);
		}

		Task<List<String>> listLoader = new Task<List<String>>() {
			{
				setOnSucceeded(workerStateEvent -> {
					pgrProgresso.setVisible(false); // stop displaying the loading indicator
				});
				setOnFailed(workerStateEvent -> getException().printStackTrace());
			}

			@Override
			protected List<String> call() throws Exception {
				proStsList = new ArrayList<>();
				obsProStsList = FXCollections.observableArrayList(proStsList);
				feedTable(Main.parameters);
				return null;
			}
		};

		Thread loadingThread = new Thread(listLoader, "list-loader");
		loadingThread.setDaemon(true);
		loadingThread.start();
	}

	public void feedTable(String parameters) {

		ArrayList<ProdutoStatus> readCsv = new ArrayList<ProdutoStatus>();
		if (parameters != null) {
			readCsv = readCsv(parameters);
		}

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

					if (item.getId() == null) {
						setStyle("-fx-background-color:lightcoral");
					}

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

		for (ProdutoStatus produtoStatus : readCsv) {
			proStsList.add(produtoStatus);
		}

		obsProStsList = FXCollections.observableArrayList(proStsList);
		tblMain.setItems(obsProStsList);

	}

	private ArrayList<ProdutoStatus> readCsv(String parameters) {
		ArrayList<ProdutoStatus> produtoStatusList = new ArrayList<ProdutoStatus>();
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(parameters), CsvPreference.STANDARD_PREFERENCE);

			// the header elements are used to map the values to the bean (names must match)
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = new CellProcessor[] { new Optional(new ParseLong()), // id
					new NotNull(), // codigo
					new NotNull(), // descricao
					new NotNull(new ParseInt()), // quantidadeEnviada
					new NotNull(new ParseDouble()), // precoEnviado
					new NotNull(new ParseDouble()), // promocaoEnviado
					new Optional(), // grade
					new Optional(new ParseInt()), // quantidadeRetornada
					new Optional(new ParseDouble()), // precoRetornado
					new Optional(new ParseDouble()) // promocaoRetornado
			};

			ProdutoStatus produtoStatus;
			while ((produtoStatus = beanReader.read(ProdutoStatus.class, header, processors)) != null) {
				produtoStatus.setChecked(new CheckBox());
				produtoStatusList.add(produtoStatus);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (beanReader != null) {
				try {
					beanReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return produtoStatusList;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (Main.parameters != null){
			feedTable(Main.parameters);	
		}else {
			feedTable("retorno.csv");
		}
		
		if (pgrProgresso.isVisible()) {
			pgrProgresso.setVisible(false);
		}
	}
}
