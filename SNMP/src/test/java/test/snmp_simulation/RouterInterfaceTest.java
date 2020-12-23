package test.snmp_simulation;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.ireasoning.protocol.snmp.SnmpConst;
import com.ireasoning.protocol.snmp.SnmpSession;

import snmp_simulation.RouterInterface;

class RouterInterfaceTest
{

	private static final String READ_COMMUNITY = "si2019";
	private static final String WRITE_COMMUNITY = "si2019";
	private static final int VERSION = SnmpConst.SNMPV2;

	// id, hostname, port, desc, type, mtu, speed, physAddr, adminStatus, operStatus
	@ParameterizedTest
	@CsvSource({
			"1, 192.168.10.1, 161, FastEthernet1/0, 6, 1500, 100000000, C4-01-8B-94-00-10, 1, 1",
			"4, 192.168.10.1, 161, FastEthernet0/1, 6, 1500, 10000000, C4-01-8B-94-00-01, 1, 1",
			"6, 192.168.10.1, 161, Null0, 1, 1500, 4294967295, '', 1, 1",
			"7, 192.168.10.1, 161, Loopback0, 24, 1514, 4294967295, '', 1, 1",
			"4, 192.168.20.1, 161, FastEthernet4/0, 6, 1500, 100000000, C4-02-16-21-00-40, 2, 2",
			"6, 192.168.20.1, 161, Serial0/0, 22, 1500, 1544000, '', 2, 2",
			"17, 192.168.20.1, 161, Loopback0, 24, 1514, 4294967295, '', 1, 1",
			"1, 192.168.30.1, 161, FastEthernet1/0, 6, 1500, 100000000, C4-03-18-E7-00-10, 2, 2",
			"10, 192.168.30.1, 161, Serial0/3, 22, 1500, 1544000, '', 2, 2",
			"12, 192.168.30.1, 161, Null0, 1, 1500, 4294967295, '', 1, 1"
	})
	void testUpdateParameters(
			String id, String hostname, int port, 
			String description, int type, int mtu, long speed, String physAddress,
			int adminStatus, int operStatus) throws IOException
	{
		SnmpSession session = new SnmpSession(hostname,
				port, 
				READ_COMMUNITY, 
				WRITE_COMMUNITY, VERSION);

		RouterInterface inf = new RouterInterface(session, id);

		assertAll("inf.updateParameters()",
				() -> assertTrue(inf.getDescription().equals(description)),
				() -> assertTrue(inf.getType() == type), () -> assertTrue(inf.getMtu() == mtu),
				() -> assertTrue(inf.getSpeed() == speed),
				() -> assertTrue(inf.getPhysAddress().equals(physAddress)),
				() -> assertTrue(inf.getAdminStatus() == adminStatus),
				() -> assertTrue(inf.getOperStatus() == operStatus)
		);

		session.close();

	}

	@ParameterizedTest
	@CsvSource({
			"1, 192.168.10.1, 161, up",
			"2, 192.168.10.1, 161, down",
			"6, 192.168.20.1, 161, down",
			"17, 192.168.20.1, 161, up",
			"4, 192.168.30.1, 161, down",
			"12, 192.168.30.1, 161, up",
	})
	void testUpdateView(String id, String hostname, int port, String status) throws IOException
	{
		SnmpSession session = new SnmpSession(hostname, port,
				READ_COMMUNITY, 
				WRITE_COMMUNITY, 
				VERSION);

		RouterInterface inf = new RouterInterface(session, id);

		inf.updateView();

		switch (status)
		{
		case "up":
			assertTrue(inf.getBackground().equals(RouterInterface.LIGHT_GREEN));
			break;
		case "down":
			assertTrue(inf.getBackground().equals(RouterInterface.LIGHT_RED));
			break;
		}

	}

}





