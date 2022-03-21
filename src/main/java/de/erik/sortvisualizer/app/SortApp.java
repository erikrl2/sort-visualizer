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

public class SortApp extends Application {

	private final int AMT = 400;
	private final double FREQUENZY = 0.05;

	private Pane root;
	private List<Rectangle> rects;

	private Parent createContent() {
		root = new Pane();
		root.setPrefSize(800, 400);
		rects = new ArrayList<>();
		if (AMT <= 400) {
			createRects();
			sortRects();
		}
		return root;
	}

	private void sortRects() {
		drawRects();
		var animationTimer = new AnimationTimer() {
			double t = 0;

			public void handle(long now) {
				t += 0.016;
				if (t >= FREQUENZY) {
					int count = 0;
					for (int i = 0; i < AMT - 1; i++) {
						if (rects.get(i).getHeight() < rects.get(i + 1).getHeight()) {
							double tmpHeight = rects.get(i).getHeight();
							rects.get(i).setHeight(rects.get(i + 1).getHeight());
							rects.get(i + 1).setHeight(tmpHeight);
							drawRects();
							count++;
						}
					}
					if (count == 0) {
						stop();
						System.out.println("sort finished");
						Platform.exit();
					}
					t = 0;
				}
			}
		};
		animationTimer.start();
	}

	private void drawRects() {
		root.getChildren().setAll(rects);
	}

	private void createRects() {
		Stack<Double> hStack = new Stack<>();
		fillStack(hStack);
		for (int i = 0; i < AMT; i++) {
			double width = 800 / AMT;
			double height = hStack.pop();
			var rect = new Rectangle(width, height, Color.STEELBLUE);
			rect.setTranslateX(width * i);
			rects.add(rect);
		}
	}

	private void fillStack(Stack<Double> hStack) {
		for (double i = 1; i <= 400; i += 400 / AMT) {
			hStack.push(i);
		}
		Collections.shuffle(hStack);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(createContent(), Color.BLACK));
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
