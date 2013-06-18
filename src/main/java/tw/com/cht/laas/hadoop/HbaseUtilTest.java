package tw.com.cht.laas.hadoop;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tw.com.cht.laas.entity.Syslog;

public class HbaseUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLog() throws IOException {
		String [] hosts = {"0a90818c"};
		long dtStart = 20130529000000L;
		long dtEnd = 20130529235959L;
		List<Syslog> syslogs = HbaseUtil.getLog(hosts, dtStart, dtEnd);
		for(Syslog syslog:syslogs) {
			System.out.println(syslog);
		}
		assertNotNull(syslogs);
	}
}
