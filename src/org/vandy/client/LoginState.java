package org.vandy.client;

import com.polaris.engine.render.Font;

public class LoginState 
{
	
	protected LoginGui loginGui;
	protected Font font;
	protected float ticksExisted = 0;
	
	public LoginState(LoginGui gui, Font boldFont)
	{
		loginGui = gui;
		font = boldFont;
	}
	
	public void render(double delta)
	{
		ticksExisted += (float) delta;
	}
	
	public void close()
	{

	}

}
