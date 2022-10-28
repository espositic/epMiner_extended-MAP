package client;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * Classe genera l'interfaccia del Client.
 */
public class Client extends Application {

    /**
     * Finestra di interfaccia.
     */
    private Stage window;
    /**
     * Scena interna alla finestra.
     */
    private Scene scene;
    /**
     * Contenitore scelta opzione.
     */
    private HBox hboxOpzione;
    /**
     * Etichetta scelta opzione.
     */
    private Label lOpzione;
    /**
     * Tendina per la scelta dell'opzione.
     */
    private ChoiceBox<String> opzione;
    /**
     * Contenitore inserimento supporto minimo.
     */
    private HBox hboxSupporto;
    /**
     * Etichetta supporto minimo.
     */
    private Label lSupporto;
    /**
     * Area per l'inserimento del supporto minimo.
     */
    private TextField tfSupporto;
    /**
     * Contenitore inserimento growrate minimo.
     */
    private HBox hboxGrowrate;
    /**
     * Etichetta growrate minimo.
     */
    private Label lGrowrate;
    /**
     * Area per l'inserimento del growrate minimo.
     */
    private TextField tfGrowrate;
    /**
     * Contenitore inserimento tabella target.
     */
    private HBox hboxTarget;
    /**
     * Etichetta tabella target.
     */
    private Label lTarget;
    /**
     * Area per l'inserimento del nome della tabella target.
     */
    private TextField tfTarget;
    /**
     * Contenitore inserimento tabella background.
     */
    private HBox hboxBackground;
    /**
     * Etichetta tabella background.
     */
    private Label lBackground;
    /**
     * Area per l'inserimento del nome della tabella background.
     */
    private TextField tfBackground;
    /**
     * Bottone per l'invio dei dati.
     */
    private Button invio;
    /**
     * Contenitore per il bottone di invio.
     */
    private HBox hboxInvia;
    /**
     * Contenitore per la TextArea in cui mostrare i risultati della ricerca.
     */
    private HBox hboxRisutati;
    /**
     * Area in cui mostrare i risultati della ricerca.
     */
    private TextArea taRisultati;
    /**
     * Contenitore che alla fine conterrà i contenitori citati prima.
     */
    private VBox layout;

    /**
     * Viene istanziata una finestra e all'interno di essa vengono
     * inserite i vari strumenti che permettono l'input all'utente.
     * (TextField, ChoiceBox e Button con le relative etichette o
     * Label). Per l'output invece si utilizza una TextArea non
     * modificabile.
     * Al click del bottone di invio, vengono effettuati dei controlli
     * sui dati di input immessi dall'utente e nel caso ci fossero
     * errori il programma rilascia degli alert. Nel caso non ci fossero
     * errori invece il Client istanzia la connsessione col Server,
     * inviando i dati di input, e si appresta a ricevere i risultati
     * della ricerca per poi stamparli nella TextArea.
     * @param stage Istanza di Stage.
     * @throws IOException Eccezione generata da Input/Output.
     */
    @Override
     public void start(Stage stage) throws IOException {
        window = stage;
        window.setTitle("EpMinClient");

        /*Sezione scelta dell'opzione*/
        hboxOpzione = new HBox();
        hboxOpzione.setPadding(new Insets(15, 15, 15, 15));
        hboxOpzione.setSpacing(15);
        lOpzione = new Label();
        lOpzione.setText("Scegli una opzione:");
        lOpzione.setPrefSize(200, 20);
        opzione = new ChoiceBox<>();
        opzione.setPrefSize(200, 20);
        opzione.setValue("Nuova Scoperta");
        opzione.getItems().add("Nuova Scoperta");
        opzione.getItems().add("Risultati in archivio");
        hboxOpzione.getChildren().addAll(lOpzione,opzione);

        /*Sezione inserimento supporto minimo*/
        lSupporto = new Label();
        lSupporto.setText("Inserisci il supporto minimo:");
        lSupporto.setPrefSize(200, 20);
        tfSupporto = new TextField();
        tfSupporto.setPrefSize(200, 20);
        tfSupporto.setPromptText("Valori compresi tra 0 e 1");
        hboxSupporto = new HBox();
        hboxSupporto.setPadding(new Insets(15, 15, 15, 15));
        hboxSupporto.setSpacing(15);
        hboxSupporto.getChildren().addAll(lSupporto,tfSupporto);
        // force the field to be numeric only
        tfSupporto.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*(\\.\\d*)?")) {
                    tfSupporto.setText(oldValue);
                }
            }
        });

        /*Sezione inserimento growrate minimo*/
        lGrowrate = new Label();
        lGrowrate.setText("Inserisci il growrate minimo:");
        lGrowrate.setPrefSize(200, 20);
        tfGrowrate = new TextField();
        tfGrowrate.setPrefSize(200, 20);
        tfGrowrate.setPromptText("Valori maggiori di 0");
        hboxGrowrate = new HBox();
        hboxGrowrate.setPadding(new Insets(15, 15, 15, 15));
        hboxGrowrate.setSpacing(15);
        hboxGrowrate.getChildren().addAll(lGrowrate,tfGrowrate);
        // force the field to be numeric only
        tfGrowrate.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*(\\.\\d*)?")) {
                    tfGrowrate.setText(oldValue);
                }
            }
        });

        /*Sezione inserimento nome tabella target*/
        lTarget = new Label();
        lTarget.setPrefSize(200, 20);
        lTarget.setText("Inserisci la tabella target:");
        tfTarget = new TextField();
        tfTarget.setPrefSize(200, 20);
        hboxTarget = new HBox();
        hboxTarget.setPadding(new Insets(15, 15, 15, 15));
        hboxTarget.setSpacing(15);
        hboxTarget.getChildren().addAll(lTarget,tfTarget);

        /*Sezione inserimento nome tabella background*/
        lBackground = new Label();
        lBackground.setPrefSize(200, 20);
        lBackground.setText("Inserisci la tabella background:");
        tfBackground = new TextField();
        tfBackground.setPrefSize(200, 20);
        hboxBackground = new HBox();
        hboxBackground.setPadding(new Insets(15, 15, 15, 15));
        hboxBackground.setSpacing(15);
        hboxBackground.getChildren().addAll(lBackground,tfBackground);

        /*Sezione invio dati*/
        invio = new Button();
        invio.setText("Invia Dati");
        hboxInvia = new HBox();
        hboxInvia.setPadding(new Insets(15,15,15,15));
        hboxInvia.setSpacing(15);
        hboxInvia.getChildren().add(invio);

        /*Sezione area risultati*/
        taRisultati = new TextArea();
        taRisultati.setEditable(false);
        taRisultati.setPrefHeight(500);
        taRisultati.setPrefWidth(500);
        hboxRisutati = new HBox();
        hboxRisutati.setPadding(new Insets(0, 0,0, 0));
        hboxRisutati.setSpacing(15);
        hboxRisutati.getChildren().add(taRisultati);

        invio.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Socket socket = null;
                InetAddress addr = null;
                try {
                    addr = InetAddress.getByName("127.0.0.1");
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                }
                final int PORT = 8080;
                System.out.println("addr = " + addr + "\nport=" + PORT);
                try {
                    socket = new Socket(addr, PORT);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println(socket);
                ObjectOutputStream out = null;
                ObjectInputStream in = null;
                try {
                    out = new ObjectOutputStream(socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    in = new ObjectInputStream(socket.getInputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                int opz=1;
                if (opzione.getValue()=="Nuova Scoperta"){
                    opz=1;
                } else {
                    opz=2;
                }
                if(tfSupporto.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Avvalora il campo Supporto", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    float supp = Float.parseFloat(tfSupporto.getText());
                    if(tfGrowrate.getText().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Avvalora il campo Growrate", ButtonType.OK);
                        alert.showAndWait();
                    } else {
                        float gr = Float.parseFloat(tfGrowrate.getText());
                        if(tfTarget.getText().isEmpty()){
                            Alert alert = new Alert(Alert.AlertType.WARNING, "Avvalora il campo Data Target", ButtonType.OK);
                            alert.showAndWait();
                        } else {
                            String tar = tfTarget.getText();
                            if(tfBackground.getText().isEmpty()){
                                Alert alert = new Alert(Alert.AlertType.WARNING, "Avvalora il campo Data Background", ButtonType.OK);
                                alert.showAndWait();
                            } else {
                                String bac = tfBackground.getText();
                                if (supp <= 0 || supp > 1) {
                                    Alert alert = new Alert(Alert.AlertType.WARNING, "Inserisci un supporto valido", ButtonType.OK);
                                    alert.showAndWait();
                                } else {
                                    if (gr <= 0) {
                                        Alert alert = new Alert(Alert.AlertType.WARNING, "Inserisci un growrate valido", ButtonType.OK);
                                        alert.showAndWait();
                                    } else {
                                        try {
                                            out.writeObject(opz);
                                            out.writeObject(supp);
                                            out.writeObject(gr);
                                            out.writeObject(tar);
                                            out.writeObject(bac);
                                            System.out.println("Informazioni inviate");
                                            String fpMiner = (String) (in.readObject());
                                            taRisultati.setText(fpMiner);
                                            String epMiner = (String) (in.readObject());
                                            taRisultati.setText(taRisultati.getText() + epMiner);
                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Richiesta salvataggio");
                                            alert.setHeaderText("Vuoi salvare i risultati su un file di testo?");
                                            alert.setContentText("Conferma la scelta.");

                                            Optional<ButtonType> result = alert.showAndWait();
                                            if (result.get() == ButtonType.OK){
                                                try (PrintWriter txt = new PrintWriter("EP_playtennis_minSup" + supp + "_minGr" + gr + ".txt")) {
                                                    txt.println(fpMiner+"\n"+epMiner);
                                                }
                                            } else {
                                            }
                                        } catch (IOException | ClassNotFoundException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        String cssLayout = "-fx-border-color: #0B0C10;\n" +
                "-fx-background-color: #45A29E;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 15;\n" +
                "-fx-border-radius: 25;\n";
        layout = new VBox(0);
        layout.setAlignment(Pos.BASELINE_LEFT);
        layout.setPadding(new Insets(20,20,20,20));
        layout.getChildren().addAll(hboxOpzione, hboxSupporto, hboxGrowrate, hboxTarget, hboxBackground, hboxInvia,hboxRisutati);
        layout.setStyle(cssLayout);
        scene = new Scene(layout,480,720);
        window.setScene(scene);
        window.setResizable(false);
        window.show();
    }

    /**
     * Lancia l'applicazione (richiama il metodo start()).
     * @param args Array di input.
     * @throws IOException Eccezione input/output.
     */
    public static void main(String[] args) throws IOException {
        launch();
    }
}