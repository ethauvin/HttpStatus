package net.thauvin.erik.httpstatus;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

/**
 * The <code>ReasonsTest</code> class.
 *
 * @author <a href="mailto:erik@thauvin.net">Erik C. Thauvin</a>
 * @created 2015-12-03
 * @since 1.0
 */
public class ReasonsTest
{
	@DataProvider(name = "reasons")
	public Object[][] reasons()
	{
		final ResourceBundle bundle = ResourceBundle.getBundle("net.thauvin.erik.httpstatus.reasons");
		final Object[][] reasons = new String[bundle.keySet().size()][2];
		int i = 0;
		for (final String key : bundle.keySet())
		{
			reasons[i][0] = key;
			reasons[i][1] = bundle.getString(key);
			i++;
		}
		return reasons;
	}

	@Test(dataProvider = "reasons")
	public void testGetReasonPhrase(String code, String reason)
			throws Exception
	{
		Assert.assertEquals(reason, Reasons.getReasonPhrase(code));
	}
}