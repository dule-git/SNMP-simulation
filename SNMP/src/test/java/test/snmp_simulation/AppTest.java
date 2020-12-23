package test.snmp_simulation;

import java.io.IOException;

import snmp_simulation.App;
import snmp_simulation.Router;

class AppTest
{

	public static void main(String[] argv) throws IOException
	{
		Router r1 = new Router("192.168.10.1", 161, new String[]{"1", "2", "3", "4", "6", "7"});
		Router r2 = new Router("192.168.20.1", 161,
				new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "12", "17"
				});
		Router r3 = new Router("192.168.30.1", 161,
				new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "12", "17"
				});
		App app = new App();
		app.addRouter(r1);
		app.addRouter(r2);
		app.addRouter(r3);
		app.start();
	}

}
