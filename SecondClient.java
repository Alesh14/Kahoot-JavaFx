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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SecondClient extends Application {
    DataOutputStream outputStream;
    DataInputStream inputStream;
    Stage window = new Stage();
    final int WIDTH = 400;
    final int HEIGHT = 600;
    String username = "Unnamed";
    String gamePin;
    int color = 0;
    AtomicInteger score = new AtomicInteger(1020);

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setTitle("Kahoot");
        VBox vBox = new VBox(10);
        gamePin = "210107088";
        TextField textField = new TextField("Game Pin");
        textField.setOnAction(actionEvent -> {
            String pin = textField.getText().trim();
            if (gamePin.equals(pin)) {
                try {
                    window.setScene(enterNickName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ImageView imageView = new ImageView(new Image(new FileInputStream("src/enn.png")));
        imageView.setFitWidth(320);
        imageView.setFitHeight(180);
        textField.setMinHeight(25);
        textField.setMaxHeight(25);
        textField.setMinWidth(200);
        textField.setMaxWidth(200);
        Button button = new Button("Enter");
        button.setOnAction(actionEvent -> {
            String pin = textField.getText().trim();
            if (gamePin.equals(pin)) {
                try {
                    window.setScene(enterNickName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        button.setMinHeight(25);
        button.setMinWidth(200);
        vBox.getChildren().addAll(textField, button);
        vBox.setAlignment(Pos.CENTER);
        VBox vBox1 = new VBox();
        vBox1.setStyle("-fx-background-color : chartreuse");
        StackPane stackPane = new StackPane();
        Rectangle rectangle = new Rectangle(220, 75);
        rectangle.setFill(Color.WHITE);
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        rectangle.setStroke(Color.BLACK);
        stackPane.getChildren().addAll(rectangle, vBox);
        vBox1.getChildren().addAll(imageView, stackPane);
        vBox1.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox1);
        window.setScene(new Scene(borderPane, WIDTH, HEIGHT));
        window.show();
        try {
            Socket socket = new Socket("localhost", 881);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
        }
    }

    public Scene enterNickName() throws Exception {
        VBox vBox = new VBox(10);
        TextField textField = new TextField("Nickname");
        ImageView imageView = new ImageView(new Image(new FileInputStream("src/enn.png")));
        imageView.setFitWidth(320);
        imageView.setFitHeight(180);
        textField.setMinHeight(25);
        textField.setMaxHeight(25);
        textField.setMinWidth(200);
        textField.setMaxWidth(200);
        textField.setOnAction(actionEvent -> {
            String nick = textField.getText().trim();
            try {
                if (nick.equals("") || nick.equals("Nickname")) {
                    nick = "Unnamed";
                }
                outputStream.writeUTF(nick);
                username = nick;
                window.setScene(waiting());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Rectangle rectangle = new Rectangle(220, 75);
        rectangle.setFill(Color.WHITE);
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        rectangle.setStroke(Color.BLACK);
        Button button = new Button("Ok, Go!");
        button.setOnAction(actionEvent -> {
            String nick = textField.getText().trim();
            try {
                if (nick.equals("") || nick.equals("Nickname")) {
                    nick = "Unnamed";
                }
                outputStream.writeUTF(nick);
                username = nick;
                window.setScene(waiting());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button.setMinHeight(25);
        button.setMinWidth(200);
        vBox.getChildren().addAll(textField, button);
        vBox.setAlignment(Pos.CENTER);
        VBox vBox1 = new VBox();
        vBox1.setStyle("-fx-background-color : cyan");
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rectangle, vBox);
        vBox1.getChildren().addAll(imageView, stackPane);
        vBox1.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox1);
        return new Scene(borderPane, WIDTH, HEIGHT);
    }

    public Scene waiting() {
        new Thread(() -> {
            try {
                String kindOfQuestion = inputStream.readUTF();
                if (kindOfQuestion.equals("fillin")) {
                    Platform.runLater(() -> {
                        window.setScene(fillin());
                    });
                }
                else if ( kindOfQuestion.equals("truefalse") ) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            window.setScene(trueFalse());
                        }
                    });
                }
                else if ( kindOfQuestion.equals("test")){
                    Platform.runLater(() -> {
                        window.setScene(test());
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView(new File("src/loading.gif").toURI().toString());
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        VBox vBox = new VBox();
        Text text = new Text("Get ready!");
        Text text1 = new Text("Loading...");
        text.setFont(Font.font("VerDana", FontPosture.ITALIC, 30));
        text1.setFont(Font.font("VerDana", FontPosture.ITALIC, 23));
        text.setFill(Color.WHITE);
        text1.setFill(Color.WHITE);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(text, imageView, text1);
        stackPane.getChildren().add(vBox);
        stackPane.setStyle("-fx-background-color : mediumpurple");
        return new Scene(stackPane, WIDTH, HEIGHT);
    }

    public Scene test() {
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            try {
                answer.set(inputStream.readUTF());
            } catch (Exception ignored) {
            }
        }).start();
        AtomicInteger sec = new AtomicInteger(20);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            sec.getAndDecrement();
            score.addAndGet(-4);
            if (sec.get() == 0) {
                Platform.runLater(() -> {
                    try {
                        outputStream.writeInt(0);
                        outputStream.writeInt(1);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    window.setScene(genius());
                    score.set(1020);
                });
            }
        }));
        timeline.setCycleCount(20);
        timeline.play();
        Label label = new Label(username);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("VerDana", FontPosture.ITALIC, 40));
        Rectangle rectangle = new Rectangle(180 * 2 + 5, 60);
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);
        Button red = new Button();
        red.setOnAction(actionEvent -> {
            try {
                timeline.stop();
                window.setScene(genius());
                if (answer.get().equals("red")) {
                    outputStream.writeInt(score.get());
                } else {
                    outputStream.writeInt(0);
                }
                outputStream.writeInt(1);
                outputStream.flush();
                score.set(1020);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Button blue = new Button();
        blue.setOnAction(actionEvent -> {
            try {
                timeline.stop();
                window.setScene(genius());
                if (answer.get().equals("blue")) {
                    outputStream.writeInt(score.get());
                } else {
                    outputStream.writeInt(0);
                }
                outputStream.writeInt(1);
                outputStream.flush();
                score.set(1020);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Button green = new Button();
        green.setOnAction(actionEvent -> {
            try {
                timeline.stop();
                window.setScene(genius());
                if (answer.get().equals("green")) {
                    outputStream.writeInt(score.get());
                } else {
                    outputStream.writeInt(0);
                }
                outputStream.writeInt(1);
                outputStream.flush();
                score.set(1020);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Button orange = new Button();
        orange.setOnAction(actionEvent -> {
            try {
                timeline.stop();
                window.setScene(genius());
                if (answer.get().equals("orange")) {
                    outputStream.writeInt(score.get());
                } else {
                    outputStream.writeInt(0);
                }
                outputStream.writeInt(1);
                outputStream.flush();
                score.set(1020);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        changeButton(red);
        changeButton(blue);
        changeButton(orange);
        changeButton(green);
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(red, orange);
        HBox hBox1 = new HBox(5);
        hBox1.setAlignment(Pos.CENTER);
        hBox1.getChildren().addAll(blue, green);
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rectangle, label);
        vBox.getChildren().addAll(stackPane, hBox, hBox1);
        return new Scene(vBox, WIDTH, HEIGHT);
    }

    public void changeButton(Button button) {
        button.setMinHeight(200);
        button.setMinWidth(180);
        if (color == 0) {
            button.setStyle("-fx-background-color : red");
            Polygon polygon = new Polygon(0, 50, 25, 0, 50, 50);
            polygon.setFill(Color.WHITE);
            button.setGraphic(polygon);
            color++;
            button.setAlignment(Pos.CENTER);
        } else if (color == 1) {
            button.setStyle("-fx-background-color : blue");
            Rectangle tort = new Rectangle(50, 50, Color.WHITE);
            button.setGraphic(tort);
            color++;
            button.setAlignment(Pos.CENTER);
        } else if (color == 2) {
            button.setStyle("-fx-background-color : orange");
            Circle domalaq = new Circle(30, Color.WHITE);
            button.setGraphic(domalaq);
            color++;
            button.setAlignment(Pos.CENTER);
        } else {
            button.setStyle("-fx-background-color : green");
            color = 0;
            Rectangle rombikAsInJu = new Rectangle(40, 40, Color.WHITE);
            rombikAsInJu.setRotate(45);
            button.setGraphic(rombikAsInJu);
            button.setAlignment(Pos.CENTER);
        }
    }

    public Scene fillin() {
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            try {
                answer.set(inputStream.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        AtomicInteger sec = new AtomicInteger(20);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            sec.getAndDecrement();
            score.addAndGet(-4);
            if (sec.get() == 0) {
                Platform.runLater(() -> {
                    try {
                        outputStream.writeInt(0);
                        outputStream.writeInt(1);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    score.set(1020);
                    window.setScene(genius());
                });
            }
        }));
        timeline.setCycleCount(20);
        timeline.play();
        TextField textArea = new TextField();
        textArea.setOnAction(actionEvent -> {
            try {
                timeline.stop();
                if (answer.get().equals(textArea.getText().trim())) {
                    outputStream.writeInt(score.get());
                    outputStream.flush();
                } else {
                    outputStream.writeInt(0);
                }
                outputStream.writeInt(1);
                outputStream.flush();
                score.set(1020);
                window.setScene(genius());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        VBox vBox = new VBox();
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(new FileInputStream("src/when.png"));
            imageView.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setFitHeight(120);
        imageView.setFitWidth(300);
        textArea.setMaxHeight(40);
        textArea.setMaxWidth(250);
        vBox.getChildren().addAll(imageView, textArea);
        vBox.setAlignment(Pos.CENTER);
        return new Scene(vBox, WIDTH, HEIGHT);
    }

    public Scene genius() {
        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView(new File("src/loading.gif").toURI().toString());
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        VBox vBox = new VBox();
        Text text = new Text("Genius machine??)");
        text.setFont(Font.font("VerDana", FontPosture.ITALIC, 30));
        text.setFill(Color.WHITE);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(text, imageView);
        stackPane.getChildren().add(vBox);
        stackPane.setStyle("-fx-background-color : purple");
        new Thread(() -> {
            try {
                String s = inputStream.readUTF();
                if (s.equals("correct")) {
                    Platform.runLater(() -> {
                        window.setScene(correct());
                    });
                } else {
                    Platform.runLater(() -> {
                        window.setScene(inCorrect());
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return new Scene(stackPane, WIDTH, HEIGHT);
    }

    public void changeButtonForTrueFalse(Button button) {
        button.setMinHeight(200);
        button.setMaxHeight(200);
        button.setMinWidth(195);
        button.setMaxWidth(195);
        if ( color == 0 ) {
            color++;
            button.setStyle("-fx-background-color : red");
        }
        else {
            color = 0;
            button.setStyle("-fx-background-color : blue");
        }
    }

    public Scene trueFalse() {
        BorderPane borderPane = new BorderPane();
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            try {
                answer.set(inputStream.readUTF());
            } catch (Exception ignored) {}
        }).start();
        AtomicInteger sec = new AtomicInteger(20);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            sec.getAndDecrement();
            score.addAndGet(-4);
            if (sec.get() == 0) {
                Platform.runLater(() -> {
                    try {
                        outputStream.writeInt(0);
                        outputStream.writeInt(1);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    window.setScene(genius());
                    score.set(1020);
                });
            }
        }));
        timeline.setCycleCount(20);
        timeline.play();
        Label label = new Label(username);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("VerDana", FontPosture.ITALIC, 40));
        Rectangle rectangle = new Rectangle(180 * 2 + 5, 60);
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);
        StackPane s = new StackPane();
        s.getChildren().addAll(rectangle, label);
        Button red = new Button();
        Polygon polygon = new Polygon(0, 50, 25, 0, 50, 50);
        polygon.setFill(Color.WHITE);
        red.setGraphic(polygon);
        changeButtonForTrueFalse(red);
        red.setAlignment(Pos.CENTER);
        red.setOnAction(actionEvent -> {
            try {
                timeline.stop();
                window.setScene(genius());
                if (answer.get().equals("True")) {
                    outputStream.writeInt(score.get());
                } else {
                    outputStream.writeInt(0);
                }
                outputStream.writeInt(1);
                outputStream.flush();
                score.set(1020);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Button blue = new Button();
        changeButtonForTrueFalse(blue);
        Rectangle rec1 = new Rectangle(35, 35, Color.WHITE);
        blue.setGraphic(rec1);
        blue.setAlignment(Pos.CENTER);
        blue.setOnAction(actionEvent -> {
            try {
                timeline.stop();
                window.setScene(genius());
                if (answer.get().equals("False")) {
                    outputStream.writeInt(score.get());
                } else {
                    outputStream.writeInt(0);
                }
                outputStream.writeInt(1);
                outputStream.flush();
                score.set(1020);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        HBox hBox = new HBox(3);
        hBox.getChildren().addAll(red, blue);
        borderPane.setTop(s);
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);
        return new Scene(borderPane, WIDTH, HEIGHT);
    }

    public Scene correct() {
        new Thread(() -> {
            try {
                String kindOfQuestion = inputStream.readUTF();
                if (kindOfQuestion.equals("fillin")) {
                    Platform.runLater(() -> {
                        window.setScene(fillin());
                    });
                }
                else if ( kindOfQuestion.equals("truefalse") ) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            window.setScene(trueFalse());
                        }
                    });
                }
                else if ( kindOfQuestion.equals("test")){
                    Platform.runLater(() -> {
                        window.setScene(test());
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        try {
            Image image = new Image(new FileInputStream("src/backkk.png"));
            imageView.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Text text = new Text("Correct answer!!!");
        text.setFont(Font.font("VerDana", FontPosture.ITALIC, 30));
        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(text, imageView);
        vBox.setAlignment(Pos.CENTER);
        return new Scene(new StackPane(vBox), WIDTH, HEIGHT);
    }

    public Scene inCorrect() {
        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        try {
            Image image = new Image(new FileInputStream("src/thanks.png"));
            imageView.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Text text = new Text("InCorrect answer!!!");
        text.setFont(Font.font("VerDana", FontPosture.ITALIC, 30));
        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(text, imageView);
        vBox.setAlignment(Pos.CENTER);
        new Thread(() -> {
            try {
                String kindOfQuestion = inputStream.readUTF();
                if (kindOfQuestion.equals("fillin")) {
                    Platform.runLater(() -> {
                        window.setScene(fillin());
                    });
                }
                else if ( kindOfQuestion.equals("truefalse") ) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            window.setScene(trueFalse());
                        }
                    });
                }
                else if ( kindOfQuestion.equals("test")) {
                    Platform.runLater(() -> {
                        window.setScene(test());
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return new Scene(new StackPane(vBox), WIDTH, HEIGHT);
    }
}