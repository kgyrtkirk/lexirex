package hu.rxd.lexirex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.rxd.lexirex.TestResults.Suite;
import hu.rxd.lexirex.TestResults.Suite.Case;

//

class Asd1 {

	static class Entry {

		public static final Comparator<? super Entry> LABEL_COMPARATOR = new Comparator<Entry>() {

			@Override
			public int compare(Entry o1, Entry o2) {
				return o1.label.compareTo(o2.label);
			}
		};
		private String label;
		private double duration;
		private String className;
		private String methodName;

		public Entry(String className, String methodName, double duration) {
			this.duration = duration;
			this.methodName = methodName;
			this.className=className+".class";
//			label = className.replaceAll(".*\\.", "") + "#" + methodName;
			label=className+"#"+methodName;
		}
		
		@Override
		public String toString() {
			return String.format("%s	%d", label,duration);
		}

	}

	public static void main(String[] args) throws Exception {
		System.out.println(" * grabbing results...");
		InputStream jsonStream = getLocalResults();
		TestResults results = parseTestResults(jsonStream);

		List<Entry> entries = new ArrayList<Entry>();

		for (Suite s : results.suites) {
			for (Case c : s.cases) {
				entries.add(new Entry(c.className, c.name, c.duration));
			}
		}
		System.out.println("#cases: " +entries.size());
		Collections.sort(entries,Entry.LABEL_COMPARATOR);
		double	sum=0.0;
		for (Entry entry : entries) {
			sum+=entry.duration;
		}
		System.out.println("#time: " + sum);
		int	N=100;
		double	d=sum/N;
		double	r=d;
		Entry	lastEntry=null;
		List<RegexRange>	ranges=new ArrayList<>();
		for (Entry entry : entries) {
			r-=entry.duration;
			if(r<0.0){
//				System.out.println(entry.label);
				ranges.add(buildRange(lastEntry,entry));
				lastEntry=entry;
				r+=d;
			}
		}
		ranges.add(buildRange(lastEntry,null));
		
		
		for (int i = 0; i < ranges.size(); i++) {
			RegexRange regexRange = ranges.get(i);
			System.out.println(regexRange);
			Pattern.compile(regexRange.toRegex());
			System.out.println(regexRange.toRegex());
			FileWriter fw = new FileWriter("/tmp/_lx0");
			fw.append(regexRange.toRegex());
			fw.close();
			if(i==1)
			break;
		}
		
	}

	static class Range1{

		public void addClassRange(RegexRange regexRange) {
			// TODO Auto-generated method stub
			
		}

		public void addExcludeRange(RegexRange regexRange) {
			// TODO Auto-generated method stub
			
		}
	}
	/**
	 * returns a regexrange for ]eL:eR]
	 * 
	 * @param eL
	 * @param eR
	 * @return
	 */
	private static RegexRange buildRange(Entry eL, Entry eR) {
		String	c0=eL!=null?eL.className:"A";
		String	c1=eR!=null?eR.className:"z";
		Range1	r1=new Range1();
		r1.addClassRange(RegexRangeBuilder.fromInclusive(c0).toInclusive(c1));
		if(eL!=null){
			r1.addExcludeRange(RegexRangeBuilder.fromInclusive(c0+"#").toExclusive(c0+"#"+eL.methodName));
		}
		if(eR!=null){
			r1.addExcludeRange(RegexRangeBuilder.fromExclusive(c1+"#"+eR.methodName).toInclusive(c1+"#\3ff"));
		}
		return RegexRangeBuilder.fromInclusive(c0).toInclusive(c1);
	}

	private static InputStream getLocalResults() throws Exception {
		return new FileInputStream(new File("/home/kirk/Downloads/kx2_results.json"));
	}

	private static InputStream getJenkinsResults() throws Exception {
		URL u = new URL(
				"http://j1:8080/job/tmp_kx_2/lastCompletedBuild/testReport/api/json?pretty=true&tree=suites[cases[className,name,duration]]");
		InputStream jsonStream = u.openStream();
		return jsonStream;
	}

	private static TestResults parseTestResults(InputStream jsonStream)
			throws IOException, JsonParseException, JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		TestResults results = mapper.readValue(jsonStream, TestResults.class);
		return results;
	}
}
