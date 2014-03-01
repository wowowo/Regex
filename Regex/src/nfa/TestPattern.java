package nfa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestPattern {
	
	public static void main(String[] args) throws FileNotFoundException {
		

		test("b(a*)|(xe)zah","baaaaaaaatbaaaaaaaaabaaaxezah");        //true
		test("^ba?zah","bazah");        //true
		test("(xe)|(be)zah","xezah");        //true
		test("(xe)|(be)zah","bezah");        //true
		test("b(xe)?zah","bzah");        //true
		test("b(xe)?zah","abzaha");        //true
		test("a(xe*)?zah","axeeeeezah");        //true
		test("(xe)|(be)zah","aaaaxezah");        //true
		test("(xe)|(be)zah","xezah");        //true
		test("f((a*)|(bc))|cf", "fbcf");     //true
		test("f((a*)|(bc))cf", "faaaaaacf");     //true 
		test("f((a*)|(bc))|cf", "faaaaf");     //true
		test("(xe)|(be)zah","aaaaxebezah");        //true
		test("(bezah)|(xezah)", "xezah");         //true
		test("////", "asd//asd");         //true
		test("x(ee*)?zah", "xzah");         //true
		test("(a*)|(xe)zah", "zah");       //true
		test("^asd", "dasdd");          //false
		test("^asd", "asd");          // true

	}
	
	private static void test(String patt, String txt) {
		
		Pattern pat = new Pattern(patt);
		System.out.println(pat.match(txt));
		
	}

}
