package xorg.apache.commons.lang;
import static org.junit.Assert.*;

import org.junit.Test;
import junit.framework.TestCase;
import xorg.apache.commons.lang.ArrayUtils;
import xorg.apache.commons.lang.StringUtils;

public class ArrayUtils_subarray_PIT_ALL_AdeqTs  {

	@Test
	public void testPIT_ALL_1(){
		
	     Object[] objectArray = { "a", "b", "c"};
	     assertEquals( "ab", StringUtils.join(ArrayUtils.subarray(objectArray, -2, 2)));
	     
	}

	@Test
	public void testPIT_ALL_2(){
		
		assertEquals("", StringUtils.join(ArrayUtils.subarray(ArrayUtils.EMPTY_OBJECT_ARRAY, 1, 2)));    
	        
	}

	@Test
	public void testPIT_ALL_3(){
		
		 Object[] nullArray = null;
	     assertNull( ArrayUtils.subarray(nullArray, 0, 2));
	     
	}

	@Test
	public void testPIT_ALL_4(){
		
	     Object[] objectArray = {"a", "b", "c"};	     
	     assertEquals(ArrayUtils.subarray(objectArray, 1, 1).length, 0);
	     
	}
	
}
