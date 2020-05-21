package com.mygdx.game;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MyGdxGame extends ApplicationAdapter {
	// max rows and columns in a grid
	private static final int MAX_ROWS = 100;
	private static final int MAX_COLS = 100;
	// maximum number of tiles to be displayed outward from camera
	private static final int DISPLAY_RANGE = 10;
	
	//libgdx resources (camera, etc.)
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private FitViewport viewport;
	
	//graphical resources
	private TextureAtlas charAtlas;
	private Animation<TextureRegion> platina; // test animation for debugging
	
	
	//tile resources
	private TextureAtlas atlas;
	private Map<String, Tile> template;
	
	//grid resources
	String[][] testBoard;
	
	//entity resources
	private Entity player;
	
	//system resources (states, timers, etc.)
	private float stateTime;
	private BitmapFont font;
	
	@Override
	public void create () {
		
		//libgdx resources
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new FitViewport(20, 20, camera);
		
		//graphical resources
		charAtlas = new TextureAtlas(Gdx.files.internal("Characters/Characters.atlas"));
		platina = animationFromAtlas(charAtlas, "Reptile", 12, 3);
		
		//tile resources
		atlas = new TextureAtlas(Gdx.files.internal("Biomes/DarkGrass/Tileset.atlas"));
		template = createTemplate(atlas);
		
		//grid resources
		// 10x10 test board will delete later
		/*testBoard = new String[][] {{"wall1","floor1","floor4","floor4","pit11","pit13","floor1","floor7","pit13","pit13"},
		{"wall1","floor1","floor4","floor4","pit11","pit13","floor0","floor6","pit13","pit13"},
		{"wall1","floor1","floor4","floor4","pit10","pit22","pit13","pit13","pit13","pit13"},
		{"wall1","floor1","floor4","floor4","floor4","pit10","pit12","pit12","pit12","pit12"},
		{"wall1","floor1","floor4","floor4","floor4","floor4","floor4","floor4","floor4","floor4"},
		{"wall1","floor1","floor4","floor4","floor4","floor4","floor4","floor4","floor4","floor4"},
		{"wall1","floor1","floor4","floor4","floor4","floor4","floor4","floor4","floor4","floor4"},
		{"wall1","floor0","floor3","floor3","floor3","floor3","floor3","floor3","floor3","floor3"},
		{"wall1","wall9","wall9","wall9","wall9","wall9","wall9","wall9","wall9","wall9"},
		{"wall0", "wall3","wall3","wall3","wall3","wall3","wall3","wall3","wall3","wall3"}};*/
		testBoard = new String[MAX_COLS][MAX_ROWS];
		for(int col = 0; col < MAX_COLS; col++) {
			for(int row = 0; row < MAX_ROWS; row++) {
				testBoard[col][row] = "floor4";
			}
		}
		testBoard[0][0] = "floor2";
		testBoard[MAX_COLS - 1][MAX_ROWS - 1] = "floor6";
		testBoard[0][MAX_ROWS - 1] = "floor8";
		testBoard[MAX_COLS - 1][0] = "floor0";
		
		// this part assumes a square grid because I got lazy and frustrated
		for(int i = 1; i < MAX_COLS - 1; i ++) {
			testBoard[0][i] = "floor5";
			testBoard[i][0] = "floor1";
			testBoard[MAX_COLS - 1][i] = "floor3";
			testBoard[i][MAX_COLS - 1] = "floor7";
		}
		
		//entity resources
		player = new Entity(platina, 2, 2, 1, 1);
		
		//system resources
		stateTime = 0f;
		font = new BitmapFont();
		font.getData().setScale(.3f, .3f);
	}

	@Override
	public void render () {
		//clear screen
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//update
		stateTime += Gdx.graphics.getDeltaTime();
		
		//update camera
		update(stateTime);
		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		/*batch.draw(template.get(testBoard[j][i]).getTexture(stateTime), i, j, 1, 1);for(int i = 0; i < 10; i ++) {
			for(int j = 0; j < 10; j++) {
				batch.draw(template.get(testBoard[j][i]).getTexture(stateTime), i, j, 1, 1);
			}
		}*/
		for(int i = (int) player.getX() - DISPLAY_RANGE; i < player.getX() + DISPLAY_RANGE; i++) {
			for(int j = (int) player.getY() - DISPLAY_RANGE; j < player.getY() + DISPLAY_RANGE; j++) {
				if(i >= 0 && i < MAX_COLS && j >= 0 && j < MAX_ROWS) {
					batch.draw(template.get(testBoard[j][i]).getTexture(stateTime), i, j, 1, 1);
				}
			}
		}
		batch.draw(player.getTexture(stateTime), player.getX(), player.getY(), player.getWidth(), player.getHeight());
		// font.draw(batch, "Hello", 0, 0);
		batch.end();
	}
	
	public void update(float st) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && player.getX() > 0) {
			if(template.get(testBoard[(int) player.getY()][(int) (player.getX() - 1)]).isPassable()) {
				player.setX(player.getX() - 1);
			}
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && player.getX() < MAX_COLS - 1) {
			if(template.get(testBoard[(int) player.getY()][(int) (player.getX() + 1)]).isPassable()) {
				player.setX(player.getX() + 1);
			}
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && player.getY() > 0) {
			if(template.get(testBoard[(int) (player.getY() - 1)][(int) (player.getX())]).isPassable()) {
				player.setY(player.getY() - 1);
			}
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.getY() < MAX_ROWS - 1) {
			if(template.get(testBoard[(int) (player.getY() + 1)][(int) (player.getX())]).isPassable()) {
				player.setY(player.getY() + 1);
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(player.getX(), player.getY(), 0);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
	
	// converts a properly formatted Atlas into a map
	// key: String (tile name such as "floor1"), value: Tile object
	public Map<String, Tile> createTemplate(TextureAtlas atlas) {
		// ATLAS MUST BE FORMATTED CORRECTLY. This includes the following regions:
		// Floor (7x3), Wall (6x3), Pit1A (3x2), Pit1B (4x2), Pit2A (3x2), Pit2B (4x2)
		
		// local variables that are used to split the AtlasRegions
		// even though they're only used once each, that may change, so I'm 
		// putting them here rather than use "magic numbers"
		int floorWidth = 7;
		int floorHeight = 3;
		int wallWidth = 6;
		int wallHeight = 3;
		int pit1Width = 3;
		int pit1Height = 2;
		int pit2Width = 4;
		int pit2Height = 2;
		
		TextureRegion[][] wall = atlas.findRegion("Wall").split(16, 16);
		TextureRegion[][] floor = atlas.findRegion("Floor").split(16, 16);
		TextureRegion[][] pit1a = atlas.findRegion("Pit1A").split(16, 16);
		TextureRegion[][] pit1b = atlas.findRegion("Pit1B").split(16, 16);
		TextureRegion[][] pit2a = atlas.findRegion("Pit2A").split(16, 16);
		TextureRegion[][] pit2b = atlas.findRegion("Pit2B").split(16, 16);
		
		Map<String, Tile> rVal = new HashMap<>();
		ArrayList<Animation<TextureRegion>> pit1Animations = new ArrayList<>();
		ArrayList<Animation<TextureRegion>> pit2Animations = new ArrayList<>();
		
		for(int row = 0; row < pit1Width; row++) {
			for(int col = 0; col < pit1Height; col++) {
				pit1Animations.add(new Animation<TextureRegion>(1.0f, new TextureRegion[] {pit1a[col][row], pit1b[col][row]}));
			}
		}
		
		for(int row = 0; row < pit2Width; row++) {
			for(int col = 0; col < pit2Height; col++) {
				pit2Animations.add(new Animation<TextureRegion>(1.0f, new TextureRegion[] {pit2a[col][row], pit2b[col][row]}));
			}
		}
		
		for(int row = 0; row < floorWidth; row++) {
			for(int col = 0; col < floorHeight; col++) {
				rVal.put("floor" + (row*floorHeight + col), new StaticTile(true, floor[col][row]));
			}
		}
		
		for(int row = 0; row < wallWidth; row++) {
			for(int col = 0; col < wallHeight; col++) {
				rVal.put("wall" + (row*wallHeight + col), new StaticTile(false, wall[col][row]));
			}
		}
		
		for(int row = 0; row < pit1Width; row++) {
			for(int col = 0; col < pit1Height; col++) {
				rVal.put("pit1" + (row*pit1Height + col), new AnimatedTile(false, pit1Animations.get(row*pit1Height+col)));
			}
		}
		
		for(int row = 0; row < pit2Width; row++) {
			for(int col = 0; col < pit2Height; col++) {
				rVal.put("pit2" + (row*pit2Height + col), new AnimatedTile(false, pit2Animations.get(row*pit1Height+col)));
			}
		}
		
		return rVal;
	}
	
	// this is a method to make animations from a texture atlas. it will be expanded upon
	// in future revisions of the project, as right now it accepts very specific/limited input
	public Animation<TextureRegion> animationFromAtlas(TextureAtlas atlas, String name, int row, int col) {
		String name0 = name + "0";
		String name1 = name + "1";
		TextureRegion[] frames = new TextureRegion[2];
		frames[0] = new TextureRegion(atlas.findRegion(name0), col * 16, row * 16, 16, 16);
		frames[1] = new TextureRegion(atlas.findRegion(name1), col * 16, row * 16, 16, 16);
		return new Animation<TextureRegion>(0.5f, frames);
	}
	
}
