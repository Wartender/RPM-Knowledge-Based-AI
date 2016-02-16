package ravensproject;
import java.util.HashMap;

public class CustomRavensObject {
	private String name;
    private HashMap<String,String> attributes;
    
    public CustomRavensObject(String name, HashMap<String, String> attributes) {
    	this.name = name;
    	this.attributes = attributes;
    }
    
    public CustomRavensObject(RavensObject copy) {
    	this.name = copy.getName();
    	this.attributes = copy.getAttributes();
    }
    
    public String getName() {
        return name;
    }
    
    public HashMap<String,String> getAttributes() {
        return attributes;
    }
    
    public boolean Compare(RavensObject object) {
    	for(String attributeName : this.getAttributes().keySet()) {
    		String attributeValue = this.getAttributes().get(attributeName);
    		if(!object.getAttributes().containsKey(attributeName))
    			return false;
    		if(attributeValue != object.getAttributes().get(attributeName))
    			return false;
    		}
    	return true;
    }
    
    public boolean Compare(CustomRavensObject object) {
    	for(String attributeName : this.getAttributes().keySet()) {
    		String attributeValue = this.getAttributes().get(attributeName);
    		if(!object.getAttributes().containsKey(attributeName))
    			return false;
    		if(attributeValue != object.getAttributes().get(attributeName))
    			return false;
    		}
    	return true;
    }
    
    public void print() {
    	System.out.print(name + ":\n\t");
    	for(String attributeName : attributes.keySet()) {
    		String attributeValue = attributes.get(attributeName);
    		System.out.print(attributeName + " : " + attributeValue + "\n\t");
    		}
    }
}
