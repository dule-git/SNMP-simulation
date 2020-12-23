package test.snmp_simulation;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import javax.swing.JFrame;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import snmp_simulation.Router;
import snmp_simulation.RouterInterface;

class RouterTest
{

	@ParameterizedTest
	@CsvSource({
			"192.168.10.1, 161, '1,2,3,4,6,7'",
			"192.168.20.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17'",
			"192.168.30.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17'"
	})
	void RouterConstructorTest(String hostname, int port, String interfaceIDsString) throws IOException
	{
		String[] interfaceIDs = interfaceIDsString.split(",");
		Router r = new Router(hostname, port, interfaceIDs);
		assertTrue(r.hasInterfaces());
	}

	@ParameterizedTest
	@CsvSource({
			"192.168.10.1, 161, '1,2,3,4,6,7', 1, FastEthernet1/0, 6, 1500, 100000000, C4-01-8B-94-00-10, 1, 1",
			"192.168.10.1, 161, '1,2,3,4,6,7', 6, Null0, 1, 1500, 4294967295, '', 1, 1",
			"192.168.20.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17', 6, Serial0/0, 22, 1500, 1544000, '', 2, 2",
			"192.168.20.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17', 17, Loopback0, 24, 1514, 4294967295, '', 1, 1",
			"192.168.30.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17', 1, FastEthernet1/0, 6, 1500, 100000000, C4-03-18-E7-00-10, 2, 2",
			"192.168.30.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17', 12, Null0, 1, 1500, 4294967295, '', 1, 1",
	})
	void initStateTest(
			String hostname, int port, String interfaceIDsString, String interfaceBeingTested, String description,
			int type, int mtu, long speed, String physAddress, int adminStatus, int operStatus
	) throws IOException
	{
		String[] interfaceIDs = interfaceIDsString.split(",");
		Router r = new Router(hostname, port, interfaceIDs);

		RouterInterface inf = r.getInterface(interfaceBeingTested);

		assertAll("inf.updateParameters()", () -> assertTrue(inf.getDescription().equals(description)),
				() -> assertTrue(inf.getType() == type), () -> assertTrue(inf.getMtu() == mtu),
				() -> assertTrue(inf.getSpeed() == speed), () -> assertTrue(inf.getPhysAddress().equals(physAddress)),
				() -> assertTrue(inf.getAdminStatus() == adminStatus),
				() -> assertTrue(inf.getOperStatus() == operStatus));
	}

	@ParameterizedTest
	@CsvSource({
			"192.168.10.1, 161, '1,2,3,4,6,7', 1, FastEthernet1/0, 6, 1500, 100000000, C4-01-8B-94-00-10, 1, 1",
			"192.168.10.1, 161, '1,2,3,4,6,7', 6, Null0, 1, 1500, 4294967295, '', 1, 1",
			"192.168.20.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17', 6, Serial0/0, 22, 1500, 1544000, '', 2, 2",
			"192.168.20.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17', 17, Loopback0, 24, 1514, 4294967295, '', 1, 1",
			"192.168.30.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17', 1, FastEthernet1/0, 6, 1500, 100000000, C4-03-18-E7-00-10, 2, 2",
			"192.168.30.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17', 12, Null0, 1, 1500, 4294967295, '', 1, 1",
	})
	void refreshTest(
			String hostname, int port, String interfaceIDsString, String interfaceBeingTested, String description, int type, int mtu, long speed,
			String physAddress, int adminStatus, int operStatus
	) throws IOException
	{
		String[] interfaceIDs = interfaceIDsString.split(",");
		Router r = new Router(hostname, port, interfaceIDs);
		
		r.refresh();

		RouterInterface inf = r.getInterface(interfaceBeingTested);
		
		assertAll("inf.updateParameters()",
				() -> assertTrue(inf.getDescription().equals(description)),
				() -> assertTrue(inf.getType() == type), () -> assertTrue(inf.getMtu() == mtu),
				() -> assertTrue(inf.getSpeed() == speed),
				() -> assertTrue(inf.getPhysAddress().equals(physAddress)),
				() -> assertTrue(inf.getAdminStatus() == adminStatus),
				() -> assertTrue(inf.getOperStatus() == operStatus)
		);
	}

	@ParameterizedTest
	@CsvSource({
			"192.168.10.1, 161, '1,2,3,4,6,7'",
			"192.168.20.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17'",
			"192.168.30.1, 161, '1,2,3,4,5,6,7,8,9,10,12,17'"
	})
	void viewTest(String hostname, int port, String interfaceIDsString) throws IOException
	{
		String[] interfaceIDs = interfaceIDsString.split(",");
		Router r = new Router(hostname, port, interfaceIDs);

		int numCols = 4;
		int numRows = r.getNumOfInterfaces() % 4 == 0 ? r.getNumOfInterfaces() / 4 : r.getNumOfInterfaces() / 4 + 1;

		JFrame frame = new JFrame();
		frame.add(r);
		frame.setSize(numCols * 400, numRows * 200);
		// frame.setVisible(true);
	}

}
