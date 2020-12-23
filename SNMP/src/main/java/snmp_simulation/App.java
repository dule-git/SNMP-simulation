package snmp_simulation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.ireasoning.protocol.TimeoutException;

public class App extends JFrame implements ItemListener
{
	private List<Router> routers;
	private Router shownRouter;
	private JComboBox<String> routerMenu;
	private JLabel secondsTillRefreshLabel;
	private int secondsTillRefresh;

	public App()
	{
		routers = new ArrayList<>();
	}

	public void addRouter(Router r)
	{
		routers.add(r);
	}

	public void start()
	{
		this.initApp();
	}

	public void initApp()
	{
		JPanel northPanel = new JPanel(new FlowLayout());

		this.routerMenu = new JComboBox<String>(new String[] { "Router 1", "Router 2", "Router 3"
		});
		this.routerMenu.setEditable(false);
		this.routerMenu.addItemListener(this);
		this.routerMenu.setSize(100, 50);

		this.secondsTillRefresh = 11;
		this.secondsTillRefreshLabel = new JLabel("Seconds till refresh: 11");

		northPanel.add(this.routerMenu);
		northPanel.add(this.secondsTillRefreshLabel);

		this.shownRouter = this.routers.get(0);

		this.setLayout(new BorderLayout());
		this.add(northPanel, BorderLayout.NORTH);
		this.add(this.shownRouter, BorderLayout.CENTER);
		int numInfs = this.shownRouter.getNumOfInterfaces();
		int numRows = numInfs % 4 == 0 ? numInfs / 4 : numInfs / 4 + 1;
		int numCols = 4;
		this.setSize(numCols * 400, numRows * 200 + 50);

		new Refresher(this);
		this.setVisible(true);
	}

	public void refresh() throws TimeoutException, IOException
	{

		for (Router r : this.routers)
			r.refresh();

		this.secondsTillRefresh = 11;
		this.secondsTillRefreshLabel.setText("Seconds till refresh: 11");

		this.remove(this.shownRouter);

		int selectedIndex = this.routerMenu.getSelectedIndex();
		this.shownRouter = this.routers.get(selectedIndex);

		this.add(this.shownRouter, BorderLayout.CENTER);

		int numInfs = this.shownRouter.getNumOfInterfaces();
		int numRows = numInfs % 4 == 0 ? numInfs / 4 : numInfs / 4 + 1;
		int numCols = 4;
		this.setSize(numCols * 400, numRows * 200 + 50);

		SwingUtilities.updateComponentTreeUI(this);
	}

	@Override
	public void itemStateChanged(ItemEvent event)
	{
		if (event.getStateChange() == ItemEvent.SELECTED)
			try
			{
				this.refresh();
			} catch (TimeoutException e)
			{
				JOptionPane.showMessageDialog(new JFrame(),
						"You have probably shut down an interface critical for the protocol to spread information and work. Please do a 'no shut' on the interface that you shut down");
			} catch (IOException e)
			{
				e.printStackTrace();
			}
	}

	public int getSecondsTillRefresh()
	{
		return secondsTillRefresh;
	}

	public void setSecondsTillRefresh(int secondsTillRefresh)
	{
		this.secondsTillRefresh = secondsTillRefresh;
		this.secondsTillRefreshLabel.setText("Seconds till refresh: " + this.secondsTillRefresh);
	}

}
