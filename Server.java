package com.example.project3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.text.FontPosture.ITALIC;

public class Server extends Application {
    // Data-field //
    Stage window;
    LinkedList<Question> questions = new LinkedList<>();
    Font font = Font.font("VerDana", ITALIC, 30);
    LinkedList<String> nicknames = new LinkedList<>();
    int idx = 0;
    LinkedList<DataInputStream> input = new LinkedList<>();
    final int WIDTH = 1000;
    LinkedList<DataOutputStream> output = new LinkedList<>();
    final int HEIGHT = 675;
    int l = 0;
    List<String> gifs = new LinkedList<>();
    String[] colors = {"red", "blue", "orange", "green"};
    Music music = new Music("C:\\Users\\User\\IdeaProjects\\project3\\src\\lilnasx.mp3");
    private String answer;
    String[] names;
    int[] scores;
    LinkedList<LinkedList<String>> buttons = new LinkedList<>();

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        questions = new Quiz().loadFromFile("src/JavaQuiz.txt"); // calling loadfromfile for creating Questions
        Label label1 = new Label("Game Pin: 210107088");
        label1.setFont(Font.font("VerDana", FontPosture.ITALIC, 30));
        Label label = new Label("Waiting for players...");
        window.setTitle("Server");
        label.setFont(Font.font("VerDana", ITALIC, 25));
        label.setWrapText(true);
        gifs.add("src/first.gif");
        VBox vertical0 = new VBox();
        gifs.add("src/second.gif");
        StackPane stackPane = new StackPane();
        gifs.add("src/fourth.gif");
        stackPane.setMinWidth(800);
        stackPane.setMinHeight(400);
        stackPane.setMaxWidth(800);
        stackPane.setMaxHeight(400);
        stackPane.getChildren().addAll(label);
        stackPane.setStyle("-fx-background-color : cyan");
        Button start = new Button("start game");
        start.setTextFill(Color.BLACK);
        start.setStyle("-fx-background-color : white");
        start.setMinWidth(50);
        start.setMinHeight(20);

        start.setOnAction(actionEvent -> {
            names = new String[nicknames.size()];
            for (int i = 0; i < nicknames.size(); ++i) {
                names[i] = nicknames.get(i);
            }
            Question question1 = questions.get(idx);
            window.setScene(showQuestion(question1.getDescription()));
        });

        ImageView imageView = new ImageView(new Image(new FileInputStream("src/enn.png")));
        imageView.setFitHeight(180);
        imageView.setFitWidth(400);
        vertical0.getChildren().addAll(label1, imageView);
        vertical0.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vertical0);
        borderPane.setCenter(new StackPane(stackPane));
        Line line = new Line(0, 250, 1000, 250);
        line.setFill(Color.WHITE);
        borderPane.getChildren().addAll(line);
        borderPane.setRight(start);
        borderPane.setStyle("-fx-background-color : cyan");
        window.setTitle("Kahoot");
        window.setScene(new Scene(borderPane, WIDTH, HEIGHT));
        music.play();
        window.show();
        final boolean[] firstIn = {true};
        new Thread(() -> {
            try {     // creating thread to open multi-client server
                ServerSocket serverSocket = new ServerSocket(881);
                while (true) {
                    Socket socket = serverSocket.accept();
                    DataInputStream inputFromClient = new DataInputStream(
                            socket.getInputStream());
                    DataOutputStream outputToClient = new DataOutputStream(
                            socket.getOutputStream());
                    String nickName = inputFromClient.readUTF();
                    // When client come in firstIn then our text "Waiting players" disappear
                    if (firstIn[0]) {
                        firstIn[0] = false;
                        Platform.runLater(() ->
                                label.setText("")
                        );
                    }
                    nicknames.add(nickName);
                    // new Client
                    input.add(inputFromClient);
                    output.add(outputToClient);
                    // adding new nicknames
                    Platform.runLater(() -> {
                        label.setText(label.getText() + nickName + " ");
                        scores = new int[nicknames.size()];
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public Scene question(int index) {
        Question question = questions.get(index);
        BorderPane borderPane = new BorderPane();
        Text text = new Text("20");
        text.setFont(Font.font("VerDana", ITALIC, 17));
        Circle circle = new Circle(25, Color.AQUAMARINE);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, text);
        borderPane.setLeft(stackPane);
        AtomicInteger n = new AtomicInteger(20);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            n.getAndDecrement();
            text.setText(n + "");
            if (n.get() == 0) {
                Platform.runLater(() -> new Music("C:\\Users\\User\\IdeaProjects\\demo4\\src\\baraban.mp3").play());
            }
        }));
        timeline.setCycleCount(20);
        timeline.play();
        Rectangle underDescription = new Rectangle(1000, 70);
        underDescription.setFill(Color.WHITE);
        if (question instanceof FillIn) {
            answer = question.getAnswer();
            HBox horizontal = new HBox(5);
            ImageView KViewer = new ImageView();
            try {
                Image K = new Image(new FileInputStream("src/bukvak.png"));
                KViewer = new ImageView(K);
                KViewer.setFitWidth(100);
                KViewer.setFitHeight(50);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String description = question.getDescription();
            Label label = new Label((index + 1) + ". " + description);
            label.setFont(Font.font("Lucida Sans Unicode", ITALIC, 20));
            horizontal.getChildren().add(KViewer);
            horizontal.getChildren().add(label);
            horizontal.setAlignment(Pos.CENTER);
            VBox bitbox = new VBox();
            bitbox.setAlignment(Pos.CENTER);
            bitbox.getChildren().add(horizontal);
            StackPane ss;
            ss = new StackPane();
            ss.getChildren().addAll(underDescription, bitbox);
            borderPane.setTop(ss);
            StackPane stk = new StackPane();
            VBox vBox = new VBox(2);
            try {
                AtomicReference<Image> when = new AtomicReference<>(new Image(new FileInputStream("src/when.png")));
                ImageView whenView = new ImageView(when.get());
                whenView.setFitWidth(400);
                whenView.setFitHeight(200);
                vBox.getChildren().add(whenView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            TextField textArea = new TextField();
            textArea.setMinWidth(400);
            textArea.setMinHeight(40);
            textArea.setMaxWidth(400);
            textArea.setMaxHeight(40);
            vBox.getChildren().add(textArea);
            vBox.setAlignment(Pos.CENTER);
            stk.getChildren().addAll(vBox);
            borderPane.setCenter(stk);
            StackPane stackPane1 = new StackPane();
            borderPane.setRight(stackPane1);
        } else if ( question instanceof TrueFalse ) {
            String description = question.getDescription();
            Label label = new Label((index + 1) + ". " + description);
            label.setFont(Font.font("VerDana", ITALIC, 20));
            label.setAlignment(Pos.CENTER);
            StackPane stackPane2 = new StackPane();
            stackPane2.getChildren().addAll(underDescription, label);
            VBox dox = new VBox();
            dox.getChildren().addAll(stackPane2);
            dox.setAlignment(Pos.CENTER);
            borderPane.setTop(dox);
            answer = question.getAnswer();
            ImageView GIF = new ImageView((new File(gifs.get(l))).toURI().toString());
            GIF.setFitHeight(200);
            GIF.setFitWidth(398);
            l = (l == 2 ? 0 : l + 1);
            VBox vBox2 = new VBox();
            vBox2.getChildren().addAll(GIF);
            vBox2.setAlignment(Pos.CENTER);
            borderPane.setCenter(vBox2);
            Button red = new Button("True");
            Button blue = new Button("False");
            red.setMinWidth((WIDTH >> 1) - 1.5);
            red.setFont(Font.font("Monaco", FontPosture.ITALIC, 30));
            red.setStyle("-fx-background-color : red; -fx-text-fill : white");
            Polygon polygon = new Polygon(0, 50, 25, 0, 50, 50);
            polygon.setFill(Color.WHITE);
            red.setGraphic(polygon);
            red.setAlignment(Pos.CENTER_LEFT);
            blue.setMinWidth((WIDTH >> 1) - 1.5);
            red.setMinHeight(100);
            blue.setMinHeight(100);
            Rectangle rec1 = new Rectangle(35, 35, Color.WHITE);
            blue.setGraphic(rec1);
            blue.setAlignment(Pos.CENTER_LEFT);
            blue.setFont(Font.font("Monaco", FontPosture.ITALIC, 30));
            blue.setStyle("-fx-background-color : blue; -fx-text-fill : white");
            HBox hBox = new HBox(3);
            hBox.getChildren().addAll(red, blue);
            hBox.setAlignment(Pos.CENTER);
            borderPane.setBottom(hBox);
        } else {
            String description = question.getDescription();
            Label label = new Label((index + 1) + ". " + description);
            label.setFont(Font.font("VerDana", ITALIC, 20));
            label.setAlignment(Pos.CENTER);
            StackPane stackPane2 = new StackPane();
            stackPane2.getChildren().addAll(underDescription, label);
            VBox dox = new VBox();
            dox.getChildren().addAll(stackPane2);
            dox.setAlignment(Pos.CENTER);
            StackPane c = new StackPane();
            c.getChildren().addAll(dox);
            borderPane.setTop(c);
            ImageView GIF = new ImageView((new File(gifs.get(l))).toURI().toString());
            GIF.setFitHeight(200);
            GIF.setFitWidth(398);
            l = (l == 2 ? 0 : l + 1);
            StackPane GIFS_STACK = new StackPane();
            GIFS_STACK.getChildren().add(GIF);
            borderPane.setCenter(GIFS_STACK);
            VBox box = new VBox(5);
            VBox box1 = new VBox(5);
            HBox box2 = new HBox(5);
            LinkedList<String> list = new LinkedList<>();
            for (int j = 0; j < 4; j++) {
                list.add(question.getOptionsAt(j));
            }
            Collections.shuffle(list);
            buttons.add(list);
            String space = "  ";
            for (int j = 0; j < 4; j++) {
                Button button = new Button(space + list.get(j));
                button.setTextFill(Color.WHITE);
                button.setFont(Font.font("VerDana", ITALIC, 20));
                if (list.get(j).equals(question.getAnswer())) {
                    answer = colors[j];
                }
                if (j == 0) {
                    Polygon polygon = new Polygon(0, 50, 25, 0, 50, 50);
                    polygon.setFill(Color.WHITE);
                    button.setGraphic(polygon);
                    button.setAlignment(Pos.CENTER_LEFT);
                    button.setStyle("-fx-background-color:" + colors[j]);
                    button.setMinHeight(80);
                    button.setMinWidth(497.5);
                    box.getChildren().add(button);
                } else if (j == 1) {
                    Rectangle tort = new Rectangle(50, 50, Color.WHITE);
                    button.setGraphic(tort);
                    button.setAlignment(Pos.CENTER_LEFT);
                    button.setStyle("-fx-background-color:" + colors[j]);
                    button.setMinHeight(80);
                    button.setMinWidth(497.5);
                    box.getChildren().add(button);
                } else if (j == 2) {
                    Circle domalaq = new Circle(24, Color.WHITE);
                    button.setGraphic(domalaq);
                    button.setAlignment(Pos.CENTER_LEFT);
                    button.setStyle("-fx-background-color:" + colors[j]);
                    button.setMinHeight(80);
                    button.setMinWidth(497.5);
                    box1.getChildren().add(button);
                } else {
                    Rectangle rombikAsInJu = new Rectangle(35, 35, Color.WHITE);
                    rombikAsInJu.setRotate(45);
                    button.setGraphic(rombikAsInJu);
                    button.setAlignment(Pos.CENTER_LEFT);
                    button.setStyle("-fx-background-color:" + colors[j]);
                    button.setMinHeight(80);
                    button.setMinWidth(497.5);
                    box1.getChildren().add(button);
                }
            }
            box2.getChildren().addAll(box, box1);
            box2.setAlignment(Pos.CENTER);
            StackPane pane = new StackPane();
            pane.getChildren().add(box2);
            borderPane.setBottom(pane);
            StackPane stackPane1 = new StackPane();
            borderPane.setRight(stackPane1);
        }
        AtomicInteger count = new AtomicInteger(0);
        new Thread(() -> {
            for (int i = 0; i < input.size(); ++i) {
                try {
                    output.get(i).writeUTF(answer);
                    output.get(i).flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            final boolean[] trues = new boolean[input.size()];
            for (int i = 0; i < input.size(); ++i) {
                try {
                    int scorre = input.get(i).readInt();
                    int m = input.get(i).readInt();
                    if (scorre != 0) {
                        scores[i] += scorre;
                        trues[i] = true;
                    } else {
                        trues[i] = false;
                    }
                    count.addAndGet(m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                timeline.stop();
                for (int i = 0; i < trues.length; ++i) {
                    if (trues[i]) {
                        try {
                            output.get(i).writeUTF("correct");
                            output.get(i).flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            output.get(i).writeUTF("InCorrect");
                            output.get(i).flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                new Music("C:\\Users\\User\\IdeaProjects\\project3\\src\\baraban.mp3").play();
                window.setScene(afterQuestion(idx));
            });
        }).start();
        return new Scene(borderPane, WIDTH, HEIGHT);
    }

    int buttt = 0;

    public Scene showQuestion(String s) {
        Label label = new Label(s);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("VerDana", ITALIC, 25));
        label.setTextFill(Color.WHITE);
        Text text = new Text("5");
        text.setFont(Font.font("VerDana", ITALIC, 30));
        ImageView imageView = new ImageView(new File("src/question.gif").toURI().toString());
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        AtomicInteger time = new AtomicInteger(5);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            time.getAndDecrement();
            text.setText(time + "");
            if (time.get() == 0) {
                Platform.runLater(() -> {
                    if (questions.get(idx) instanceof FillIn) {
                        for (DataOutputStream dataOutputStream : output) {
                            try {
                                dataOutputStream.writeUTF("fillin");
                                dataOutputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if (questions.get(idx) instanceof TrueFalse ) {
                        for (DataOutputStream dataOutputStream : output) {
                            try {
                                dataOutputStream.writeUTF("truefalse");
                                dataOutputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        for (DataOutputStream dataOutputStream : output) {
                            try {
                                dataOutputStream.writeUTF("test");
                                dataOutputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    window.setScene(question(idx));
                });
            }
        }));
        timeline.setCycleCount(5);
        VBox vBox = new VBox(20);
        Rectangle underDescription = new Rectangle(1000, 70);
        underDescription.setFill(Color.BLACK);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(underDescription, label);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color : white");
        vBox.getChildren().addAll(imageView, stackPane, text);
        StackPane stackPane1 = new StackPane(vBox);
        stackPane1.setStyle("-fx-background-color : white");
        timeline.play();
        return new Scene(stackPane1, WIDTH, HEIGHT);
    }

    public Scene afterQuestion(int index) {
        Question question = questions.get(index);
        BorderPane borderPane = new BorderPane();
        Rectangle underDescription = new Rectangle(1000, 70);
        underDescription.setFill(Color.WHITE);
        answer = question.getAnswer();
        if (question instanceof FillIn) {
            HBox horizontal = new HBox(5);
            ImageView KViewer = new ImageView();
            try {
                Image K = new Image(new FileInputStream("src/bukvak.png"));
                KViewer = new ImageView(K);
                KViewer.setFitWidth(100);
                KViewer.setFitHeight(50);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String description = question.getDescription();
            Label label = new Label((index + 1) + ". " + description);
            label.setFont(Font.font("Lucida Sans Unicode", ITALIC, 20));
            horizontal.getChildren().add(KViewer);
            horizontal.getChildren().add(label);
            horizontal.setAlignment(Pos.CENTER);
            VBox bitbox = new VBox();
            bitbox.setAlignment(Pos.CENTER);
            bitbox.getChildren().add(horizontal);
            StackPane ss;
            ss = new StackPane();
            ss.getChildren().addAll(underDescription, bitbox);
            borderPane.setTop(ss);
            StackPane stk = new StackPane();
            VBox vBox = new VBox(2);
            try {
                AtomicReference<Image> when = new AtomicReference<>(new Image(new FileInputStream("src/when.png")));
                ImageView whenView = new ImageView(when.get());
                whenView.setFitWidth(400);
                whenView.setFitHeight(200);
                vBox.getChildren().add(whenView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            TextField textArea = new TextField(question.getAnswer());
            textArea.setFont(Font.font("VerDana", ITALIC, 23));
            textArea.setMinWidth(400);
            textArea.setMinHeight(40);
            textArea.setMaxWidth(400);
            textArea.setMaxHeight(40);
            vBox.getChildren().add(textArea);
            vBox.setAlignment(Pos.CENTER);
            stk.getChildren().addAll(vBox);
            borderPane.setCenter(stk);
            Button right = new Button("skip");
            right.setOnAction(actionEvent1 -> {
                int n = idx + 1;
                if (n == questions.size()) {
                    window.setScene(end());
                } else {
                    window.setScene(currentleaderBoard());
                }
            });
            StackPane stackPane1 = new StackPane();
            stackPane1.getChildren().add(right);
            borderPane.setRight(stackPane1);
        }
        else if ( question instanceof TrueFalse ) {
            String description = question.getDescription();
            Label label = new Label((index + 1) + ". " + description);
            label.setFont(Font.font("VerDana", ITALIC, 20));
            label.setAlignment(Pos.CENTER);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(underDescription, label);
            VBox dox = new VBox();
            dox.getChildren().addAll(stackPane);
            dox.setAlignment(Pos.CENTER);
            borderPane.setTop(dox);
            ImageView GIF = new ImageView((new File(gifs.get(l))).toURI().toString());
            GIF.setFitHeight(200);
            GIF.setFitWidth(398);
            l = (l == 2 ? 0 : l + 1);
            VBox vBox2 = new VBox();
            vBox2.getChildren().addAll(GIF);
            vBox2.setAlignment(Pos.CENTER);
            borderPane.setCenter(vBox2);
            Button red = new Button("True");
            Button blue = new Button("False");
            if ( answer.equals("True") ) {
                blue.setOpacity(0.5);
            }
            else {
                red.setOpacity(0.5);
            }
            red.setMinWidth((WIDTH >> 1) - 1.5);
            red.setFont(Font.font("Monaco", FontPosture.ITALIC, 30));
            red.setStyle("-fx-background-color : red; -fx-text-fill : white");
            Polygon polygon = new Polygon(0, 50, 25, 0, 50, 50);
            polygon.setFill(Color.WHITE);
            red.setGraphic(polygon);
            red.setAlignment(Pos.CENTER_LEFT);
            blue.setMinWidth((WIDTH >> 1) - 1.5);
            red.setMinHeight(100);
            blue.setMinHeight(100);
            Rectangle rec1 = new Rectangle(35, 35, Color.WHITE);
            blue.setGraphic(rec1);
            blue.setAlignment(Pos.CENTER_LEFT);
            blue.setFont(Font.font("Monaco", FontPosture.ITALIC, 30));
            blue.setStyle("-fx-background-color : blue; -fx-text-fill : white");
            HBox hBox = new HBox(3);
            hBox.getChildren().addAll(red, blue);
            hBox.setAlignment(Pos.CENTER);
            borderPane.setBottom(hBox);
            Button right = new Button("skip");
            right.setOnAction(actionEvent1 -> {
                int n = idx + 1;
                if (n == questions.size()) {
                    window.setScene(end());
                } else {
                    window.setScene(currentleaderBoard());
                }
            });
            StackPane stackPane1 = new StackPane();
            stackPane1.getChildren().add(right);
            borderPane.setRight(stackPane1);
        }
        else {
            String description = question.getDescription();
            Label label = new Label((index + 1) + ". " + description);
            label.setFont(Font.font("VerDana", ITALIC, 20));
            label.setAlignment(Pos.CENTER);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(underDescription, label);
            VBox dox = new VBox();
            dox.getChildren().addAll(stackPane);
            dox.setAlignment(Pos.CENTER);
            StackPane c = new StackPane();
            c.getChildren().addAll(dox);
            borderPane.setTop(c);
            ImageView GIF = new ImageView((new File(gifs.get(l))).toURI().toString());
            GIF.setFitHeight(200);
            GIF.setFitWidth(398);
            l = (l == 2 ? 0 : l + 1);
            StackPane GIFS_STACK = new StackPane();
            GIFS_STACK.getChildren().add(GIF);
            borderPane.setCenter(GIFS_STACK);
            VBox box = new VBox(5);
            VBox box1 = new VBox(5);
            HBox box2 = new HBox(5);
            LinkedList<String> list = buttons.get(buttt);
            buttt++;
            String space = "  ";
            for (int j = 0; j < 4; j++) {
                Button button = new Button(space + list.get(j));
                button.setTextFill(Color.WHITE);
                button.setFont(Font.font("VerDana", ITALIC, 20));
                if (list.get(j).equals(question.getAnswer())) {
                    answer = colors[j];
                }
                if (j == 0) {
                    Polygon polygon = new Polygon(0, 50, 25, 0, 50, 50);
                    polygon.setFill(Color.WHITE);
                    button.setGraphic(polygon);
                    button.setAlignment(Pos.CENTER_LEFT);
                    button.setStyle("-fx-background-color:" + colors[j]);
                    button.setMinHeight(80);
                    button.setMinWidth(497.5);
                    box.getChildren().add(button);
                } else if (j == 1) {
                    Rectangle tort = new Rectangle(50, 50, Color.WHITE);
                    button.setGraphic(tort);
                    button.setAlignment(Pos.CENTER_LEFT);
                    button.setStyle("-fx-background-color:" + colors[j]);
                    button.setMinHeight(80);
                    button.setMinWidth(497.5);
                    box.getChildren().add(button);
                } else if (j == 2) {
                    Circle domalaq = new Circle(24, Color.WHITE);
                    button.setGraphic(domalaq);
                    button.setAlignment(Pos.CENTER_LEFT);
                    button.setStyle("-fx-background-color:" + colors[j]);
                    button.setMinHeight(80);
                    button.setMinWidth(497.5);
                    box1.getChildren().add(button);
                } else {
                    Rectangle rombikAsInJu = new Rectangle(35, 35, Color.WHITE);
                    rombikAsInJu.setRotate(45);
                    button.setGraphic(rombikAsInJu);
                    button.setAlignment(Pos.CENTER_LEFT);
                    button.setStyle("-fx-background-color:" + colors[j]);
                    button.setMinHeight(80);
                    button.setMinWidth(497.5);
                    box1.getChildren().add(button);
                }
                if (!button.getText().trim().equals(question.getAnswer())) {
                    button.setOpacity(0.6);
                }
            }
            box2.getChildren().addAll(box, box1);
            box2.setAlignment(Pos.CENTER);
            StackPane pane = new StackPane();
            pane.getChildren().add(box2);
            borderPane.setBottom(pane);
            Button right = new Button("skip");
            right.setOnAction(actionEvent12 -> {
                int n = idx + 1;
                if (n == questions.size()) {
                    window.setScene(end());
                } else
                    window.setScene(currentleaderBoard());
            });
            StackPane stackPane1 = new StackPane();
            stackPane1.getChildren().add(right);
            borderPane.setRight(stackPane1);
        }
        return new Scene(borderPane, WIDTH, HEIGHT);
    }

    public void sort() {
        for (int i = 0; i < scores.length; ++i) {
            for (int j = i + 1; j < scores.length; ++j) {
                if (scores[i] < scores[j]) {
                    int temp = scores[i];
                    scores[i] = scores[j];
                    scores[j] = temp;
                    String tempt = names[i];
                    names[i] = names[j];
                    names[j] = tempt;
                    DataInputStream tempp = input.get(i);
                    input.set(i, input.get(j));
                    input.set(j, tempp);
                    DataOutputStream temppr = output.get(i);
                    output.set(i, output.get(j));
                    output.set(j, temppr);
                }
            }
        }
    }

    public Scene currentleaderBoard() {
        sort();
        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox(20);
        Text text1 = new Text("Current LeaderBoard");
        text1.setFont(Font.font("VerDana", ITALIC, 30));
        vBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < scores.length; ++i) {
            Button button = new Button(names[i] + "    " + scores[i]);
            button.setStyle("-fx-background-color : white");
            button.setFont(Font.font("VerDana", ITALIC, 20));
            button.setMinWidth(300);
            button.setMinHeight(30);
            vBox.getChildren().add(button);
        }
        AtomicInteger num = new AtomicInteger(3);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            num.getAndDecrement();
            if (num.get() == 0) {
                idx++;
                Question question = questions.get(idx);
                window.setScene(showQuestion(question.getDescription()));
            }
        }));
        borderPane.setTop(new StackPane(text1));
        borderPane.setCenter(vBox);
        timeline.setCycleCount(3);
        timeline.play();
        vBox.setStyle("-fx-background-color : mediumpurple");
        return new Scene(borderPane, WIDTH, HEIGHT);
    }

    public Scene end() {
        sort();
        Button close = new Button("close");
        close.setStyle("-fx-background-color : red");
        close.setOnAction(event -> {
            window.close();
            System.exit(0);
        });
        Label label = new Label();
        label.setText("THE PLACE");
        label.setFont(Font.font("VerDana", ITALIC, 50));
        label.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setRight(new StackPane(close));
        borderPane.setTop(new StackPane(label));
        HBox place = new HBox(3);
        Button firstPlace = new Button("1");
        firstPlace.setAlignment(Pos.CENTER);
        changeColorButton(firstPlace);
        firstPlace.setMinWidth(100);
        firstPlace.setMinHeight(200);
        VBox firstPlace1 = new VBox(2);
        Text text = new Text(names[0]);
        text.setFont(font);
        if (input.size() > 1) {
            Button secondPlace = new Button("2");
            changeColorButton(secondPlace);
            secondPlace.setMinWidth(100);
            secondPlace.setAlignment(Pos.CENTER);
            secondPlace.setMinHeight(170);
            place.getChildren().add(secondPlace);
            Text text1 = new Text(names[1]);
            text1.setFont(font);
            VBox secondPlace1 = new VBox(2);
            secondPlace1.setAlignment(Pos.BOTTOM_CENTER);
            secondPlace1.getChildren().addAll(text1, secondPlace);
            place.getChildren().addAll(secondPlace1);
        }
        firstPlace1.getChildren().addAll(text, firstPlace);
        firstPlace1.setAlignment(Pos.BOTTOM_CENTER);
        place.getChildren().add(firstPlace1);
        if (input.size() > 2) {
            Button thirdPlace = new Button("3");
            changeColorButton(thirdPlace);
            thirdPlace.setMinHeight(100);
            thirdPlace.setAlignment(Pos.CENTER);
            thirdPlace.setMinWidth(100);
            VBox thirdPlace1 = new VBox(2);
            thirdPlace1.setAlignment(Pos.BOTTOM_CENTER);
            Text text1 = new Text(names[2]);
            text1.setFont(font);
            thirdPlace1.getChildren().addAll(text1, thirdPlace);
            place.getChildren().add(thirdPlace1);
        }
        place.setAlignment(Pos.BOTTOM_CENTER);
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(place);
        borderPane.setCenter(box);
        borderPane.setStyle("-fx-background-color : chartreuse");
        return new Scene(borderPane, WIDTH, HEIGHT);
    }

    public void changeColorButton(Button button) {
        button.setStyle("-fx-background-color : purple");
        button.setFont(Font.font("VerDana", ITALIC, 70));
    }
}