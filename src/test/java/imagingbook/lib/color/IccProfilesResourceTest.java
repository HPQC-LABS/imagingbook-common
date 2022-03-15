package imagingbook.lib.color;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import imagingbook.lib.util.resource.ResourceLocation;

public class IccProfilesResourceTest {
	
	private ResourceLocation loc = new imagingbook.lib.color.iccProfiles.RLOC();

	@Test
	public void test() {
		assertNotNull(loc.getResource("AdobeRGB1998.icc"));
		assertEquals("wrong resource count", 7, loc.getResourceNames().length);
	}

}
