package ravensproject;

import java.util.Collections;
// Uncomment these lines to access image processing.
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Set;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
	String currprob;
	HashMap<String, Integer> sizes;
	static final int CTR_MATCH_TOL = 400;
	static final int OTR_MATCH_TOL = 400;
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
    public Agent() {
    	sizes = new HashMap<String, Integer>();
        sizes.put("very small", 1);
        sizes.put("small", 2);
        sizes.put("medium", 3);
        sizes.put("large", 4);
        sizes.put("very large", 5);
        sizes.put("huge", 6);
    }
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return a String representing its
     * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName().
     * 
     * In addition to returning your answer at the end of the method, your Agent
     * may also call problem.checkAnswer(String givenAnswer). The parameter
     * passed to checkAnswer should be your Agent's current guess for the
     * problem; checkAnswer will return the correct answer to the problem. This
     * allows your Agent to check its answer. Note, however, that after your
     * agent has called checkAnswer, it will *not* be able to change its answer.
     * checkAnswer is used to allow your Agent to learn from its incorrect
     * answers; however, your Agent cannot change the answer to a question it
     * has already answered.
     * 
     * If your Agent calls checkAnswer during execution of Solve, the answer it
     * returns will be ignored; otherwise, the answer returned at the end of
     * Solve will be taken as your Agent's answer to this problem.
     * 
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public int Solve(RavensProblem problem) {
    	currprob = problem.getName();
    	if (!problem.hasVerbal()) {
    		return VisualSolve(problem);
    	}
    	HashMap<String,RavensFigure> figures = problem.getFigures();
    	if (problem.getProblemType().equals("2x2"))
    	{
    		RavensFigure A = figures.get("A");
    		RavensFigure B = figures.get("B");
    		RavensFigure C = figures.get("C");
    		
    		CustomRavensFigure PostB = MatchObjects(A, B);
    		CustomRavensFigure PostC = MatchObjects(A, C);
    		
    		CustomRavensFigure AtoB = GetTransformation(A, PostB);
    		CustomRavensFigure AtoC = GetTransformation(A, PostC);
    		
    		CustomRavensFigure BtoD = ApplyTransformation(PostB, AtoC);
    		CustomRavensFigure CtoD = ApplyTransformation(PostC, AtoB);
    		
    		
    		int BtoDsuggests = -1;
    		int CtoDsuggests = -2;
    		int BtoDlowest = 999;
    		int CtoDlowest = 999;
    		for(int i = 1; i < 7; i++) {
    			int[] BtoDdiff = CompareResults(BtoD, figures.get(String.valueOf(i)));
    			if (BtoDdiff[0] < BtoDlowest) {
    				BtoDlowest = BtoDdiff[0];
    				BtoDsuggests = i;
    			}
    			int[] CtoDdiff = CompareResults(CtoD, figures.get(String.valueOf(i)));
    			if (CtoDdiff[0] < CtoDlowest) {
    				CtoDlowest = CtoDdiff[0];
    				CtoDsuggests = i;
    			}
    		}
    		
    		if(BtoDsuggests == CtoDsuggests) {
    			return BtoDsuggests;
    		}
    		
    		else if (BtoDlowest < CtoDlowest) {
    			return BtoDsuggests;
    		}
    		
    		else {
    			return CtoDsuggests;
    		}
    		
    	}

    	if (problem.getProblemType().equals("3x3"))
    	{
    		HashMap<Integer, Integer> results = new HashMap<Integer, Integer>();
    		RavensFigure A = figures.get("A");
    		RavensFigure B = figures.get("B");
    		RavensFigure C = figures.get("C");
    		RavensFigure D = figures.get("D");
    		RavensFigure E = figures.get("E");
    		RavensFigure F = figures.get("F");
    		RavensFigure G = figures.get("G");
    		RavensFigure H = figures.get("H");

    		CustomRavensFigure PostB = MatchObjects(A, B);
    		CustomRavensFigure PostC = MatchObjects(PostB, C);
    		CustomRavensFigure PostD = MatchObjects(A, D);
    		CustomRavensFigure PostE = MatchObjects(PostB, E);
    		CustomRavensFigure PostF = MatchObjects(PostC, F);
    		CustomRavensFigure PostG = MatchObjects(PostD, G);
    		CustomRavensFigure PostH = MatchObjects(PostG, H);
    		

    		//count heuristic
    		int iscount = ((B.getObjects().size() - A.getObjects().size()) - (D.getObjects().size() - A.getObjects().size())) + ((G.getObjects().size() - D.getObjects().size()) - (C.getObjects().size() - B.getObjects().size())) + ((H.getObjects().size() - G.getObjects().size()) - (F.getObjects().size() - C.getObjects().size())) + ((E.getObjects().size() - D.getObjects().size()) - (E.getObjects().size() - B.getObjects().size()));
    		int countdiff = 999;
    		int countsuggests = -1;
    		if (iscount == 0 && (G.getObjects().size() - H.getObjects().size()) != 0) {
    			int totalsize = (H.getObjects().size() - G.getObjects().size()) + H.getObjects().size();
    			for(int i = 1; i < 9; i++) {
    				RavensFigure candidate = figures.get(String.valueOf(i));
    				if(candidate.getObjects().size() == totalsize) {
    					int[] diff = CompareResults(new CustomRavensFigure(G), candidate);
    					if(diff[0] < countdiff) {
    						countdiff = diff[0];
    						countsuggests = i;
    					}
    				}
    			}
    			results.put(countdiff, countsuggests);
    		}

    		//basic transform heuristic
    		CustomRavensFigure EtoF = GetTransformation(PostE, PostF);
    		CustomRavensFigure EtoH = GetTransformation(PostE, PostH);
    		
    		CustomRavensFigure FtoI = ApplyTransformation(PostF, EtoH);
    		CustomRavensFigure HtoI = ApplyTransformation(PostH, EtoF);

    		int FtoIsuggests = -1;
    		int HtoIsuggests = -2;
    		int FtoIlowest = 999;
    		int HtoIlowest = 999;
    		
    		for(int i = 1; i < 9; i++) {
    			int[] FtoIdiff = CompareResults(FtoI, figures.get(String.valueOf(i)));
    			FtoIdiff[0] += Math.abs(FtoI.getObjects().size() - figures.get(String.valueOf(i)).getObjects().size()) * 10;
    			if (FtoIdiff[0] < FtoIlowest) {
    				FtoIlowest = FtoIdiff[0];
    				FtoIsuggests = i;
    			}
    			int[] HtoIdiff = CompareResults(HtoI, figures.get(String.valueOf(i)));
    			HtoIdiff[0] += Math.abs(HtoI.getObjects().size() - figures.get(String.valueOf(i)).getObjects().size()) * 10;
    			if (HtoIdiff[0] < HtoIlowest) {
    				HtoIlowest = HtoIdiff[0];
    				HtoIsuggests = i;
    			}
    		}
    		
    		results.put(HtoIlowest, HtoIsuggests);
    		results.put(FtoIlowest, FtoIsuggests);
    		
    		if(countdiff < 999)
    			return countsuggests;
    		//System.out.println("Problem " + currprob + " count diff: " + countdiff + ": " + countsuggests + "\nHtoIdiff: " + HtoIlowest + ": " + HtoIsuggests + "\nFtoIdiff: " + FtoIlowest + ": " + FtoIsuggests + "\n suggest: " + results.get(Collections.min(results.keySet())));
    		
    		if(FtoIsuggests == HtoIsuggests) {
    			return FtoIsuggests;
    		}
    		
    		else if (FtoIlowest < HtoIlowest) {
    			return FtoIsuggests;
    		}
    		
    		else {
    			return HtoIsuggests;
    		}
    		
    	}


        return -1;
    }
    
    @SuppressWarnings("unchecked")
	private int[] CompareResults(CustomRavensFigure D, RavensFigure candidate) {
    	int[] result = new int[2];
    	HashMap<String, CustomRavensObject> DObjs = (HashMap<String, CustomRavensObject>)D.getObjects().clone();
    	HashMap<String, RavensObject> candidateObjs = (HashMap<String, RavensObject>)candidate.getObjects().clone();
    	
    	for(String objectName : D.getObjects().keySet()) {
    		CustomRavensObject thisObject = D.getObjects().get(objectName);
    		for(String cobjectName : candidate.getObjects().keySet()) {
    			RavensObject cObject = candidate.getObjects().get(cobjectName);
    			if(ObjDifference(thisObject, cObject, true)[0] == 0) {
    				DObjs.remove(objectName);
    				candidateObjs.remove(cobjectName);
    			}
    		}
    	}
    	
    	if(DObjs.isEmpty() && candidateObjs.isEmpty()) {
    		return new int[] {0,0};
    	}
    	
    	else if(DObjs.isEmpty()) {
    		return new int[] {candidateObjs.size() * 10, candidateObjs.size()};
    	}
    	
    	else if(candidateObjs.isEmpty()) {
    		return new int[] {DObjs.size() * 10, DObjs.size()};
    	}
    	
    	while (!DObjs.isEmpty()) {	 
    		CustomRavensObject thisObject = null;
    		String objectName = null;
    		for(String a : DObjs.keySet()) {
    			thisObject = DObjs.get(a);
    			objectName = a;
    			break;
    		}
			int[] lowest = new int[] {999, 0};
			String lowestDname = "";
			String lowestCname = "";	 
			for(String cobjectName : candidate.getObjects().keySet()) {
				RavensObject cObject = candidate.getObjects().get(cobjectName);
				int[] diff = ObjDifference(thisObject, cObject, true);
				if (diff[0] < lowest[0]) {
					lowest = diff;
					lowestDname = objectName;
					lowestCname = cobjectName;
				}
			}
			result[0] += lowest[0];
			result[1] += lowest[1];
			DObjs.remove(lowestDname);
			candidateObjs.remove(lowestCname);
    	}
    	
    	if(!candidateObjs.isEmpty()) {
    		result[0] += candidateObjs.size() * 5;
    		result[1] += candidateObjs.size();
    	}
    	
    	return result;
    }
    
    private CustomRavensFigure ApplyTransformation(CustomRavensFigure A, CustomRavensFigure Transform) {
    	HashMap<String, CustomRavensObject> result = new HashMap<String, CustomRavensObject>();
    	
    	for(String objectName : A.getObjects().keySet()) {
    		CustomRavensObject thisObject = A.getObjects().get(objectName);
    		HashMap<String, String> attributes = new HashMap<String, String>();
    		if(Transform.getObjects().containsKey(objectName)) {
    			if(Transform.getObjects().get(objectName).getAttributes().values().contains("baleeted"))
    				continue;
    			for(String attributeName : thisObject.getAttributes().keySet()) {
    				String attributeValue = thisObject.getAttributes().get(attributeName);
    				if (Transform.getObjects().get(objectName).getAttributes().containsKey(attributeName)) {
    					if(attributeName.equals("angle"))
    					{
    						if(Transform.getObjects().get(objectName).getAttributes().get("angle").equals("hflip")) {
    							if(Integer.parseInt(attributeValue) == 45 || Integer.parseInt(attributeValue) == 225)
    								attributes.put("angle", String.valueOf((Integer.parseInt(attributeValue) + 90) % 360));
    							else
    								attributes.put("angle", String.valueOf((Integer.parseInt(attributeValue) - 90) % 360));
    						}
    						else if (Transform.getObjects().get(objectName).getAttributes().get("angle").equals("vflip")) {
    							if(Integer.parseInt(attributeValue) == 45 || Integer.parseInt(attributeValue) == 225)
    								attributes.put("angle", String.valueOf((Integer.parseInt(attributeValue) + 270) % 360));
    							else
    								attributes.put("angle", String.valueOf((Integer.parseInt(attributeValue) - 270) % 360));
    						}
    						else
    							attributes.put("angle", String.valueOf(Integer.parseInt(attributeValue) + Integer.parseInt(Transform.getObjects().get(objectName).getAttributes().get("angle"))));
    					}
    					else if (attributeName.equals("alignment")) {
    						String[] parts = attributeValue.split("-");
    						String tval = Transform.getObjects().get(objectName).getAttributes().get(attributeName);
    						if(attributeValue.contains("-")) {
    							if(attributeValue.indexOf("-") == 0) {
    								attributes.put("alignment", parts[0] + tval);
    							}
    							else
    								attributes.put("alignment", tval + parts[1]);
    						}
    						else
    							attributes.put(attributeName, Transform.getObjects().get(objectName).getAttributes().get(attributeName));
    					}
    					else {
    						attributes.put(attributeName, Transform.getObjects().get(objectName).getAttributes().get(attributeName));
    					}
    				}
    				else {
    					attributes.put(attributeName, attributeValue);
    				}
    			}
    			result.put(objectName, new CustomRavensObject(objectName, attributes));
    		}
    		else {
    			result.put(objectName, thisObject);
    		}
    		
    		}
    	
    	return new CustomRavensFigure("result", result);
    }
    
    private CustomRavensFigure GetTransformation(RavensFigure A, CustomRavensFigure B) {
    	return GetTransformation(new CustomRavensFigure(A), B);
    }

    private CustomRavensFigure GetTransformation(CustomRavensFigure A, CustomRavensFigure B) {
    	HashMap<String, CustomRavensObject> objecttranforms = new HashMap<String, CustomRavensObject>();
    	for(String objectName : A.getObjects().keySet()) {
    		CustomRavensObject thisObject = A.getObjects().get(objectName);
    		if(B.getObjects().containsKey(objectName)) {
    			HashMap<String, String> transformations = new HashMap<String, String>();
    			for(String attributeName : thisObject.getAttributes().keySet()) {
    				String attributeValue = thisObject.getAttributes().get(attributeName);
    				if(B.getObjects().get(objectName).getAttributes().containsKey(attributeName)) {
	    				String BattributeValue = B.getObjects().get(objectName).getAttributes().get(attributeName);
	    				if(!attributeValue.equals(BattributeValue)) {
		    				if(attributeName.equals("angle")) {
		    					int a = Integer.parseInt(attributeValue);
		    					int b = Integer.parseInt(BattributeValue);
		    					if(Math.abs(b - a) == 90)
		    						transformations.put("angle", "hflip");
		    					else if (Math.abs(b - a) == 270)
		    						transformations.put("angle", "vflip");
		    					else
		    						transformations.put("angle", String.valueOf(Integer.parseInt(BattributeValue) - Integer.parseInt(attributeValue)));
		    				}
		    				else if (attributeName.equals("above") || attributeName.equals("overlaps") || attributeName.equals("inside")) {
		    					transformations.put(attributeName, BattributeValue);
		    				}
		    				else if(attributeName.equals("alignment")) {
		    					String[] aparts = attributeValue.split("-");
		    					String[] bparts = BattributeValue.split("-");
		    					if(aparts.length > 1 && bparts.length > 1) {
		    						if(!aparts[0].equals(bparts[0])) {
		    							transformations.put(attributeName, bparts[0] + "-");
		    						}
		    						else
		    							transformations.put(attributeName, "-" + bparts[1]);
		    					}
		    					else {
		    						transformations.put(attributeName, BattributeValue);
		    					}
		    				}
		    				else {
		    					transformations.put(attributeName, BattributeValue);
		    				}
	    				}
    				}
    			}
    			objecttranforms.put(objectName, new CustomRavensObject(objectName, transformations));
    		}
    		else {
    			HashMap<String, String> transformations = new HashMap<String, String>();
    			transformations.put(objectName, "baleeted");
    			objecttranforms.put(objectName, new CustomRavensObject(objectName, transformations));
    		}
    	}
    	return new CustomRavensFigure("transformation", objecttranforms);
    }
    
    private CustomRavensFigure MatchObjects(RavensFigure StartFigure, RavensFigure FinalFigure) {
    	return MatchObjects(new CustomRavensFigure(StartFigure), new CustomRavensFigure(FinalFigure));
    }
    
    private CustomRavensFigure MatchObjects(CustomRavensFigure StartFigure, RavensFigure FinalFigure) {
    	return MatchObjects(StartFigure, new CustomRavensFigure(FinalFigure));
    }
    
    private CustomRavensFigure MatchObjects(RavensFigure StartFigure, CustomRavensFigure FinalFigure) {
    	return MatchObjects(new CustomRavensFigure(StartFigure), FinalFigure);
    }
    
    private CustomRavensFigure MatchObjects(CustomRavensFigure StartFigure, CustomRavensFigure FinalFigure) {
    	int StartCount = StartFigure.getObjects().size();
    	int FinalCount = FinalFigure.getObjects().size();
    	HashMap<String, CustomRavensObject> objects = new HashMap<String, CustomRavensObject>();
    	HashMap<String, String> matchedObjs = new HashMap<String, String>();
    	
    	for(String StartobjectName : StartFigure.getObjects().keySet()) {
    		CustomRavensObject StartObject = StartFigure.getObjects().get(StartobjectName);
    		for(String FinalobjectName : FinalFigure.getObjects().keySet()) {
    			CustomRavensObject FinalObject = FinalFigure.getObjects().get(FinalobjectName);
    				if (ObjDifference(StartObject, FinalObject, false)[0] == 0) {
    					//System.out.println("Matched " + StartobjectName + " to " + FinalobjectName + " from figure " + StartFigure.getName() + " to figure " + FinalFigure.getName() + " in problem " + currprob);
    					objects.put(StartObject.getName(), StartObject);
    					matchedObjs.put(StartobjectName, FinalobjectName);
    				}
    			}
    		}
    	
    	Set<String> matched = objects.keySet();
    	
    	
    	if(matched.size() == StartCount && StartCount == FinalCount)
    		return new CustomRavensFigure("Post" + FinalFigure.getName(), objects);
    	
    	
    	else if (matched.size() == StartCount && FinalCount > StartCount) {
    		boolean ismatched = false;
    		for(String objectName : FinalFigure.getObjects().keySet()) {
    			ismatched = false;
    			CustomRavensObject thisObject = FinalFigure.getObjects().get(objectName);
    			for(String tocompare : matched) {
    				if(objects.get(tocompare).Compare(thisObject))
    					ismatched = true;
    			}
    			if (ismatched)
    				continue;
    			objects.put(thisObject.getName(), thisObject);
    			}
    		return new CustomRavensFigure("Post" + FinalFigure.getName(), objects);
    	}
    	
    	else if(matched.size() == FinalCount && StartCount > FinalCount) {
    		return new CustomRavensFigure("Post" + FinalFigure.getName(), objects);
    	}
    	
    	for(String objectName : FinalFigure.getObjects().keySet()) {
    		CustomRavensObject thisObject = FinalFigure.getObjects().get(objectName);
    		if(!matchedObjs.values().contains(thisObject.getName())) {
    			HashMap<Integer, String> diffs = new HashMap<Integer, String>();
    			int lowest = 999;
    			for(String test : StartFigure.getObjects().keySet()) {
    				CustomRavensObject testObject = StartFigure.getObjects().get(test);
    				if(!matchedObjs.containsKey(test)){
	    				int[] diffval = ObjDifference(testObject, thisObject, false);
	    				diffs.put(diffval[0], testObject.getName());
	    				if (lowest > diffval[0])
	    					lowest = diffval[0];
    				}
    			}
    			objects.put(diffs.get(lowest), thisObject);
    			matchedObjs.put(diffs.get(lowest), thisObject.getName());
    			//System.out.println("Matched " + diffs.get(lowest) + " to " + thisObject.getName() + " from figure " + StartFigure.getName() + " to figure " + FinalFigure.getName() + " in problem " + currprob);
    		}
    	}
    	
    	//int[][][] differences = new int[StartCount][FinalCount][2];
    	return new CustomRavensFigure("Post" + FinalFigure.getName(), objects);
    }
    
    private int[] ObjDifference(RavensObject A, RavensObject B, boolean ignoreSpatial) {
    	return ObjDifference(new CustomRavensObject(A), new CustomRavensObject(B), ignoreSpatial);
    }
    
    private int[] ObjDifference(CustomRavensObject A, RavensObject B, boolean ignoreSpatial) {
    	return ObjDifference(A, new CustomRavensObject(B), ignoreSpatial);
    }
    
    private int[] ObjDifference(RavensObject A, CustomRavensObject B, boolean ignoreSpatial) {
    	return ObjDifference(new CustomRavensObject(A), B, ignoreSpatial);
    }
    
    private int[] ObjDifference(CustomRavensObject A, CustomRavensObject B, boolean ignoreSpatial) {
    	int diff = 0;
    	int[] result = new int[] {0, 0};
    	HashMap<String, String> AAttributes = A.getAttributes();
    	HashMap<String, String> BAttributes = B.getAttributes();

    	for(String attributeName : AAttributes.keySet()) {
    		String attributeValue = AAttributes.get(attributeName);
    		if(BAttributes.containsKey(attributeName)) {
    			switch(attributeName) {
    			case "shape":
    				if(!(attributeValue.equals(BAttributes.get(attributeName)))) {
    					diff += 4;
    					result[1] += 1;
    				}
    			case "size":
    				if(!(attributeValue.equals(BAttributes.get(attributeName)))) {
    					try{
    						diff += Math.abs(sizes.get(attributeValue) - sizes.get(BAttributes.get(attributeName)));
    					}
    					catch(Exception e){
    						diff += 5;
    					}
    					
    					result[1] += 1;
    				}
	    		case "fill":
					if(!(attributeValue.equals(BAttributes.get(attributeName)))) {
						diff += 4;
						result[1] += 1;
					}
	    		case "alignment":
					if(!(attributeValue.equals(BAttributes.get(attributeName)))) {
						diff += 3;
						result[1] += 1;
					}
	    		case "angle":
					if(!(attributeValue.equals(BAttributes.get(attributeName)))) {
						diff += 1;
						result[1] += 1;
					}
				default:
					if(ignoreSpatial) {
						if (attributeName.equals("inside") || attributeName.equals("above") || attributeName.equals("overlaps")) {
							if(!(attributeValue.length() == BAttributes.get(attributeName).length())) {
								diff += 2;
								result[1] += 1;
							}
						}
					}
					else if(!(attributeValue.equals(BAttributes.get(attributeName)))) {
						diff += 2;
						result[1] += 1;
					}
				}
    		}
    		else
    			diff += 1;
    			result[1] += 1;
    	}
    	for (String a : BAttributes.keySet()) {
    		if(!AAttributes.containsKey(a))
    			diff += 1;
				result[1] += 1;
    	}
    	result[0] = diff;
    	//System.out.println("diff between: " + A.getName() + " and " + B.getName() + " is: " + diff);
    	return result;
    }
    
    private int VisualSolve(RavensProblem problem) {
    	if(problem.getProblemType().equals("3x3")) {
    		HashMap<String, RavensFigure> figures =  problem.getFigures();
    		HashMap<Integer, Integer> results = new HashMap<Integer, Integer>();
    		
    		BufferedImage Aimg;
    		BufferedImage Bimg;
    		BufferedImage Cimg;
    		BufferedImage Dimg;
    		BufferedImage Eimg;
    		BufferedImage Fimg;
    		BufferedImage Gimg;
    		BufferedImage Himg;
    		
    		VisualFigure A;
    		VisualFigure B;
    		VisualFigure C;
    		VisualFigure D;
    		VisualFigure E;
    		VisualFigure F;
    		VisualFigure G;
    		VisualFigure H;
    		
    		//solutions
    		BufferedImage sol1img;
    		BufferedImage sol2img;
    		BufferedImage sol3img;
    		BufferedImage sol4img;
    		BufferedImage sol5img;
    		BufferedImage sol6img;
    		BufferedImage sol7img;
    		BufferedImage sol8img;
    		
    		VisualFigure sol1;
    		VisualFigure sol2;
    		VisualFigure sol3;
    		VisualFigure sol4;
    		VisualFigure sol5;
    		VisualFigure sol6;
    		VisualFigure sol7;
    		VisualFigure sol8;
    		
    		try {
    		//figures
    		Aimg = ImageIO.read(new File(figures.get("A").getVisual()));
    		Bimg = ImageIO.read(new File(figures.get("B").getVisual()));
    		Cimg = ImageIO.read(new File(figures.get("C").getVisual()));
    		Dimg = ImageIO.read(new File(figures.get("D").getVisual()));
    		Eimg = ImageIO.read(new File(figures.get("E").getVisual()));
    		Fimg = ImageIO.read(new File(figures.get("F").getVisual()));
    		Gimg = ImageIO.read(new File(figures.get("G").getVisual()));
    		Himg = ImageIO.read(new File(figures.get("H").getVisual()));
    		
    		A = new VisualFigure(Aimg);
    		B = new VisualFigure(Bimg);
    		C = new VisualFigure(Cimg);
    		D = new VisualFigure(Dimg);
    		E = new VisualFigure(Eimg);
    		F = new VisualFigure(Fimg);
    		G = new VisualFigure(Gimg);
    		H = new VisualFigure(Himg);
    		
    		//solutions
    		sol1img = ImageIO.read(new File(figures.get("1").getVisual()));
    		sol2img = ImageIO.read(new File(figures.get("2").getVisual()));
    		sol3img = ImageIO.read(new File(figures.get("3").getVisual()));
    		sol4img = ImageIO.read(new File(figures.get("4").getVisual()));
    		sol5img = ImageIO.read(new File(figures.get("5").getVisual()));
    		sol6img = ImageIO.read(new File(figures.get("6").getVisual()));
    		sol7img = ImageIO.read(new File(figures.get("7").getVisual()));
    		sol8img = ImageIO.read(new File(figures.get("8").getVisual()));
    		
    		sol1 = new VisualFigure(sol1img);
    		sol2 = new VisualFigure(sol2img);
    		sol3 = new VisualFigure(sol3img);
    		sol4 = new VisualFigure(sol4img);
    		sol5 = new VisualFigure(sol5img);
    		sol6 = new VisualFigure(sol6img);
    		sol7 = new VisualFigure(sol7img);
    		sol8 = new VisualFigure(sol8img);
    		}
    		catch (Exception e) {
    			System.out.println("Failed to read image files");
    			return -1;
    		}
    		
    		VisualFigure[] sols = {sol1, sol2, sol3, sol4, sol5, sol6, sol7, sol8};
    		
    		int horizchange = 0;
    		horizchange += A.compare(B) + A.compare(C) + D.compare(E) + D.compare(F) + G.compare(H);
    		int horizsol = -1;
    		int horizsoldiff = 9999;
    		for (int i = 0; i < 8; i++) {
    			if (G.compare(sols[i]) < horizsoldiff) {
    				horizsoldiff = G.compare(sols[i]);
    				horizsol = i+1;
    			}
    		}
    		
    		results.put(horizchange, horizsol);
    		
    		int vertchange = 0;
    		vertchange += A.compare(D) + A.compare(G) + B.compare(E) + B.compare(H) + C.compare(F);
    		int vertsol = -1;
    		int vertsoldiff = 9999;
    		for (int i = 0; i < 8; i++) {
    			if (G.compare(sols[i]) < vertsoldiff) {
    				vertsoldiff = G.compare(sols[i]);
    				vertsol = i+1;
    			}
    		}
    		
    		results.put(vertchange, vertsol);
    		
    		int diagchange = 0;
    		diagchange += A.compare(E) + B.compare(F) + D.compare(H);
    		int diagsol = -1;
    		int diagsoldiff = 9999;
    		for (int i = 0; i < 8; i++) {
    			if (E.compare(sols[i]) < diagsoldiff) {
    				diagsoldiff = E.compare(sols[i]);
    				diagsol = i+1;
    			}
    		}
    		
    		results.put(diagchange, diagsol);
    		
    		int xorchange = 0;
    		xorchange += (A.xor(B)).compare(C) + (D.xor(E)).compare(F) + (A.xor(D)).compare(G) + (B.xor(E)).compare(H);
    		int xorsol = -1;
    		int xorsoldiff = 9999;
    		VisualFigure candidate = G.xor(H);
    		for (int i = 0; i < 8; i++) {
    			if (candidate.compare(sols[i]) < xorsoldiff) {
    				xorsoldiff = candidate.compare(sols[i]);
    				xorsol = i+1;
    			}
    		}
    		
    		results.put(xorchange, xorsol);
    		
    		int andchange = 0;
    		andchange += (A.and(B)).compare(C) + (D.and(E)).compare(F) + (A.and(D)).compare(G) + (B.and(E)).compare(H);
    		int andsol = -1;
    		int andsoldiff = 9999;
    		candidate = G.and(H);
    		for (int i = 0; i < 8; i++) {
    			if (candidate.compare(sols[i]) < andsoldiff) {
    				andsoldiff = candidate.compare(sols[i]);
    				andsol = i+1;
    			}
    		}
    		
    		results.put(andchange, andsol);
    		
    		//xor with object shapes
    		int xor2change= 0;
    		xor2change += (D.xor(A.xor(B))).compare(E) + (G.xor(D.xor(E))).compare(H) + (B.xor(A.xor(D))).compare(E) + (C.xor(B.xor(E))).compare(F);
    		int xor2sol = -1;
    		int xor2soldiff = 9999;
    		candidate = F.xor(E.xor(H));
    		for (int i = 0; i < 8; i++) {
    			if (candidate.compare(sols[i]) < xor2soldiff) {
    				xor2soldiff = candidate.compare(sols[i]);
    				xor2sol = i+1;
    			}
    		}
    		
    		results.put(xor2change, xor2sol);
    		
    		int addchange = 0;
    		addchange += (A.add(B)).compare(C) + (D.add(E)).compare(F) + (A.add(D)).compare(G) + (B.add(E)).compare(H);
    		int addsol = -1;
    		int addsoldiff = 9999;
    		candidate = C.add(F);
    		for (int i = 0; i < 8; i++) {
    			if (candidate.compare(sols[i]) < addsoldiff) {
    				addsoldiff = candidate.compare(sols[i]);
    				addsol = i+1;
    			}
    		}
    		
    		results.put(addchange, addsol);
    		
    		VisualFigure Actr;
    		VisualFigure Bctr;
    		VisualFigure Cctr;
    		VisualFigure Dctr;
    		VisualFigure Ectr;
    		VisualFigure Fctr;
    		VisualFigure Gctr;
    		VisualFigure Hctr;
    		
    		try {
	    		Actr = A.getCenterObj();
	    		Bctr = B.getCenterObj();
	    		Cctr = C.getCenterObj();
	    		Dctr = D.getCenterObj();
	    		Ectr = E.getCenterObj();
	    		Fctr = F.getCenterObj();
	    		Gctr = G.getCenterObj();
	    		Hctr = H.getCenterObj();
    		}
    		
    		catch(Exception e) {
    			System.out.println(e.getMessage());
    			return -1;
    		}
    		
    		VisualFigure Aotr = A.xor(Actr);
    		VisualFigure Botr = B.xor(Bctr);
    		VisualFigure Cotr = C.xor(Cctr);
    		VisualFigure Dotr = D.xor(Dctr);
    		VisualFigure Eotr = E.xor(Ectr);
    		VisualFigure Fotr = F.xor(Fctr);
    		VisualFigure Gotr = G.xor(Gctr);
    		VisualFigure Hotr = H.xor(Hctr);
    		
    		
    		int Actrmatches = 1;
    		int Bctrmatches = 1;
    		int Cctrmatches = 1;
    		int Aotrmatches = 1;
    		int Botrmatches = 1;
    		int Cotrmatches = 1;
    		
    		if (Actr.compare(Dctr) < CTR_MATCH_TOL) {
    			Actrmatches++;
    		}
    		
    		if (Actr.compare(Ectr) < CTR_MATCH_TOL) {
    			Actrmatches++;
    		}
    		
    		if (Actr.compare(Fctr) < CTR_MATCH_TOL) {
    			Actrmatches++;
    		}
    		
    		if (Actr.compare(Gctr) < CTR_MATCH_TOL) {
    			Actrmatches++;
    		}
    		
    		if (Actr.compare(Hctr) < CTR_MATCH_TOL) {
    			Actrmatches++;
    		}
    		
    		if (Bctr.compare(Dctr) < CTR_MATCH_TOL) {
    			Bctrmatches++;
    		}
    		
    		if (Bctr.compare(Ectr) < CTR_MATCH_TOL) {
    			Bctrmatches++;
    		}
    		
    		if (Bctr.compare(Fctr) < CTR_MATCH_TOL) {
    			Bctrmatches++;
    		}
    		
    		if (Bctr.compare(Gctr) < CTR_MATCH_TOL) {
    			Bctrmatches++;
    		}
    		
    		if (Bctr.compare(Hctr) < CTR_MATCH_TOL) {
    			Bctrmatches++;
    		}
    		
    		if (Cctr.compare(Dctr) < CTR_MATCH_TOL) {
    			Cctrmatches++;
    		}
    		
    		if (Cctr.compare(Ectr) < CTR_MATCH_TOL) {
    			Cctrmatches++;
    		}
    		
    		if (Cctr.compare(Fctr) < CTR_MATCH_TOL) {
    			Cctrmatches++;
    		}
    		
    		if (Cctr.compare(Gctr) < CTR_MATCH_TOL) {
    			Cctrmatches++;
    		}
    		
    		if (Cctr.compare(Hctr) < CTR_MATCH_TOL) {
    			Cctrmatches++;
    		}
    		
    		if (Aotr.compare(Dotr) < OTR_MATCH_TOL) {
    			Aotrmatches++;
    		}
    		
    		if (Aotr.compare(Eotr) < OTR_MATCH_TOL) {
    			Aotrmatches++;
    		}
    		
    		if (Aotr.compare(Fotr) < OTR_MATCH_TOL) {
    			Aotrmatches++;
    		}
    		
    		if (Aotr.compare(Gotr) < OTR_MATCH_TOL) {
    			Aotrmatches++;
    		}
    		
    		if (Aotr.compare(Hotr) < OTR_MATCH_TOL) {
    			Aotrmatches++;
    		}
    		
    		if (Botr.compare(Dotr) < OTR_MATCH_TOL) {
    			Botrmatches++;
    		}
    		
    		if (Botr.compare(Eotr) < OTR_MATCH_TOL) {
    			Botrmatches++;
    		}
    		
    		if (Botr.compare(Fotr) < OTR_MATCH_TOL) {
    			Botrmatches++;
    		}
    		
    		if (Botr.compare(Gotr) < OTR_MATCH_TOL) {
    			Botrmatches++;
    		}
    		
    		if (Botr.compare(Hotr) < OTR_MATCH_TOL) {
    			Botrmatches++;
    		}
    		
    		if (Cotr.compare(Dotr) < OTR_MATCH_TOL) {
    			Cotrmatches++;
    		}
    		
    		if (Cotr.compare(Eotr) < OTR_MATCH_TOL) {
    			Cotrmatches++;
    		}
    		
    		if (Cotr.compare(Fotr) < OTR_MATCH_TOL) {
    			Cotrmatches++;
    		}
    		
    		if (Cotr.compare(Gotr) < OTR_MATCH_TOL) {
    			Cotrmatches++;
    		}
    		
    		if (Cotr.compare(Hotr) < OTR_MATCH_TOL) {
    			Cotrmatches++;
    		}
    		
    		int outrxorctrsamediff = 0;
    		int outrxorctrsamesol = -1;
    		
    		outrxorctrsamediff += Actr.compare(Bctr) + Actr.compare(Cctr) + Dctr.compare(Ectr) + Dctr.compare(Fctr) + Gctr.compare(Hctr) + Aotr.compare(Dotr) + Aotr.compare(Gotr) + Botr.compare(Eotr) + Botr.compare(Hotr) + Cotr.compare(Fotr);
    		int outrxorctrsamesoldiff = 9999;
    		if (Aotrmatches == 3 && Botrmatches == 3 && Cotrmatches == 2) {
    			candidate = Gctr.xor(Cotr);
    		}
    		if (Aotrmatches == 3 && Botrmatches == 2 && Cotrmatches == 3) {
    			candidate = Gctr.xor(Botr);
    		}
    		if (Aotrmatches == 2 && Botrmatches == 3 && Cotrmatches == 3) {
    			candidate = Gctr.xor(Aotr);
    		}
    		for (int i = 0; i < 8; i++) {
    			if (candidate.compare(sols[i]) < outrxorctrsamesoldiff) {
    				outrxorctrsamesoldiff = candidate.compare(sols[i]);
    				outrxorctrsamesol = i+1;
    			}
    		}
    		
    		results.put(outrxorctrsamediff, outrxorctrsamesol);
    		
    		int outrsamectrxordiff = 0;
    		int outrsamectrxorsol = -1;
    		
    		outrsamectrxordiff += Aotr.compare(Botr) + Aotr.compare(Cotr) + Dotr.compare(Eotr) + Dotr.compare(Fotr) + Gotr.compare(Hotr) + Actr.compare(Dctr) + Actr.compare(Gctr) + Bctr.compare(Ectr) + Bctr.compare(Hctr) + Cctr.compare(Fctr);
    		int outrsamectrxorsoldiff = 9999;
    		if (Actrmatches == 3 && Bctrmatches == 3 && Cctrmatches == 2) {
    			candidate = Cctr.xor(Gotr);
    		}
    		if (Aotrmatches == 3 && Botrmatches == 2 && Cotrmatches == 3) {
    			candidate = Bctr.xor(Gotr);
    		}
    		if (Aotrmatches == 2 && Botrmatches == 3 && Cotrmatches == 3) {
    			candidate = Actr.xor(Gotr);
    		}
    		for (int i = 0; i < 8; i++) {
    			if (candidate.compare(sols[i]) < outrsamectrxorsoldiff) {
    				outrsamectrxorsoldiff = candidate.compare(sols[i]);
    				outrsamectrxorsol = i+1;
    			}
    		}
    		
    		results.put(outrsamectrxordiff, outrsamectrxorsol);
    		
    		candidate = null;
    		
    		if(Actrmatches == Bctrmatches && Bctrmatches == 3 && Cctrmatches == 2) {
    			if(Aotrmatches == Botrmatches && Botrmatches == 3 && Cotrmatches == 2) {
        			candidate = Cctr.xor(Cotr);
        		}
    			if(Aotrmatches == Cotrmatches && Cotrmatches == 3 && Botrmatches == 2) {
        			candidate = Cctr.xor(Botr);
        		}
    			if(Cotrmatches == Botrmatches && Botrmatches == 3 && Aotrmatches == 2) {
        			candidate = Cctr.xor(Aotr);
        		}
    			if(Aotrmatches == 1 || Botrmatches == 1 || Cotrmatches == 1) {
    				if (Aotrmatches == 2) {
    					candidate = Cctr.xor(Aotr);
    				}
    				if (Botrmatches == 2) {
    					candidate = Cctr.xor(Botr);
    				}
    				if (Cotrmatches == 2) {
    					candidate = Cctr.xor(Cotr);
    				}
    			}
    			
    			if (Aotrmatches == 1 && Botrmatches == 1 && Cotrmatches == 1) {
    				if (Gotr.compare(Hotr) < OTR_MATCH_TOL) {
    					candidate = Cctr.xor(Gotr);
    				}
    				if(Cotr.compare(Fotr) < OTR_MATCH_TOL) {
    					candidate = Cctr.xor(Cotr);
    				}
    			}
    		}
    		
    		if(Cctrmatches == Bctrmatches && Bctrmatches == 3 && Actrmatches == 2) {
    			if(Aotrmatches == Botrmatches && Botrmatches == 3 && Cotrmatches == 2) {
        			candidate = Actr.xor(Cotr);
        		}
    			if(Aotrmatches == Cotrmatches && Cotrmatches == 3 && Botrmatches == 2) {
        			candidate = Actr.xor(Botr);
        		}
    			if(Cotrmatches == Botrmatches && Botrmatches == 3 && Aotrmatches == 2) {
        			candidate = Actr.xor(Aotr);
        		}
    			if(Aotrmatches == 1 || Botrmatches == 1 || Cotrmatches == 1) {
    				if (Aotrmatches == 2) {
    					candidate = Actr.xor(Aotr);
    				}
    				if (Botrmatches == 2) {
    					candidate = Actr.xor(Botr);
    				}
    				if (Cotrmatches == 2) {
    					candidate = Actr.xor(Cotr);
    				}
    			}
    			if (Aotrmatches == 1 && Botrmatches == 1 && Cotrmatches == 1) {
    				if (Gotr.compare(Hotr) < OTR_MATCH_TOL) {
    					candidate = Actr.xor(Gotr);
    				}
    				if(Cotr.compare(Fotr) < OTR_MATCH_TOL) {
    					candidate = Actr.xor(Cotr);
    				}
    			}
    		}
    		
    		if(Actrmatches == Cctrmatches && Cctrmatches == 3 && Bctrmatches == 2) {
    			if(Aotrmatches == Botrmatches && Botrmatches == 3 && Cotrmatches == 2) {
        			candidate = Bctr.xor(Cotr);
        		}
    			if(Aotrmatches == Cotrmatches && Cotrmatches == 3 && Botrmatches == 2) {
        			candidate = Bctr.xor(Botr);
        		}
    			if(Cotrmatches == Botrmatches && Botrmatches == 3 && Aotrmatches == 2) {
        			candidate = Bctr.xor(Aotr);
        		}
    			if(Aotrmatches == 1 || Botrmatches == 1 || Cotrmatches == 1) {
    				if (Aotrmatches == 2) {
    					candidate = Bctr.xor(Aotr);
    				}
    				if (Botrmatches == 2) {
    					candidate = Bctr.xor(Botr);
    				}
    				if (Cotrmatches == 2) {
    					candidate = Bctr.xor(Cotr);
    				}
    			}
    			if (Aotrmatches == 1 && Botrmatches == 1 && Cotrmatches == 1) {
    				if (Gotr.compare(Hotr) < OTR_MATCH_TOL) {
    					candidate = Bctr.xor(Gotr);
    				}
    				if(Cotr.compare(Fotr) < OTR_MATCH_TOL) {
    					candidate = Bctr.xor(Cotr);
    				}
    			}
    		}
    		
    		if (candidate != null) {
    			int complexxorsol = -1;
    			int complexxorsoldiff = 9999;
        		for (int i = 0; i < 8; i++) {
        			if (candidate.compare(sols[i]) < complexxorsoldiff) {
        				complexxorsoldiff = candidate.compare(sols[i]);
        				complexxorsol = i+1;
        			}
        		}
        		
        		results.put(complexxorsoldiff, complexxorsol);
    		}
    		
    		
    		//get best solution
    		int sol = 9999;
    		for (int key : results.keySet()) {
    			if (key < sol) sol = key;
    		}
    		if (sol == 9999) {
    			return -1;
    		}
    		return results.get(sol);
    	}
    	return -1;
    }
}
