package de.erik.sortvisualizer.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SortApp extends Application {

	private final double INTERVAL = 0.032;
	private int amount;

	private Pane root;
	private List<Rectangle> rects;

	private Parent createContent() {
		root = new Pane();
		root.setPrefSize(800, 400);
		rects = new ArrayList<>();
		sortRects();
		return root;
	}

	private final int amts[] = { 8, 10, 20, 25, 40, 50, 100 };

	private void sortRects() {
		amount = amts[(int) (Math.random() * amts.length)];
		createRects();
		drawRects();
		new AnimationTimer() {
			double t = 0;
			boolean done;

			public void handle(long now) {
				t += 0.016;
				if (t >= INTERVAL) {
					done = true;
					for (int i = 0; i < amount - 1; i++) {
						for (int j = 0; j < i; j++)
							rects.get(j).setFill(Color.STEELBLUE);
						rects.get(i).setFill(Color.BLUE);
						if (rects.get(i).getHeight() > rects.get(i + 1).getHeight()) {
							rects.get(i + 1).setFill(Color.STEELBLUE);
							double tmpHeight = rects.get(i).getHeight();
							rects.get(i).setHeight(rects.get(i + 1).getHeight());
							rects.get(i + 1).setHeight(tmpHeight);
							double tmpTrans = rects.get(i).getTranslateY();
							rects.get(i).setTranslateY(rects.get(i + 1).getTranslateY());
							rects.get(i + 1).setTranslateY(tmpTrans);
							drawRects();
							done = false;
							break;
						}
					}
					if (done) {
						stop();
						sortRects();
					}
					t = 0;
				}
			}
		}.start();
	}

	private void drawRects() {
		root.getChildren().setAll(rects);
	}

	private void createRects() {
		Stack<Integer> hStack = new Stack<>();
		fillStack(hStack);
		rects.clear();
		for (int i = 0; i < amount; i++) {
			double width = 800 / amount;
			double height = hStack.pop();
			var rect = new Rectangle(width, height, Color.STEELBLUE);
			rect.setTranslateX(width * i);
			rect.setTranslateY(400 - height);
			rects.add(rect);
		}
	}

	private void fillStack(Stack<Integer> hStack) {
		for (int height = 1; height <= 400; height += 400 / amount) {
			hStack.push(height);
		}
		Collections.shuffle(hStack);
	}

	@Override
	public void start(Stage stage) throws Exception {
		var scene = new Scene(createContent(), Color.TRANSPARENT);
		scene.setOnKeyPressed(e -> Platform.exit());
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setAlwaysOnTop(true);
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
