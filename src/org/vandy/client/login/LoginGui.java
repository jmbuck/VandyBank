package org.vandy.client.login;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.opengl.GL11;
import org.vandy.client.GuiScreen;

import com.polaris.engine.App;
import com.polaris.engine.options.Key;
import com.polaris.engine.render.Draw;

public class LoginGui extends GuiScreen
{

	private LoginState state;
	
	private String[] customerSearch;

	public LoginGui(App app) 
	{
		super(app);
	}

	@Override
	public void init()
	{
		super.init();
		
		setState(new LoginLoading(this, boldFont));
	}

	@Override
	public void render(double delta)
	{
		super.render(delta);

		state.render(delta);
	}

	public void close()
	{
		font.destroy();
		boldFont.destroy();
		GLFW.glfwSetCharCallback(application.getWindow(), null);
	}

	public void setState(LoginState newState) 
	{
		if(state != null)
		state.close();
		
		state = newState;
		newState.init();
	}
	
	public String[] getCustomers()
	{
		return customerSearch;
	}

	public void setPotentials(String[] potentials)
	{
		customerSearch = potentials;
	}

	public LoginState getState() 
	{
		return state;
	}

}
