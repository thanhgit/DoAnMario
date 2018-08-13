package com.example.doanmario;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.example.doanmario.SceneManager.SceneType;

public class TutorialScene extends BaseScene{
	private Sprite tutorial;

	@Override
	public void createScene() {
		tutorial=new Sprite(0, 0, resourcesManager.tutorial_background_region, vbom){

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
			       pGLState.enableDither();
			}
		};
		tutorial.setPosition(400, 240);
		attachChild(tutorial);
	}

	@Override
	public void onBackKeyPressed() {
		
		SceneManager.getInstance().loadMenuSceneFromTutorial(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_TUTORIAL;
	}

	@Override
	public void disposeScene() {
		tutorial.detachSelf();
	    tutorial.dispose();
	    this.detachSelf();
	    this.dispose();
	}

}
