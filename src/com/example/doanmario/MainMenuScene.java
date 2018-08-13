package com.example.doanmario;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import com.example.doanmario.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener{
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int MENU_TUTORIAL=2;
	private final int MENU_EXIT=3;
	
	private HUD menuHUD;
	@Override
	public void createScene() {
		createMenuChildScene();
	}
	
	private void createMenuChildScene()
	{
		menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(400, 240);
	    
	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
	    final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_region, vbom), 1.2f, 1);
	    final IMenuItem tutorialMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_TUTORIAL, resourcesManager.tutorial_region, vbom), 1.2f, 1);
	    final IMenuItem exitMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_EXIT, resourcesManager.exit_region, vbom), 1.2f, 1);
	    
	    Sprite background=new Sprite(0, 0, resourcesManager.menu_background_region, vbom)
	    {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
		         super.preDraw(pGLState, pCamera);
		            pGLState.enableDither();
			}
	    };
	    menuChildScene.attachChild(background);
	    menuChildScene.addMenuItem(playMenuItem);
	    menuChildScene.addMenuItem(optionsMenuItem);
	    menuChildScene.addMenuItem(tutorialMenuItem);
	    menuChildScene.addMenuItem(exitMenuItem);
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
	    playMenuItem.setPosition(160,40);
	    optionsMenuItem.setPosition(160,-30);
	    tutorialMenuItem.setPosition(160,-100);
	    exitMenuItem.setPosition(160, -170);
	   menuChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(menuChildScene);
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID())
        {
        case MENU_PLAY:
        	 SceneManager.getInstance().loadGameScene(engine);
            return true;
        case MENU_OPTIONS:
            return true;
        case MENU_TUTORIAL:
        	SceneManager.getInstance().createTutorialScene();
        	return true;
        case MENU_EXIT:
        	System.exit(0);
        	return true;
        default:
            return false;
	}
	}
}
