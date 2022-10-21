package tr;

import tr.Triangle;

import static org.junit.Assert.*;
import org.junit.Test;

public class TriangleTest {
	
    //part one
//    @Test
//    public void testClassify1(){
//        Triangle trian = new Triangle();
//        int res = trian.classify(0, 0, 0);
//        int expRes = Triangle.INVALID;
//		
//        assertTrue(res == expRes);
//    }
	
    @Test
    public void testClassify2(){
        Triangle trian = new Triangle();
        int res = trian.classify(2, 2, 3);
        int expRes = Triangle.ISOSCELES;
		
        assertTrue(res == expRes);
    }
	
    @Test
    public void testClassify3(){
        Triangle trian = new Triangle();
        int res = trian.classify(2, 2, 2);
        int expRes = Triangle.EQUILATERAL;
		
        assertTrue(res == expRes);
    }
	
    @Test
    public void testClassify4(){
        Triangle trian = new Triangle();
        int res = trian.classify(3, 2, 1);
        int expRes = Triangle.SCALENE;
		
        assertTrue(res == expRes);
    }
    
    @Test
    public void testClassify5(){
        Triangle trian = new Triangle();
        
        int res = trian.classify(1, 2, 5);
        int expRes = Triangle.INVALID;
		
        assertTrue(res == expRes);
    }
    
    @Test
    public void testClassify6(){
        Triangle trian = new Triangle();
        int res = trian.classify(1, 2, 2);
        int expRes = Triangle.ISOSCELES;
		
        assertTrue(res == expRes);
    }
    
    @Test
    public void testClassify7(){
        Triangle trian = new Triangle();
        int res = trian.classify(1, 1, 2);
        int expRes = Triangle.INVALID;
		
        assertTrue(res == expRes);
    }
    
    @Test
    public void testClassify8(){
        Triangle trian = new Triangle();
        int res = trian.classify(5, 2, 5);
        int expRes = Triangle.ISOSCELES;
		
        assertTrue(res == expRes);
    }
	
	
    //part two
    @Test
    public void testClassify9(){
        Triangle trian = new Triangle();
        int res = trian.classify(1, 1, 0);
        int expRes = Triangle.INVALID;
		
        assertTrue(res == expRes);
    }
	
    @Test
    public void testClassify10(){
        Triangle trian = new Triangle();
        int res = trian.classify(1, 0, 1);
        int expRes = Triangle.INVALID;
		
        assertTrue(res == expRes);
    }
	
    @Test
    public void testClassify13(){
        Triangle trian = new Triangle();
        
        int res = trian.classify(1, 2, 1);
        int expRes = Triangle.INVALID;
		
        assertTrue(res == expRes);
    }
	
    @Test
    public void testClassify14(){
        Triangle trian = new Triangle();
        int res = trian.classify(2, 1, 1);
        int expRes = Triangle.INVALID;
		
        assertTrue(res == expRes);
    }
	
    //part tree
//    @Test
//    public void testClassify15(){
//        Triangle trian = new Triangle();
//        int res = trian.classify(0, 1, 0);
//        int expRes = Triangle.INVALID;
//		
//        assertTrue(res == expRes);
//    }
//	
    @Test
    public void testClassify16(){
        Triangle trian = new Triangle();
        
        int res = trian.classify(1, 2, 3);
        int expRes = Triangle.SCALENE;
		
        assertTrue(res == expRes);
    }
	
    @Test
    public void testClassify17(){
        Triangle trian = new Triangle();
        int res = trian.classify(2, 3, 1);
        int expRes = Triangle.SCALENE;
		
        assertTrue(res == expRes);
    }
	
    @Test
    public void testClassify18(){
        Triangle trian = new Triangle();
        int res = trian.classify(0, 1, 1);
        int expRes = Triangle.INVALID;
		
        assertTrue(res == expRes);
    }
	
//    @Test
//    public void testClassify19(){
//        Triangle trian = new Triangle();
//        
//        int res = trian.classify(-1, 1, 1);
//        int expRes = Triangle.INVALID;
//		
//        assertTrue(res == expRes);
//    }
	
    //part four
	
    @Test
    public void testClassify20(){
        Triangle trian = new Triangle();
        int res = trian.classify(7, 17, 9);
        int expRes = Triangle.INVALID;
		
        assertTrue(res == expRes);
    }
	
    @Test
    public void testClassify21(){
        Triangle trian = new Triangle();
        int res = trian.classify(17, 7, 9);
        int expRes = Triangle.INVALID;
		
        assertTrue(res == expRes);
    }
		
}
