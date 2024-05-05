package org.example.sample;

import Models.ListaCard;
import Models.Unit;
import Models.Usuario;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.*;

import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    private static Usuario usuarioDB;
    @FXML
    public ScrollPane scrollPane;
    public AnchorPane newList;
    public ScrollPane scrollPane1;
    public FlowPane panelTarjetas1;
    @FXML
    public ComboBox<String> lineaBatallaBox;
    @FXML
    public Label puntosLabel;
    @FXML
    private TextField nombreLista;
    @FXML
    private Button buttonPDF;
    @FXML
    private ComboBox<String> lideresComboBox;
    @FXML
    private ComboBox puntosComboBox;
    @FXML
    private ChoiceBox faccionChoiceBox;
    @FXML
    private RadioButton caosRadio;
    @FXML
    private RadioButton destRadio;
    @FXML
    private RadioButton ordenRadio;
    @FXML
    private RadioButton muerteRadio;
    @FXML
    private FlowPane panelTarjetas;
    @FXML
    private Label usuarioActivo;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private ObservableList<String> lideresObservableList = FXCollections.observableArrayList();
    @FXML
    private List<Object> unidadesSeleccionadas = new ArrayList<>();

    public void setUsuarioActivo(Usuario usuarioDB) {
        MainController.usuarioDB = usuarioDB;
        String formatNombre = usuarioDB.getUsuario().substring(0, 1).toUpperCase() + usuarioDB.getUsuario().substring(1);
        usuarioActivo.setText(formatNombre);

        cargarTarjetas();

    }

    public void initialize() {

        panelTarjetas.setHgap(55);
        panelTarjetas.setVgap(10);

        toggleGroup = new ToggleGroup();
        caosRadio.setToggleGroup(toggleGroup);
        destRadio.setToggleGroup(toggleGroup);
        ordenRadio.setToggleGroup(toggleGroup);
        muerteRadio.setToggleGroup(toggleGroup);

        caosRadio.setOnAction(actionEvent -> onRadioButtonSelected());
        destRadio.setOnAction(actionEvent -> onRadioButtonSelected());
        ordenRadio.setOnAction(actionEvent -> onRadioButtonSelected());
        muerteRadio.setOnAction(actionEvent -> onRadioButtonSelected());

        faccionChoiceBox.setOnAction(actionEvent -> getUnit());

        ObservableList<Integer> puntos = FXCollections.observableArrayList(1000, 1500, 2000, 3000);
        puntosComboBox.setItems(puntos);
        puntosComboBox.getSelectionModel().selectFirst();


        lideresComboBox.setOnAction(event -> {
            Platform.runLater(() -> {
                String selectedUnit = lideresComboBox.getValue();
                if (selectedUnit != null) {
                    crearTarjetaUnidad(selectedUnit);
                    lideresComboBox.getSelectionModel().clearSelection();
                }
            });
        });

        lineaBatallaBox.setOnAction(event -> {
            Platform.runLater(() -> {
                String selectedUnit = lineaBatallaBox.getValue();
                if (selectedUnit != null) {
                    crearTarjetaUnidad(selectedUnit);
                    lineaBatallaBox.getSelectionModel().clearSelection();
                }
            });
        });

        unidadesSeleccionadas = new ArrayList<>();
    }

    @FXML
    private void cargarTarjetas() {

        panelTarjetas.getChildren().clear();

        List<ListaCard> listaTarjetas = getListas();
        for (ListaCard listaCard : listaTarjetas) {
        VBox tarjeta = crearTarjeta(listaCard);
        panelTarjetas.getChildren().add(tarjeta);
        }
    }

    @FXML
    private VBox crearTarjeta(ListaCard listaCard) {


        ImageView basura = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/trash.png"))));
        basura.setFitWidth(20);
        basura.setPreserveRatio(true);

        Button borrar = new Button();
        borrar.setGraphic(basura);
        final ListaCard listaCardFinal = listaCard;
        borrar.setOnAction(event -> eliminarLista(listaCardFinal));

        Label nombre = new Label(listaCard.getNombre());

        String faccion = listaCard.getFaccion();

        String imagePath = "/images/" + faccion + ".png";
        String imagePathLower = imagePath.toLowerCase();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePathLower)));
        ImageView miniatura = new ImageView(image);
        miniatura.setFitWidth(250);
        miniatura.setPreserveRatio(true);

        nombre.setStyle("-fx-font-weight: bold; -fx-text-fill: WHITE");

        VBox tarjeta =new VBox();
        tarjeta.getStyleClass().add("tarjeta");

        tarjeta.getChildren().addAll(miniatura, nombre);

        tarjeta.setPrefWidth(250);
        tarjeta.setPrefHeight(300);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setStyle("-fx-border-color: WHITE; -fx-border-width: 1px; -fx-border-radius: 5px;");
        tarjeta.getChildren().add(borrar);

        tarjeta.setOnMouseClicked(mouseEvent -> {
            abrirDocumento(listaCard.getRuta());
        });

        tarjeta.setOnMouseEntered(mouseEvent -> tarjeta.setCursor(Cursor.HAND));
        tarjeta.setOnMouseExited(mouseEvent -> tarjeta.setCursor(Cursor.DEFAULT));

        return tarjeta;
    }

    @FXML
    private void eliminarLista(ListaCard listaCard) {
        File documento = new File(listaCard.getRuta());
        if (documento.exists()) {
            if (documento.delete()) {
                eliminarDeBBDD(listaCard.getID_lista());
                cargarTarjetas();
                panelTarjetas.getChildren().removeIf(tarjeta -> {
                    tarjeta.getId();
                    return false;
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("404");
                alert.setContentText("No se pudo eliminar el archivo");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("404");
            alert.setContentText("El archivo no existe");
            alert.showAndWait();
        }
    }

    @FXML
    private void eliminarDeBBDD(int idLista) {
        Connection connection = null;
        String url = "jdbc:mysql://localhost/wucedb";
        String user = "root";
        String pwd = "";
        String sql = "DELETE FROM listas WHERE id_lista = ?";

        try {
            connection = DriverManager.getConnection(url, user, pwd);
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, idLista);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("404");
            alert.setContentText("Error al eliminar la entrada de la base de datos.");
            alert.showAndWait();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void abrirDocumento(String rutaDocumento) {
        File documento = new File(rutaDocumento);
        if (documento.exists()) {
            try {
                Desktop.getDesktop().open(documento);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("404");
            alert.setContentText("No se encuentra el documento.");
            alert.showAndWait();
        }
    }

    @FXML
    public static List<ListaCard> getListas() {
        List<ListaCard> listas = new ArrayList<>();
        Connection connection = null;
        String url = "jdbc:mysql://localhost/wucedb";
        String user = "root";
        String pwd = "";

        try {
            connection = DriverManager.getConnection(url, user, pwd);
            String sql = "SELECT * FROM listas WHERE ID_usuario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,usuarioDB.getID_usuario());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                ListaCard lista = new ListaCard(rs);
                listas.add(lista);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return listas;
    }

    @FXML
    private void getUnit() {
        try {
            List<Map<String, Object>> filas;
            filas = inputYaml();
            String selectedAlliance = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
            String selectedFaction = (String) faccionChoiceBox.getValue();

            List<String> liders = new ArrayList<>();
            List<String> lineaBatalla = new ArrayList<>();
            for (Map<String, Object> unidad : filas) {
                String rol = (String) unidad.get("rol");
                String alianza = (String) unidad.get("gran_alianza");
                String faccion = (String) unidad.get("alianza");

                if ("lider".equals(rol) && selectedAlliance.equals(alianza) && faccion.equals(selectedFaction)) {
                    String unidadNombre = (String) unidad.get("nombre");
                    liders.add(unidadNombre);
                } else if ("linea_batalla".equals(rol) && selectedAlliance.equals(alianza) && faccion.equals(selectedFaction)) {
                    String unidadNombre = (String) unidad.get("nombre");
                    lineaBatalla.add(unidadNombre);
                }


            }
            lideresComboBox.setItems(FXCollections.observableArrayList(liders));
            lineaBatallaBox.setItems(FXCollections.observableArrayList(lineaBatalla));

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar el archivo YAML");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private List<Map<String, Object>> inputYaml() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("src/main/resources/data/units.yaml");
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);

        return (List<Map<String, Object>>) data.get("filas");
    }

    @FXML
    private void crearTarjetaUnidad(String unidadNombre) {
        try {

            List<Map<String, Object>> filas = inputYaml();

            for (Map<String, Object> unidad : filas) {
                String nombre = (String) unidad.get("nombre");
                if (nombre.equals(unidadNombre)) {
                    Unit unit = new Unit(
                            nombre,
                            (String) unidad.get("movimiento"),
                            (String) unidad.get("salva"),
                            (int) unidad.get("puntos"),
                            (int) unidad.get("heridas"),
                            (int) unidad.get("coraje"),
                            (List<String>) unidad.get("habilidades"),
                            (List<String>) unidad.get("claves"),
                            (List<String>) unidad.get("armasMelee"),
                            (List<String>) unidad.get("missileWeapons")
                    );
                    int puntos = (int) unidad.get("puntos");
                    int puntosActual = Integer.parseInt(puntosLabel.getText());
                    int puntosTotal = puntosActual + puntos;
                    puntosLabel.setText(String.valueOf(puntosTotal));

                    mostrarInfo(unit);
                    unidadesSeleccionadas.add(unit);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar el archivo YAML");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void mostrarInfo(Unit unit) {
        VBox tarjeta = new VBox();
        tarjeta.setSpacing(10);
        tarjeta.setStyle("-fx-background-color: #333333; -fx-padding: 10px;");

        Label nombreLabel = new Label("Nombre: " + unit.getNombre());
        nombreLabel.setStyle("-fx-text-fill: white;");
        Label movimientoLabel = new Label( "Movimiento: " + unit.getMovimiento());
        movimientoLabel.setStyle("-fx-text-fill: white;");
        Label salvaLabel = new Label("Salva: " + unit.getSalva());
        salvaLabel.setStyle("-fx-text-fill: white;");
        Label puntosLabelUnidad = new Label(String.valueOf("Puntos: " + unit.getPuntos()));
        puntosLabelUnidad.setStyle("-fx-text-fill: white;");
        Label heridasLabel = new Label(String.valueOf("Heridas: " + unit.getHeridas()));
        heridasLabel.setStyle("-fx-text-fill: white;");


        Button eliminarButton = new Button("Eliminar");
        eliminarButton.setOnAction(event -> {

            restarPuntos(unit);
            eliminarUnidad(unit);

            panelTarjetas1.getChildren().remove(tarjeta);
        });

        tarjeta.getChildren().addAll(nombreLabel, movimientoLabel, salvaLabel, puntosLabelUnidad, heridasLabel, eliminarButton);

        panelTarjetas1.getChildren().add(tarjeta);
    }

    @FXML
    private void eliminarUnidad(Unit unit) {
        unidadesSeleccionadas.remove(unit);
    }

    @FXML
    private void restarPuntos(Unit unit) {
        String textoPuntos = puntosLabel.getText();
        textoPuntos = textoPuntos.replaceAll("[^\\d]","");
        int puntosActual = Integer.parseInt(textoPuntos);
        int puntosUnidad = unit.getPuntos();
        int puntosTotal = puntosActual - puntosUnidad;
        puntosLabel.setText(String.valueOf(puntosTotal));
    }

    @FXML
    protected void onNewListButtonClick() {

        scrollPane.setVisible(false);
        newList.setVisible(true);
        scrollPane1.setVisible(true);
        panelTarjetas1.setVisible(true);
    }

    @FXML
    protected void onMyListButtonClick() {
        scrollPane.setVisible(true);
        newList.setVisible(false);
        scrollPane1.setVisible(false);
        panelTarjetas1.setVisible(false);
    }

    @FXML
    private void onRadioButtonSelected() {
        RadioButton selectedRadio = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadio != null) {
            String granAlianza = selectedRadio.getText();
            try {
                List<String> facciones = cargarFaccionesGranAlianza(granAlianza);
                ObservableList<String> factionList = FXCollections.observableArrayList(facciones);
                faccionChoiceBox.setItems(factionList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private List<String> cargarFaccionesGranAlianza(String granAlianza) throws Exception {
        InputStream inputStream = new FileInputStream("src/main/resources/data/facciones.yaml");

        Yaml yaml = new Yaml();
        Map<String, List<Map<String, String>>> data = yaml.load(inputStream);

        return data.get("filas").stream()
                .filter(faccion -> faccion.get("gran_alianza").equalsIgnoreCase(granAlianza))
                .map(faccion -> faccion.get("nombre"))
                .collect(Collectors.toList());
    }

    @FXML
    private String nombreRandom() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @FXML
    private void guardarPDF(String nombreLista, String faccion) {
        try {
            Document document = new Document();
            String nombre = nombreRandom();
            String ruta = "src/main/resources/listas/" + nombre + ".pdf";
            guardarEnBBDD(nombreLista, ruta, usuarioDB.getID_usuario(), faccion);
            PdfWriter.getInstance(document, new FileOutputStream(ruta));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Chunk chunk = new Chunk(nombreLista, font);

            document.add(chunk);

            for (Object unidad : unidadesSeleccionadas) {
                if (unidad instanceof Unit) {
                    Unit unit = (Unit) unidad;
                    agregarUnidad(document,unit);
                }
            }

            document.close();

            cargarTarjetas();
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void agregarUnidad(Document document, Unit unit) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase("Nombre: " + unit.getNombre()));
        paragraph.add(new Phrase("Movimiento: " + unit.getMovimiento()));
        paragraph.add(new Phrase("Salva: " + unit.getSalva()));
        paragraph.add(new Phrase("Puntos: " + unit.getPuntos()));
        paragraph.add(new Phrase("Heridas: " + unit.getHeridas()));
        paragraph.add(new Phrase("Coraje: " + unit.getCoraje()));
        paragraph.add(new Phrase("Habilidades: " + unit.getHabilidades()));
        paragraph.add(new Phrase("Claves: " + unit.getClaves()));
        paragraph.add(new Phrase("Armas CaC: " + unit.getArmasMelee()));
        paragraph.add(new Phrase("Armas a Distancia: " + unit.getMissileWeapons()));

        document.add(paragraph);
    }

    @FXML
    private void guardarEnBBDD(String nombreLista, String nombre, int idUsuario, String faccion) throws SQLException {
        Connection connection = null;
        String url = "jdbc:mysql://localhost/wucedb";
        String user = "root";
        String pwd = "";

        String sql;
        try {
            connection = DriverManager.getConnection(url, user, pwd);
            sql = "INSERT INTO listas (nombre, ruta, ID_usuario, faccion) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, nombreLista);
                pstmt.setString(2, nombre);
                pstmt.setInt(3, idUsuario);
                pstmt.setString(4, faccion);
                pstmt.executeUpdate();
                System.out.println("Datos guardados exitosamente");
        } catch (SQLException e) {
            e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onButtonPDFClick() {
        String nombre = nombreLista.getText();
        String faccion = (String) faccionChoiceBox.getValue();
        if (nombre != null && !nombre.isEmpty()) {
            if (faccion != null && !faccion.isEmpty()) {
                guardarPDF(nombre, faccion);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, seleccione una facción antes de generar el PDF");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, ingrese un nombre para la lista antes de generar el PDF");
            alert.showAndWait();
        }
    }
}

