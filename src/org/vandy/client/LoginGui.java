package org.vandy.client;

import java.io.File;

import org.lwjgl.opengl.GL11;

import com.polaris.engine.App;
import com.polaris.engine.Gui;
import com.polaris.engine.render.Font;
import com.polaris.engine.render.Shader;
import com.polaris.engine.render.VBO;

public class LoginGui extends Gui
{

	private Font playRegular;
	private VBO render;
	
	public LoginGui(App app) 
	{
		super(app);
	}
	
	public LoginGui(App app, double ticksExisted)
	{
		super(app, ticksExisted);
	}
	
	@Override
	public void init()
	{
		playRegular = Font.createFont(new File("Play-Regular.ttf"), 64);
		GL11.glClearColor(1, 1, 1, 1);
		render = playRegular.draw("TEST", 100, 100, 0);
	}
	
	@Override
	public void render(double delta)
	{
		application.gl2d();
		//GL11.glColor4d(1, 1, 1, 1);
		playRegular.bind();
		Shader.POS_TEXTURE.bind();
		render.bind();
		render.setupDrawEnable();
		render.draw();
		Shader.POS_TEXTURE.unbind();
	}

}
