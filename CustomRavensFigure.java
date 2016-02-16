package ravensproject;
import java.util.HashMap;

public class CustomRavensFigure {
	private String name;
    private HashMap<String,CustomRavensObject> objects;
    
    public CustomRavensFigure(String name, HashMap<String, CustomRavensObject> objects) {
    	this.name = name;
    	this.objects = objects;
    }
    
    public CustomRavensFigure(RavensFigure input) {
    	this.name = input.getName();
    	objects = new HashMap<String,CustomRavensObject>();
    	for(String objectName : input.getObjects().keySet()) {
    		RavensObject thisObject = input.getObjects().get(objectName);
    		objects.put(objectName, new CustomRavensObject(thisObject));
    		}
    }
    
    public String getName() {
        return name;
    }
    public HashMap<String,CustomRavensObject> getObjects() {
        return objects;
    }
    
    public void print() {
    	System.out.println(name + ":");
    	for( CustomRavensObject object : objects.values()) {
    		object.print();
    	}
    }
}
