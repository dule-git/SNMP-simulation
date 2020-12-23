package snmp_simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.ireasoning.protocol.TimeoutException;
import com.ireasoning.protocol.snmp.SnmpPdu;
import com.ireasoning.protocol.snmp.SnmpSession;

public class RouterInterface extends JPanel
{
	private String id;
	private SnmpSession session;
	private String description;
	private int type;
	private int mtu;
	private long speed;
	private String physAddress;
	private int adminStatus;
	private int operStatus;

	private String[] oids;

	private JLabel descriptionLabel;
	private JLabel typeLabel;
	private JLabel mtuLabel;
	private JLabel speedLabel;
	private JLabel physAddressLabel;
	private JLabel adminStatusLabel;
	private JLabel operStatusLabel;
	private JPanel infoPanel;

	public static final Color LIGHT_GREEN = new Color(204, 255, 204);
	public static final Color LIGHT_RED = new Color(255, 153, 153);

	public RouterInterface(SnmpSession session, String id) throws IOException
	{
		this.session = session;
		this.id = id;

		this.initOids();

		this.updateParameters();

		this.initPanel();

	}

	public void initOids()
	{
		String ifDesc = ".1.3.6.1.2.1.2.2.1.2." + this.id;
		String ifType = ".1.3.6.1.2.1.2.2.1.3." + this.id;
		String ifMtu = ".1.3.6.1.2.1.2.2.1.4." + this.id;
		String ifSpeed = ".1.3.6.1.2.1.2.2.1.5." + this.id;
		String ifPhysAddress = ".1.3.6.1.2.1.2.2.1.6." + this.id;
		String ifAdminStatus = ".1.3.6.1.2.1.2.2.1.7." + this.id;
		String ifOperStatus = ".1.3.6.1.2.1.2.2.1.8." + this.id;

		this.oids = new String[] { ifDesc, ifType, ifMtu, ifSpeed, ifPhysAddress, ifAdminStatus, ifOperStatus
		};

	}

	public void initPanel()
	{
		this.setLayout(new BorderLayout());
		JLabel name = new JLabel("Interface " + this.id, SwingConstants.CENTER);
		name.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		this.add(name, BorderLayout.NORTH);
		this.setSize(400, 200);

		infoPanel = new JPanel(new GridLayout(7, 2));

		Font fontLabel = new Font(Font.DIALOG, Font.BOLD, 15);
		Font fontValue = new Font(Font.DIALOG, Font.PLAIN, 15);

		JLabel ifDescLabel = new JLabel("ifDesc:", SwingConstants.CENTER);
		ifDescLabel.setFont(fontLabel);
		this.infoPanel.add(ifDescLabel);
		this.descriptionLabel = new JLabel(this.description, SwingConstants.CENTER);
		this.descriptionLabel.setFont(fontValue);
		this.infoPanel.add(this.descriptionLabel);

		JLabel ifTypeLabel = new JLabel("ifType:", SwingConstants.CENTER);
		ifTypeLabel.setFont(fontLabel);
		this.infoPanel.add(ifTypeLabel);
		this.typeLabel = new JLabel(this.getTypeAsString(), SwingConstants.CENTER);
		this.typeLabel.setFont(fontValue);
		this.infoPanel.add(this.typeLabel);

		JLabel ifMtuLabel = new JLabel("ifMtu:", SwingConstants.CENTER);
		ifMtuLabel.setFont(fontLabel);
		this.infoPanel.add(ifMtuLabel);
		this.mtuLabel = new JLabel(this.mtu + "", SwingConstants.CENTER);
		this.mtuLabel.setFont(fontValue);
		this.infoPanel.add(this.mtuLabel);

		JLabel ifSpeedLabel = new JLabel("ifSpeed:", SwingConstants.CENTER);
		ifSpeedLabel.setFont(fontLabel);
		this.infoPanel.add(ifSpeedLabel);
		this.speedLabel = new JLabel(this.speed + "", SwingConstants.CENTER);
		this.speedLabel.setFont(fontValue);
		this.infoPanel.add(this.speedLabel);

		JLabel ifPhysAddressLabel = new JLabel("physAddress:", SwingConstants.CENTER);
		ifPhysAddressLabel.setFont(fontLabel);
		this.infoPanel.add(ifPhysAddressLabel);
		this.physAddressLabel = new JLabel(this.physAddress, SwingConstants.CENTER);
		this.physAddressLabel.setFont(fontValue);
		this.infoPanel.add(this.physAddressLabel);

		JLabel ifAdminStatusLabel = new JLabel("adminStatus:", SwingConstants.CENTER);
		ifAdminStatusLabel.setFont(fontLabel);
		this.infoPanel.add(ifAdminStatusLabel);
		this.adminStatusLabel = new JLabel(this.adminStatus == 1 ? "up" : "down", SwingConstants.CENTER);
		this.adminStatusLabel.setFont(fontValue);
		this.infoPanel.add(this.adminStatusLabel);

		JLabel ifOperStatusLabel = new JLabel("operStatus:", SwingConstants.CENTER);
		ifOperStatusLabel.setFont(fontLabel);
		this.infoPanel.add(ifOperStatusLabel);
		this.operStatusLabel = new JLabel(this.operStatus == 1 ? "up" : "down", SwingConstants.CENTER);
		this.operStatusLabel.setFont(fontValue);
		this.infoPanel.add(this.operStatusLabel);

		Color color = this.adminStatus == 1 ? LIGHT_GREEN : LIGHT_RED;
		this.setBackground(color);
		this.infoPanel.setBackground(color);

		this.add(this.infoPanel, BorderLayout.CENTER);

		this.setVisible(true);
	}

	public void updateParameters() throws IOException, TimeoutException
	{
		SnmpPdu pdu = this.session.snmpGetRequest(this.oids);
		this.parsePdu(pdu);
	}

	private void parsePdu(SnmpPdu pdu)
	{
		this.description = pdu.getVarBind(0).getValue().toString();
		this.type = Integer.parseInt(pdu.getVarBind(1).getValue().toString());
		this.mtu = Integer.parseInt(pdu.getVarBind(2).getValue().toString());
		this.speed = Long.parseLong(pdu.getVarBind(3).getValue().toString());
		this.physAddress = pdu.getVarBind(4).getValue().toString();
		this.physAddress = this.physAddress.replace("0x", "");
		this.physAddress = this.physAddress.replace(" ", "-");
		this.adminStatus = Integer.parseInt(pdu.getVarBind(5).getValue().toString());
		this.operStatus = Integer.parseInt(pdu.getVarBind(6).getValue().toString());
	}

	public void updateView()
	{
		if (this.adminStatus == 1)
		{
			this.setBackground(LIGHT_GREEN);
			this.infoPanel.setBackground(LIGHT_GREEN);
		} else
		{
			this.setBackground(LIGHT_RED);
			this.infoPanel.setBackground(LIGHT_RED);
		}

		this.descriptionLabel.setText(this.description);
		this.typeLabel.setText(this.getTypeAsString());
		this.mtuLabel.setText(this.mtu + "");
		this.speedLabel.setText(this.speed + "");
		this.physAddressLabel.setText(this.physAddress);
		this.adminStatusLabel.setText(this.adminStatus == 1 ? "up" : "down");
		this.operStatusLabel.setText(this.operStatus == 1 ? "up" : "down");
	}

	public void refresh() throws IOException, TimeoutException
	{
		this.updateParameters();
		this.updateView();
	}

	public String getTypeAsString()
	{
		switch (this.type)
		{
		case 1:
			return "other";
		case 6:
			return "ethernetCsmacd";
		case 22:
			return "propPointToPointSerial";
		case 24:
			return "softwareLoopback";
		default:
			return "";
		}
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getMtu()
	{
		return mtu;
	}

	public void setMtu(int mtu)
	{
		this.mtu = mtu;
	}

	public long getSpeed()
	{
		return speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public String getPhysAddress()
	{
		return physAddress;
	}

	public void setPhysAddress(String physAddress)
	{
		this.physAddress = physAddress;
	}

	public int getAdminStatus()
	{
		return adminStatus;
	}

	public void setAdminStatus(int adminStatus)
	{
		this.adminStatus = adminStatus;
	}

	public int getOperStatus()
	{
		return operStatus;
	}

	public void setOperStatus(int operStatus)
	{
		this.operStatus = operStatus;
	}

}
