package recursive;

/**
 * 
 * A recursive implementation of a regular expressions engine;
 * Returns true if the regular expression is anywhere in the text;
 * supported metachars :
 * <p>
 *  - ^ - check if the text begins with the regex </br>
 *  - $ - check if the text ends with the regex </br>
 *  - * - check for zero or more of the preceding char </br>
 *  - /d - check for digits (char class) </br>
 *  - /l - check for lowercase letters  (char class) </br>
 *  - ? - can skip match </br>
 *  - . - match anything </br>
 *  - / escape metachars, or start char class </p>
 **/
public class Machine {

	private String txt;
	private char[]  regex;
	
	public boolean match(String regexp, String txt) {
		
		if(regexp.length() < 1)
			if(txt.length() < 1)
				return true;
			else if (txt.length() > 0)
				return false;
	
		this.regex = regexp.toCharArray();
		this.txt = txt;
		int i = 0;
		
		// if regex starts with '^'
		// match only in the beginning
		if(regex[0] == '^')
			return matchhere(1,0);
		
		
		// while there are characters left in the text
		// try to match the string from the beginning
		do {
			
			if(matchhere(0,i))
				return true;
				
		} while(i++ < txt.length()); 
		
		return false;
	}

	private boolean matchhere(int regexC, int textC) {
		
		// if question mark skip it
		if(regex.length - 1 > regexC && regex[regexC] == '?')
			regexC++;
		
		//if all of the characters in the regex have been 
		// matched return true
		if(regex.length <= regexC)
			return true;
		
		// match the next char of the regex as escaped
		if(regex[regexC] == '/')
			return escaped(regexC+1, textC);
		
		// match for ending
		if(regex[regexC] == '$' && (regexC == (regex.length - 1))) 
			return textC == txt.length();
		
		// match star
		if(regexC < regex.length - 2 && regex[regexC + 1] == '*')
			return matchstar(regexC, regexC + 2, textC);
		
		//if next char is "?" skip try to skip the current char
		if(regexC < regex.length - 2 && regex[regexC + 1] == '?')
			if( matchhere(regexC + 2, textC))
				return true;
		
		// if still within the text and the regex and text match, or the regex char is '.' advance
		if(textC < txt.length() && (regex[regexC] == txt.charAt(textC) || regex[regexC] == '.'))
			return matchhere(regexC + 1,textC + 1);
		
		return false;
	}
	
	/**
	 * match the escaped char to the current textC
	 * @param regexC
	 * @param textC
	 * @return
	 */
	private boolean escaped(int regexC, int textC) {

		char c = 0;
		
		
		if(textC < txt.length())
			 c = txt.charAt(textC);
		
		else return false;
		
		// check char if character is digit
		if(regex[regexC] == 'd') 
			if(regexC + 1 < regex.length && regex[regexC + 1] == '*')
				return matchEscapedStar(regex[regexC],regexC + 2, textC);
			else if (c > 47 && c < 58)
				return matchhere(regexC + 1, textC + 1);
			else return false;
		
		//check if character is lowercase letter
		if(regex[regexC] == 'l') 
			if(regexC + 1 < regex.length && regex[regexC + 1] == '*')
				return matchEscapedStar(regex[regexC],regexC + 2, textC);
			else if (c > 96 && c < 123)
				return matchhere(regexC + 1, textC + 1);
			else return false;
		
		//check if character is any other metachar
		if(regex[regexC] == c)
			return matchhere(regexC + 1,textC + 1);
		
		return false;
	}

	/**
	 * method that acts like the normal matchStart
	 * but works with character classes
	 * @param c - class defining character (d or l)
	 * @param i - position in regex
	 * @param textC - position in text
	 * @return
	 */
	private boolean matchEscapedStar(char c, int i, int textC) {
		
		do {
			if(matchhere(i, textC)) 
				return true;
			
		} while(textC < txt.length() && (inRange(txt.charAt(textC++), c)));
		
		return false;
		
	}

	/**
	 * 
	 * @param t - character to be matched
	 * @param c - class defining character (d or l)
	 * @return
	 */
	private boolean inRange(char t, char c) {
		
		if(c == 'l')
			if(t > 96 && t < 123)
				return true;
		
		if (c == 'd')
			if(t > 47 && t < 58)
				return true;
		
		return false;
	}

/**
 * 
 * @param regexC - position of the char before the star
 * @param i - the char after the star
 * @param textC - the text char
 * @return
 */
	private boolean matchstar(int regexC, int i, int textC) {
		
		char c = regex[regexC];
		
		// while the text characters match the char before the star
		do {
			if(matchhere(i, textC)) 				
				return true;
			
		} while(textC < txt.length() && (txt.charAt(textC++) == c || c=='.'));
		
		return false;
	}

	/***
	 * 
	 *   Tests (all should be true)
	 * 
	 **/
	public static void main(String[] args) {
		
		Machine machine = new Machine();
		
		System.out.println(machine.match("a?b", "ab"));
		System.out.println(machine.match("a?b", "b"));
		System.out.println(!machine.match("a?b", "a"));
		System.out.println(!machine.match("ab?a", "a")); 
		
		System.out.println(machine.match("a*b", "aab"));
		System.out.println(machine.match("a*b", "b"));
		System.out.println(!machine.match("a*b", "a"));
		System.out.println(!machine.match("a/*", "aa"));

		System.out.println(machine.match("asd", "dsaasd"));
		System.out.println(!machine.match("^asd", "dsaasd"));
		
		System.out.println(machine.match("asd$", "dsaasd"));
		System.out.println(!machine.match("^asd$", "dsaasd"));
		System.out.println(machine.match("^asd$", "asd"));
		
		System.out.println(machine.match("/d/d*/./d/d*f", "14562.5456f"));
		System.out.println(!machine.match("/d/d*/./d/d*f", "14562.5a456f"));
		System.out.println(machine.match("/l", "a"));

	}

}
