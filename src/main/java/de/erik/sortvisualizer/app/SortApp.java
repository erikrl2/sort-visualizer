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

	private final double INTERVAL = 0.1; // 0.032
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
		root.getChildren().setAll(rects);
		new AnimationTimer() {
			double t = 0;
			boolean done;

			public void handle(long now) {
				t += 0.016;
				if (t >= INTERVAL) {
					done = true;
					for (int i = 0; i < amount - 1; i++) {
						if (rects.get(i).getHeight() > rects.get(i + 1).getHeight()) {
							var cur = rects.get(i);
							var nxt = rects.get(i +1);
							double tmpHeight = cur.getHeight();
							cur.setHeight(nxt.getHeight());
							nxt.setHeight(tmpHeight);
							double tmpTrans = cur.getTranslateY();
							cur.setTranslateY(nxt.getTranslateY());
							nxt.setTranslateY(tmpTrans);
							var tmpFill = cur.getFill();
							cur.setFill(nxt.getFill());
							nxt.setFill(tmpFill);
							done = false;
							i++;
						}
					}
					if (done) {
						stop();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						sortRects();
					}
					t = 0;
				}
			}
		}.start();
	}

	private void createRects() {
		Stack<Integer> hStack = new Stack<>();
		for (int height = 1; height <= 400; height += 400 / amount)
			hStack.push(height);
		Collections.shuffle(hStack);
		rects.clear();
		int grgb = 0;
		final int inc = 224 / amount;
		for (int i = 0; i < amount; i++) {
			double width = 800 / amount;
			double height = hStack.pop();
			var rect = new Rectangle(width, height, Color.grayRgb(grgb));
			rect.setTranslateX(width * i);
			rect.setTranslateY(400 - height);
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
