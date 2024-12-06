package controller;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import log.*;
import javafx.fxml.FXML;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ApacheController {
    @FXML
    private Label toRequest;
    @FXML
    private Label toFail;
    @FXML
    private Label toSize;
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TableView<AccessLog> tableView;
    @FXML
    private TableColumn<AccessLog, String> ipColumn;
    @FXML
    private TableColumn<AccessLog, String> logColumn;
    @FXML
    private TableColumn<AccessLog, String> userIDColumn;
    @FXML
    private TableColumn<AccessLog, String> dateColumn;
    @FXML
    private TableColumn<AccessLog, String> timeColumn;
    @FXML
    private TableColumn<AccessLog, String> requestColumn;
    @FXML
    private TableColumn<AccessLog, Integer> statusColumn;
    @FXML
    private TableColumn<AccessLog, Integer> sizeColumn;
    @FXML
    private TableColumn<AccessLog, String> refererColumn;
    @FXML
    private TableColumn<AccessLog, String> userAgentColumn;
    private ArrayList<AccessLog> nlist;

    private ObservableList<AccessLog> masterData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        logColumn.setCellValueFactory(new PropertyValueFactory<>("log"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        requestColumn.setCellValueFactory(new PropertyValueFactory<>("request"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        refererColumn.setCellValueFactory(new PropertyValueFactory<>("referer"));
        userAgentColumn.setCellValueFactory(new PropertyValueFactory<>("userAgent"));

        // Thêm dữ liệu mẫu để kiểm tra
        loadData x = new loadData();

        ArrayList<AccessLog> list = x.load();
        toRequest.setText(Integer.toString(x.getTotal()));
        String toSizeString = String.format("%.2f",x.getTotalSize());
        toSize.setText(toSizeString + "  Gb");
        toFail.setText(Integer.toString(x.getTotalFail()));
        masterData.addAll(list);
        tableView.setItems(masterData);
    }
    public void Search(){
        ObservableList<AccessLog> filteredData = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        searchEngine s = new searchEngine();
        String text = searchField.getText();
        String nbegin = "",nend = "";
        nlist = s.Search(text);
        LocalDate begin = beginDate.getValue();
        LocalDate end = endDate.getValue();
        if(begin != null){
             nbegin = begin.format(formatter);
        }
        if(end != null){
             nend = end.format(formatter);
        }
//        for(AccessLog s : nlist){
//
//        }
        nlist = s.SearchByDate(nlist,nbegin,nend);
        filteredData.addAll(nlist);
        tableView.setItems(filteredData);
        //filteredData.clear();
    }
}
