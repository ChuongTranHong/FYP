package com.piezo.screen;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.utils.Scaling;
import com.piezo.maingame.PiezoGame;
import com.piezo.util.IOIOThread;


public class MainMenu extends GameScreen {
	
	Skin skin;
	PiezoGame game;
	Texture texture;
	private Stage ui;
	private Table window;
//	SpriteBatch spriteBatch;private Sprite          bgSprite;
	public MainMenu(final PiezoGame game){
		
		this.game=game;
//		spriteBatch=new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/samurai.jpg"));
		
//		bgSprite=new Sprite(texture,Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight() / 2);
//		TextureRegion image = new TextureRegion(new Texture(Gdx.files.internal(Art.badlogicSmall)));
		
		 Image image = new Image(texture);
		
//		 libgdx.x=Gdx.graphics.getWidth() / 2;
//		 libgdx.y=Gdx.graphics.getHeight() / 2;
		skin = new Skin(Gdx.files.internal("data/uiskin.json"), Gdx.files.internal("data/uiskin.png"));
	
	    ui = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), false);
	    System.out.println(" stage width "+ui.width()+" height "+ui.height());
	    System.out.println(" image width "+texture.getWidth()+" height "+texture.getHeight());
	    float ratiox=ui.width()/texture.getWidth();
	    float ratioy=ui.height()/texture.getHeight();
	    System.out.println(" ratio x "+ratiox+ " ratio y "+ratioy);
//	    Scaling scale;
//	    scale.apply(image.getImageWidth(), image.getImageHeight(), ui.width(), ui.height());
	    image.scaleX=ratiox;
	    image.scaleY=ratioy;

	    Gdx.input.setInputProcessor(ui);
	    window = new Table("window");
	    window.width = ui.width();
	    window.height = ui.height();
	    window.x = 0;
	    window.y = 0;
	    window.debug(); 
	    
	    Label fps = new Label("fps: ",skin.getStyle(LabelStyle.class),"fps");
	    Label title = new Label("SAMURAI FRUIT",skin.getStyle(LabelStyle.class),"title");
	    Button newGame = new TextButton("New game",skin.getStyle(TextButtonStyle.class),"new");
	    Button optionMenu = new TextButton("option",skin.getStyle(TextButtonStyle.class),"Options");
	    Button helpMenu = new TextButton("help",skin.getStyle(TextButtonStyle.class),"Help");
	    optionMenu.setClickListener(new ClickListener() {
			
			public void click(Actor actor, float x, float y) {
				// TODO Auto-generated method stub
				game.setScreen(new ConfigurationScreen(game));
			}
		});
//	    newGame.
	    newGame.setClickListener(new ClickListener() {


			public void click(Actor actor, float x, float y) {
				// TODO Auto-generated method stub
				System.out.println(" newg game click");
				game.setScreen(new RunningScreen(game));
			}
	    });
//	    Image libgdx = new Image("libgdx", image);
	    window.row().fill(false,false).expand(true,false).padTop(10).padBottom(20);
	    window.add(title);
	    
	    Table container = new Table("menu");
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(newGame);
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(optionMenu);
	    container.row().fill(true, true).expand(true, true).pad(10, 0, 10, 0);
	    container.add(helpMenu);
	    window.row().fill(0.5f,1f).expand(true,false);
	    window.add(container);
	    
	    Table extras = new Table("extras");
	    extras.row().fill(false,false).expand(true,true);
	    extras.add(fps).left().center().pad(0,25,25,0); 
//	    extras.add(libgdx).right().center().pad(0,0,25,25);
	    window.row().fill(true,false).expand(true,true);
	    window.add(extras).bottom();  
	    ui.addActor(image);
	    ui.addActor(window);
	   

		
	}
	public void render(float delta) {
		int centerX = Gdx.graphics.getWidth() / 2;
		int centerY = Gdx.graphics.getHeight() / 2;
	
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    ((Label)ui.findActor("fps")).setText("fps: " + Gdx.graphics.getFramesPerSecond());  
	    ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	    
	    Table.drawDebug(ui);
//	    spriteBatch.begin();
//		spriteBatch.draw(texture, centerX - texture.getWidth() / 2, centerY - texture.getHeight() / 2+40, 0, 0, texture.getWidth(),
//				texture.getHeight());
		ui.draw();
//	    spriteBatch.end();
	}

	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void hide() {
		// TODO Auto-generated method stub
		
	}

	public void pause() {
		// TODO Auto-generated method stub
		
	}

	public void resume() {
		// TODO Auto-generated method stub
		
	}	
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	
}
