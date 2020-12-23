package snmp_simulation;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.ireasoning.protocol.TimeoutException;

public class Refresher extends Thread
{

	private App app;
	private boolean backFromException = false;

	public Refresher(App app)
	{
		this.app = app;
		this.start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				if (this.backFromException)
				{
					this.app.refresh();
					this.backFromException = false;
				}

				int secondsTillRefresh = this.app.getSecondsTillRefresh();
				if (secondsTillRefresh == 1)
				{
					this.app.refresh();
				} else
				{
					this.app.setSecondsTillRefresh(secondsTillRefresh - 1);
				}
				Thread.sleep(1000);

			} catch (InterruptedException e)
			{
				e.printStackTrace();
			} catch (TimeoutException e)
			{
				JOptionPane.showMessageDialog(new JFrame(),
						"You have probably shut down an interface critical for the protocol to spread information and work. Please do a 'no shut' on the interface that you shut down");
				this.backFromException = true;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
