package snmp_simulation;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.ireasoning.protocol.TimeoutException;
import com.ireasoning.protocol.snmp.SnmpConst;
import com.ireasoning.protocol.snmp.SnmpSession;

public class Router extends JPanel
{

	private SnmpSession session;
	private List<RouterInterface> interfaces;

	private static final String READ_COMMUNITY = "si2019";
	private static final String WRITE_COMMUNITY = "si2019";
	private static final int VERSION = SnmpConst.SNMPV2;

	public Router(String hostname, int port, String[] interfaceIDs) throws IOException
	{
		this.session = new SnmpSession(hostname, port, READ_COMMUNITY, WRITE_COMMUNITY, VERSION);

		this.session.setTimeout(2000);

		this.initInterfaces(interfaceIDs);

		this.initPanel();
	}

	public void initPanel()
	{
		int numCols = 4;
		int numRows = this.interfaces.size() % 4 == 0 ? this.interfaces.size() / 4 : this.interfaces.size() / 4 + 1;

		this.setLayout(new GridLayout(numRows, numCols));
		this.setSize(numCols * 400, numRows * 200);

		this.interfaces.forEach(inf -> {
			this.add(inf);
		});

		this.setVisible(true);
	}

	public void initInterfaces(String[] interfaceIDs) throws IOException
	{
		this.interfaces = new ArrayList<RouterInterface>();

		for (String id : interfaceIDs)
		{
			RouterInterface inf = new RouterInterface(this.session, id);
			this.interfaces.add(inf);
		}

		if (this.interfaces.size() == 0)
			this.interfaces = null;
	}

	public void refresh() throws TimeoutException, IOException
	{
		for (RouterInterface inf : this.interfaces)
			inf.refresh();
	}

	public boolean hasInterfaces()
	{
		return this.interfaces != null;
	}

	public RouterInterface getInterface(String id)
	{
		for (RouterInterface inf : this.interfaces)
			if (inf.getId().contentEquals(id))
				return inf;
		return null;
	}

	public int getNumOfInterfaces()
	{
		return this.interfaces.size();
	}

}
