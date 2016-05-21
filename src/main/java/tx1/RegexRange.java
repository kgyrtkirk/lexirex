package tx1;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegexRange {

	private String labelS;
	private String labelE;
	private String commonP;
	private List<String> exprs;

	/**
	 * @param s inclusive
	 * @param e exclusive
	 */
	public RegexRange(String labelS, String labelE) {
		exprs = new ArrayList<>();
		this.labelS = labelS;
		this.labelE = labelE;
		
		commonP=commonPrefix(labelS,labelE);
		char u = labelE.charAt(0);
		System.out.println((int)u);
		System.out.println(clearPrint(u));
		
		
		int	cL=commonP.length();
		
		// special case...S=E
		if(cL == labelE.length() && cL == labelS.length()){
			exprs.add("");
			return;
		}

		
		// add all prefixed with labelS
		
		
		if(labelS.length()>commonP.length()){
			exprs.add(Pattern.quote(labelS.substring(cL))+".+");
		}
		for(int i=labelS.length()-1;i>commonP.length();i--){
			exprs.add(Pattern.quote(labelS.substring(cL,i))+"["+succ(labelS.charAt(i))+"-\\xff].*");
		}
		{
		int i=commonP.length();
		// FIXME |commonP| == |labelS| || ==|labelE|
			if(commonP.length()==labelS.length() || commonP.length()==labelE.length()){
				if(commonP.length()==labelS.length()){
					exprs.add("[\\x00-"+pred(labelE.charAt(i))+"].*");
				}else{
					throw new RuntimeException("impossible labelE<labelS");
				}
			}else{
				if(labelS.charAt(i)+1 < labelE.charAt(i)){
					exprs.add("["+succ(labelS.charAt(i))+"-"+pred(labelE.charAt(i))+"].*");
				}
			}
		}
		for(int i=commonP.length()+1;i<labelE.length();i++){
			exprs.add(Pattern.quote(labelE.substring(cL,i))+"[\\x00-"+pred(labelE.charAt(i))+"].*");
		}
		exprs.add(Pattern.quote(labelE));
//		char[] last = labelS.toCharArray();
//		char[] target = labelE.toCharArray();
		
		
//		labelS.toCharArray();
		
	}
	
	private String succ(char c) {
		return excapeIfNeeded(succ1(c));
	}

	private char succ1(char c) {
		if(c=='\377'){
			return '\377';
		}
		return (char) (c+1);
	}
	private String pred(char c) {
		return excapeIfNeeded(pred1(c));
	}

	private String excapeIfNeeded(char pred1) {
		if(clearPrint(pred1)){
			return ""+pred1;
		}
		else{
			return String.format("\\x%02x", (int)pred1);
		}
	}

	private char pred1(char c) {
		if(c=='\0'){
			return '\0';
		}
		return (char) (c-1);
	}

	private boolean clearPrint(char u) {
		return ('a'<=u && u <='z') ||
				('A'<=u && u <='Z') || 
				('0'<=u && u <='9');
	}

	@Override
	public String toString() {
		return String.format("[%s..%s[  p: %s", labelS,labelE,commonP);
	}

	private String commonPrefix(String a, String b) {
		int	l=Math.min(a.length(),b.length());
		for(int i=0;i<l;i++){
			if(a.charAt(i)!=b.charAt(i)){
				return a.substring(0,i);
			}
		}
		return a.substring(0,l);
	}

	public List<String> getExprs() {
		return exprs;
	}

public	String getRegex(){
		StringBuffer sb = new StringBuffer();
		sb.append(Pattern.quote(commonP));
		sb.append('(');
		for (int i = 0; i < exprs.size(); i++) {
			if(i>0)sb.append('|');
			sb.append(exprs.get(i));
		}
		sb.append(')');
		return sb.toString();
	}
}
