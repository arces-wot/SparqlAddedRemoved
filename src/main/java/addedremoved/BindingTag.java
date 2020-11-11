package addedremoved;

//
//SUBJECT("subject"),
//PREDICATE("predicate"),
//OBJECT("object"),
//GRAPH("graph");
public enum BindingTag {
	  SUBJECT("s"),
	  PREDICATE("p"),
	  OBJECT("o"),
	  GRAPH("g");
	
    private final String tag;
    
    BindingTag(final String tag) {
        this.tag = tag;
    }
  
    @Override
    public String toString() {
        return tag;
    }


}
