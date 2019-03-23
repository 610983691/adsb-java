import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.coulee.aicw.StartManagerMain;
import com.coulee.aicw.service.ISMSService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=StartManagerMain.class)
public class SMSServiceTest {
	@Autowired
	private ISMSService smsService;

	@Test
	public void applyReceptionSMSTest() {
		String telphone = "17716124112";
		String msg = "321 1.2.3.4 1";
		String smsnumber = "0015";
		smsService.applyReceptionSMS(telphone,msg,smsnumber);
		System.out.println("applyReceptionSMSTest--" );
	}
}
