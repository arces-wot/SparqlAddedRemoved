package factories;

public enum RequestName {
	  SIMPLE_INSERT("SIMPLE_INSERT"),
	  SIMPLE_QUERY("SIMPLE_QUERY"),
	  SIMPLE_DELETE("SIMPLE_DELETE"),
	  //------------------LUMB query
	  QUERY1("QUERY1"),
	  QUERY2("QUERY2"),
	  QUERY3("QUERY3"),
	  QUERY4("QUERY4"),
	  QUERY5("QUERY5"),
	  QUERY6("QUERY6"),
	  QUERY7("QUERY7"),
	  QUERY8("QUERY8"),
	  QUERY9("QUERY9"),
	  QUERY10("QUERY10"),
	  QUERY11("QUERY11"),
	  QUERY12("QUERY12"),
	  QUERY13("QUERY13"),
	  QUERY14("QUERY14"),
	  //------------------------- update for LUMB test
	  UPDATE_FOR_Q2("UPDATE_FOR_Q2"),
	  ROLLBACK_FOR_Q2("ROLLBACK_FOR_Q2"),
	  UPDATE_FOR_Q3("UPDATE_FOR_Q3"),
	  ROLLBACK_FOR_Q3("ROLLBACK_FOR_Q3"),
	  UPDATE_FOR_Q4("UPDATE_FOR_Q4"),
	  ROLLBACK_FOR_Q4("ROLLBACK_FOR_Q4");
	
    private final String name;
    
    RequestName(final String name) {
        this.name = name;
    }
  
    @Override
    public String toString() {
        return name;
    }
}
