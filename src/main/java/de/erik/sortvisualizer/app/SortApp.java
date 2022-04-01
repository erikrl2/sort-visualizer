package de.erik.sortvisualizer.app;

import java.util.ArrayList;
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

	private final double INTERVAL = 0.032; // 0.032
	private int amount;
	final double W = 1200; // 800
	final double H = 600; // 600

	private Pane root;
	private List<Rectangle> rects;
	private boolean toggle;

	private Parent createContent() {
		root = new Pane();
		root.setPrefSize(W, H);
		rects = new ArrayList<>();
		sortRects();
		return root;
	}

	private void sortRects() {
		amount = 100; // 8, 10, 20, 25, 40, 50, 100
		createRects();
		root.getChildren().setAll(rects);
		new AnimationTimer() {
			double t = 0;
			int effect = 0;
			boolean done;

			public void handle(long now) {
				t += 0.016;
				if (t >= INTERVAL) {
					done = true;
					for (int i = 0; i < amount - 1; i++) {
						if (toggle ? rects.get(i).getHeight() < rects.get(i + 1).getHeight()
								: rects.get(i).getHeight() > rects.get(i + 1).getHeight()) {
							double tmpHeight = rects.get(i).getHeight();
							rects.get(i).setHeight(rects.get(i + 1).getHeight());
							rects.get(i + 1).setHeight(tmpHeight);
							double tmpTrans = rects.get(i).getTranslateY();
							rects.get(i).setTranslateY(rects.get(i + 1).getTranslateY());
							rects.get(i + 1).setTranslateY(tmpTrans);
							var tmpFill = rects.get(i).getFill();
							rects.get(i).setFill(rects.get(i + 1).getFill());
							rects.get(i + 1).setFill(tmpFill);
							done = false;
							i += effect;
						}
					}
					if (done) {
						toggle = !toggle;
						effect = (int) (Math.random() * 3);
					} else
						t = 0;
				}
			}
		}.start();
	}

	private void createRects() {
		Stack<Integer> hStack = new Stack<>();
		for (int height = 1; height <= H; height += H / amount)
			hStack.push(height);
		rects.clear();
		int grgb = 0;
		final int inc = 224 / amount;
		for (int i = 0; i < amount; i++) {
			double width = W / amount;
			double height = hStack.pop();
			var rect = new Rectangle(width, height, Color.grayRgb(grgb));
			rect.setTranslateX(width * i);
			rect.setTranslateY(H - height);
			rects.add(rect);
			grgb += inc;
		}
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
