package hu.rxd.lexirex;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RegexRangeIntegrationTest {

	private String s;
	private String e;
	private List<String> wordList;
	private int offS;
	private int offE;

	@Parameters(name="{0}..{1}")
	public static Iterable<Object[]> getParameters() {
		List<String> wordList = generateLex(new char[] { 'a', 'b', 'c' }, 4);
		ArrayList<Object[]> cases = new ArrayList<>();
		for(int i=0;i<wordList.size();i++){
			for(int j=i;j<wordList.size();j++){
				cases.add(new Object[]{ wordList.get(i),wordList.get(j),wordList,i,j });
			}
		}

		return cases;
	}

	private static List<String> generateLex(char[] characters, int maxLength) {
		List<String> res = new ArrayList<>();
		int[] off = new int[maxLength];
		char[] c = new char[maxLength];
		int o = 0;
		while (o >= 0) {
			if (off[o] == 0) {
				res.add(new String(c, 0, o));
			}
			off[o]++;
			if (off[o] == characters.length + 1) {
				off[o] = 0;
				o--;
			} else {
				c[o] = characters[off[o] - 1];
				o++;
				if (o == maxLength) {
					res.add(new String(c, 0, o));
					o--;
				}
			}
		}
		return res;
	}

	public RegexRangeIntegrationTest(String s,String e,List<String>wordList,int i,int j) {
		this.s = s;
		this.e = e;
		this.wordList = wordList;
		this.offS = i;
		this.offE = j;
	}
	
	@Test
	public void fullII() {
		Pattern p=RegexRangeBuilder.fromInclusive(s).toInclusive(e).toPattern();
		for(int i=0;i<wordList.size();i++){
			boolean	match=p.matcher(wordList.get(i)).matches();
			if(offS<=i && i<=offE){
				assertTrue(wordList.get(i),match);
			}else{
				assertFalse(match);
			}
		}
	}
	
	@Test
	public void testExclusion(){
		if (offS != offE) {
			Pattern p = RegexRangeBuilder.fromExclusive(s).toExclusive(e).toPattern();
			assertFalse(p.matcher(wordList.get(offS)).matches());
			assertFalse(p.matcher(wordList.get(offE)).matches());
		}
	}
	

}
