package com.example.doanmario;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class Enemy extends AnimatedSprite{
	
	public Body body;
	protected EnemySensors lastdirection;

	private boolean isMoving = false;
	public Enemy(float pX, float pY, VertexBufferObjectManager vbo,
			BoundCamera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourcesManager.getInstance().koopaTextureRegion, vbo);
		createPhysics(camera, physicsWorld);
	}
	
	public abstract void onDie();
	public abstract void IA();
	
	private void createPhysics(final BoundCamera camera,
			PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("enemy");
		body.setFixedRotation(true);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				IA();
				if (getY() <= 0) {
					onDie();
				}
			}
		});
	}
	
	public void jump() {
		if (body.getLinearVelocity().x == 0) {
			body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
		}
	}
	
	public void move(EnemySensors to) {

		if (to == EnemySensors.LEFT) {

			this.isMoving = true;

			lastdirection = to;

			this.animate(new long[] { 400, 400 }, 2, 3, true);

			body.setLinearVelocity(-1 * GameConstants.PLAYER_VELOCITY, 0);

		} else if (to == EnemySensors.RIGHT) {

			this.isMoving = true;

			lastdirection = to;

			this.animate(new long[] { 400, 400}, 0, 1, true);

			body.setLinearVelocity(1 * GameConstants.PLAYER_VELOCITY, 0);
		} else if (to == EnemySensors.STOP) {
			this.isMoving = false;
			body.setLinearVelocity(0, 0);

			if (lastdirection == EnemySensors.LEFT) {
				this.stopAnimation(2);
			} else if (lastdirection == EnemySensors.RIGHT) {
				this.stopAnimation(0);
			}
		}

	}
}
